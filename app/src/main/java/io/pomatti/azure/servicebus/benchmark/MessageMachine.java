package io.pomatti.azure.servicebus.benchmark;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageMachine {

  Logger logger = LoggerFactory.getLogger(getClass());

  ServiceBusSender sender;

  public MessageMachine(ServiceBusSender sender) {
    this.sender = sender;
  }

  public void start() throws RuntimeException {
    List<String> dataset = getLargeDataset();
    var messageQuantity = dataset.size();

    int size = Integer.parseInt(Config.getProperty("app.sender_threads"));
    ForkJoinPool pool = new ForkJoinPool(size);

    Instant starts = Instant.now();
    try {
      pool.submit(() -> dataset.stream().parallel().forEach(body -> {
        sender.send(body);
      })).get();
      Instant ends = Instant.now();

      logger.info(String.format("Total messages sent: %s", messageQuantity));
      logger.info(String.format("Duration: %s ms", Duration.between(starts, ends).toMillis()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private List<String> getLargeDataset() {
    logger.info("Started building datasource");
    Integer qty = Integer.parseInt(Config.getProperty("app.message_quantity"));
    List<String> dataset = new ArrayList<>();
    for (int i = 0; i < qty; i++) {
      dataset.add(UUID.randomUUID().toString());
    }
    logger.info(dataset.size() + "");
    logger.info("Finished building datasource");
    return dataset;
  }

}

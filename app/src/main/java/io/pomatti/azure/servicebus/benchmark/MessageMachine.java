package io.pomatti.azure.servicebus.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

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

    int size = Integer.parseInt(Config.getProperty("app.sender_threads"));
    ForkJoinPool pool = new ForkJoinPool(size);

    pool.submit(() -> dataset.stream().parallel().forEach(body -> {
      sender.send(body);
    }));

    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
      pool.shutdown();
    } catch (InterruptedException e) {
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

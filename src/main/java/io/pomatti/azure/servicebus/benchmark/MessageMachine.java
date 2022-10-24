package io.pomatti.azure.servicebus.benchmark;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    Set<Integer> dataset = getLargeDataset();
    var messageQuantity = dataset.size();
    var messageBodyBytes = Integer.parseInt(Config.getProperty("app.message_body_bytes"));

    final String body = "8".repeat(messageBodyBytes);

    int size = Integer.parseInt(Config.getProperty("app.sender_threads"));
    ForkJoinPool pool = new ForkJoinPool(size);

    Instant starts = Instant.now();
    try {
      pool.submit(() -> dataset.stream().parallel().forEach(i -> {
        sender.send(body);
      })).get();
      Instant ends = Instant.now();

      long durationInSeconds = Duration.between(starts, ends).toMillis() / 1000;

      logger.info(String.format("Total messages sent: %s", messageQuantity));
      logger.info(String.format("Duration: %s seconds", durationInSeconds));
      if (durationInSeconds > 0) {
        logger.info(String.format("Throughput: %s messages/sec", messageQuantity / durationInSeconds));
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private Set<Integer> getLargeDataset() {
    Set<Integer> counter = new HashSet<>();
    Integer qty = Integer.parseInt(Config.getProperty("app.message_quantity"));
    for (int i = 0; i < qty; i++) {
      counter.add(i);
    }
    return counter;
  }

}

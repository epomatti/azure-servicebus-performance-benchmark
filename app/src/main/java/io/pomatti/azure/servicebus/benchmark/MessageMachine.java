package io.pomatti.azure.servicebus.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageMachine {

  Logger logger = LoggerFactory.getLogger(getClass());

  ServiceBusSender sender;

  public MessageMachine(ServiceBusSender sender) {
    this.sender = sender;
  }

  public void start() {
    List<String> dataset = getLargeDataset();
    dataset.parallelStream().forEach(body -> {
      sender.send(body);
    });
    logger.info("Finished stream");
  }

  private List<String> getLargeDataset() {
    logger.info("Started building datasource");
    List<String> dataset = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      dataset.add(UUID.randomUUID().toString());
    }
    logger.info(dataset.size() + "");
    logger.info("Finished building datasource");
    return dataset;
  }

}

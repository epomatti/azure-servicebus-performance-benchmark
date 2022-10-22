package io.pomatti.azure.servicebus.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownThread extends Thread {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ServiceBusConsumer consumer;

  public ShutdownThread(ServiceBusConsumer consumer) {
    this.consumer = consumer;
  }

  public void run() {
    logger.info("Closing Service Bus processor...");
    consumer.close();
    logger.info("Finished closing Service Bus processor");
  }

}
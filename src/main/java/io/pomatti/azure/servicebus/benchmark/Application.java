package io.pomatti.azure.servicebus.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  private static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
    Config.load();

    boolean initConsumer = Boolean.parseBoolean(Config.getProperty("app.init_consumer"));
    boolean initSender = Boolean.parseBoolean(Config.getProperty("app.init_sender"));
    int consumerConcurrentClients = Integer.parseInt(Config.getProperty("app.servicebus.concurrent_clients"));
    int senderConcurrentClients = Integer.parseInt(Config.getProperty("app.sender_concurrent_clients"));

    if (initConsumer) {
      for (int i = 0; i < consumerConcurrentClients; i++) {
        var consumer = new ServiceBusConsumer();
        consumer.start();
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(consumer));
      }
    }

    if (initSender) {
      for (int i = 0; i < senderConcurrentClients; i++) {
        var sender = new SenderThread();
        sender.start();
        // sender.join();
        logger.info(String.format("Started thread %s", i));
      }
    }
  }

}

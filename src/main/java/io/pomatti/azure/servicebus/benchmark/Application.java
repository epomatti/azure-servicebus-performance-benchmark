package io.pomatti.azure.servicebus.benchmark;

public class Application {
  public static void main(String[] args) {
    Config.load();

    boolean initConsumer = Boolean.parseBoolean(Config.getProperty("app.init_consumer"));
    boolean initSender = Boolean.parseBoolean(Config.getProperty("app.init_sender"));
    int consumerConcurrentClients = Integer.parseInt(Config.getProperty("app.servicebus.concurrent_clients"));

    if (initConsumer) {
      for (int i = 0; i < consumerConcurrentClients; i++) {
        var consumer = new ServiceBusConsumer();
        consumer.start();
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(consumer));
      }
    }

    if (initSender) {
      ServiceBusSender sender = new ServiceBusSender();
      sender.start();
      sender.warmup();
      Runtime.getRuntime().addShutdownHook(new ShutdownThread(sender));

      new MessageMachine(sender).start();
    }
  }

}

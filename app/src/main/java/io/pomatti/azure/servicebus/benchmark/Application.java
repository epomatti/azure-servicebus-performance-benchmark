package io.pomatti.azure.servicebus.benchmark;

public class Application {
  public static void main(String[] args) {
    Config.load();

    // Starts Consumer
    var consumer = new ServiceBusConsumer();
    consumer.start();
    Runtime.getRuntime().addShutdownHook(new ShutdownThread(consumer));

  }

}

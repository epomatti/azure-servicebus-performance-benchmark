package io.pomatti.azure.servicebus.benchmark;

public class Application {
  public static void main(String[] args) {
    Config.load();

    boolean initConsumer = Boolean.parseBoolean(Config.getProperty("app.init_consumer"));
    boolean initSender = Boolean.parseBoolean(Config.getProperty("app.init_sender"));

    if (initConsumer) {
      var consumer = new ServiceBusConsumer();
      consumer.start();
      Runtime.getRuntime().addShutdownHook(new ShutdownThread(consumer));
    }

    if (initSender) {
      ServiceBusSender sender = new ServiceBusSender();
      sender.start();
      sender.warmup();
      Runtime.getRuntime().addShutdownHook(new ShutdownThread(sender));

      new MessageMachine(sender).start();
    }

    // List<String> largeDataset = getLargeDataset();
    // ForkJoinPool customThreadPool = new ForkJoinPool(5);
    // customThreadPool.submit(() ->
    // largeDataset.parallelStream().forEach(System.out::println));
    // customThreadPool.shutdownNow();

  }

}

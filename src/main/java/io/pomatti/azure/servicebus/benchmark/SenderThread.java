package io.pomatti.azure.servicebus.benchmark;

public class SenderThread extends Thread {

  @Override
  public void run() {
    ServiceBusSender sender = new ServiceBusSender();
    sender.start();
    sender.warmup();
    Runtime.getRuntime().addShutdownHook(new ShutdownThread(sender));
    new MessageMachine(sender).start();
  }

}

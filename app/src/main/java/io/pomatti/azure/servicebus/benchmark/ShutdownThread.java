package io.pomatti.azure.servicebus.benchmark;

import java.io.Closeable;
import java.io.IOException;

public class ShutdownThread extends Thread {

  private Closeable consumer;

  public ShutdownThread(Closeable consumer) {
    this.consumer = consumer;
  }

  public void run() {
    try {
      consumer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
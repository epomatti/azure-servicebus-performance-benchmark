package io.pomatti.azure.servicebus.benchmark;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

public class ServiceBusSender implements Closeable {

  Logger logger = LoggerFactory.getLogger(getClass());

  private ServiceBusClientBuilder clientBuilder;
  private ServiceBusSenderClient client;

  public void start() {
    var connectionString = Config.getProperty("app.servicebus.connection_string");
    var queueName = Config.getProperty("app.servicebus.queue");
    this.clientBuilder = new ServiceBusClientBuilder()
        .connectionString(connectionString);
    client = this.clientBuilder.sender()
        .queueName(queueName)
        .buildClient();
  }

  public void warmup() {
    client.createMessageBatch();
  }

  public void send(String body) {
    client.sendMessage(new ServiceBusMessage(body));
  }

  public void close() {
    this.client.close();
  }

}

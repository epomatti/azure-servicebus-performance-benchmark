package io.pomatti.azure.servicebus.benchmark;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {

  private static Properties properties;

  public static void load() {
    properties = new Properties();
    String fileName = "app.properties";
    try (FileInputStream fis = new FileInputStream(fileName)) {
      properties.load(fis);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Properties getProperties() {
    return properties;
  }

  public static String getProperty(String name) {
    return properties.getProperty(name);
  }
}

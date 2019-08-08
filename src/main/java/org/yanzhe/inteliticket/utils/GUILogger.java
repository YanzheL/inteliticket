package org.yanzhe.inteliticket.utils;

import java.util.function.Consumer;

public class GUILogger {

  private static Consumer<String> func;

  public GUILogger() {
  }

  public GUILogger(Consumer<String> logFunc) {
    if (func == null) {
      func = logFunc;
    }
  }

  public void log(String msg) {
    if (func != null) {
      func.accept(msg);
    }
  }
}

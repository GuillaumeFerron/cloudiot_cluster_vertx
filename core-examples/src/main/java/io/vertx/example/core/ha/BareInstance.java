package io.vertx.example.core.ha;

import io.vertx.core.Launcher;

/**
 * This is a bare verticle intended to be deployed as a back up verticle.
 *
 * The verticle is intended to be launched using the `-ha` option.
 */
public class BareInstance {

  public static void main(String[] args) {
    Launcher.main(new String[]{"bare"});
  }
}

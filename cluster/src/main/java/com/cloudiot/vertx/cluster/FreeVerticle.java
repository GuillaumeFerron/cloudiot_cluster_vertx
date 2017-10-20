package com.cloudiot.vertx.cluster;

import io.vertx.core.Launcher;

/**
 * Created by Guillaume Ferron on the 20/10/2017
 */

/**
 * The Verticles deployed when the master verticle is down
 */
public class FreeVerticle {

    /**
     * The command to be launched as vertx, with the parameters
     * @param args
     */
    public static void main(String[] args) {
        Launcher.main(new String[]{"bare"});
    }
}

package io.vertx.example.core.ha;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.lang.management.ManagementFactory;

/***
 * This is a server verticle intended to be deployed as a main verticle.
 *
 * The verticle is intended to be launched using the `-ha` option.
 ***/
public class Server extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {

        Launcher.main(new String[]{"run", Server.class.getName(), "-ha"});
    }

    @Override
    public void start() throws Exception {
        String QUEUE_NAME = "task_queue";

        // PARAMETRE DE CONNEXION RABBITMQ
        RabbitMQOptions config = new RabbitMQOptions();
        // Each parameter is optional
        // The default parameter with be used if the parameter is not set
        config.setUser("test");
        config.setPassword("test");
        config.setHost("10.45.0.254"); // adresse IP du cluster rabbitMQ
        config.setPort(5672);
        config.setConnectionTimeout(6000); // in milliseconds
        config.setRequestedHeartbeat(60); // in seconds
        config.setHandshakeTimeout(6000); // in milliseconds
        config.setRequestedChannelMax(5);
        config.setNetworkRecoveryInterval(500); // in milliseconds
        config.setAutomaticRecoveryEnabled(true);
        RabbitMQClient client = RabbitMQClient.create(vertx, config);

        vertx.createHttpServer().requestHandler(req -> {
            client.start(v-> {
                vertx.setPeriodic(100, id -> { // Toutes les 100 miliisecondes
                    client.basicGet(QUEUE_NAME, true, getResult -> {
                        if (getResult.succeeded()) {
                            JsonObject msg = getResult.result();
                            final String response = msg.getString("body");
                            req.response().end(response);
                        } else {
                            getResult.cause().printStackTrace();
                        }
                    });
                });
            });
        }).listen(8080);
    }
}

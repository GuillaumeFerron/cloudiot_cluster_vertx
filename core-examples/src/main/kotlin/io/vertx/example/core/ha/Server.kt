package io.vertx.example.core.ha

import io.vertx.kotlin.rabbitmq.*
import io.vertx.rabbitmq.RabbitMQClient
import io.vertx.rabbitmq.RabbitMQOptions

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var QUEUE_NAME = "task_queue"

    // PARAMETRE DE CONNEXION RABBITMQ
    var config = RabbitMQOptions()
    // Each parameter is optional
    // The default parameter with be used if the parameter is not set
    config.user = "test"
    config.password = "test"
    config.host = "10.45.0.254"
    config.port = 5672
    config.connectionTimeout = 6000
    config.requestedHeartbeat = 60
    config.handshakeTimeout = 6000
    config.requestedChannelMax = 5
    config.networkRecoveryInterval = 500
    config.automaticRecoveryEnabled = true
    var client = RabbitMQClient.create(vertx, config)

    vertx.createHttpServer().requestHandler({ req ->
      client.start({ v ->
        vertx.setPeriodic(100, { id ->
          client.basicGet(QUEUE_NAME, true, { getResult ->
            if (getResult.succeeded()) {
              var msg = getResult.result()
              var response = msg.getString("body")
              req.response().end(response)
            } else {
              getResult.cause().printStackTrace()
            }
          })
        })
      })
    }).listen(8080)
  }
}

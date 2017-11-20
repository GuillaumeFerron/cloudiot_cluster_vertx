import io.vertx.rabbitmq.RabbitMQClient
def QUEUE_NAME = "task_queue"

// PARAMETRE DE CONNEXION RABBITMQ
def config = [:]
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
def client = RabbitMQClient.create(vertx, config)

vertx.createHttpServer().requestHandler({ req ->
  client.start({ v ->
    vertx.setPeriodic(100, { id ->
      client.basicGet(QUEUE_NAME, true, { getResult ->
        if (getResult.succeeded()) {
          def msg = getResult.result()
          def response = msg.body
          req.response().end(response)
        } else {
          getResult.cause().printStackTrace()
        }
      })
    })
  })
}).listen(8080)

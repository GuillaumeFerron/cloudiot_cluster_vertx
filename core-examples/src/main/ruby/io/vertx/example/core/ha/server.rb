require 'vertx-rabbitmq/rabbit_mq_client'
QUEUE_NAME = "task_queue"

# PARAMETRE DE CONNEXION RABBITMQ
config = {
}
# Each parameter is optional
# The default parameter with be used if the parameter is not set
config['user'] = "test"
config['password'] = "test"
config['host'] = "10.45.0.254"
config['port'] = 5672
config['connectionTimeout'] = 6000
config['requestedHeartbeat'] = 60
config['handshakeTimeout'] = 6000
config['requestedChannelMax'] = 5
config['networkRecoveryInterval'] = 500
config['automaticRecoveryEnabled'] = true
client = VertxRabbitmq::RabbitMQClient.create($vertx, config)

$vertx.create_http_server().request_handler() { |req|
  client.start() { |v_err,v|
    $vertx.set_periodic(100) { |id|
      client.basic_get(QUEUE_NAME, true) { |getResult_err,getResult|
        if (getResult_err == nil)
          msg = getResult
          response = msg['body']
          req.response().end(response)
        else
          getResult_err.print_stack_trace()
        end
      }
    }
  }
}.listen(8080)

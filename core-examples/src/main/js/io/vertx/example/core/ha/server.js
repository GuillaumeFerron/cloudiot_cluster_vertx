var RabbitMQClient = require("vertx-rabbitmq-js/rabbit_mq_client");
var QUEUE_NAME = "task_queue";

// PARAMETRE DE CONNEXION RABBITMQ
var config = {
};
// Each parameter is optional
// The default parameter with be used if the parameter is not set
config.user = "test";
config.password = "test";
config.host = "10.45.0.254";
config.port = 5672;
config.connectionTimeout = 6000;
config.requestedHeartbeat = 60;
config.handshakeTimeout = 6000;
config.requestedChannelMax = 5;
config.networkRecoveryInterval = 500;
config.automaticRecoveryEnabled = true;
var client = RabbitMQClient.create(vertx, config);

vertx.createHttpServer().requestHandler(function (req) {
  client.start(function (v, v_err) {
    vertx.setPeriodic(100, function (id) {
      client.basicGet(QUEUE_NAME, true, function (getResult, getResult_err) {
        if (getResult_err == null) {
          var msg = getResult;
          var response = msg.body;
          req.response().end(response);
        } else {
          getResult_err.printStackTrace();
        }
      });
    });
  });
}).listen(8080);


Build : ./gradlew :cluster:clean :cluster:build. 

Run Server : vertx run -cp ./cluster/build/libs/cluster.jar --ha com.cloudiot.vertx.cluster.ServerVerticle

Run Bare Verticles : vertx bare -cp ./cluster/build/libs/cluster.jar -cluster-host IP_adress_of_host

Server listening on port 8000

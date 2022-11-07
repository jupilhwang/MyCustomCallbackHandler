## MyCustomCallbackHandler

### kafka server.properties
```
listeners=client://:9092,broker://:9091
listener.security.protocol.map=client:SASL_PLAINTEXT,broker:PLAINTEXT
inter.broker.listener.name=broker

listener.name.client.sasl.enabled.mechanisms=PLAIN
listener.name.client.plain.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required;
listener.name.client.plain.sasl.server.callback.handler.class=me.jhwang.MyCustomCallbackHandler

```

### kafa client properties
```
sasl.mechanism=PLAIN
security.protocol=SASL_PLAINTEXT
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="client11" password="client-secret";
```

kafka command 
```
kafka-topics --bootstrap-server localhost:9092 --command-config client.properites --list
```

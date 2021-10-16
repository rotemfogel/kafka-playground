package me.rotemfo.worker;

import com.typesafe.config.Config;
import org.apache.kafka.clients.producer.ProducerConfig;

abstract class TickerSymbolWorker extends Thread {

    protected final Config kafkaConfig;
    protected final String topic;
    protected final String bootstrapServers;

    protected TickerSymbolWorker(final Config kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
        this.topic = kafkaConfig.getString("topic");
        this.bootstrapServers = kafkaConfig.getString(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
    }
}

package me.rotemfo.worker;

import com.typesafe.config.Config;
import me.rotemfo.model.TickerSymbol;
import me.rotemfo.serde.TickerSymbolDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TickerSymbolConsumer extends TickerSymbolWorker {
    private static final Logger logger = LoggerFactory.getLogger(TickerSymbolConsumer.class);

    public TickerSymbolConsumer(final Config kafkaConfig) {
        super(kafkaConfig);
    }

    @Override
    public void run() {
        logger.info("consumer worker {} started", Thread.currentThread().getId());
        final Config consumerConfig = kafkaConfig.getConfig("consumer");

        final Map<String, Object> consumerProps = new HashMap<String, Object>() {{
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            put(ConsumerConfig.GROUP_ID_CONFIG,
                    consumerConfig.getString(ConsumerConfig.GROUP_ID_CONFIG));
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TickerSymbolDeserializer.class.getName());
        }};

        KafkaConsumer<String, TickerSymbol> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, TickerSymbol> records =
                    consumer.poll(Duration.of(1000, ChronoUnit.MILLIS));
            records.forEach(record -> {
                logger.info("consumer {} received Ticker {}:{}", Thread.currentThread().getId(), record.key(), record.value());
            });
            consumer.commitAsync();
        }
    }
}

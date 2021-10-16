package me.rotemfo.worker;

import com.typesafe.config.Config;
import me.rotemfo.model.TickerSymbol;
import me.rotemfo.serde.TickerSymbolSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TickerSymbolProducer extends TickerSymbolWorker {
    private static final Logger logger = LoggerFactory.getLogger(TickerSymbolProducer.class);

    private final boolean useTransactions;

    public TickerSymbolProducer(final Config kafkaConfig,
                                final boolean useTransactions) {
        super(kafkaConfig);
        this.useTransactions = useTransactions;
    }

    public TickerSymbolProducer(final Config kafkaConfig) {
        this(kafkaConfig, kafkaConfig.getConfig("producer").getBoolean("useTransactions"));
    }

    @Override
    public void run() {
        logger.info("producer worker {} started", Thread.currentThread().getId());
        final Map<String, Object> producerProps = new HashMap<String, Object>() {{
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TickerSymbolSerializer.class.getName());
        }};
        // set transaction.id value in producer
        if (useTransactions)
            producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,
                    kafkaConfig.getConfig("producer").getString(ProducerConfig.TRANSACTIONAL_ID_CONFIG));

        KafkaProducer<String, TickerSymbol> producer = new KafkaProducer<>(producerProps);
        // initialize producer with transactions
        if (useTransactions) producer.initTransactions();

        Random random = new Random();
        random.setSeed(300);

        List<String> keys = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        final TickerSymbol tickerSymbol = new TickerSymbol("MSFT", 0d);
        while (true) {
            if (useTransactions) producer.beginTransaction();
            keys.forEach(key -> {
                tickerSymbol.setValue(random.nextDouble() * 100);
                producer.send(new ProducerRecord<>(topic, key, tickerSymbol), (recordMetadata, exception) -> {
                    if (exception != null) {
                        logger.error("error sending ticker {}:{} to topic {}", key, tickerSymbol, topic);
                    }
                    // should retry
                    else {
                        logger.info("producer sent ticker {}:{} to topic {}", key, tickerSymbol, topic);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            });
            if (useTransactions) producer.commitTransaction();
        }
    }
}

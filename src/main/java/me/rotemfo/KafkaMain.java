package me.rotemfo;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.rotemfo.worker.TickerSymbolConsumer;
import me.rotemfo.worker.TickerSymbolProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaMain {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMain.class);

    public static void main(String[] args) {
        logger.info("KafkaMain starting...");
        final Config config = ConfigFactory.defaultApplication();
        final Config kafkaConfig = config.getConfig("kafka");

        final int consumers = kafkaConfig.getInt("consumers");
        ExecutorService consumerRunner = Executors.newFixedThreadPool(consumers);

        for (int i = 0; i < consumers; i++) {
            TickerSymbolConsumer consumer = new TickerSymbolConsumer(kafkaConfig);
            consumerRunner.submit(consumer);
            consumer.start();
        }

        TickerSymbolProducer producer = new TickerSymbolProducer(kafkaConfig);
        producer.start();
    }
}

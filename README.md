# kafka-playground
Kafka Playground project

### Build:
* Download docker & docker-compose
* run
  
  ```docker-compose up```
* create the topic:

  ```
  docker exec -it broker1 bash
  # kafka-topcics --create --topic tickers \
    --partitions 3 --replication-factor 3
  ```

### Run
* Edit the `application.conf` file and set the `useTransaction` parameter (default `true`)
* Run the KafkaMain class
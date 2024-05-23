package org.fos.kafka.consumer;

import org.apache.avro.specific.SpecificRecord;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecord> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}

package demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Component
public class SchedulingConsumer implements Consumer<MyMessage> {
    private final static String SEARCH_OUTPUT_1 = "search1-out";
    private final static String SEARCH_OUTPUT_2 = "search2-out";
    private final StreamBridge streamBridge;


    @Autowired
    public SchedulingConsumer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void accept(MyMessage message) {
        System.out.println("******************");
        System.out.println("SchedulingConsumer");
        System.out.println("******************");
        System.out.println("Received message " + message);
        System.out.println("******************");
        MessageBuilder<MyMessage> messageBuilder = MessageBuilder.withPayload(message);
        String destination;
        switch (message.getType()){
            case 1:
                destination = SEARCH_OUTPUT_1;
                break;
            case 2:
            default:
                destination = SEARCH_OUTPUT_2;
        }
        String messagekey = String.valueOf(Math.random() * 10);
        streamBridge.send(destination, messageBuilder.setHeader(KafkaHeaders.MESSAGE_KEY, messagekey.getBytes(StandardCharsets.UTF_8)).build());
    }

}

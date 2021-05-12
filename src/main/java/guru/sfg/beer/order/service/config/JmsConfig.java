package guru.sfg.beer.order.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String QUEUE_VALIDATE_ORDER = "validate-order";
    public static final String QUEUE_VALIDATE_ORDER_RESULT = "validate-order-result";
    public static final String QUEUE_ALLOCATE_ORDER = "allocate-order";


    /**
     * Bean -> message converter
     * Using Jackson libraries to convert message to JSON
     * ie Object -> To JSON String and vice versa, JSON String -> To Java Object
     * @return converter
     */
    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        return converter;
    }

}

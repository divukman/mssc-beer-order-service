package guru.sfg.beer.order.service.services.testcomponents;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_VALIDATE_ORDER)
    public void listen(Message message) {


        log.debug(" **** *** *** *** BEER ORDER VALIDATION LISTENER");
        ValidateOrderRequest validateOrderRequest = (ValidateOrderRequest) message.getPayload();

        log.debug(" **** *** *** *** BEER ORDER VALIDATION LISTENER 2");
        jmsTemplate.convertAndSend(JmsConfig.QUEUE_VALIDATE_ORDER_RESULT,
                ValidateOrderResult.builder()
                .isValid(true)
                .orderId(validateOrderRequest.getBeerOrderDto().getId())
                .build()
                );
    }
}

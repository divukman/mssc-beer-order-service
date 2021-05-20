package guru.sfg.beer.order.service.services.testcomponents;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.QUEUE_ALLOCATE_ORDER)
    public void listen(Message message) {


        log.debug(" **** *** *** *** BEER ORDER ALLOCATION LISTENER");
        AllocateOrderRequest allocateOrderRequest = (AllocateOrderRequest) message.getPayload();

        log.debug(" **** *** *** *** BEER ORDER ALLOCATION LISTENER 2");
        jmsTemplate.convertAndSend(JmsConfig.QUEUE_ALLOCATE_ORDER_RESULT,
                AllocateOrderResult.builder()
                        .beerOrderDto(allocateOrderRequest.getBeerOrderDto())
                        .allocationError(false)
                        .pendingInventory(false)
                        .build()
                );
    }
}

package guru.sfg.beer.order.service.services.listeners;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.QUEUE_VALIDATE_ORDER_RESULT)
    public void listen(final ValidateOrderResult validateOrderResult) {
        log.debug(" *** *** ***");
        log.debug("Order validation result received...");
        log.debug("Order id: " + validateOrderResult.getOrderId() + " is valid: " + validateOrderResult.isValid());
        log.debug(" *** *** ***");

        beerOrderManager.processValidationResult(validateOrderResult.getOrderId(), validateOrderResult.isValid());
    }

}

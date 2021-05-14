package guru.sfg.beer.order.service.services.listeners;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.QUEUE_ALLOCATE_ORDER_RESULT)
    public void listen(final AllocateOrderResult allocateOrderResult) {
        log.debug(" *** *** ***");
        log.debug("Allocate order result received...");
        log.debug("Order id: " + allocateOrderResult.getBeerOrderDto().getId()+ ", allocation done: " + allocateOrderResult.isPendingInventory() + ", error happened: " + allocateOrderResult.isAllocationError());
        log.debug(" *** *** ***");

        if (!allocateOrderResult.isAllocationError() && !allocateOrderResult.isPendingInventory()) {
            beerOrderManager.beerOrderAllocationPassed(allocateOrderResult.getBeerOrderDto());
        } else if (!allocateOrderResult.isAllocationError() && allocateOrderResult.isPendingInventory()) {
            beerOrderManager.beerOrderAllocationPendingInventory(allocateOrderResult.getBeerOrderDto());
        } else  if (allocateOrderResult.isAllocationError()) {
            beerOrderManager.beerOrderAllocationFailed(allocateOrderResult.getBeerOrderDto());
        }
       // beerOrderManager.processValidationResult(validateOrderResult.getOrderId(), validateOrderResult.isValid());
    }

}

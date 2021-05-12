package guru.sfg.beer.order.service.sm.actions;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import guru.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import guru.sfg.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateOrderRequestAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {
    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        log.debug("Validating order request...");

        final UUID beerOrderId = UUID.class.cast(stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.BEER_ORDER_ID));
        final BeerOrder beerOrder = beerOrderRepository.findById(beerOrderId).get();

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_VALIDATE_ORDER,new ValidateOrderRequest(beerOrderMapper.beerOrderToDto(beerOrder)));

        log.debug("Sent validation request to the queue " + JmsConfig.QUEUE_VALIDATE_ORDER + " for an order with id " + beerOrderId.toString());
    }

}

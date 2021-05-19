package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.services.BeerOrderManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public void preStateChange(
            State<BeerOrderStatusEnum,
            BeerOrderEventEnum> state,
            Message<BeerOrderEventEnum> message,
            Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> rootStateMachine) {

        Optional.ofNullable(message)
                .ifPresent(beerOrderEventEnumMessage -> {
                    // Read beer order Id from the header
                    Optional.ofNullable(message.getHeaders().getOrDefault(BeerOrderManagerImpl.BEER_ORDER_ID, null))
                            .ifPresent(obj -> {
                                final UUID beerOrderId = UUID.class.cast(obj);
                                final BeerOrder beerOrder = beerOrderRepository.findById(beerOrderId).get();
                                beerOrder.setOrderStatus(state.getId());
                                beerOrderRepository.saveAndFlush(beerOrder);
                            });
                });
    }
}

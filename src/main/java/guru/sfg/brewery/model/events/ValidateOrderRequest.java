package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderRequest implements Serializable {
    private static final long serialVersionUID = 7438255714694047832L;

    private BeerOrderDto beerOrderDto;
}

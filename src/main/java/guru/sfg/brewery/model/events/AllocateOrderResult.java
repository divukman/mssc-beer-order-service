package guru.sfg.brewery.model.events;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateOrderResult implements Serializable {
    private static final long serialVersionUID = 7138255714694047812L;

    private BeerOrderDto beerOrderDto;
    private boolean allocationError;
    private boolean pendingInventory;
}

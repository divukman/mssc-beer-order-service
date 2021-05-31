package guru.sfg.beer.order.service.domain;

public enum BeerOrderStatusEnum {
    NEW,

    VALIDATION_PENDING,
    VALIDATED,
    VALIDATION_EXCEPTION,

    ALLOCATION_PENDING,
    ALLOCATED,
    ALLOCATION_EXCEPTION,
    PENDING_INVENTORY,

    PICKED_UP,

    DELIVERED,
    DELIVERY_EXCEPTION,

    CANCELED
}

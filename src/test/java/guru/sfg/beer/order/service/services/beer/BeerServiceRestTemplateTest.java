package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
class BeerServiceRestTemplateTest {

    @Autowired
    BeerService beerService;

    @Test
    public void testGetBeerByUPC() {
        final BeerDto beerDto = beerService.getBeer("0631234200036");

        System.out.println(beerDto);
    }

}
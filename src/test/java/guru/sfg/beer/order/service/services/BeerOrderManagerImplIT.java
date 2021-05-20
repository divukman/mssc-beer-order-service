package guru.sfg.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderLine;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.services.beer.BeerServiceRestTemplate;
import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(WireMockExtension.class)
@SpringBootTest
public class BeerOrderManagerImplIT {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    Customer testCustomer;
    UUID beerId = UUID.randomUUID();


    @TestConfiguration
    public static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer wireMockServer = with(wireMockConfig().port(8083));
            wireMockServer.start();
            return wireMockServer;
        }

    }

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(
                Customer.builder()
                .customerName("Test Customer 1")
                .build()
        );
    }


    @Test
    void testNewToAllocated() throws JsonProcessingException, InterruptedException {
        BeerDto beerDto = BeerDto.builder()
                .id(beerId)
                .upc("1234")
                .build();

        wireMockServer.stubFor(
                get(BeerServiceRestTemplate.BEER_BY_UPC_ROOT_PATH + "1234")
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto)))
        );

        BeerOrder beerOrder = createBeerOrder();
        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        final UUID beeerOrderId = savedBeerOrder.getId();

        //Thread.sleep(4000);

        await().atMost(5, TimeUnit.SECONDS).until(() -> {
            System.out.println("waiting");
            final BeerOrder beerOrder1 = beerOrderRepository.findById(beeerOrderId).get();
            return beerOrder1.getOrderStatus() == BeerOrderStatusEnum.ALLOCATION_PENDING;
        });

        savedBeerOrder =beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertNotNull(savedBeerOrder);
        assertEquals( BeerOrderStatusEnum.ALLOCATED, savedBeerOrder.getOrderStatus());
    }

    public BeerOrder createBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .build();
        Set<BeerOrderLine> beerOrderLines = new HashSet<>();
        beerOrderLines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc("1234")
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());
        beerOrder.setBeerOrderLines(beerOrderLines);
        return beerOrder;
    }
}

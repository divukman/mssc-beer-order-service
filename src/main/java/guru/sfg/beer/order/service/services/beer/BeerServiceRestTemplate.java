package guru.sfg.beer.order.service.services.beer;

import guru.sfg.beer.order.service.services.beer.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Component
public class BeerServiceRestTemplate implements BeerService {

    private static final String BEER_BY_UPC_PATH = "/api/v1/beerUpc/{beerUpc}";

    private final RestTemplate restTemplate;

    private String beerServiceHost;

    public BeerServiceRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public void setBeerServiceHost(String beerInventoryServiceHost) {
        this.beerServiceHost = beerInventoryServiceHost;
    }

    @Override
    public BeerDto getBeer(String upc) {
        log.debug("Calling Beer Service get beer by upc: " + upc);

        ResponseEntity<BeerDto> responseEntity = restTemplate.exchange(beerServiceHost + BEER_BY_UPC_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BeerDto>() {},
                (Object) upc);

        BeerDto beerDto = Objects.requireNonNull(responseEntity.getBody());

        return beerDto;
    }
}

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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Component
public class BeerServiceRestTemplate implements BeerService {

    private static final String BEER_BY_UPC_PATH = "/api/v1/beerUpc/{beerUpc}";
    private static final String BEER_BY_ID_PATH = "/api/v1/beer/";

    private final RestTemplate restTemplate;

    private String beerServiceHost;

    public BeerServiceRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public void setBeerServiceHost(String beerInventoryServiceHost) {
        this.beerServiceHost = beerInventoryServiceHost;
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        log.debug("Calling Beer Service get beer by upc: " + upc);

        ResponseEntity<BeerDto> responseEntity = restTemplate.exchange(beerServiceHost + BEER_BY_UPC_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BeerDto>() {},
                (Object) upc);

        BeerDto beerDto = Objects.requireNonNull(responseEntity.getBody());

        return Optional.of(beerDto);
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        return Optional.of( restTemplate.getForObject(beerServiceHost + BEER_BY_ID_PATH + id.toString(), BeerDto.class) );
    }
}

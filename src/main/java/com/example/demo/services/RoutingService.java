package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.classes.CountryDataDto;
import com.example.demo.classes.Path;
import com.example.demo.exceptions.CountryNotFoundException;
import com.example.demo.exceptions.PathNotFoundException;
import com.example.demo.helpers.Graph;

@Service
public class RoutingService {
    private static final String dataUrl = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    private RestTemplate restTemplate;

    @Autowired
    public RoutingService(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }

    public Path calculate(String origin, String destination) throws CountryNotFoundException, PathNotFoundException {
        Map<String, CountryDataDto> countriesMap = this.getCountries().stream()
                .collect(Collectors.toMap(CountryDataDto::getCca3, Function.identity()));
        CountryDataDto originCountry = countriesMap.get(origin);
        CountryDataDto destinationCountry = countriesMap.get(destination);
        this.validateCountries(originCountry, destinationCountry, origin, destination);
        Queue<CountryDataDto> route = Graph.searchGraphPath(originCountry, destinationCountry, countriesMap);
        return new Path(route.stream()
                .map(country -> country.getCca3())
                .collect(Collectors.toList()));
    }

    public void validateCountries(CountryDataDto originCountry, CountryDataDto destinationCountry, String origin,
            String destination) {
        if (originCountry == null) {
            throw new CountryNotFoundException(origin + " is not a valid Country Code", HttpStatus.NOT_FOUND);
        }
        if (destinationCountry == null) {
            throw new CountryNotFoundException(
                    destination + " is not a valid Country Code", HttpStatus.NOT_FOUND);
        }

        if (!originCountry.getRegion().isLandConnectionWith(destinationCountry)) {
            throw new PathNotFoundException(originCountry.getName().getOfficial() + " does not cross land with "
                    + destinationCountry.getName().getOfficial(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<CountryDataDto> getCountries() {
        ResponseEntity<List<CountryDataDto>> response = restTemplate.exchange(dataUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CountryDataDto>>() {
                });
        return response.getBody();
    }
}

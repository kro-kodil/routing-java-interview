package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.classes.CountryDataDto;
import com.example.demo.classes.Route;
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

    public Route calculate(String origin, String destination) {
        try {
            Map<String, CountryDataDto> countriesMap = this.getCountries().stream()
                    .collect(Collectors.toMap(CountryDataDto::getCca3, Function.identity()));

            CountryDataDto originCountry = countriesMap.get(origin);
            CountryDataDto destinationCountry = countriesMap.get(origin);

            if (originCountry.equals(null)) {
                //throw new ResponseStatusException(HttpStatusCode., "Unable to find resource");
            }

            Queue<CountryDataDto> route = Graph.searchGraphPath(originCountry, destinationCountry,
                    countriesMap);

            return new Route(route.stream()
                    .map(country -> country.getCca3())
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.classes.CountryDataDto;

@Service
public class RoutingService {
    private static final String dataUrl = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    private RestTemplate restTemplate;
    private Hashtable<String, CountryDataDto> countriesHashTable = new Hashtable<>();

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

    public String calculate(String origin, String destination) {
        try {
            Map<String, CountryDataDto> countriesMap = this.getCountries().stream()
                    .collect(Collectors.toMap(CountryDataDto::getCca3, Function.identity()));
            return countriesMap.get(origin).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
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

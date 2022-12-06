package com.example.demo.helpers;

import java.util.Map;
import com.example.demo.classes.CountryDataDto;

public class Graph {
    public static String[] searchGraphPath(String origin, String destination,
            Map<String, CountryDataDto> countriesMap) {
        CountryDataDto originCountry = countriesMap.get(origin);
        CountryDataDto destinationCountry = countriesMap.get(destination);
        return null;
    }
}

package com.example.demo.helpers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import com.example.demo.classes.CountryDataDto;

public class Graph {
    public static Queue<CountryDataDto> searchGraphPath(CountryDataDto currentCountry,
            CountryDataDto destinationCountry,
            Map<String, CountryDataDto> countriesMap) {
        // BFS Implementation
        Map<CountryDataDto, Boolean> visited = new HashMap<>();
        Queue<CountryDataDto> queue = new LinkedList<>();
        Queue<CountryDataDto> path = new LinkedList<>();

        visited.put(currentCountry, true);
        queue.add(currentCountry);
        path.add(currentCountry);

        while (queue.size() != 0) {
            currentCountry = queue.poll();
            path.add(currentCountry);
            System.out.println("Current visitting: " + currentCountry.getCca3());

            for (String border : currentCountry.getBorders()) {
                CountryDataDto borderCountry = countriesMap.get(border);
                if (borderCountry.getCca3().equals(destinationCountry.getCca3())) {
                    currentCountry = borderCountry;
                    path.add(currentCountry);
                    System.out.println("Shortest path to: " + currentCountry.getCca3());
                    return path;
                }

                if (!visited.containsKey(borderCountry)) {
                    visited.put(borderCountry, true);
                    System.out.println("Border Country: " + borderCountry.getCca3());
                    queue.add(borderCountry);
                }
            }

            path.remove(currentCountry);
        }

        return path;
    }
}

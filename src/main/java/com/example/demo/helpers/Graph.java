package com.example.demo.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import com.example.demo.classes.CountryDataDto;

public class Graph {
    public static List<CountryDataDto> searchGraphPath(CountryDataDto originCountry,
            CountryDataDto destinationCountry,
            Map<String, CountryDataDto> countriesMap) {
        // BFS Implementation
        CountryDataDto currentCountry = originCountry;
        Map<CountryDataDto, Boolean> visited = new HashMap<>();
        Queue<CountryDataDto> queue = new LinkedList<>();
        List<CountryDataDto> path = new ArrayList<>();
        Map<CountryDataDto, CountryDataDto> parentNodes = new HashMap<>();

        if (originCountry.getCca3().equals(destinationCountry.getCca3())) {
            path.add(currentCountry);
            return path;
        }

        visited.put(currentCountry, true);
        queue.add(currentCountry);

        while (queue.size() != 0) {
            currentCountry = queue.poll();
            System.out.println("Current visitting: " + currentCountry.getCca3());

            for (String border : currentCountry.getBorders()) {
                CountryDataDto borderCountry = countriesMap.get(border);
                if (borderCountry.getCca3().equals(destinationCountry.getCca3())) {
                    parentNodes.put(borderCountry, currentCountry);
                    System.out.println("Shortest path to: " + currentCountry.getCca3());
                    return Graph.backtrackPath(parentNodes, destinationCountry);
                }

                if (!visited.containsKey(borderCountry)) {
                    visited.put(borderCountry, true);
                    parentNodes.put(borderCountry, currentCountry);
                    System.out.println("Border Country: " + borderCountry.getCca3());
                    queue.add(borderCountry);
                }
            }
        }

        return null;
    }

    public static List<CountryDataDto> backtrackPath(Map<CountryDataDto, CountryDataDto> parentNodes,
            CountryDataDto destinationCountry) {
        CountryDataDto node = destinationCountry;
        List<CountryDataDto> path = new ArrayList<CountryDataDto>();

        while (node != null) {
            path.add(node);
            node = parentNodes.get(node);
        }
        Collections.reverse(path);
        return path;
    }
}

package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.services.RoutingService;

@Controller
public class RoutingResource {
    @Autowired
    private RoutingService routingService;

    @GetMapping("/calculate/{origin}/{destination}")
    @ResponseBody
    public String calculate(@PathVariable String origin, @PathVariable String destination) {
        return routingService.calculate(origin, destination);
    }

}

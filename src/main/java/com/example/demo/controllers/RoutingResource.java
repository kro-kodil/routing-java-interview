package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.exceptions.CountryNotFoundException;
import com.example.demo.exceptions.PathNotFoundException;
import com.example.demo.services.RoutingService;

@Controller
public class RoutingResource {
    @Autowired
    private RoutingService routingService;

    @GetMapping("/routing/{origin}/{destination}")
    @ResponseBody
    public ResponseEntity routing(@PathVariable String origin, @PathVariable String destination) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(routingService.calculate(origin, destination));

        } catch (CountryNotFoundException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (PathNotFoundException e) {
            return ResponseEntity
                    .status(e.getStatus())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}

package com.example.demo.classes;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Path {
    private List<String> route = null;

    public Path(List<String> route) {
        this.route = route;
    }

    public List<String> getRoute() {
        return this.route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

}

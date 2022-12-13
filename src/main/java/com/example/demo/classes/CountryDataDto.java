package com.example.demo.classes;

import java.util.List;

import com.example.demo.enums.Region;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDataDto {
    private String cca3;
    private CountryNameDto name;
    private Region region;
    private String subregion;
    private List<String> borders;
}

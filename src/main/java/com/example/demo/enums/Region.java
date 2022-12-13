package com.example.demo.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.demo.classes.CountryDataDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Region {
	@JsonProperty("Africa")
	AFRICA,
	@JsonProperty("Americas")
	AMERICAS,
	@JsonProperty("Antarctic")
	ANTARCTIC,
	@JsonProperty("Asia")
	ASIA,
	@JsonProperty("Europe")
	EUROPE,
	@JsonProperty("Oceania")
	OCEANIA;

	public boolean isLandConnectionWith(CountryDataDto destinationCountry) {
		List<Region> connectedRegions = new ArrayList<Region>(Arrays.asList(Region.AFRICA, Region.ASIA, Region.EUROPE));
		if (this.equals(destinationCountry.getRegion())) {
			return true;
		}

		return connectedRegions.contains(this)
				&& connectedRegions.contains(destinationCountry.getRegion());

	}
}

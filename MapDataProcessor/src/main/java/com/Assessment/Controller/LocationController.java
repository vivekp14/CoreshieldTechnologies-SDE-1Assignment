package com.Assessment.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Assessment.Model.Location;
import com.Assessment.Model.MergedLocation;
import com.Assessment.Service.LocationService;
import com.Assessment.Utility.JsonLoader;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
	
    @Autowired
    private LocationService locationService;

    @GetMapping("/merge")
    public List<MergedLocation> mergeData() throws IOException {
        return locationService.processAndMergeData();
    }

    @GetMapping("/valid-points")
    public Map<String, Long> countValidPoints() throws IOException {
        List<MergedLocation> mergedLocations = locationService.processAndMergeData();
        return locationService.countValidPointsPerType(mergedLocations);
    }

    @GetMapping("/average-rating")
    public Map<String, Double> averageRating() throws IOException {
        List<MergedLocation> mergedLocations = locationService.processAndMergeData();
        return locationService.calculateAverageRatingPerType(mergedLocations);
    }

    @GetMapping("/highest-reviews")
    public MergedLocation highestReviews() throws IOException {
        List<MergedLocation> mergedLocations = locationService.processAndMergeData();
        return locationService.findHighestReviewsLocation(mergedLocations);
    }

    @GetMapping("/incomplete-data")
    public List<MergedLocation> incompleteData() throws IOException {
        List<Location> locations = JsonLoader.loadJson("src/main/resources/locations.json", new TypeReference<>() {});
        List<MergedLocation> mergedLocations = locationService.processAndMergeData();
        return locationService.findIncompleteData(locations, mergedLocations);
    }
}

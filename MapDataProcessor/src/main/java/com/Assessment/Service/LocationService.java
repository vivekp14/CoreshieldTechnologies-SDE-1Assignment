package com.Assessment.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Assessment.Model.Location;
import com.Assessment.Model.MergedLocation;
import com.Assessment.Model.Metadata;
import com.Assessment.Utility.JsonLoader;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class LocationService {
    public List<MergedLocation> processAndMergeData() throws IOException {
    	 System.out.println("Processing merge data...");
    	    // Mock or test JSON loading if required
    	 
        List<Location> locations = JsonLoader.loadJson("src/main/resources/locations.json", new TypeReference<>() {});
        List<Metadata> metadataList = JsonLoader.loadJson("src/main/resources/metadata.json", new TypeReference<>() {});
        
        Map<String, Metadata> metadataMap = metadataList.stream()
                .collect(Collectors.toMap(Metadata::getId, metadata -> metadata));
        
        List<MergedLocation> mergedLocations = new ArrayList<>();
        for (Location location : locations) {
            Metadata metadata = metadataMap.get(location.getId());
            if (metadata != null) {
                MergedLocation mergedLocation = new MergedLocation();
                mergedLocation.setId(location.getId());
                mergedLocation.setLatitude(location.getLatitude());
                mergedLocation.setLongitude(location.getLongitude());
                mergedLocation.setType(metadata.getType());
                mergedLocation.setRating(metadata.getRating());
                mergedLocation.setReviews(metadata.getReviews());
                mergedLocations.add(mergedLocation);
            }
        }
        return mergedLocations;
    }

    public Map<String, Long> countValidPointsPerType(List<MergedLocation> mergedLocations) {
        return mergedLocations.stream()
                .collect(Collectors.groupingBy(MergedLocation::getType, Collectors.counting()));
    }

    public Map<String, Double> calculateAverageRatingPerType(List<MergedLocation> mergedLocations) {
        return mergedLocations.stream()
                .collect(Collectors.groupingBy(MergedLocation::getType, 
                        Collectors.averagingDouble(MergedLocation::getRating)));
    }

    public MergedLocation findHighestReviewsLocation(List<MergedLocation> mergedLocations) {
        return mergedLocations.stream()
                .max(Comparator.comparingInt(MergedLocation::getReviews))
                .orElse(null);
    }

    public List<MergedLocation> findIncompleteData(List<Location> locations, List<MergedLocation> mergedLocations) {
        Set<String> mergedIds = mergedLocations.stream()
                .map(MergedLocation::getId)
                .collect(Collectors.toSet());
        return locations.stream()
                .filter(location -> !mergedIds.contains(location.getId()))
                .map(location -> {
                    MergedLocation incomplete = new MergedLocation();
                    incomplete.setId(location.getId());
                    incomplete.setLatitude(location.getLatitude());
                    incomplete.setLongitude(location.getLongitude());
                    return incomplete;
                })
                .collect(Collectors.toList());
    }
}

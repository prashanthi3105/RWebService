import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public static List<String> getAIMSADGroups(List<String> adGroupMembership) {
    // Fetch the AD group map from ApplicationConstants
    Map<String, ADGroupDtls> adGroupMap = ApplicationConstants.getAdGrpMap();

    // Check for null or empty inputs
    if (adGroupMembership != null && adGroupMap != null && !adGroupMap.isEmpty()) {
        // Step 1: Extract all existing group names from the map
        List<String> existingGroupNames = new ArrayList<>();
        for (ADGroupDtls group : adGroupMap.values()) {
            if (group.getAdGrpName() != null) {
                existingGroupNames.add(group.getAdGrpName().trim().toLowerCase()); // Normalize for comparison
            }
        }

        // Step 2: Initialize the result list
        List<String> nonExistingGroups = new ArrayList<>();

        // Step 3: Compare adGroupMembership with existingGroupNames
        for (String group : adGroupMembership) {
            if (group != null && !existingGroupNames.contains(group.trim().toLowerCase())) {
                nonExistingGroups.add(group); // Add only non-matching groups
            }
        }

        // Return the filtered list
        return nonExistingGroups;
    }

    // If the map is null or empty, return the full adGroupMembership
    return adGroupMembership != null ? new ArrayList<>(adGroupMembership) : Collections.emptyList();
}
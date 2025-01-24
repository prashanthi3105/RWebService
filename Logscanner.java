Thank you for clarifying! Now I understand your requirement correctly:
	1.	You have a list of all AD groups from the AD group map (A, B, C, D), which represents the data in the database.
	2.	You have a list of AD groups assigned to the user from the session (adGroupMembership), such as A, C.
	3.	You want to filter and show only the groups from adGroupMembership that are not present in the AD group map. For example:
	•	If adGroupMap contains A, B, D and adGroupMembership contains A, C, the output should be C (because C is in the session but not in the database).

Here’s the corrected implementation to achieve this:

Corrected getAIMSADGroups() Method

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public static List<String> getAIMSADGroups(List<String> adGroupMembership) {
    // Get the AD group map from ApplicationConstants
    Map<String, ADGroupDtls> adGroupMap = ApplicationConstants.getAdGrpMap();

    // Check for null or empty inputs
    if (adGroupMembership != null && adGroupMap != null && !adGroupMap.isEmpty()) {
        // Extract all adGrpNames from the AD group map
        List<String> existingGroupNames = adGroupMap.values().stream()
                .map(ADGroupDtls::getAdGrpName)
                .collect(Collectors.toList());

        // Filter adGroupMembership to find groups not present in the database
        List<String> nonExistingGroups = adGroupMembership.stream()
                .filter(group -> !existingGroupNames.contains(group)) // Keep only groups not in the database
                .collect(Collectors.toList());

        return nonExistingGroups;
    }

    // Return an empty list if inputs are null/empty
    return Collections.emptyList();
}

Explanation
	1.	Extract Existing Groups (adGrpName) from adGroupMap:
	•	Use adGroupMap.values().stream().map(ADGroupDtls::getAdGrpName) to get a list of adGrpName values already present in the database.
	2.	Filter the Session List (adGroupMembership):
	•	Use filter(group -> !existingGroupNames.contains(group)) to keep only those groups from adGroupMembership that are not in the database.
	3.	Return Filtered Groups:
	•	The method returns the list of groups from the session that do not exist in the database.

Controller Integration

Here’s how to integrate this method into your controller:

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@Controller
public class InitBqrController {

    public String handleRequest(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        // Retrieve AD Group Membership from the session
        List<String> adGroupMembership = (List<String>) request.getSession().getAttribute("adGroupMembership");

        // Log session attribute for debugging
        System.out.println("AD Group Membership: " + adGroupMembership);

        // Get non-existing AD groups
        List<String> nonExistingGroups = getAIMSADGroups(adGroupMembership);

        // Set filtered groups in the session
        request.getSession().setAttribute("adGroups", nonExistingGroups);

        // Log the filtered groups
        System.out.println("Filtered AD Groups for Dropdown: " + nonExistingGroups);

        // Return the logical view name
        return "initBqrPage";
    }
}

Updated singleScreen.jsp

Ensure the JSP dynamically renders the dropdown options from the session data.

<!DOCTYPE html>
<html>
<head>
    <title>Filtered Dropdown</title>
</head>
<body>
    <h1>Filtered Dropdown Example</h1>

    <!-- Dropdown -->
    <form action="/submitAction" method="post">
        <label for="adGroups">AIMS AD Groups:</label>
        <select id="adGroups" name="adGroups">
            <c:forEach items="${sessionScope.adGroups}" var="group">
                <option value="${group}">${group}</option>
            </c:forEach>
        </select>
        <button type="submit">Submit</button>
    </form>
</body>
</html>

Example Scenario

Input
	1.	adGroupMap (Groups in Database):

Map<String, ADGroupDtls> adGroupMap = Map.of(
    "Group A", new ADGroupDtls("Group A"),
    "Group B", new ADGroupDtls("Group B"),
    "Group D", new ADGroupDtls("Group D")
);


	2.	adGroupMembership (Groups from Session):
	•	["Group A", "Group C"]

Output

The filtered list will contain:
	•	["Group C"]

Debugging Steps
	1.	Log Inputs:
	•	Log the adGroupMembership and adGroupMap values to confirm the data.

System.out.println("AD Group Membership: " + adGroupMembership);
System.out.println("Existing AD Groups in Database: " + existingGroupNames);


	2.	Log Filtered Results:
	•	Log the output of the filtering step to ensure the correct groups are retained.

System.out.println("Filtered AD Groups: " + nonExistingGroups);


	3.	Session Consistency:
	•	Ensure the adGroupMembership session attribute is correctly populated before calling getAIMSADGroups().

Key Points
	•	This implementation ensures that only groups not in the database but present in the session are displayed in the dropdown.
	•	The filtering logic is designed to be robust, with null/empty checks for all inputs.
	•	Debug logs will help identify any issues with the data flow.

Let me know if you need further clarifications or enhancements!
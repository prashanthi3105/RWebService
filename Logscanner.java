Thank you for clarifying! Based on your updated explanation, here’s the complete implementation:

Flow Summary
	1.	Retrieve Filtered Data:
	•	Display the group names not in the database but available in the session (getAIMSADGroups logic).
	•	These values are shown in a dropdown on the JSP.
	2.	Add a New Text Field:
	•	The New AD Group text field will allow the user to enter a name.
	3.	Form Submission:
	•	When the user selects a dropdown value and enters a New AD Group name, clicking “Submit” will send the data to the backend.
	4.	Database Insertion:
	•	The selected dropdown value and New AD Group name are inserted into the database table (TERT_AIMS_AD_GROUPS).

1. Updated JSP Code

Here’s the JSP to render the dropdown, text field, and submit button:

<!DOCTYPE html>
<html>
<head>
    <title>Add New AD Group</title>
</head>
<body>
    <h1>Add New AD Group</h1>

    <!-- Form to submit the selected dropdown and new AD group -->
    <form action="/adminMaintenance" method="post">
        <!-- Dropdown for filtered group names -->
        <label for="existingAdGroup">Select Existing AD Group:</label>
        <select id="existingAdGroup" name="existingAdGroup" required>
            <c:forEach items="${adGroups}" var="group">
                <option value="${group}">${group}</option>
            </c:forEach>
        </select>

        <!-- Text field for New AD Group -->
        <br/><br/>
        <label for="newAdGroupName">New AD Group Name:</label>
        <input type="text" id="newAdGroupName" name="newAdGroupName" required />

        <!-- Submit button -->
        <br/><br/>
        <button type="submit">Add AD Group</button>
    </form>
</body>
</html>

2. Updated handleRequest Method in the Controller

Enhance your existing handleRequest method to:
	1.	Handle Form Submission:
	•	Detect if the form has been submitted using the presence of existingAdGroup and newAdGroupName.
	•	Insert the data into the database.
	2.	Load Filtered Data:
	•	Retrieve and filter the session data to show in the dropdown.

Here’s the updated controller code:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AdminMaintenanceController {

    @Autowired
    private AdGroupService adGroupService;

    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        // Check for form submission
        String existingAdGroup = request.getParameter("existingAdGroup");
        String newAdGroupName = request.getParameter("newAdGroupName");

        if (existingAdGroup != null && newAdGroupName != null) {
            // Form submission detected
            System.out.println("Form Submission Detected");
            System.out.println("Selected Existing AD Group: " + existingAdGroup);
            System.out.println("Entered New AD Group Name: " + newAdGroupName);

            // Insert into database
            adGroupService.addAdGroup(existingAdGroup, newAdGroupName);

            // Redirect to avoid duplicate submissions
            return "redirect:/adminMaintenance";
        }

        // Load filtered groups for GET request
        List<String> filteredGroups = getFilteredAdGroups(request);
        request.setAttribute("adGroups", filteredGroups);

        // Forward to JSP
        return "admin-maintenance-page.jsp";
    }

    private List<String> getFilteredAdGroups(HttpServletRequest request) {
        // Logic to fetch and filter AM, CD groups
        List<String> adGroupMembership = (List<String>) request.getSession().getAttribute("adGroupMembership");
        return adGroupService.getFilteredAdGroups(adGroupMembership);
    }
}

3. Service Layer

Update the service layer to include methods for filtering groups and inserting into the database:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdGroupService {

    @Autowired
    private AdGroupDAO adGroupDAO;

    public List<String> getFilteredAdGroups(List<String> adGroupMembership) {
        // Call the utility to filter groups
        return AdGroupUtils.getAIMSADGroups(adGroupMembership);
    }

    public void addAdGroup(String existingAdGroup, String newAdGroupName) {
        // Insert data into the database
        adGroupDAO.insertAdGroup(existingAdGroup, newAdGroupName);
    }
}

4. DAO Layer

Write the actual database insertion logic in the DAO layer:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdGroupDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertAdGroup(String existingAdGroup, String newAdGroupName) {
        // SQL query to insert the data
        String sql = "INSERT INTO TERT_AIMS_AD_GROUPS (EXISTING_AD_GROUP, NEW_AD_GROUP_NAME) VALUES (?, ?)";

        // Execute the query
        jdbcTemplate.update(sql, existingAdGroup, newAdGroupName);

        // Log success
        System.out.println("Inserted AD Group: " + existingAdGroup + ", New AD Group Name: " + newAdGroupName);
    }
}

Database Table Structure

Ensure your TERT_AIMS_AD_GROUPS table has the following structure:

Column	Type	Description
EXISTING_AD_GROUP	VARCHAR	The selected dropdown value.
NEW_AD_GROUP_NAME	VARCHAR	The free-text field value.

Workflow
	1.	GET Request:
	•	handleRequest loads the filtered groups (not in the database) and forwards them to admin-maintenance-page.jsp.
	2.	Form Submission (POST):
	•	When the form is submitted, handleRequest detects the existingAdGroup and newAdGroupName parameters.
	•	The data is inserted into the TERT_AIMS_AD_GROUPS table.
	3.	Database Insertion:
	•	The DAO layer handles the SQL INSERT query to store the data in the table.
	4.	Page Reload:
	•	The controller redirects back to the same page after form submission to reload the filtered data.

Testing
	1.	JSP:
	•	Ensure the dropdown shows only the filtered groups (not in the database).
	•	Ensure the New AD Group field is editable.
	2.	Database Insertion:
	•	After submission, verify that the selected dropdown value and text field value are inserted into the TERT_AIMS_AD_GROUPS table.
	3.	Debugging Logs:
	•	Check the logs at each step to ensure the data flows correctly between layers.

Let me know if you need further refinements!
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>New AD Group</title>
</head>
<body>
    <h2>Select New AD Group</h2>
    <form action="processADGroup.jsp" method="post">
        <label for="adGroup">New AD Group:</label>
        <select id="adGroup" name="newADGroup">
            <option value="">-- Select an AD Group --</option>
            <option value="group1">Group 1</option>
            <option value="group2">Group 2</option>
            <option value="group3">Group 3</option>
            <option value="group4">Group 4</option>
        </select>
        <br><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
    <title>Single Screen</title>
</head>
<body>
    <h1>Dropdown Example</h1>

    <!-- Dropdown -->
    <form action="/submitAction" method="post">
        <label for="adGroups">AIMS AD Groups:</label>
        <select id="adGroups" name="adGroups">
            <c:forEach items="${requestScope.adGroups}" var="group">
                <option value="${group}">${group}</option>
            </c:forEach>
        </select>
        <button type="submit">Submit</button>
    </form>
</body>
</html>
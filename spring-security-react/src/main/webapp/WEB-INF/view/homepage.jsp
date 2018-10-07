<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head></head>

<body>
    <h1>This is the body of the sample view</h1>

    <security:authorize access="hasRole('ROLE_USER')">
        This text is only visible to a user
        <br/> <br/>
        <a href="<c:url value="/admin/adminpage.html" />">Restricted Admin Page</a>
        <br/> <br/>
    </security:authorize>
	
    <security:authorize access="hasRole('ROLE_ADMIN')">
        This text is only visible to an admin
        <br/>
        <a href="<c:url value="/admin/adminpage.html" />">Admin Page</a>
        <br/>
    </security:authorize>

    <a href="<c:url value="/perform_logout" />">Logout</a>

</body>
</html>
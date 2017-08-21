<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${event.name}</title>
	<style>
		table {
		    border-collapse: collapse;
		    width: 100%;
		}
		
		td, th {
		    border: 1px solid #dddddd;
		    text-align: left;
		    padding: 8px;
		}
		
		tr:nth-child(even) {
		    background-color: #dddddd;
		}
	</style>
</head>
<body>
	<p><a href="/events">All events</a></p>
	<h1>${event.name}</h1>
	<p>Host: ${event.host.firstName} ${event.host.lastName}</p>
	<p>Date: <fmt:formatDate pattern="MMMM dd, yyyy" value="${event.date}"/></p>
	<p>Location: ${event.city}, ${event.state}</p>
	<p>People who are attending this event: ${event.attendees.size()}</p>
	
	<c:if test="${!empty event.attendees}">
		<table>
			<tr>
				<th>Name</th>
				<th>Location</th>
			</tr>
			<c:forEach items="${event.attendees}" var="attendee">
				<tr>
					<td>${attendee.firstName} ${attendee.lastName}</td>
					<td>${attendee.city}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	<h2>Message Wall</h2>
	<c:if test="${!empty comments}">
		<c:forEach items="${comments}" var="comment" varStatus="loop">
			<p>${comment.author.firstName} ${comment.author.lastName} says: ${comment.text}</p>
			<c:if test="${loop.index < comments.size() - 1}">
				<p>-----------------------------------------</p>
			</c:if>
		</c:forEach>
	</c:if>
	<p><form:errors path="comment.*"/></p>
	<form:form method="POST" action="/events/${event.id}/newcomment" modelAttribute="comment">
        <p>
            <form:label path="text">Add Comment:</form:label>
            <br>
            <form:textarea path="text"/>
        </p>
        <input type="submit" value="Submit"/>
    </form:form>
</body>
</html>
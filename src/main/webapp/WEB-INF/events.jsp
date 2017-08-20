<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Events</title>
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
	<form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout" />
    </form>
	
	<h1>Welcome, ${currentUser.firstName}</h1>
	
	<c:if test="${!empty eventsInState}">
		<p>Here are some of the events in your state:</p>
		<table>
			<tr>
				<th>Name</th>
				<th>Date</th>
				<th>Location</th>
				<th>Host</th>
				<th>Action / Status</th>
			</tr>
			<c:forEach items="${eventsInState}" var="event">
				<tr>
					<td><a href="/events/${event.id}">${event.name}</a></td>
					<td><fmt:formatDate pattern="MMMM dd, yyyy" value="${event.date}"/></td>
					<td>${event.city}</td>
					<td>${event.host.firstName}</td>
					<td>
						<c:choose>
							<c:when test="${event.host == currentUser}">
							Edit | Delete
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${event.attendees.contains(currentUser)}">
										Joining | Cancel
									</c:when>
									<c:otherwise>
										Join
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	<c:if test="${!empty eventsOutOfState}">
		<p>Here are some of the events in other states:</p>
		<table>
			<tr>
				<th>Name</th>
				<th>Date</th>
				<th>City</th>
				<th>State</th>
				<th>Host</th>
				<th>Action</th>
			</tr>
			<c:forEach items="${eventsOutOfState}" var="event">
				<tr>
					<td>${event.name}</td>
					<td><fmt:formatDate pattern="MMMM dd, yyyy" value="${event.date}"/></td>
					<td>${event.city}</td>
					<td>${event.state}</td>
					<td>${event.host.firstName}</td>
					<td>Join</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	<h3>Create an Event</h3>
	<p><form:errors path="event.*"/></p>
	<form:form method="POST" action="/events" modelAttribute="event">
        <p>
            <form:label path="name">Name:</form:label>
            <form:input path="name"/>
        </p>
        <p>
            <form:label path="date">Date:</form:label>
            <form:input type="date" path="date"/>
        </p>
        <p>
            <form:label path="city">Location:</form:label>
            <form:input path="city"/>
            <form:select path="state" items="${states}"/>
        </p>
        <input type="submit" value="Create Event"/>
    </form:form>
</body>
</html>
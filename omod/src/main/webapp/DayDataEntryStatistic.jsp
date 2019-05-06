<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table>
	<th>Data</th>

	<c:forEach items="${userDates}" var="s">
		<th>${fn:toUpperCase(s.user)}</th>
	</c:forEach>

	<c:forEach items="${userDates}" var="t">
		<tr>
			<td>${t.date}</td>
			<c:forEach items="${userDates}" var="g">
				<td>${g.totalObs}</td>
			</c:forEach>
		</tr>
	</c:forEach>
</table>

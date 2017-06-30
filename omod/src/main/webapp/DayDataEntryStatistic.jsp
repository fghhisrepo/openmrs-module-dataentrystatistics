<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<table>
	<th>Data</th>

	<c:forEach items="${userDates}" var="s">
		<th>${s.user}</th>
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

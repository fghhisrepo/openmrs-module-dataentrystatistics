<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Data Entry Statistics"
	otherwise="/login.htm"
	redirect="/module/@MODULE_ID@/dataEntryStatistics.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/scripts/validation.js" />

<h2>
	<spring:message code="dataentrystatistics.title" />
</h2>

<form:form action="register" method="post">

	<table border="0">
		<tr>
			<td>Provider</td>
			<td><select path="provider" items="${providers}" /></td>
		</tr>
		<tr>
			<td>Location</td>
			<td><select path="provider" items="${providers}" /></td>
		</tr>

		<tr>
			<td><spring:message code="general.fromDate" />:</td>
			<td><input type="text" size="10" onClick="showCalendar(this)" />
			</td>
		</tr>

		<tr>
			<td><spring:message code="general.toDate" />:</td>
			<td><input type="text" size="10" onClick="showCalendar(this)" />
			</td>
		</tr>
	</table>
</form:form>

<p />


<%@ include file="/WEB-INF/template/footer.jsp"%>
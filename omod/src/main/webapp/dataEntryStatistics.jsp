
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Data Entry Statistics"
	otherwise="/login.htm"
	redirect="/module/@MODULE_ID@/dataEntryStatistics.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp"%>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<script type='text/javascript'
	src='https://code.jquery.com/jquery-1.11.0.min.js'></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>

<openmrs:htmlInclude file="/scripts/validation.js" />

<h2>
	<spring:message code="dataentrystatistics.title" />
</h2>

<form method="post">

	<p align="right">
		<a href="#" id="export" role='button'>Export Table Data Into a CSV
			File</a>
	</p>

	<fieldset>
		<table style="width: 30%;">
			<tr>
				<td><spring:message code="dataentrystatistics.obsCreator" />:</td>
				<td><spring:bind path="command.obsCreator">
						<select name="${status.expression}">
							<c:forEach items="${roles}" var="role">
								<option>${role}</option>
							</c:forEach>
						</select>
					</spring:bind></td>
			</tr>
			<tr id="orderByTr">
				<td><spring:message code="dataentrystatistics.orderBy" />:</td>
				<td><spring:bind path="command.orderBy">
						<select name="${status.expression}" width="60%" id="orderBy">
							<option></option>
							<c:forEach items="${orderBys}" var="orderBy">
								<option>${orderBy}</option>
							</c:forEach>
						</select>
					</spring:bind></td>
			</tr>
			<tr id="locationTr">
				<td><spring:message code="dataentrystatistics.location" /></td>
				<td><spring:bind path="command.location">
						<openmrs_tag:locationField formFieldName="location"
							initialValue="${status.value}" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr id="reportTypeTr">
				<td><spring:message code="dataentrystatistics.type" />:</td>
				<td><spring:bind path="command.reportType">

						<select name="${status.expression}" width="60%" id="reportType" >
							<option></option>
							<c:forEach items="${reportTypes}" var="reportType">
								<option>${reportType}</option>
							</c:forEach>
						</select>
					</spring:bind></td>
			</tr>

			<tr id="startDateTr">
				<td><spring:message code="dataentrystatistics.startDate" />:</td>
				<td><spring:bind path="command.fromDate">
						<input type="text" name="${status.expression}" size="10"
							value="${status.value}" id="startDate" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr id="endDateTr">
				<td><spring:message code="dataentrystatistics.endDate" />:</td>
				<td><spring:bind path="command.toDate">
						<input type="text" name="${status.expression}" size="10"
							value="${status.value}" id="endDate" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr id="fromMonthTr">
				<td><spring:message code="dataentrystatistics.startMonth" />:</td>
				<td><spring:bind path="command.fromMonth">
						<input type="text" name="${status.expression}" size="10"
							value="${status.value}" id="fromMonth" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr id="toMonthTr">
				<td><spring:message code="dataentrystatistics.endMonth" />:</td>
				<td><spring:bind path="command.toMonth">
						<input type="text" name="${status.expression}" size="10"
							value="${status.value}" id="toMonth" />
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>

			<tr>
				<td></td>
				<td><input type="submit"
					value="<spring:message code="general.view"/>" /></td>
			</tr>
		</table>
	</fieldset>
</form>

<p />

<c:out value="${command.table.htmlTable}" escapeXml="false" />

<openmrs:htmlInclude
	file="/moduleResources/dataentrystatistics/form-type.css" />
<openmrs:htmlInclude
	file="/moduleResources/dataentrystatistics/form-type.js" />

<%@ include file="/WEB-INF/template/footer.jsp"%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<!-- <style type="text/css"> -->
<%-- 	<%@ include file="/WEB-INF/css/bootstrap-theme.css"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap-theme.css.map"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap.css"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap-theme.min.css.map"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap.css.map"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap.min.css"%> --%>
<%-- 	<%@ include file="/WEB-INF/css/bootstrap.min.css.map"%> --%>
<!-- </style> -->


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

<form>
	<section>
		<fieldset>
			<legend> Data Statistic Entry </legend>
			<table>
				<tr>
					<td><spring:message code="dataentrystatistics.obsCreator" />:</td>
					<td><spring:bind path="command.encUserColumn">
							<select name="${status.expression}" width="10">
								<option value="creator"
									<c:if test="${command.encUserColumn=='creator'}">selected</c:if>><spring:message
										code="dataentrystatistics.encounterCreator" /></option>
								<option value="provider"
									<c:if test="${command.encUserColumn=='provider'}">selected</c:if>><spring:message
										code="dataentrystatistics.encounterProvider" /></option>
							</select>
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>
				<tr>
					<td><spring:message code="dataentrystatistics.location" />:</td>
					<td><spring:bind path="command.encUserColumn">
							<openmrs_tag:locationField formFieldName="location"
								initialValue="${status.value}" />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>

				<tr>
					<td><spring:message code="general.fromDate" />:</td>
					<td><spring:bind path="command.fromDate">
							<input type="text" name="${status.expression}" size="10"
								value="${status.value}" onClick="showCalendar(this)" />
							<c:if test="${status.errorMessage != ''}">
								<span class="error">${status.errorMessage}</span>
							</c:if>
						</spring:bind></td>
				</tr>
				<tr>
					<td><spring:message code="general.toDate" />:</td>
					<td><spring:bind path="command.toDate">
							<input type="text" name="${status.expression}" size="10"
								value="${status.value}" onClick="showCalendar(this)" />
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
	</section>
</form>

<table style="width: 100%" border="1">
	<th>Data</th>
	<c:forEach items="${nomes}" var="s">
		<th>${s}</th>
	</c:forEach>

	<c:forEach items="${m}" var="t">
		<tr>
			<td>${t.key}</td>
			<td>12</td>
			<td>12</td>
		</tr>
	</c:forEach>

</table>

<p />
<%@ include file="/WEB-INF/template/footer.jsp"%>
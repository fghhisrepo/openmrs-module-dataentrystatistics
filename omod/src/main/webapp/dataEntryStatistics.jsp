
<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="View Data Entry Statistics" otherwise="/login.htm" redirect="/module/@MODULE_ID@/dataEntryStatistics.list" />

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="/WEB-INF/view/admin/maintenance/localHeader.jsp" %>

<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<openmrs:htmlInclude file="/scripts/validation.js" />

<h2><spring:message code="dataentrystatistics.title"/></h2>

<form method="post">

	<p align="right"><a href="/downloadCSV">Download CSV</a></p>

<fieldset>
	<table  style="width: 30%;">
	<tr>
		<td><spring:message code="dataentrystatistics.obsCreator"/>:</td>
		<td>
			<spring:bind path="command.obsCreator">			
				<select name="${status.expression}" width="10">
					<c:forEach items="${roles}" var="role">
		                <option>${role}</option>
		            </c:forEach>
				</select>
			</spring:bind>
		</td>
	</tr>
		<tr>
			<td><spring:message code="dataentrystatistics.location" /></td>
				<td><spring:bind path="command.location">
						<openmrs_tag:locationField formFieldName="location"
							initialValue="${status.value}" />
						
						<c:if test="${status.errorMessage != ''}">
							<span class="error">${status.errorMessage}</span>
						</c:if>
					</spring:bind></td>
			</tr>
	<tr>
	
		<td><spring:message code="general.fromDate"/>:</td>
		<td>
			<spring:bind path="command.fromDate">
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" onClick="showCalendar(this)" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="general.toDate"/>:</td>
		<td>
			<spring:bind path="command.toDate">
				<input type="text" name="${status.expression}" size="10" 
					   value="${status.value}" onClick="showCalendar(this)" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if> 
			</spring:bind>
		</td>
	</tr>
	
		<tr>
		<td><spring:message code="dataentrystatistics.month"/>:</td>
		<td>
			<spring:bind path="command.month">			
				<select name="${status.expression}" width="60%">
				<c:forEach items="${months}" var="month">
	                <option>${month}</option>
	            </c:forEach>
				</select>
			</spring:bind>
		</td>
		
		<td><spring:message code="dataentrystatistics.year"/>:</td>
		
		<td>
			<spring:bind path="command.year">			
				<select name="${status.expression}" width="60%">
				<c:forEach items="${years}" var="year">
	                <option>${year}</option>
	            </c:forEach>
				</select>
			</spring:bind>
		</td>
	</tr>
	
	<tr>
		<td></td>
		<td><input type="submit" value="<spring:message code="general.view"/>" /></td>
	</tr>
	</table>
	</fieldset>
</form>

<p/>

<c:out value="${command.table.htmlTable}" escapeXml="false"/>

<%@ include file="/WEB-INF/template/footer.jsp" %>
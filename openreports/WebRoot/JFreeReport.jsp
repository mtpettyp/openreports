<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:if test="report == null || !report.isDisplayInline()">

<a class="back-link img-report-small" href="reportList.action"><s:text name="link.back.reports"/></a>
<a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.groups"/></a>  	
  
<br/>

</s:if>

<s:actionerror/>
  
<div align="center">
 
<s:property value="html"/>
   
</div>

<s:include value="Footer.jsp" />




	

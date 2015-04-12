<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/>

<a class="back-link img-report-small" href="listReports.action"><s:text name="link.admin.reports"/></a>
 
<br/><br/>

<div align="center">   

	<div class="important img-group" id="instructions" style="width: 70%;"><s:text name="editReportGroups.title"/> <s:property value="report.name"/></div></td>
 
  <form action="editReportGroups.action" class="dialog-form" style="width: 75%;">  
  
  <br>
  
  <table class="dialog">
  	<s:iterator id="group" value="groups">  
    <tr class="a">
      <td class="boldText" width="90%" ><s:property value="name"/></td>
      <td width="10%">
        <input type="checkbox" name="groupIds" value="<s:property value="id"/>"
          <s:iterator id="groupForReport" value="groupsForReport">         
            <s:if test="#group.id == #groupForReport.id">
              CHECKED
            </s:if>
          </s:iterator>
        >
      </td>
    </tr>
    </s:iterator>
    </table>
    
    <br>
    
   
    <div class="button-bar" id="buttons">
    	<input class="standardButton" type="submit" name="submitType" value="<s:text name="button.save"/>">
    </div>
    
    <input type="hidden" name="id" value="<s:property value="id"/>">
    <input type="hidden" name="command" value="<s:property value="command"/>">    
   
  	</form>
  	
  <br/> 
</div>

<s:include value="Footer.jsp" />

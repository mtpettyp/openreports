<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<script language="JavaScript" type="text/JavaScript">

function setDefaultExportType()
{
	if (optionsForm.exportType.length)
   	{
 		optionsForm.exportType[0].checked=true
 	}
 	else
 	{
 	    optionsForm.exportType.checked=true
 	}
}

function setBlankTarget()
{	
	optionsForm.target="_blank";		
}

function setNoTarget()
{
	optionsForm.target="";		
}
</script>

<div align="center">

  <a class="back-link img-report-small" href="reportList.action"><s:text name="link.back.reports"/></a>
  <a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.groups"/></a>
  
  <br/><br/>
	
  <div class="img-export important" id="instructions" style="width: 70%;">
	  <s:text name="reportOptions.title"/> <s:property value="report.name"/>
  </div>
  
  <form action="reportOptions.action" name="optionsForm" class="dialog-form" style="width: 75%;">    
		  
  <table class="dialog" >    
    <tr>
      <td class="boldText"><s:text name="reportOptions.exportType"/></td>   
      <s:if test="report.pdfExportEnabled">
      <td>
       <input type="radio" name="exportType" value="0">PDF
      </td>
      </s:if>
      <s:if test="report.htmlExportEnabled">      
      <td>
        <input type="radio" name="exportType" value="2">HTML
      </td>
      </s:if>
      <s:if test="report.csvExportEnabled">   
      <td>
        <input type="radio" name="exportType" value="3">CSV      
      </td>
      </s:if>
      <s:if test="report.xlsExportEnabled || report.jXLSReport">      
      <td>
        <input type="radio" name="exportType" value="1">XLS
      </td>
      </s:if>
      <s:if test="report.rtfExportEnabled">   
      <td>
        <input type="radio" name="exportType" value="5">RTF
      </td>
      </s:if>
      <s:if test="report.textExportEnabled">   
      <td>
        <input type="radio" name="exportType" value="6">Text
      </td>
      </s:if>
      <s:if test="report.excelExportEnabled">        
      <td>
        <input type="radio" name="exportType" value="7">Excel
      </td>
      </s:if>
      <s:if test="report.jasperReport && report.imageExportEnabled">       
	  <td>
        <input type="radio" name="exportType" value="4">Image
      </td>  
      </s:if>
      <script language="JavaScript" type="text/JavaScript">
        setDefaultExportType()
	  </script>        
    </tr>    
  </table>
  
  <br>
  
  <div id="buttons" class="button-bar">  
    
        <input type="submit" onClick="setNoTarget()" class="standardButton" name="submitRun" value="<s:text name="reportOptions.run"/>">
        <input type="submit" onClick="setBlankTarget()" class="standardButton" name="submitRun" value="<s:text name="reportOptions.runNewWindow"/>">
        <s:if test="user.scheduler">         
  		  <input type="submit" onClick="setNoTarget()" class="standardButton" name="submitSchedule" value="<s:text name="reportOptions.schedule"/>">
		</s:if>    	
  </div>
       
  </form>   
  
  <br/> 
  
</div>

<s:include value="Footer.jsp" />


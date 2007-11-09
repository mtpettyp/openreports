<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<div align="center">

  <div class="img-upload important" id="instructions" style="width:75%;"><s:text name="uploadReportFiles.uploadFile"/></div></td>
  
  <form action="reportUpload.action" method="POST" enctype="multipart/form-data" class="dialog-form" style="width:80%;">
  
  <br/>  
  
  <table>
    <tr colspan="2">      
      <td>
        <input type="file" name="reportFile" value="Browse..." size="50"/>&nbsp;
        <input type="submit" class="standardButton" value="Upload">
      </td>
    </tr>   
  </table> 
  <input type="hidden" name="command" value="upload">  
  
  <br/>
  
  </form>  
  
  <br/> 
  
  <div class="img-report important" id="files" style="width:75%"><s:text name="uploadReportFiles.currentFiles"/></div></td>
    
  <table style="width:80%">
    <s:iterator id="reportTemplate" value="reportTemplates">  	
    <tr>       
      	<td>
      		<b><s:property value="templateName"/></b>  <br/>
      		<s:set name="index" value="1"/>
      		<s:iterator id="revision" value="revisions">      		
      			<s:property/>
      			<a href="reportUpload.action?command=download&revision=<s:property/>" title="Download"><img border="0" src="images/download.gif"/></a>
      			<s:if test="revisionCount > 1 && #index > 1">
      			<a href="reportUpload.action?command=revert&revision=<s:property/>" title="Revert"><img border="0" src="images/revert.gif"/></a>
      			</s:if>
      			<s:set name="index" value="2"/>
      		</s:iterator>
      		<hr>
      	</td>       
    </tr>
    </s:iterator>          
  </table>
</div>
<br>
		
<s:include value="Footer.jsp" />
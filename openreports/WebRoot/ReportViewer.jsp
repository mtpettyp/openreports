<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">

<div class="important" id="reportName" style="width: 70%;"><s:property value="report.name"/></div></td>      

 
<div align="center">				

	<form method="post" action="reportViewer.action" class="dialog-form" style="width: 75%;">
			
		<table width="75%">
			<tr>						
				<td align="center">
				    <div  id="instructions">
					<b><s:text name="reportViewer.page"/>
					<input align="right" size="3" type="text" name="pageIndex" value="<s:property value="pageIndex"/>">
					of <s:property value="pageCount"/> </b>|<b> <s:text name="reportViewer.zoom"/></b>
					<select name="zoom">
						<option value="0.25" <s:if test="zoom == .25">SELECTED </s:if> >25%</option>
						<option value="0.5" <s:if test="zoom == .5">SELECTED </s:if> >50%</option>
						<option value=".75" <s:if test="zoom == .75">SELECTED </s:if> >75%</option>
						<option value="1" <s:if test="zoom == 1">SELECTED </s:if> >100%</option>
						<option value="1.25" <s:if test="zoom == 1.25">SELECTED </s:if> >125%</option>
						<option value="1.5" <s:if test="zoom == 1.5">SELECTED </s:if> >150%</option>
						<option value="1.75" <s:if test="zoom == 1.75">SELECTED </s:if>>175%</option>
						<option value="2"  <s:if test="zoom == 2">SELECTED </s:if> >200%</option>
					</select>				
					<input type="hidden" name="pageCount" value="<s:property value="pageCount"/>"/>						
					<input type="submit" name="submitType" value="<s:text name="button.refresh"/>" class="standardButton"/>						
					</div>
				</td>									
			</tr>					
		</table>
					
		<input type="hidden" name="reportName" value="<s:property value="report.name"/>">	
				
	</form>			
			
	<br/>
	
	<img border="1" src="reportViewer.action?submitType=image&pageIndex=<s:property value="pageIndex"/>&zoom=<s:property value="zoom"/>"/>	
		
		
			
</div>	

<br/>

<s:include value="Footer.jsp" />


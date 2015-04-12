<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<div align="center">  

  <a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.groups"/></a>	
  
  <br/>

  <div class="img-report important" id="instructions">
  	<s:text name="reportList.title"/>
  </div>  
  
  <s:set name="reports" value="reports" scope="request" /> 
  
   <display:table name="reports" class="displayTag" sort="list" requestURI="reportList.action" >                  
    <display:column property="name" titleKey="label.name" href="reportDetail.action" paramId="reportId" paramProperty="id" sortable="true" headerClass="sortable"/>        
    <display:column property="description" titleKey="label.description" sortable="true" headerClass="sortable"/>              
  </display:table> 
  
  <br>
  
</div>

<s:include value="Footer.jsp" /> 


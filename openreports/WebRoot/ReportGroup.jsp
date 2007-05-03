<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<div align="center">     
 
  <br/>
  
  <div class="img-group important" id="instructions">Groups</div>
  
  <s:set name="reportGroups" value="reportGroups" scope="request" />  	 
 
  <display:table name="reportGroups" class="displayTag" sort="list" requestURI="reportGroup.action" >  	      
    <display:column property="name" title="Name" href="reportList.action" paramId="groupId" paramProperty="id" sortable="true" headerClass="sortable"/>  	     
    <display:column property="description" title="Description" sortable="true" headerClass="sortable"/>  	     	     
  </display:table>
   
</div>

<s:include value="Footer.jsp" />


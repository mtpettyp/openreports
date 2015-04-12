<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
  
      <div class="instructions" id="instructions">
  		<a href="editGroup.action?command=add">
  			<img border="0" src="images/add.gif"/><s:text name="link.admin.addReportGroup"/>
  		</a>
  	 	<a href="reportAdmin.action?command=add">
  			<img border="0" src="images/back.gif"/> <s:text name="link.back.admin"/>
  		</a>
  	</div>
  	

  <br/>
  
  <s:set name="reportGroups" value="reportGroups" scope="request" />
  
  <display:table name="reportGroups" class="displayTag" sort="list" requestURI="listGroups.action" decorator="org.efs.openreports.util.HRefColumnDecorator"> 	      
    <display:column property="name" href="editGroup.action?command=edit" paramId="id" paramProperty="id" titleKey="label.name" sortable="true" headerClass="sortable"/>    	     	      	     
    <display:column property="description" titleKey="label.description" sortable="true" headerClass="sortable"/>  	     	        	 
    <display:column property="usersLink" title="" href="editGroupUsers.action" paramId="id" paramProperty="id"/>  	     	         	     
    <display:column property="removeLink" title="" href="deleteGroup.action" paramId="id" paramProperty="id"/>  	     	     
  </display:table>    
  <br>
</div>

<s:include value="Footer.jsp" />

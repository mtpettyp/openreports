<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">
  
      <div class="instructions" id="instructions">
  		<a href="editUser.action?command=add">
  			<img border="0" src="images/add.gif"/>Add User
  		</a>
  		<a href="reportAdmin.action?command=add">
  			<img border="0" src="images/back.gif"/> Back to Admin Menu
  		</a>
  	  </div>
  	  
  <br/> 
  
  <s:set name="users" value="users" scope="request" />
   
  <display:table name="users" class="displayTag" sort="list" requestURI="listUsers.action" decorator="org.efs.openreports.util.HRefColumnDecorator">  	      
    <display:column property="name" href="editUser.action?command=edit" paramId="id" paramProperty="id" title="Name" sortable="true" headerClass="sortable"/>
    <display:column property="removeLink" title="" href="deleteUser.action" paramId="id" paramProperty="id"/>  	     	     
  </display:table>  
  <br>  
</div>

<s:include value="Footer.jsp" /> 


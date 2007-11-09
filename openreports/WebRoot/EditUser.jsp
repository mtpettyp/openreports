<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<script type="text/javascript" src="js/yui-ext/yui-ext-core.js"></script>
<script type="text/javascript" src="js/add-to-user.js"></script>

<a class="back-link img-report-small" href="listUsers.action"><s:text name="link.back.users"/></a>
 
<br/>

<div align="center">  
  
	<div class="important img-user" id="instructions" style="width: 80%;">
		   <s:if test="command.equals('add')">
		   	<s:text name="editUser.addUser"/>
		   </s:if>
		   <s:if test="!command.equals('add')">
		   	<s:text name="editUser.selectedUser"/> <s:property value="name"/>
		   </s:if>
	</div>	
  
  <form action="editUser.action" class="dialog-form" style="width: 85%;">
  
  <table >  
    <tr class="a">
      <td class="boldText"><s:text name="label.name"/></td>
      <td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.tags"/></td>
      <td><input type="text" size="60" name="tags" value="<s:property value="tags"/>"></td>
    </tr>    
    <tr class="b">
      <td class="boldText"><s:text name="label.password"/></td>
      <td><input type="password" size="60" name="password" value="<s:property value="password"/>"></td>
    </tr>    
    <tr class="a">
      <td class="boldText"><s:text name="label.confirmPassword"/></td>
      <td><input type="password" size="60" name="passwordConfirm" value="<s:property value="passwordConfirm"/>"></td>
    </tr>    
    <tr class="a">
      <td class="boldText"><s:text name="label.externalId"/></td>
      <td><input type="text" size="60" name="externalId" value="<s:property value="externalId"/>"></td>
    </tr>
    <tr class="b">
      <td class="boldText"><s:text name="label.email"/></td>
      <td><input type="text" size="60" name="email" value="<s:property value="email"/>"></td>
    </tr>    
    <tr>
    	<td class="boldText"><s:text name="label.dashboardReport"/></td>
    	<td>    	
   	        <select name="reportId">
				<option value="-1">(None)</option>   
				<s:iterator id="report" value="reports">
					<option value="<s:property value="id"/>" <s:if test="id == reportId">selected="selected"</s:if> /><s:property value="name"/>
				</s:iterator>
			</select>		
        </select>   	    
   	   </td>  
   	</tr>    
    <tr>
      <td valign="top" class="boldText"><s:text name="label.roles"/></td>
      <td>
      	<ul class="checklist" style="height:12em;width:24em;">    
      		<li>
      			<s:checkbox name="advancedScheduler" fieldValue="true"  theme="simple"/>				
			    <s:text name="role.advancedScheduler"/>    
			</li>  	
      		<li>
				<s:checkbox name="chartAdmin" fieldValue="true" theme="simple"/>	
			    <s:text name="role.chartAdmin"/>  		    
			</li>  		
			<li>
				<s:checkbox name="dataSourceAdmin" fieldValue="true" theme="simple"/>	
			    <s:text name="role.dataSourceAdmin"/>  			    
			</li>
			<li>
				<s:checkbox name="uploader" fieldValue="true" theme="simple"/>
				<s:text name="role.uploader"/>  
			</li>	
			<li>
				<s:checkbox name="groupAdmin" fieldValue="true" theme="simple"/>
			    <s:text name="role.groupAdmin"/>  
			</li>
			<li>
				<s:checkbox name="logViewer" fieldValue="true" theme="simple"/>
			    <s:text name="role.logViewer"/>  		     
			</li>
			<li>
				<s:checkbox name="parameterAdmin" fieldValue="true" theme="simple"/>
			    <s:text name="role.parameterAdmin"/>  
			</li>
			<li>
				<s:checkbox name="reportAdmin" fieldValue="true" theme="simple"/>
			    <s:text name="role.reportAdmin"/>  
			</li>
			<li>
				<s:checkbox name="rootAdmin" fieldValue="true" theme="simple"/>
			    <s:text name="role.rootAdmin"/>  
			</li>
			<li>
      			<s:checkbox name="scheduler" fieldValue="true" theme="simple"/>
			    <s:text name="role.scheduler"/>  
			</li>						
		</ul>
	 </td>		
    </tr>       
    <tr>
      <td class="boldText" valign="top"><s:text name="label.groups"/></td>
      <td>
      	<ul id="currentGroups" class="checklist" style="height:12em;width:24em;">
      		<s:iterator id="reportGroup" value="reportGroupsForUser">      	   
      		<li>
      			 <input type="checkbox" name="groupIds" value="<s:property value="id"/>" CHECKED>
			      <s:property value="name"/>
			 </li>
			 </s:iterator>
		</ul>
	  </td>
      <td valign="top">
        </ul><input class="standardButton" type="button" id="showAddGroup" value="<s:text name="button.addGroup"/>">
      </td>
	</tr>    
    </table>
   
    <s:actionerror/>
    
    <div class="button-bar" id="buttons" >
      	<input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">
        <input class="standardButton" type="submit" name="submitDuplicate" value="<s:text name="button.duplicate"/>">  		
    </div>
    
    <input type="hidden" name="id" value="<s:property value="id"/>">
    <input type="hidden" name="command" value="<s:property value="command"/>">    
   
  </form> 
</div>

<div id="addGroupDialog">
  <div class="hd">Add Group to User</div>
  <div class="bd">  
    <form  >  
    <table class="dialog"  >    
      <tr>
        <td class="boldText" width="20%"><s:text name="label.filterBy"/></td>
        <td>
            <input type="radio" id="filterByName" name="filterType" value="name" CHECKED>Name 
            <input type="radio" id="filterByTag" name="filterType" value="tag">Tag
        </td>
      </tr> 
      <tr>
        <td class="boldText" width="20%"><s:text name="label.filter"/></td>
        <td><input type="text" id="filter" size="35" ></td>
      </tr>  
      <tr>
        <td valign="top" class="boldText"><s:text name="label.groups"/></td>
        <td>
          <select id="availableGroups" class="checklist" size="16" style="width: 20em;" multiple>        
          <s:iterator id="reportGroup" value="reportGroups">            
            <s:set name="available" value="true"/>    
            <s:iterator id="reportGroupForUser" value="reportGroupsForUser">     
              <s:if test="#reportGroup.id == #reportGroupForUser.id">
                <s:set name="available" value="false"/>  
              </s:if>
            </s:iterator>
            <s:if test="#available"> 
              <option id="<s:property value="id"/>|$!action.getTags($reportGroup.id)" value="<s:property value="id"/>"><s:property value="name"/></option>
            </s:if>
           </s:iterator>            
          </select>          
        </td>
      </tr>
    </table>
    </form>
  </div>  
</div>


<s:include value="Footer.jsp" />


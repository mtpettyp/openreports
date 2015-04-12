<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="datasourceDialog">
	<div class="hd"><s:text name="addDataSourceDialog.title"/></div>
	<div class="bd">
		
	<form action="addDataSourceDialog.action" method="post">	 
  
	<table class="dialog"> 	
		<tr >
			<td class="boldText" width="20%"><s:text name="label.useJNDI"/></td>
			<td>
				<img  id="jndi" src="images/help.gif" title="<s:text name="tooltip.datasource.jndi"/>">				        
			</td>
			<td><s:checkbox name="jndi" fieldValue="true" theme="simple" /></td>
		</tr>  
		<tr id="dsName">
			<td class="boldText" width="20%"><s:text name="label.name"/></td>
			<td>&nbsp;</td>
			<td><input type="text" size="50" name="name"></td>
		</tr>         
		<tr id="dsUrl">
			<td  class="boldText" width="20%"><s:text name="label.url"/></td>
			<td>
				<img  id="url" src="images/help.gif" title="<s:text name="tooltip.datasource.url"/>">                      
			</td>
			<td><input type="text" size="50" name="url" value="<s:property value="url"/>"></td>
		</tr>	
		<tr id="dsDriver">
			<td class="boldText" width="20%"><s:text name="label.driver"/></td>
			<td>
				<img  id="driver" src="images/help.gif" title="<s:text name="tooltip.datasource.driver"/>">				        
			</td>
			<td><input id="testD" type="text" size="50" name="driver" value="<s:property value="driver"/>"></td>
		</tr>
		<tr id="dsUser">
			<td class="boldText" width="20%"><s:text name="label.username"/></td>
			<td>&nbsp;</td>
			<td><input type="text" size="50" name="userName" value="<s:property value="userName"/>"></td>
		</tr>
		<tr id="dsPassword">
			<td class="boldText" width="20%"><s:text name="label.password"/></td>
			<td>&nbsp;</td>
			<td><input type="password" size="50" name="password" value="<s:property value="password"/>"></td>
		</tr>
		<tr id="dsMI">
			<td class="boldText" width="20%"><s:text name="label.maxIdle"/></td>
			<td>
				<img id="maxidle" src="images/help.gif" title="<s:text name="tooltip.datasource.maxIdle"/>">				        
			</td>
			<td><input type="text" size="50" name="maxIdle" value="<s:property value="maxIdle"/>"></td>
		</tr>
		<tr id="dsMA">
			<td  class="boldText" width="20%"><s:text name="label.maxActive"/></td>
			<td>
				<img id="maxactive" src="images/help.gif" title="<s:text name="tooltip.datasource.maxActive"/>">				        
			</td>
			<td><input type="text" size="50" name="maxActive" value="<s:property value="maxActive"/>"></td>     	
		</tr>
		<tr id="dsMW">
			<td class="boldText" width="20%"><s:text name="label.maxWait"/></td>
			<td>
				<img id="maxwait" src="images/help.gif" title="<s:text name="tooltip.datasource.maxWait"/>">				        
			</td>
			<td><input type="text" size="50" name="maxWait" value="<s:property value="maxWait"/>"></td>     	
		</tr>
		<tr id="dsQuery">
			<td class="boldText" width="20%"><s:text name="label.validationQuery"/></td>
			<td>
				<img id="query" src="images/help.gif" title="<s:text name="tooltip.datasource.query"/>">				        
			</td>
			<td><input type="text" size="50" name="validationQuery" value="<s:property value="validationQuery"/>"></td>     	
		</tr>   
    </table>    
	
   <input type="hidden" name="id" value="0">
   <input type="hidden" name="command" value="add">  
   <input type="hidden" name="submitType" value="Ok">
   <input type="hidden" name="submitOk" value="Ok">      
     
   </form> 

</div>
</div>		
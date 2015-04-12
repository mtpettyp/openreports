<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<a class="back-link img-report-small" href="listDataSources.action"><s:text name="link.back.dataSources"/></a>
 
<br/>    

<div align="center">

	  <div class="important img-ds" id="instructions" style="width: 80%;">
	  	<s:if test="command.equals('add')">
		  	<s:text name="editDataSource.addDataSource"/>
	   	</s:if>
	   	<s:if test="!command.equals('add')">
	 		<s:text name="editDataSource.selectedDataSource"/> <s:property value="name"/>
	   	</s:if> 	
	  </div>
	  	
  <form action="editDataSource.action" name="dsForm" class="dialog-form" style="width: 85%;">	 
  
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
			<td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
		</tr>         
		<tr id="dsUrl">
			<td  class="boldText" width="20%"><s:text name="label.url"/></td>
			<td>
				<img  id="url" src="images/help.gif" title="<s:text name="tooltip.datasource.url"/>">                      
			</td>
			<td><input type="text" size="60" name="url" value="<s:property value="url"/>"></td>
		</tr>	
		<tr id="dsDriver">
			<td class="boldText" width="20%"><s:text name="label.driver"/></td>
			<td>
				<img  id="driver" src="images/help.gif" title="<s:text name="tooltip.datasource.driver"/>">				        
			</td>
			<td><input id="testD" type="text" size="60" name="driver" value="<s:property value="driver"/>"></td>
		</tr>
		<tr id="dsUser">
			<td class="boldText" width="20%"><s:text name="label.username"/></td>
			<td>&nbsp;</td>
			<td><input type="text" size="60" name="userName" value="<s:property value="userName"/>"></td>
		</tr>
		<tr id="dsPassword">
			<td class="boldText" width="20%"><s:text name="label.password"/></td>
			<td>&nbsp;</td>
			<td><input type="password" size="60" name="password" value="<s:property value="password"/>"></td>
		</tr>
		<tr id="dsMI">
			<td class="boldText" width="20%"><s:text name="label.maxIdle"/></td>
			<td>
				<img id="maxidle" src="images/help.gif" title="<s:text name="tooltip.datasource.maxIdle"/>">				        
			</td>
			<td><input type="text" size="60" name="maxIdle" value="<s:property value="maxIdle"/>"></td>
		</tr>
		<tr id="dsMA">
			<td  class="boldText" width="20%"><s:text name="label.maxActive"/></td>
			<td>
				<img id="maxactive" src="images/help.gif" title="<s:text name="tooltip.datasource.maxActive"/>">				        
			</td>
			<td><input type="text" size="60" name="maxActive" value="<s:property value="maxActive"/>"></td>     	
		</tr>
		<tr id="dsMW">
			<td class="boldText" width="20%"><s:text name="label.maxWait"/></td>
			<td>
				<img id="maxwait" src="images/help.gif" title="<s:text name="tooltip.datasource.maxWait"/>">				        
			</td>
			<td><input type="text" size="60" name="maxWait" value="<s:property value="maxWait"/>"></td>     	
		</tr>
		<tr id="dsQuery">
			<td class="boldText" width="20%"><s:text name="label.validationQuery"/></td>
			<td>
				<img id="query" src="images/help.gif" title="<s:text name="tooltip.datasource.query"/>">				        
			</td>
			<td><input type="text" size="60" name="validationQuery" value="<s:property value="validationQuery"/>"></td>     	
		</tr>   
    </table>    
    
      	 <div class="button-bar" id="buttons" >
			<input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">
			<input class="standardButton" type="submit" name="submitDuplicate" value="<s:text name="button.duplicate"/>">
		</div>
	
   <input type="hidden" name="id" value="<s:property value="id"/>">
   <input type="hidden" name="command" value="<s:property value="command"/>">  
     
  </form>
  <br> 
  
  <div class="error"><s:actionerror/></div>  
  
</div>

<s:include value="Footer.jsp" />

<script type="text/javascript">
  var jndiTooltip = new YAHOO.widget.Tooltip("jndiTooltip", { context:"jndi" } );
  var urlTooltip = new YAHOO.widget.Tooltip("urlTooltip", { context:"url" } );
  var driverTooltip = new YAHOO.widget.Tooltip("driverTooltip", { context:"driver" } );
  var maxidleTooltip = new YAHOO.widget.Tooltip("maxidleTooltip", { context:"maxidle" } );
  var maxactiveTooltip = new YAHOO.widget.Tooltip("maxactiveTooltip", { context:"maxactive" } );
  var maxwaitTooltip = new YAHOO.widget.Tooltip("maxwaitTooltip", { context:"maxwait" } );
  var queryTooltip = new YAHOO.widget.Tooltip("queryTooltip", { context:"query" } );
</script>



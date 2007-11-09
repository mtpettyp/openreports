<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/>

<a class="back-link img-report-small" href="listAlerts.action"><s:text  name="link.back.alerts"/></a>
 
<br/>

<div align="center"> 
	 
  <div class="important img-alert" id="instructions" style="width: 80%;">
     <s:if test="command.equals('add')">
	   	<s:text name="editAlert.addAlert"/>
	 </s:if>
	 <s:if test="!command.equals('add')">
	 	<s:text name="editAlert.selectedAlert"/> <s:property value="name"/>
	 </s:if> 
  </div>	 
  
  <form action="editAlert.action" method="post" class="dialog-form" style="width: 85%;">  
  <table class="dialog" >  
    <tr class="a">
      <td class="boldText" width="30%"><s:text  name="label.name"/></td>
      <td>
      	&nbsp;
      </td>
      <td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
    </tr>
    <tr class="b">
      <td class="boldText" width="30%"><s:text  name="label.description"/></td>
      <td>
      	&nbsp;
      </td>
      <td><input type="text" size="60" name="description" value="<s:property value="description"/>"></td>
    </tr>    
    <tr class="b">
      <td class="boldText" ><s:text  name="label.query"/></td>
      <td>
      	<img  id="query" src="images/help.gif" title="<s:text name="tooltip.alert.query"/>">           
      </td>
      <td><textarea rows="3" cols="45" name="query"><s:property value="query"/></textarea></td>
    </tr>    
    <tr class="a">
      <td class="boldText" width="20%"><s:text  name="label.dataSource"/></td>
      <td>
      	&nbsp;
      </td>
      <td>
        <s:select name="dataSourceId" list="dataSources" listKey="id" listValue="name" theme="simple"/>               
      </td>
    </tr>    
   </table>
  
    <div class="button-bar" id="buttons" >
    	<input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">   
    </div>     
    
  <input type="hidden" name="id" value="<s:property value="id"/>">
  <input type="hidden" name="command" value="<s:property value="command"/>">
  
  </form>
  <br>  
</div>
<br>

<s:include value="Footer.jsp" />

<script type="text/javascript">  
  var queryTooltip = new YAHOO.widget.Tooltip("queryTooltip", { context:"query" } );
</script>


<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<script type="text/javascript" src="js/add-datasource.js"></script>

<a class="back-link img-report-small" href="listReportParameters.action"><s:text name="link.admin.reportParameters"/></a>
 
<br/>

<div align="center">   
	
	<div class="important img-param" id="instructions" style="width: 70%;">
		   <s:if test="command.equals('add')">
		   	<s:text name="editReportParameter.addReportParameter"/>
		   </s:if>
		   <s:if test="!command.equals('add')">
		   	<s:text name="editReportParameter.selectedReportParameter"/> <s:property value="name"/>
		   </s:if>
	</div>
  
  <form action="editReportParameter.action" name="paramForm" method="post"  class="dialog-form" style="width: 75%;">  
  
  <table class="dialog" >   
    <tr class="a">
      <td class="boldText"  width="30%"><s:text name="label.name"/></td>
      <td>
      	&nbsp;
      </td>
      <td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
    </tr>
    <tr class="b">
      <td class="boldText"  width="30%"><s:text name="label.description"/></td>
      <td>
      	&nbsp;
      </td>
      <td><input type="text" size="60" name="description" value="<s:property value="description"/>"></td>
    </tr>
    <tr class="a">
      <td class="boldText" ><s:text name="label.required"/></td>
      <td>
      	&nbsp;
      </td>
      <td><s:checkbox name="required" fieldValue="true" theme="simple"/></td>
    </tr>
    <tr class="a">
      <td class="boldText"  width="20%"><s:text name="label.class"/></td>
      <td>
      	<img id="class" src="images/help.gif" title="<s:text name="tooltip.parameter.class"/>">				        
      </td>
      <td>
      	<s:select name="className" list="classNames" theme="simple"/>	       
      </td>
    </tr>
    <tr class="b">
      <td class="boldText"  width="20%"><s:text name="label.type"/></td>
      <td>
      	&nbsp;				        
      </td>
      <td>
      	<s:select name="type" list="types" theme="simple"/>	       
      </td>
    </tr>
    <tr id="multipleSelectId" class="a">     
      <td class="boldText" ><s:text name="label.multipleSelect"/></td>
      <td>
      	<img id="multiselect" src="images/help.gif" title="<s:text name="tooltip.parameter.multiSelect"/>">				                 
      </td>
      <td><s:checkbox name="multipleSelect" fieldValue="true" theme="simple"/></td>
    </tr>
    <tr>
      <td class="boldText"  width="30%"><s:text name="label.defaultValue"/></td>
      <td>
      	&nbsp;
      </td>
      <td><input type="text" size="60" name="defaultValue" value="<s:property value="defaultValue"/>"></td>
    </tr>
    <tr id="paramData" class="b">
      <td class="boldText" ><s:text name="label.data"/></td>
      <td>
      	<img id="data" src="images/help.gif" title="<s:text name="tooltip.parameter.data"/>">				        
      </td>
      <td><textarea rows="5" cols="58" name="data"><s:property value="data"/></textarea></td>
    </tr>    
    <tr id="paramDataSourceId" class="a">
      <td  class="boldText"  width="20%"><s:text name="label.dataSource"/></td>
      <td>
      	<img id="datasource" src="images/help.gif" title="<s:text name="tooltip.parameter.dataSource"/>">				        
      </td>
      <td>
      	<s:select id="datasourceSelect" name="dataSourceId"  list="dataSources" listKey="id" listValue="name" headerKey="-1" headerValue=" -- None -- " theme="simple"/>        
        <input type="button" id="showAddDataSource" value="<s:text name="button.addDataSource"/>">
      </td>
    </tr>      
    </table>       
   
   <div class="button-bar" id="buttons" >
        <input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">
        <input class="standardButton" type="submit" name="submitDuplicate" value="<s:text name="button.duplicate"/>">
        <input class="standardButton" type="submit" name="submitValidate" value="<s:text name="button.validate"/>">
   </div>
   
  <input type="hidden" name="id" value="<s:property value="id"/>">
  <input type="hidden" name="command" value="<s:property value="command"/>"></th>
  
  </form>
  <br>   
  <div id="response" class="error"><s:actionerror/></div>
  <s:if test="parameterValues">
  <table class="dialog" >
  	<tr>
  	  <th colspan="2"><s:text name="editReportParameter.parameterValues"/></th>
  	</tr>
  	<tr>
  	  <th><s:text name="label.id"/></th>
  	  <th><s:text name="label.description"/></th>
  	</tr>
  	<s:iterator id="parameterValue" value="parameterValues">
  	<tr class="b">
  	  <td><s:property value="id"/></td>
  	  <td><s:property value="description"/></td>
  	</tr>
  	</s:iterator>	
  </table>
  </s:if>
 
</div>
<br>

<s:include value="AddDataSourceDialog.jsp" />

<s:include value="Footer.jsp" />

<script type="text/javascript">
  var classTooltip = new YAHOO.widget.Tooltip("classTooltip", { context:"class" } );
  var typeTooltip = new YAHOO.widget.Tooltip("typeTooltip", { context:"type" } );
  var multiselectTooltip = new YAHOO.widget.Tooltip("multiselectTooltip", { context:"multiselect" } );
  var dataTooltip = new YAHOO.widget.Tooltip("dataTooltip", { context:"data" } );
  var datasourceTooltip = new YAHOO.widget.Tooltip("datasourceTooltip", { context:"datasource" } );
</script>


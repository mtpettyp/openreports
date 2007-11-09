<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="parameterDialog">
	<div class="hd"><s:text name="addReportParameterDialog.title"/></div>
	<div class="bd">
		
    <form action="addParameterDialog.action" name="paramForm" method="post" ">  
  
    <table class="dialog" >    
      <tr class="a">
        <td class="boldText"  width="30%"><s:text name="label.name"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="60" name="name" ></td>
      </tr>
      <tr class="b">
        <td class="boldText"  width="30%"><s:text name="label.description"/></td>
        <td>
        	&nbsp;
        </td>
        <td><input type="text" size="60" name="description" ></td>
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
      		<s:select name="dataSourceId"  list="dataSources" listKey="id" listValue="name" headerKey="-1" headerValue=" -- None -- " theme="simple"/>      
      	</td>
      </tr>      
      </table>  
   
      <input type="hidden" name="id" value="0">
      <input type="hidden" name="command" value="add">
      <input type="hidden" name="submitOk" value="Ok">
  
  </form>

</div>
</div>		
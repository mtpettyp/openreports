<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/>

<script type="text/javascript" src="js/yui-ext/yui-ext-core.js"></script>
<script type="text/javascript" src="js/add-to-group.js"></script>

<script type="text/javascript">
  var tags = [<s:property escape="false" value="tagList"/>];            
</script>

<a class="back-link img-report-small" href="listGroups.action"><s:text name="link.back.groups"/></a>
 
<br/>

<div align="center">  
  
  <div class="important img-group" id="instructions" style="width: 70%;">
  	   <s:if test="command.equals('add')">
	   	<s:text name="editGroup.addGroup"/>
	   </s:if>
	   <s:if test="!command.equals('add')">
	 	<s:text name="editGroup.selectedGroup"/> <s:property value="name"/>
	   </s:if> 
  </div>	  
  
  <form action="editGroup.action" class="dialog-form" style="width: 75%;">
  
  <table class="dialog" >    
    <tr class="a">
      <td class="boldText" width="20%"><s:text name="label.name"/></td>
      <td colspan="2"><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
    </tr>
    <tr class="b">
      <td class="boldText"><s:text name="label.description"/></td>
      <td colspan="2"><input type="text" size="60" name="description" value="<s:property value="description"/>"></td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.tags"/></td>
      <td colspan="2"><input type="text" size="60" name="tags" value="<s:property value="tags"/>"></td>
    </tr>
    <tr>
   	  <td valign="top" class="boldText"><s:text name="label.reports"/></td>
   	  <td>
   		<ul id="currentReports" class="checklist" style="height:20em;width:24em;">
   		<s:iterator id="report" value="reportsInGroup">
		   <li>      
        	<input type="checkbox" name="reportIds" value="<s:property value="id"/>" CHECKED>
        	<s:property value="name"/>
      	   </li>
        </s:iterator>
      </td>
      <td valign="top">
        </ul><input class="standardButton" type="button" id="showAddReport" value="<s:text name="button.addReports"/>">
      </td>
    </tr>
  </table>
   
  <div class="button-bar" id="buttons" >
       <input class="standardButton" type="submit" name="submitOk" value="<s:text name="button.save"/>">
       <input class="standardButton" type="submit" name="submitDuplicate" value="<s:text name="button.duplicate"/>">
  </div>
    
  <input type="hidden" name="id" value="<s:property value="id"/>">
  <input type="hidden" name="command" value="<s:property value="command"/>">    
  
  </form>  
  
</div>
  
<div id="addReportDialog">
  <div class="hd"><s:text name="editGroup.addReportsToGroup.title"/></div>
  <div class="bd">  
    <form>  
    <table class="dialog"  >   
      <tr>
        <td class="boldText" width="20%"><s:text name="label.filterBy"/></td>
        <td>
            <input type="radio" id="filterByName" name="filterType" value="name" CHECKED><s:text name="label.name"/> 
            <input type="radio" id="filterByTag" name="filterType" value="tag"><s:text name="label.tags"/>
        </td>
      </tr>   
      <tr>
        <td class="boldText" width="20%"><s:text name="label.filter"/></td>
        <td><input type="text" id="filter" size="35" ></td>
      </tr>              
      <tr>
        <td valign="top" class="boldText"><s:text name="label.reports"/></td>
        <td>
          <select id="availableReports" class="checklist" size="16" style="width: 20em;" multiple>        
          <s:iterator id="report" value="reports">            
            <s:set name="available" value="true"/>    
            <s:iterator id="reportInGroup" value="reportsInGroup">     
              <s:if test="#report.id == #reportInGroup.id">
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


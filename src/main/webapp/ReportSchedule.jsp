<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />
<s:head/>

<style type="text/css">
   table {margin: 0px;}
   table td {padding: 0px;}
   table.dialog {margin: 10px;}
   table.dialog td {padding: 3px;}   
</style>

<s:actionerror/> 

<script language="JavaScript" src="js/date-picker.js"></script>

<div align="center">

  <a class="back-link img-report-small" href="reportList.action"><s:text name="link.back.reports"/></a>
  <a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.groups"/></a>
  	
  <br/><br/>
  
   <div class="img-schedule important" id="instructions" style="width: 70%;">
		<s:text name="reportSchedule.title"/> <s:property value="report.name"/>
   </div>
		 
  <form action="<s:property value="#request.get('struts.request_uri')"/>" name="scheduleDetail" class="dialog-form" style="width: 75%;">   
  
  <table class="dialog" >         
    <tr>
      <td class="boldText"><s:text name="label.description"/></td>         
      <td colspan="6">      
        <input type="text" name="description" value="<s:property value="description"/>" size="80">
      </td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.scheduleType"/></td>         
      <td>
        <input type="radio" name="scheduleType" value="0" CHECKED><s:text name="label.once"/>
      </td>
      <td>
			<input type="radio" name="scheduleType" value="5" <s:if test="scheduleType == 5">CHECKED</s:if> ><s:text name="label.hourly"/>
	  </td>
      <td>
        <input type="radio" name="scheduleType" value="1" <s:if test="scheduleType == 1">CHECKED</s:if> ><s:text name="label.daily"/>
      </td>
      <td>
        <input type="radio" name="scheduleType" value="4" <s:if test="scheduleType == 4">CHECKED</s:if> ><s:text name="label.weekdays"/>
      </td>     
    </tr>  
    <tr>
      <td class="boldText">&nbsp;</td> 
      <td>
        <input type="radio" name="scheduleType" value="2" <s:if test="scheduleType == 2">CHECKED</s:if> ><s:text name="label.weekly"/>
      </td>     
	  <td>
        <input type="radio" name="scheduleType" value="3" <s:if test="scheduleType == 3">CHECKED</s:if> ><s:text name="label.monthly"/>
      </td>  
      <s:if test="#session.user.advancedScheduler">      
      <td><input type="radio" name="scheduleType" value="6" <s:if test="scheduleType == 6">CHECKED</s:if> ><s:text name="label.cron"/></td>
      </s:if>
   	</tr>        
    <tr>
      <td class="boldText"><s:text name="label.startDate"/></td>         
      <td colspan="2">
      	<s:datetimepicker name="startDate" value="%{startDate}" theme="simple" displayFormat="%{dateFormat}"  />     
      </td>
      <s:if test="#session.user.advancedScheduler">  
      <td class="boldText"><s:text name="label.cronExpression"/> <font color="red">*</font></td>         
      <td colspan="2">
        <input type="text" name="cron" value="<s:property value="cron"/>" size="20" maxLength="20" />      
      </td>    
      </s:if>    
    </tr>    
    <tr>
     <td class="boldText"><s:text name="label.startTime"/></td>         
      <td colspan="2">
        <input type="text" name="startHour" value="<s:property value="startHour"/>" size="2" maxLength="2" /> :   
        <input type="text" name="startMinute" value="<s:property value="startMinute"/>" size="2" maxLength="2" />      
        <select name="startAmPm" value="<s:property value="startAmPm"/>">
          <option value="AM">AM</option>
          <option value="PM" <s:if test="startAmPm.equals('PM')">SELECTED</s:if> >PM</option>
        </select>
      </td>  
       <td class="boldText"><s:text name="label.numberOfHours"/><font color="red">*</font></td>         
      <td colspan="2">
     	 <input type="text" name="hours" value="<s:property value="hours"/>" size="20" maxLength="20" />    
      </td>      	
    </tr>   
    <s:if test="#session.user.alertUser">   
    <tr>
      <td class="boldText" width="20%"><s:text name="label.condition"/></td>
      <td colspan="6">
        <s:select name="alertId" list="alerts" listKey="id" listValue="name" headerKey="-1" headerValue=" -- None -- " theme="simple"/>			
        <s:select name="alertOperator" list="operators" emptyOption="false" theme="simple"/>                 
        <input type="text" name="alertLimit" value="<s:property value="alertLimit"/>" size="10" maxLength="10" />      
      </td>
    </tr> 
    </s:if>  
    <s:if test="!#session.user.alertUser">   
    	<input type="hidden" name="alertId" value="-1">
    </s:if>
     <tr>
      <td class="boldText"><s:text name="label.recipients"/></td>         
      <td colspan="6">      
        <textarea rows="2" cols="60" name="recipients"><s:property value="recipients"/></textarea>
      </td>
    </tr>     
  </table> 
  
  <br>
  

  <div class="button-bar" id="buttons" >
  	    <input type="hidden" name="scheduleName" value="<s:property value="scheduleName"/>">
 			    <input type="hidden" name="userId" value="<s:property value="userId"/>">
        <input type="submit" class="standardButton" name="submitScheduledReport" value="<s:text name="button.submit"/>">
  </div>
 
  
  </form>    
   
   <div class="error"> 
   		<s:text name="reportSchedule.message.hours"/><br>
   		<s:if test="#session.user.advancedScheduler">
   		<s:text name="reportSchedule.message.cron"/>
   		</s:if>
   </div>    
  
</div>

<s:include value="Footer.jsp" />


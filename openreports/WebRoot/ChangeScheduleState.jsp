<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<br/>

<div align="center">  
  
  <div class="important img-schedule" id="instructions"  style="width: 70%;"><s:text name="changeScheduleState.title"/></div>   
  
  <form action="changeScheduleState.action" class="dialog-form" style="width: 75%;">
  <table class="dialog" >   
    <tr class="a">
      <td class="boldText" width="20%"><s:text name="label.name"/></td>
      <td><s:property value="name"/></td>      
    </tr>   
     <tr>
      <td class="boldText" width="20%"><s:text name="label.description"/></td>
      <td><s:property value="description"/></td>      
    </tr>   
    <tr class="b">
      <td class="boldText" width="20%"><s:text name="label.startDate"/></td>
      <td><s:property value="startDate"/></>
    </tr>
    <tr class="a">
      <td class="boldText" width="20%"><s:text name="label.scheduleType"/></td>
      <td><s:property value="scheduleTypeName"/></>
    </tr>
     <tr >
      <td class="boldText" width="20%"><s:text name="label.currentState"/></td>
      <td><s:property value="scheduleState"/></td>      
    </tr>   
    </table>
   
    <div class="button-bar" id="buttons">
        <input class="standardButton" type="submit" name="submitOk" value="<s:property value="buttonName"/>">
        <input class="standardButton" type="submit" name="submitCancel" value="<s:text name="button.cancel"/>">
    	<input type="hidden" name="scheduleName" value="<s:property value="scheduleName"/>">     
	    <input type="hidden" name="userId" value="<s:property value="userId"/>">  
    </div>
   
  </form>
  <br> 
</div>

<s:include value="Footer.jsp" />



<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<br/><br/>

<div align="center">

  <div class="img-delete important" id="instructions" style="width: 70%;"><s:text name="deleteScheduledReport.title"/></div>  

  <form action="<s:property value="#request.get('struts.request_uri')"/>" class="dialog-form" style="width: 75%;">  
  
  <br>
  
  <table class="dialog">
    <tr>
      <td class="boldText" width="20%"><s:text name="label.name"/></td>
      <td><s:property value="name"/></td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.description"/></td>
      <td><s:property value="description"/></td>
    </tr>
    <tr>
      <td class="boldText" width="20%"><s:text name="label.startDate"/></td>
      <td><s:property value="startDate"/></td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.scheduleType"/></td>
      <td><s:property value="scheduleTypeName"/></td>
    </tr>     
  </table>
  
  <br>  
  
   <div class="button-bar" id="buttons">
        <input class="standardButton" type="submit" name="submitDelete" value="<s:text name="button.delete"/>">
        <input class="standardButton" type="submit" name="submitCancel" value="<s:text name="button.cancel"/>">
        <input type="hidden" name="userId" value="<s:property value="userId"/>">
        <input type="hidden" name="scheduleName" value="<s:property value="scheduleName"/>">  
   </div>  
   
 </form>  
      
  <br> 
  
  <div class="error"><s:actionerror/></div> 
  
</div>

<s:include value="Footer.jsp" /> 


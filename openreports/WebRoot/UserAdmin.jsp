<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<script type="text/javascript">
  var userAdminReportTabs = new YAHOO.widget.TabView("userAdminReportTabs");
</script>

<style>
  .yui-navset ul {
    padding-right: 450px;
  }
</style>

<div align="center">

<br/>

<div id="userAdminReportTabs" class="yui-navset" style="width: 800px;">
  <ul class="yui-nav">
    <li class="selected"><a href="preferences"><em><s:text name="userAdmin.preferences"/></em></a></li>
    <s:if test="user.alertUser">    
    <li><a href="alerts"><em><s:text name="userAdmin.alerts"/></em></a></li>
    </s:if>
  </ul>            
  <div class="yui-content">
  
    <div id="preferences"> 
  
    <form action="userAdmin.action">
  
    <table class="dialog">
      <tr class="a">
        <td class="boldText"><s:text name="label.username"/></td>
        <td><input type="text" size="60" name="name" value="<s:property value="name"/>"></td>
      </tr>
      <tr class="b">
        <td class="boldText"><s:text name="label.password"/></td>
        <td><input type="password" size="60" name="password" value="<s:property value="password"/>"></td>
      </tr>    
      <tr class="a">
        <td class="boldText"><s:text name="label.confirmPassword"/></td>
        <td><input type="password" size="60" name="passwordConfirm" value="<s:property value="passwordConfirm"/>"></td>
      </tr>      
      <tr class="b">
        <td class="boldText"><s:text name="label.email"/></td>
        <td><input type="text" size="60" name="email" value="<s:property value="email"/>"></td>
      </tr>  
      <s:if test="#session.user.dashboardUser">  
      <tr>
    	<td class="boldText"><s:text name="label.dashboardReport"/></td>
    	<td>
   	     <select name="reportId">    
   	      <option value="-1">(None)</option>     
          <s:iterator id="report" value="reports">
           <option value="<s:property value="id"/>" <s:if test="id == reportId">selected="selected"</s:if> /><s:property value="name"/>
          </s:iterator>
        </select>   	    
   	   </td>  
   	  </tr>
   	  </s:if>  	    
      <tr>
        <td align="center" class="dialogButtons" colspan="2">
        <hr>       
      	<input class="standardButton" type="submit" name="submitType" value="<s:text name="button.save"/>">       
        </td>
      </tr>   
    </table>
    
    </form>  
    
    </div>
  
    <s:if test="user.alertUser">   
  
    <div id="alerts"> 
  
    <table class="dialog">
      <tr>
        <td class="boldText" colspan="2"><s:text name="label.alert"/></td>
        <td class="boldText"><s:text name="label.operator"/>&nbsp;</td>     
        <td class="boldText"><s:text name="label.limit"/></td>     
        <td class="boldText"><s:text name="label.report"/></td>   
        <td class="boldText" colspan="2">&nbsp;</td>
      </tr>      
      <s:iterator id="alert" value="user.alerts" status="iteratorStatus">
      <form action="userAdminAlerts.action" class="alert-form" >
      <tr class="a">   	 
   	    <td colspan="2">
   	      <s:property value="alert.name"/>   	       	
   	    </td>     	     	      	  
   	    <td>
   	      <select name="alertOperator">        	   
           <s:iterator id="aOperator" value="operators">
           	<option value="<s:property/>" <s:if test="#aOperator.equals(#alert.operator)">selected="selected"</s:if> /><s:property/>
           </s:iterator>
          </select>   	    
   	    </td>  
   	    <td>
   	      <input type="text" size="6" name="alertLimit" value="<s:property value="limit"/>">   	    
   	    </td>  
   	    <td>
   	      <select name="reportId">    
   	      <option value="-1"> -- None -- </option>    
   	       <s:iterator id="report" value="reports">
           <option value="<s:property value="id"/>" <s:if test="id == report.id">selected="selected"</s:if> /><s:property value="name"/>
          </s:iterator>        
          </select>   	    
   	    </td>  
   	    <td class="dialogButtons">
   	  	 <input class="standardButton" type="submit" name="submitUpdate" value="<s:text name="button.update"/>">
   	    </td> 
   	    <td class="dialogButtons">
   	  	 <input class="standardButton" type="submit" name="submitDelete" value="<s:text name="button.delete"/>">
   	    </td> 
      </tr>   
      <input type="hidden" name="id" value="<s:property value="id"/>">
      <input type="hidden" name="alertId" value="<s:property value="#iteratorStatus.index + 1" />"/>
    </form>
    </s:iterator>   
    <form action="userAdminAlerts.action" class="alert-form">          
    <tr class="a">
      <td colspan="2">
        <select name="alertId">  
           <s:iterator id="alert" value="alerts">             
            <option value="<s:property value="id"/>"><s:property value="name"/></option>
          </s:iterator>
        </select>
      </td>   
      <td>
   	      <select name="alertOperator">         
          <s:iterator id="operator" value="operators">       
          <option value="<s:property value="operator"/>"><s:property value="operator"/> &nbsp;</option>
          </s:iterator>
        </select>   	    
   	  </td>  
   	  <td>
   	    <input type="text" size="6" name="alertLimit" value="0">   	    
   	  </td>  
   	  <td>
   	     <select name="reportId">    
   	      <option value="-1">(None)</option>     
          <s:iterator id="report" value="reports">
          <option value="<s:property value="id"/>"/><s:property value="name"/>
          </s:iterator>
        </select>   	    
   	  </td>     
      <td class="dialogButtons">
      	<input class="standardButton" type="submit" name="submitAdd" value="<s:text name="button.add"/>">
      </td>             
    </tr> 
  	<input type="hidden" name="id" value="<s:property value="id"/>"> 
  	<input type="hidden" name="command" value="<s:property value="command"/>"> 
  	</form>  
    
    </table>
  
    </div>
  
    </s:if>
  
  </div>
  
</div>
 
<s:include value="Footer.jsp" />


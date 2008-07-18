<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />
<s:head/>

<style type="text/css">
   table {margin: 0px;}
   table td {padding: 0px;}
   table.dialog {margin: 10px;}
   table.dialog td {padding: 3px;}   
</style>

<div align="center">  

<s:if test="!report.displayInline">  
  <a class="back-link img-report-small" href="reportList.action"><s:text name="link.back.reports"/></a>
  <a class="back-link img-group-small" href="reportGroup.action"><s:text name="link.back.groups"/></a>  

  <br/><br/>
   
  <div class="img-param important" id="instructions" style="width: 70%;">
	<s:text name="reportDetail.title"/> <s:property value="report.name"/>
  </div> 

</s:if> 
  
  <form action="reportDetail.action" name="reportDetail" method="post" class="dialog-form" style="width: 75%;" >  
    
  <table class="dialog"> 
  	<s:iterator id="reportParameterMap" value="reportParameters">    
    <tr>
      <td class="boldText">     
      	<s:property value="reportParameter.description"/>
      	<s:if test="required">         	
      	    *
      	</s:if>    	
      </td>     
      <s:if test="reportParameter.type.equals('Text')">        
      <td>
        <input type="text" name="<s:property value="reportParameter.name"/>" value="<s:property value="reportParameter.defaultValue"/>" length="50">       
      </td>
      </s:if>    
      <s:if test="reportParameter.type.equals('Date')">   
       <td> 
       	<s:if test="required && reportParameter.defaultValue == null">
      	 	<s:datetimepicker name="%{reportParameter.name}" value="%{defaultDate}" theme="simple" displayFormat="%{dateFormat}"  />
        </s:if>
        <s:if test="reportParameter.defaultValue != null">
       		<s:datetimepicker name="%{reportParameter.name}" value="%{reportParameter.defaultValue}" theme="simple" displayFormat="%{dateFormat}" />
        </s:if>
        <s:if test="!required && reportParameter.defaultValue == null">
       		<s:datetimepicker name="%{reportParameter.name}" theme="simple" displayFormat="%{dateFormat}" />
        </s:if>        
       </td>   
      </s:if>     
      <s:if test="reportParameter.type.equals('Query') || reportParameter.type.equals('List') || reportParameter.type.equals('Boolean') ">         
      <td>       
        <select name="<s:property value="reportParameter.name"/>"  <s:if test="reportParameter.multipleSelect">size="4" multiple</s:if> >        
		  <s:if test="required && reportParameter.defaultValue == null !reportParameter.type.equals('Boolean') ">  
		    <option value="" SELECTED>(None)</option>
		  </s:if>
		  <s:iterator id="value" value="reportParameter.values">		            
            <option value="<s:property value="id"/>" <s:if test="description.equals(reportParameter.defaultValue)">SELECTED</s:if> ><s:property value="description"/></option>
          </s:iterator>
        </select>
      </td>
      </s:if>    
    </tr>      
    </s:iterator>        
 </table>  
    
	<div class="button-bar" id="buttons" >
	   <input class="standardButton" type="submit" value="Ok" name="submitType">        
       <input type="hidden" name="reportId" value="<s:property value="reportId"/>">        
       <input type="hidden" name="step" value="<s:property value="step"/>">      
       <input type="hidden" name="displayInline" value="<s:property value="displayInline"/>">
	</div>  
  
  </form>  
  
  <div class="importantSmall"><s:text name="reportDetail.requiredParameters"/></div>
  
  <br/>
  
  <div class="error">
  	<s:actionerror/>
  </div> 
 
</div>

<s:include value="Footer.jsp" />

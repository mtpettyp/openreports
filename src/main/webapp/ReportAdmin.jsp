<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<div align="center">

	<br/>		
	           
	<table class="adminMenu">
 	<tr>
 		<th><s:text name="reportAdmin.tab.reportAdmin"/><hr/></th>  		
 		<th><s:text name="reportAdmin.tab.userAdmin"/><hr/></th> 
 		<th><s:text name="reportAdmin.tab.generalAdmin"/><hr/></th> 			
 	</tr>  	
    <tr> 
        <td style="vertical-align: top;"><table>
        
        <s:if test="#session.user.alertAdmin">    		
        <tr class="even">        
    	<td class="img-alert">      	
		  	<a href="listAlerts.action"><s:text name="link.admin.alerts"/></a> <br/>
         	<s:text name="reportAdmin.message.alerts"/>         
        </td> 
        </tr>
        </s:if> 
        <s:if test="#session.user.chartAdmin">  
        <tr class="even">   	
        <td class="img-chart">        
        	<a href="listCharts.action"><s:text name="link.admin.charts"/></a> <br/>
        	<s:text name="reportAdmin.message.charts"/>          
	    </td>
	    </tr>
	    </s:if>
	    <s:if test="#session.user.dataSourceAdmin">    
	    <tr class="even">   
	    <td class="img-ds">             
         	<a href="listDataSources.action"><s:text name="link.admin.dataSources"/></a><br/>
         	<s:text name="reportAdmin.message.dataSources"/>         	
       	</td>
       	</tr>
       	</s:if> 
       	<s:if test="#session.user.reportAdmin">
       	<tr class="even">
       	<td class="img-report" > 		
			<a href="listReports.action"><s:text name="link.admin.reports"/></a><br/>
			<s:text name="reportAdmin.message.reports"/>            
        </td>
        </tr> 
        </s:if>  
        <s:if test="#session.user.parameterAdmin">       
        <tr class="even">
        <td class="img-param">           
        	<a href="listReportParameters.action"><s:text name="link.admin.reportParameters"/></a><br/>  
        	<s:text name="reportAdmin.message.reportParameters"/>          
        </td>        
        </tr>
        </s:if> 
        
        </table></td>     
         
    	<td style="vertical-align: top;"><table>
    	
    	<s:if test="#session.user.groupAdmin">      
    	<tr class="even">  
 		<td class="img-group">             
         	<a href="listGroups.action"><s:text name="link.admin.groups"/></a>  <br/>
         	<s:text name="reportAdmin.message.groups"/>         
        </td>
        </tr>
        </s:if>
        <s:if test="#session.user.rootAdmin">  
        <tr class="even">
        <td class="img-user">       	
         	<a href="listUsers.action"><s:text name="link.admin.users"/></a>   <br/>
         	<s:text name="reportAdmin.message.users"/>            
        </td>         
        </tr>
        </s:if>
        
        </table></td>
  		<td style="vertical-align: top;"><table>  		
  			
  		<s:if test="#session.user.schedulerAdmin">  
  		<tr class="even">    
        <td class="img-schedule">          
          <a class="icon" href="schedulerAdmin.action"><s:text name="link.admin.scheduler"/></a><br/>   
          <s:text name="reportAdmin.message.scheduler"/>        
        </td> 
        </tr>
        </s:if>
        <tr class="even">	      
        <td class="img-log">
          <a class="icon" href="search.action"><s:text name="link.admin.search"/></a><br/>   
          <s:text name="reportAdmin.message.search"/>
        </td> 
        </tr>
        <s:if test="#session.user.rootAdmin"> 
        <tr class="even">
       	<td class="img-settings">         	 
         	<a href="editProperties.action"><s:text name="link.admin.settings"/></a><br/>
         	<s:text name="reportAdmin.message.settings"/>        
        </td>
        </tr>
        </s:if>       
        <s:if test="#session.user.logViewer">  
        <tr class="even">  
        <td class="img-queryreport">        
         	<a href="viewReportLogs.action"><s:text name="link.admin.logs"/></a><br/>
         	<s:text name="reportAdmin.message.logs"/>       
        </td>
        </tr>
        </s:if>         
		
		</td></tr></table> 
	
	</td></tr></table>   
          
</div>

<s:include value="Footer.jsp" />


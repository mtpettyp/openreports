<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<script type="text/javascript">
  var adminTabs = new YAHOO.widget.TabView("adminTabs");
</script>

<style>
  .yui-navset ul {
    padding-right: 450px;
  }
</style>

<div align="center">

	<br/>	
	
	<div id="adminTabs" class="yui-navset" style="width: 800px;">
  		<ul class="yui-nav">
    		<li class="selected"><a href="reportAdmin"><em><s:text name="reportAdmin.tab.reportAdmin"/></em></a></li>
    		<li><a href="userAdmin"><em><s:text name="reportAdmin.tab.userAdmin"/></em></a></li>
    		<li><a href="generalAdmin"><em><s:text name="reportAdmin.tab.generalAdmin"/></em></a></li>    
  		</ul>            
 	<div class="yui-content">
  
    <div id="reportAdmin">     
				
	<ul class="adminmenu">			
		<s:if test="#session.user.reportAdmin">
		<li class="img-report">	   	
			<a href="listReports.action"><s:text name="link.admin.reports"/></a><br/>
			<s:text name="reportAdmin.message.reports"/>       
        </li>
        <hr/>
        </s:if> 
        <s:if test="#session.user.parameterAdmin">          
        <li class="img-param">   
        	<a href="listReportParameters.action"><s:text name="link.admin.reportParameters"/></a><br/>  
        	<s:text name="reportAdmin.message.reportParameters"/>        	
        </li>
        <hr/>
        </s:if>   
        <s:if test="#session.user.dataSourceAdmin">         
        <li class="img-ds">
         	<a href="listDataSources.action"><s:text name="link.admin.dataSources"/></a><br/>
         	<s:text name="reportAdmin.message.dataSources"/>
        </li>
        <hr/>        
       	</s:if>       
       	<s:if test="#session.user.alertAdmin">    		
		<li class="img-report">         	
         	<a href="listAlerts.action"><s:text name="link.admin.alerts"/></a> <br/>
         	<s:text name="reportAdmin.message.alerts"/>        
        </li>
        <hr/>	
        </s:if>    
        <s:if test="#session.user.chartAdmin">  
        <li class="img-chart"> 
        	<a href="listCharts.action"><s:text name="link.admin.charts"/></a> <br/>
        	<s:text name="reportAdmin.message.charts"/>        	
        </li>
       	<hr/>
        </s:if>  
        
    </ul>
    </div>
    
    <div id="userAdmin">
    <ul class="adminmenu">
         
        <s:if test="#session.user.groupAdmin">          
        <li class="img-group">  	
         	<a href="listGroups.action"><s:text name="link.admin.groups"/></a>  <br/>
         	<s:text name="reportAdmin.message.groups"/>         	
        </li>
        <hr/>
        </s:if>
       	<s:if test="#session.user.rootAdmin">  
        <li class="img-user">         
         	<a href="listUsers.action"><s:text name="link.admin.users"/></a>   <br/>
         	<s:text name="reportAdmin.message.users"/>      
        </li>
        <hr/>
        </s:if> 
    
    </ul>
    </div>
    
    <div id="generalAdmin">
    <ul class="adminmenu">
           
        <s:if test="#session.user.schedulerAdmin">  
        <li class="img-schedule">
          <a class="icon" href="schedulerAdmin.action"><s:text name="link.admin.scheduler"/></a><br/>   
          <s:text name="reportAdmin.message.scheduler"/>
        </li>
        <hr/>     
        </s:if>
        <li class="img-schedule">
          <a class="icon" href="search.action"><s:text name="link.admin.search"/></a><br/>   
          <s:text name="reportAdmin.message.search"/>
        </li>
        <hr/>        	
       	<s:if test="#session.user.rootAdmin">  
       	<li class="img-settings">
         	<a href="editProperties.action"><s:text name="link.admin.settings"/></a><br/>
         	<s:text name="reportAdmin.message.settings"/>
        </li>
        <hr/>
        </s:if>       
        <s:if test="#session.user.logViewer">  
        <li class="img-log">
         	<a href="viewReportLogs.action"><s:text name="link.admin.logs"/></a><br/>
         	<s:text name="reportAdmin.message.logs"/>
        </li>
        <hr/>
        </s:if>       
     </ul>  
     </div>
     </div>     	
        
</div>

<s:include value="Footer.jsp" />


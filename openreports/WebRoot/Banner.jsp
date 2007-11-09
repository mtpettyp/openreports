<%@ taglib prefix="s" uri="/struts-tags" %>

<html>

<head>
  <meta HTTP-EQUIV="Pragma" CONTENT="public">
  <meta HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
  <title><s:text name="application.title"/></title>
  <link rel="stylesheet" type="text/css" href="css/yui/reset-fonts-grids.css" />
  <link href="css/openreports.css" rel="stylesheet" type="text/css">
  <!-- YUI Dependencies -->
  <script type="text/javascript" src="js/yui/yahoo-min.js"></script>
  <script type="text/javascript" src="js/yui/dom-min.js"></script>
  <script type="text/javascript" src="js/yui/event-min.js"></script>
  <script type="text/javascript" src="js/yui/animation-min.js"></script>
  <script type="text/javascript" src="js/yui/dragdrop-min.js"></script>
  <script type="text/javascript" src="js/yui/connection-min.js"></script>
  <script type="text/javascript" src="js/yui/container-min.js"></script>
  <script type="text/javascript" src="js/yui/element-beta-min.js"></script>
  <script type="text/javascript" src="js/yui/tabview-min.js"></script>
  <script type="text/javascript" src="js/yui/button-beta-min.js"></script> 
  <link rel="stylesheet" type="text/css" href="css/yui/container.css" />
  <link rel="stylesheet" type="text/css" href="css/yui/tabview.css">
  <link rel="stylesheet" type="text/css" href="css/yui/button.css">    
  <!-- End YUI Dependencies -->
  <script type="text/javascript" src="js/highlight.js"></script>
</head>

<body class="yui-skin-sam">

<s:if test="report == null || !report.isDisplayInline()">

<div class="menu"> 

	<div>
		<ul class="vert">			
			<li class="logo">			  
				<s:text name="application.title"/>			
			</li>                     
		</ul>
	</div>   
	<s:if test="#session.breadcrumbs != null">  
	<div id="usermenu">	    
  		<ul class="vert">   	
  		  <s:if test="#session.user">	 
          <li>
          	<span id="logoffButton" class="yui-button yui-link-button"> 
	    		<span class="first-child"> 
          			<a href="logout.action"><s:text name="banner.logoff"/></a>
          		</span>
          	</span>                   
          </li>         
          <li>
            <span id="userAdminButton" class="yui-button yui-link-button <s:if test="#session.breadcrumbs.startsWith('userAdmin')">yui-button-hover yui-link-button-hover</s:if>"> 
	    		<span class="first-child"> 
          			<a href="userAdmin.action">
          				<s:text name="banner.preferences"/>
          			</a>
          		</span>
          	</span>             
          </li> 
          </s:if>
          <s:if test="#session.user.adminUser">
          	<li>
          		<span id="adminButton" class="yui-button yui-link-button <s:if test="#session.breadcrumbs.startsWith('reportAdmin')">yui-button-hover yui-link-button-hover</s:if>"> 
	    			<span class="first-child"> 
            			<a  href="reportAdmin.action">
            				<s:text name="banner.administration"/>
            			</a>
            		</span>
            	</span>
            </li>
          </s:if>  
          <s:if test="#session.user.scheduler">
          <li>
          	<span id="schedulerButton" class="yui-button yui-link-button <s:if test="#session.breadcrumbs.startsWith('listScheduledReports')">yui-button-hover yui-link-button-hover</s:if>"> 
	    		<span class="first-child"> 
          			<a href="listScheduledReports.action">
          				<s:text name="banner.scheduledReports"/>
          			</a>
          		</span>
          	</span> 
          </li> 
          </s:if>
          <s:if test="#session.user">
          <li>
          	<span id="reportsButton" class="yui-button yui-link-button <s:if test="#session.breadcrumbs.startsWith('reportGroup')">yui-button-hover yui-link-button-hover</s:if>"> 
	    		<span class="first-child"> 
          			<a href="reportGroup.action">
          				<s:text name="banner.reports"/>
          			</a>
          		</span>
          	</span> 
          </li>
          </s:if>
          <s:if test="#session.user.dashboardUser">
          <li>
          	<span id="dashboardButton" class="yui-button yui-link-button <s:if test="#session.breadcrumbs.startsWith('dashboard')">yui-button-hover yui-link-button-hover</s:if>"> 
	    		<span class="first-child"> 
          			<a href="dashboard.action">
          				<s:text name="banner.dashboard"/>
          			</a>
          		</span>
          	</span>          
          </li>
          </s:if>      
        </ul>
    </div>
    </s:if>    
    
</div> 

<script type="text/javascript">
	var menuButton1 = new YAHOO.widget.Button("logoffButton");
	var menuButton2 = new YAHOO.widget.Button("reportsButton");
	var menuButton3 = new YAHOO.widget.Button("adminButton");
	var menuButton4 = new YAHOO.widget.Button("dashboardButton");
	var menuButton4 = new YAHOO.widget.Button("schedulerButton");
	var menuButton4 = new YAHOO.widget.Button("userAdminButton");
</script>    

</s:if>
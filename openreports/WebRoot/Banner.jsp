<%@ taglib prefix="s" uri="/struts-tags" %>

<html>

<head>
  <meta HTTP-EQUIV="Pragma" CONTENT="public">
  <meta HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
  <title>OpenReports</title>
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
  <link rel="stylesheet" type="text/css" href="css/yui/container.css" />
  <link rel="stylesheet" type="text/css" href="css/yui/tabview.css">
  <link rel="stylesheet" type="text/css" href="css/yui/border_tabs.css">
  <!-- End YUI Dependencies -->
  <script type="text/javascript" src="js/highlight.js"></script>
</head>

<body>

<s:if test="report == null || !report.isDisplayInline()">

<div class="menu"> 

	<div>
		<ul class="vert">
			<li>
				<img style="padding: 5px;" src="images/logo.gif"/>				
			</li>
			<li class="logo">
				OpenReports			
			</li>
		</ul>
	</div>   
	<s:if test="#session.breadcrumbs != null">  
	<div id="usermenu">	    
  		<ul class="vert">   		 
          <li>
          	<a href="logout.action">Logoff</a>          
          </li>
          <li>
          	<a href="userAdmin.action" <s:if test="#session.breadcrumbs.startsWith('userAdmin')">class="selected"</s:if>>
          		Preferences
          	</a>             
          </li> 
          <s:if test="#session.user.adminUser">
          	<li>
            	<a href="reportAdmin.action" <s:if test="#session.breadcrumbs.startsWith('reportAdmin')">class="selected"</s:if>>
            		Administration
            	</a>
            </li>
          </s:if>  
          <s:if test="#session.user.scheduler">
          <li>
          	<a href="listScheduledReports.action" <s:if test="#session.breadcrumbs.startsWith('listScheduledReports')">class="selected"</s:if>>
          		Scheduled Reports
          	</a> 
          </li> 
          </s:if>
          <li>
          	<a href="reportGroup.action" <s:if test="#session.breadcrumbs.startsWith('reportGroup')">class="selected"</s:if>>
          		Reports
          	</a> 
          </li>
          <s:if test="#session.user.dashboardUser">
          <li>
          	<a href="dashboard.action" <s:if test="#session.breadcrumbs.startsWith('dashboard')">class="selected"</s:if>>
          		Dashboard
          	</a>          
          </li>
          </s:if>      
        </ul>
    </div>
    </s:if>    
    
</div> 

</s:if>
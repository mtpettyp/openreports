--------------------------------------------------
OpenReports - JPivot/Mondrian Integration - README
--------------------------------------------------

Notes
-----

1) In order to run OLAP/JPivot reports under JDK 1.6+, the xalan-2.6.0.jar must be removed 
from the WEB-INF/lib directory before deploying OpenReports. 

2) A common problem occurs with the Catalog Definition in datasources.xml. This must contain the 
full path to your Mondrian schema. In the example, the path is:

c:\openreports-tomcat\reports\SampleData.mondrian.xml

If you are running OpenReports on Linux, you must add file:// to the path to indicate the resource 
is a File, for example:

file:///usr/share/openreports-tomcat/reports/SampleData.mondrian.xml 
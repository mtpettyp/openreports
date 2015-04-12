---------------------
OpenReports - README
---------------------

See the changes.txt for a list of changes in this release of OpenReports.

--------------------
**** IMPORTANT *****
--------------------

Starting with OpenReports 1.0 the baseDir and mail.smtp.host properties are
set from the General Settings Administration page. The baseDir must be set 
to the full path to your report template files in order to run reports. 
The mail.smtp.host property must set in order to email scheduled reports.

---------------
Getting Started
---------------

1) Create OpenReports and Quartz tables in your database
	- See database/schema folders for the DDL
2) Insert an Admin user into REPORT_USER table
3) Update openreports.properties with your database connection information.
4) Deploy the expanded webapp, or generate a WAR file using 'ant war'.
5) Set baseDir and mail.smtp.host properties from General Settings Admin page.

To use the OpenReports sample databases, start the databases using the
startup script in the openreports-database folder, and skip steps 1-3.

---------------

Read the OpenReports Install Guide in the docs folder for more information.

Please post any questions, comments or problems to the OpenReports
forum on SourceForge.
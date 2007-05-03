---------------------
OpenReports - README
---------------------

See the changes.txt for a list of changes in this release of OpenReports.

--------------------
**** IMPORTANT *****
--------------------

Starting with OpenReports 1.0 the baseDir and mail.smtp.host properties are
set from the General Settings Administration page. The openreports.properties
file has been removed. The baseDir must be set to the full path to your 
JasperReports files in order to run JasperReports. The mail.smtp.host property
must set in order to email scheduled reports.

---------------
Getting Started
---------------

1) Create OpenReports and Quartz tables in your database
	- See database/schema folders for the DDL
2) Insert an Admin user into REPORT_USER table
3) Update hibernate.properties with your database connection information.
4) Update quartz.properties with your database connection information.
5) Deploy the expanded webapp, or generate a WAR file using 'ant war'.
6) Set baseDir and mail.smtp.host properties from General Settings Admin page.

To use the OpenReports sample databases, start the databases using the
startup script in the openreports-database folder, and skip steps 1-4.

---------------

Read the OpenReports Install Guide in the docs folder for more information.

Please post any questions, comments or problems to the OpenReports
forum on SourceForge.
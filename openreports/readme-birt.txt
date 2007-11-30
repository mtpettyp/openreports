---------------------------------------
OpenReports - BIRT Integration - README
---------------------------------------

Starting with OpenReports 2.1-M1, the Eclipse BIRT reporting system is
integrated into OpenReports. This integration allows you to run and 
schedule BIRT reports through the OpenReports interface.

--------------------
**** Important *****
--------------------

There are two steps that need to be performed before using BIRT reports
with OpenReports:

- Create a 'temp' directory under your 'reports/images' directory.

- Download the or-3.0-birt-2.2-platform.zip file and unzip to your 
  'reports' directory.  
  
--------------------
** Image Cleanup **
--------------------
  
When exporting reports to HTML, the BIRT engine will create temporary
image files in your 'reports/images/temp' directory. The Image Cleanup
tab on the Administration Settings page provides the ability to delete
these files.

--------------------
  **** Notes ****
--------------------

- OpenReports will override the datasource properties contained in your
  .rptdesign files if the datasource name matches the name of an existing
  OpenReports datasource.
 
- If you are experiencing Out of Memory errors when running BIRT 
  reports, try increasing the maximum heap size allocated by Java to 
  your application server.
  
--------------------
 ** Contributions **
--------------------

I would like to thank Roberto Nibali, Joe Ammann, and Jason Weathersby for 
their contributions to the OpenReports BIRT integration project.


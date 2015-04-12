create table OR_PROPERTIES (PROPERTY_ID number(10,0) not null, PROPERTY_KEY varchar2(255 char) not null unique, PROPERTY_VALUE varchar2(255 char), primary key (PROPERTY_ID))

create table OR_TAGS (TAG_ID number(10,0) not null, TAGGED_OBJECT_ID number(10,0) not null, TAGGED_OBJECT_CLASS varchar2(255 char) not null, TAG_VALUE varchar2(255 char) not null, TAG_TYPE varchar2(255 char) not null, primary key (TAG_ID))

create table REPORT (REPORT_ID number(10,0) not null, NAME varchar2(255 char) not null unique, DESCRIPTION varchar2(255 char) not null, REPORT_FILE varchar2(255 char) not null, PDF_EXPORT number(1,0) not null, CSV_EXPORT number(1,0) not null, XLS_EXPORT number(1,0) not null, HTML_EXPORT number(1,0) not null, RTF_EXPORT number(1,0) not null, TEXT_EXPORT number(1,0) not null, EXCEL_EXPORT number(1,0) not null, IMAGE_EXPORT number(1,0) not null, FILL_VIRTUAL number(1,0) not null, HIDDEN_REPORT number(1,0) not null, REPORT_QUERY clob, DATASOURCE_ID number(10,0), CHART_ID number(10,0), EXPORT_OPTION_ID number(10,0), primary key (REPORT_ID))

create table REPORT_ALERT (ALERT_ID number(10,0) not null, NAME varchar2(255 char) not null unique, DESCRIPTION varchar2(255 char) not null, ALERT_QUERY clob not null, DATASOURCE_ID number(10,0), primary key (ALERT_ID))

create table REPORT_CHART (CHART_ID number(10,0) not null, NAME varchar2(255 char) not null unique, DESCRIPTION varchar2(255 char) not null, CHART_QUERY clob not null, CHART_TYPE number(10,0) not null, WIDTH number(10,0) not null, HEIGHT number(10,0) not null, X_AXIS_LABEL varchar2(255 char), Y_AXIS_LABEL varchar2(255 char), SHOW_LEGEND number(1,0) not null, SHOW_TITLE number(1,0) not null, SHOW_VALUES number(1,0) not null, PLOT_ORIENTATION number(10,0), DATASOURCE_ID number(10,0), REPORT_ID number(10,0), OVERLAY_CHART_ID number(10,0), primary key (CHART_ID))

create table REPORT_DATASOURCE (DATASOURCE_ID number(10,0) not null, NAME varchar2(255 char) not null unique, DRIVER varchar2(255 char), URL varchar2(255 char) not null, USERNAME varchar2(255 char), PASSWORD varchar2(255 char), MAX_IDLE number(10,0), MAX_ACTIVE number(10,0), MAX_WAIT number(19,0), VALIDATION_QUERY varchar2(255 char), JNDI number(1,0), primary key (DATASOURCE_ID))

create table REPORT_DELIVERY_LOG (DELIVERY_LOG_ID number(10,0) not null, START_TIME timestamp, END_TIME timestamp, STATUS varchar2(255 char), MESSAGE clob, DELIVERY_METHOD varchar2(255 char), LOG_ID number(10,0), DELIVERY_INDEX number(10,0), primary key (DELIVERY_LOG_ID))

create table REPORT_EXPORT_OPTIONS (EXPORT_OPTION_ID number(10,0) not null, XLS_REMOVE_EMPTY_SPACE number(1,0) not null, XLS_ONE_PAGE_PER_SHEET number(1,0) not null, XLS_AUTO_DETECT_CELL number(1,0) not null, XLS_WHITE_BACKGROUND number(1,0) not null, HTML_REMOVE_EMPTY_SPACE number(1,0) not null, HTML_WHITE_BACKGROUND number(1,0) not null, HTML_USE_IMAGES number(1,0) not null, HTML_WRAP_BREAK number(1,0) not null, primary key (EXPORT_OPTION_ID))

create table REPORT_GROUP (GROUP_ID number(10,0) not null, NAME varchar2(255 char) not null unique, DESCRIPTION varchar2(255 char) not null, primary key (GROUP_ID))

create table REPORT_GROUP_MAP (GROUP_ID number(10,0) not null, REPORT_ID number(10,0) not null, MAP_ID number(10,0) not null, primary key (GROUP_ID, MAP_ID))

create table REPORT_LOG (LOG_ID number(10,0) not null, START_TIME timestamp, END_TIME timestamp, STATUS varchar2(255 char), MESSAGE clob, EXPORT_TYPE number(10,0), REQUEST_ID varchar2(255 char), REPORT_ID number(10,0), USER_ID number(10,0), ALERT_ID number(10,0), primary key (LOG_ID))

create table REPORT_PARAMETER (PARAMETER_ID number(10,0) not null, NAME varchar2(255 char) not null unique, TYPE varchar2(255 char) not null, CLASSNAME varchar2(255 char) not null, DATA clob, DATASOURCE_ID number(10,0), DESCRIPTION varchar2(255 char), REQUIRED number(1,0), MULTI_SELECT number(1,0), DEFAULT_VALUE varchar2(255 char), primary key (PARAMETER_ID))

create table REPORT_PARAMETER_MAP (REPORT_ID number(10,0) not null, PARAMETER_ID number(10,0), REQUIRED number(1,0), SORT_ORDER number(10,0), STEP number(10,0), MAP_ID number(10,0) not null, primary key (REPORT_ID, MAP_ID))

create table REPORT_USER (REPORTUSER_ID number(10,0) not null, NAME varchar2(255 char) not null unique, PASSWORD varchar2(255 char) not null, EXTERNAL_ID varchar2(255 char), EMAIL_ADDRESS varchar2(255 char), PDF_EXPORT_TYPE number(10,0) not null, DEFAULT_REPORT_ID number(10,0), primary key (REPORTUSER_ID))

create table USER_ALERT_MAP (USER_ID number(10,0) not null, ALERT_ID number(10,0), REPORT_ID number(10,0), ALERT_LIMIT number(10,0), ALERT_OPERATOR varchar2(255 char), MAP_ID number(10,0) not null, primary key (USER_ID, MAP_ID))

create table USER_GROUP_MAP (USER_ID number(10,0) not null, GROUP_ID number(10,0) not null, MAP_ID number(10,0) not null, primary key (USER_ID, MAP_ID))

create table USER_SECURITY (USER_ID number(10,0) not null, ROLE_NAME varchar2(255 char))

alter table REPORT add constraint FK8FDF4934F4DD5A50 foreign key (EXPORT_OPTION_ID) references REPORT_EXPORT_OPTIONS

alter table REPORT add constraint FK8FDF49344330D5A7 foreign key (DATASOURCE_ID) references REPORT_DATASOURCE

alter table REPORT add constraint FK8FDF4934164AA2ED foreign key (CHART_ID) references REPORT_CHART

alter table REPORT_ALERT add constraint FKF81C86714330D5A7 foreign key (DATASOURCE_ID) references REPORT_DATASOURCE

alter table REPORT_CHART add constraint FKF836D4F3AAEF4A13 foreign key (REPORT_ID) references REPORT

alter table REPORT_CHART add constraint FKF836D4F34330D5A7 foreign key (DATASOURCE_ID) references REPORT_DATASOURCE

alter table REPORT_CHART add constraint FKF836D4F3C83B69FC foreign key (OVERLAY_CHART_ID) references REPORT_CHART

alter table REPORT_DELIVERY_LOG add constraint FKC783FD84632801ED foreign key (LOG_ID) references REPORT_LOG

alter table REPORT_GROUP_MAP add constraint FKEF946211AAEF4A13 foreign key (REPORT_ID) references REPORT

alter table REPORT_GROUP_MAP add constraint FKEF946211DF17134D foreign key (GROUP_ID) references REPORT_GROUP

alter table REPORT_LOG add constraint FK901BE599E4B42987 foreign key (USER_ID) references REPORT_USER

alter table REPORT_LOG add constraint FK901BE599AAEF4A13 foreign key (REPORT_ID) references REPORT

alter table REPORT_LOG add constraint FK901BE59920DA4A2D foreign key (ALERT_ID) references REPORT_ALERT

alter table REPORT_PARAMETER add constraint FKBC64163E4330D5A7 foreign key (DATASOURCE_ID) references REPORT_DATASOURCE

alter table REPORT_PARAMETER_MAP add constraint FK23FF1FBB1AFAD98D foreign key (PARAMETER_ID) references REPORT_PARAMETER

alter table REPORT_PARAMETER_MAP add constraint FK23FF1FBBAAEF4A13 foreign key (REPORT_ID) references REPORT

alter table REPORT_USER add constraint FK7364F3F6EE01FD95 foreign key (DEFAULT_REPORT_ID) references REPORT

alter table USER_ALERT_MAP add constraint FKD83C845E4B42987 foreign key (USER_ID) references REPORT_USER

alter table USER_ALERT_MAP add constraint FKD83C845AAEF4A13 foreign key (REPORT_ID) references REPORT

alter table USER_ALERT_MAP add constraint FKD83C84520DA4A2D foreign key (ALERT_ID) references REPORT_ALERT

alter table USER_GROUP_MAP add constraint FKC49EBE8E4B42987 foreign key (USER_ID) references REPORT_USER

alter table USER_GROUP_MAP add constraint FKC49EBE8DF17134D foreign key (GROUP_ID) references REPORT_GROUP

alter table USER_SECURITY add constraint FK7DE1C934E4B42987 foreign key (USER_ID) references REPORT_USER

create sequence hibernate_sequence


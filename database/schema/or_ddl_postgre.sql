create table OR_PROPERTIES (PROPERTY_ID int4 not null, PROPERTY_KEY varchar(255) not null unique, PROPERTY_VALUE varchar(255), primary key (PROPERTY_ID))

create table OR_TAGS (TAG_ID int4 not null, TAGGED_OBJECT_ID int4 not null, TAGGED_OBJECT_CLASS varchar(255) not null, TAG_VALUE varchar(255) not null, TAG_TYPE varchar(255) not null, primary key (TAG_ID))

create table REPORT (REPORT_ID int4 not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, REPORT_FILE varchar(255) not null, PDF_EXPORT bool not null, CSV_EXPORT bool not null, XLS_EXPORT bool not null, HTML_EXPORT bool not null, RTF_EXPORT bool not null, TEXT_EXPORT bool not null, EXCEL_EXPORT bool not null, IMAGE_EXPORT bool not null, FILL_VIRTUAL bool not null, HIDDEN_REPORT bool not null, REPORT_QUERY text, DATASOURCE_ID int4, CHART_ID int4, EXPORT_OPTION_ID int4, primary key (REPORT_ID))

create table REPORT_ALERT (ALERT_ID int4 not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, ALERT_QUERY text not null, DATASOURCE_ID int4, primary key (ALERT_ID))

create table REPORT_CHART (CHART_ID int4 not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, CHART_QUERY text not null, CHART_TYPE int4 not null, WIDTH int4 not null, HEIGHT int4 not null, X_AXIS_LABEL varchar(255), Y_AXIS_LABEL varchar(255), SHOW_LEGEND bool not null, SHOW_TITLE bool not null, SHOW_VALUES bool not null, PLOT_ORIENTATION int4, DATASOURCE_ID int4, REPORT_ID int4, OVERLAY_CHART_ID int4, primary key (CHART_ID))

create table REPORT_DATASOURCE (DATASOURCE_ID int4 not null, NAME varchar(255) not null unique, DRIVER varchar(255), URL varchar(255) not null, USERNAME varchar(255), PASSWORD varchar(255), MAX_IDLE int4, MAX_ACTIVE int4, MAX_WAIT int8, VALIDATION_QUERY varchar(255), JNDI bool, primary key (DATASOURCE_ID))

create table REPORT_DELIVERY_LOG (DELIVERY_LOG_ID int4 not null, START_TIME timestamp, END_TIME timestamp, STATUS varchar(255), MESSAGE text, DELIVERY_METHOD varchar(255), LOG_ID int4, DELIVERY_INDEX int4, primary key (DELIVERY_LOG_ID))

create table REPORT_EXPORT_OPTIONS (EXPORT_OPTION_ID int4 not null, XLS_REMOVE_EMPTY_SPACE bool not null, XLS_ONE_PAGE_PER_SHEET bool not null, XLS_AUTO_DETECT_CELL bool not null, XLS_WHITE_BACKGROUND bool not null, HTML_REMOVE_EMPTY_SPACE bool not null, HTML_WHITE_BACKGROUND bool not null, HTML_USE_IMAGES bool not null, HTML_WRAP_BREAK bool not null, primary key (EXPORT_OPTION_ID))

create table REPORT_GROUP (GROUP_ID int4 not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, primary key (GROUP_ID))

create table REPORT_GROUP_MAP (GROUP_ID int4 not null, REPORT_ID int4 not null, MAP_ID int4 not null, primary key (GROUP_ID, MAP_ID))

create table REPORT_LOG (LOG_ID int4 not null, START_TIME timestamp, END_TIME timestamp, STATUS varchar(255), MESSAGE text, EXPORT_TYPE int4, REQUEST_ID varchar(255), REPORT_ID int4, USER_ID int4, ALERT_ID int4, primary key (LOG_ID))

create table REPORT_PARAMETER (PARAMETER_ID int4 not null, NAME varchar(255) not null unique, TYPE varchar(255) not null, CLASSNAME varchar(255) not null, DATA text, DATASOURCE_ID int4, DESCRIPTION varchar(255), REQUIRED bool, MULTI_SELECT bool, DEFAULT_VALUE varchar(255), primary key (PARAMETER_ID))

create table REPORT_PARAMETER_MAP (REPORT_ID int4 not null, PARAMETER_ID int4, REQUIRED bool, SORT_ORDER int4, STEP int4, MAP_ID int4 not null, primary key (REPORT_ID, MAP_ID))

create table REPORT_USER (REPORTUSER_ID int4 not null, NAME varchar(255) not null unique, PASSWORD varchar(255) not null, EXTERNAL_ID varchar(255), EMAIL_ADDRESS varchar(255), PDF_EXPORT_TYPE int4 not null, DEFAULT_REPORT_ID int4, primary key (REPORTUSER_ID))

create table USER_ALERT_MAP (USER_ID int4 not null, ALERT_ID int4, REPORT_ID int4, ALERT_LIMIT int4, ALERT_OPERATOR varchar(255), MAP_ID int4 not null, primary key (USER_ID, MAP_ID))

create table USER_GROUP_MAP (USER_ID int4 not null, GROUP_ID int4 not null, MAP_ID int4 not null, primary key (USER_ID, MAP_ID))

create table USER_SECURITY (USER_ID int4 not null, ROLE_NAME varchar(255))

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


create table OR_PROPERTIES (PROPERTY_ID int identity not null, PROPERTY_KEY varchar(255) not null unique, PROPERTY_VALUE varchar(255) null, primary key (PROPERTY_ID), unique (PROPERTY_KEY))

create table OR_TAGS (TAG_ID int identity not null, TAGGED_OBJECT_ID int not null, TAGGED_OBJECT_CLASS varchar(255) not null, TAG_VALUE varchar(255) not null, TAG_TYPE varchar(255) not null, primary key (TAG_ID))

create table REPORT (REPORT_ID int identity not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, REPORT_FILE varchar(255) not null, PDF_EXPORT tinyint not null, CSV_EXPORT tinyint not null, XLS_EXPORT tinyint not null, HTML_EXPORT tinyint not null, RTF_EXPORT tinyint not null, TEXT_EXPORT tinyint not null, EXCEL_EXPORT tinyint not null, IMAGE_EXPORT tinyint not null, FILL_VIRTUAL tinyint not null, HIDDEN_REPORT tinyint not null, REPORT_QUERY text null, DATASOURCE_ID int null, CHART_ID int null, EXPORT_OPTION_ID int null, primary key (REPORT_ID), unique (NAME))

create table REPORT_ALERT (ALERT_ID int identity not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, ALERT_QUERY text not null, DATASOURCE_ID int null, primary key (ALERT_ID), unique (NAME))

create table REPORT_CHART (CHART_ID int identity not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, CHART_QUERY text not null, CHART_TYPE int not null, WIDTH int not null, HEIGHT int not null, X_AXIS_LABEL varchar(255) null, Y_AXIS_LABEL varchar(255) null, SHOW_LEGEND tinyint not null, SHOW_TITLE tinyint not null, SHOW_VALUES tinyint not null, PLOT_ORIENTATION int null, DATASOURCE_ID int null, REPORT_ID int null, OVERLAY_CHART_ID int null, primary key (CHART_ID), unique (NAME))

create table REPORT_DATASOURCE (DATASOURCE_ID int identity not null, NAME varchar(255) not null unique, DRIVER varchar(255) null, URL varchar(255) not null, USERNAME varchar(255) null, PASSWORD varchar(255) null, MAX_IDLE int null, MAX_ACTIVE int null, MAX_WAIT numeric(19,0) null, VALIDATION_QUERY varchar(255) null, JNDI tinyint null, primary key (DATASOURCE_ID), unique (NAME))

create table REPORT_DELIVERY_LOG (DELIVERY_LOG_ID int identity not null, START_TIME datetime null, END_TIME datetime null, STATUS varchar(255) null, MESSAGE text null, DELIVERY_METHOD varchar(255) null, LOG_ID int null, DELIVERY_INDEX int null, primary key (DELIVERY_LOG_ID))

create table REPORT_EXPORT_OPTIONS (EXPORT_OPTION_ID int identity not null, XLS_REMOVE_EMPTY_SPACE tinyint not null, XLS_ONE_PAGE_PER_SHEET tinyint not null, XLS_AUTO_DETECT_CELL tinyint not null, XLS_WHITE_BACKGROUND tinyint not null, HTML_REMOVE_EMPTY_SPACE tinyint not null, HTML_WHITE_BACKGROUND tinyint not null, HTML_USE_IMAGES tinyint not null, HTML_WRAP_BREAK tinyint not null, primary key (EXPORT_OPTION_ID))

create table REPORT_GROUP (GROUP_ID int identity not null, NAME varchar(255) not null unique, DESCRIPTION varchar(255) not null, primary key (GROUP_ID), unique (NAME))

create table REPORT_GROUP_MAP (GROUP_ID int not null, REPORT_ID int not null, MAP_ID int not null, primary key (GROUP_ID, MAP_ID))

create table REPORT_LOG (LOG_ID int identity not null, START_TIME datetime null, END_TIME datetime null, STATUS varchar(255) null, MESSAGE text null, EXPORT_TYPE int null, REQUEST_ID varchar(255) null, REPORT_ID int null, USER_ID int null, ALERT_ID int null, primary key (LOG_ID))

create table REPORT_PARAMETER (PARAMETER_ID int identity not null, NAME varchar(255) not null unique, TYPE varchar(255) not null, CLASSNAME varchar(255) not null, DATA text null, DATASOURCE_ID int null, DESCRIPTION varchar(255) null, REQUIRED tinyint null, MULTI_SELECT tinyint null, DEFAULT_VALUE varchar(255) null, primary key (PARAMETER_ID), unique (NAME))

create table REPORT_PARAMETER_MAP (REPORT_ID int not null, PARAMETER_ID int null, REQUIRED tinyint null, SORT_ORDER int null, STEP int null, MAP_ID int not null, primary key (REPORT_ID, MAP_ID))

create table REPORT_USER (REPORTUSER_ID int identity not null, NAME varchar(255) not null unique, PASSWORD varchar(255) not null, EXTERNAL_ID varchar(255) null, EMAIL_ADDRESS varchar(255) null, PDF_EXPORT_TYPE int not null, DEFAULT_REPORT_ID int null, primary key (REPORTUSER_ID), unique (NAME))

create table USER_ALERT_MAP (USER_ID int not null, ALERT_ID int null, REPORT_ID int null, ALERT_LIMIT int null, ALERT_OPERATOR varchar(255) null, MAP_ID int not null, primary key (USER_ID, MAP_ID))

create table USER_GROUP_MAP (USER_ID int not null, GROUP_ID int not null, MAP_ID int not null, primary key (USER_ID, MAP_ID))

create table USER_SECURITY (USER_ID int not null, ROLE_NAME varchar(255) null)

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


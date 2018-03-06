
    alter table VALLOC.PASSWORD 
        drop constraint FK_USER_SUBJECT_ID_TO_PASSWORD;

    alter table VALLOC.PERMISSION 
        drop constraint FKFE0FB1CF8FD7312F;

    alter table VALLOC.PERMISSION 
        drop constraint FK_PERMISSION_TYPE_ID;

    alter table VALLOC.PERMISSION_ACTION 
        drop constraint FKA65CE6A697D3C85;

    alter table VALLOC.PERMISSION_ACTION 
        drop constraint FK_PERMISSION_ID;

    alter table VALLOC.TARGET_HOST 
        drop constraint FK_HOST_ID;

    alter table VALLOC.TARGET_HOST 
        drop constraint FK_TARGET_ENVIRONMENT_ID;

    alter table VALLOC.USER_SUBJECT_ROLES 
        drop constraint FK_USER_SUBJECT_ID;

    alter table VALLOC.USER_SUBJECT_ROLES 
        drop constraint FK_ROLE_ID;

    drop table VALLOC.HOST;

    drop table VALLOC.PASSWORD;

    drop table VALLOC.PERMISSION;

    drop table VALLOC.PERMISSION_ACTION;

    drop table VALLOC.PERMISSION_TYPE;

    drop table VALLOC.ROLE;

    drop table VALLOC.TARGET_ENVIRONMENT;

    drop table VALLOC.TARGET_HOST;

    drop table VALLOC.USER_SUBJECT;

    drop table VALLOC.USER_SUBJECT_ROLES;

    drop table VALLOC.hibernate_unique_key;

    create table VALLOC.HOST (
        ID integer not null,
        NAME varchar(500) not null,
        VERSION integer,
        primary key (ID)
    );

    create table VALLOC.PASSWORD (
        ID integer not null,
        EXPIRED_ON timestamp,
        PASSWORD varchar(500) not null,
        USER_SUBJECT_ID integer,
        primary key (ID)
    );

    create table VALLOC.PERMISSION (
        ID integer not null,
        DESCRIPTION varchar(500) not null,
        NAME varchar(100) not null,
        PERMISSION_TYPE_ID integer not null,
        primary key (ID)
    );

    create table VALLOC.PERMISSION_ACTION (
        ID integer not null,
        DESCRIPTION varchar(500) not null,
        NAME varchar(100) not null,
        PERMISSION_ID integer not null,
        primary key (ID)
    );

    create table VALLOC.PERMISSION_TYPE (
        ID integer not null,
        DESCRIPTION varchar(500) not null,
        NAME varchar(100) not null,
        primary key (ID)
    );

    create table VALLOC.ROLE (
        ID integer not null,
        DESCRIPTION varchar(500) not null,
        NAME varchar(100) not null,
        primary key (ID)
    );

    create table VALLOC.TARGET_ENVIRONMENT (
        ID integer not null,
        NAME varchar(100) not null,
        PATH varchar(500) not null,
        primary key (ID)
    );

    create table VALLOC.TARGET_HOST (
        ADMIN_PORT integer not null,
        HOST_ORDER_NUMBER integer not null,
        WORKER_PORT integer not null,
        TARGET_ENVIRONMENT_ID integer,
        HOST_ID integer,
        primary key (HOST_ID, TARGET_ENVIRONMENT_ID)
    );

    create table VALLOC.USER_SUBJECT (
        ID integer not null,
        EMAIL varchar(75) not null,
        IS_ENABLED smallint not null,
        FIRST_NAME varchar(50) not null,
        LAST_NAME varchar(50) not null,
        USERNAME varchar(50) not null,
        primary key (ID)
    );

    create table VALLOC.USER_SUBJECT_ROLES (
        USER_SUBJECT_ID integer not null,
        ROLE_ID integer not null,
        primary key (USER_SUBJECT_ID, ROLE_ID)
    );

    alter table VALLOC.PASSWORD 
        add constraint FK_USER_SUBJECT_ID_TO_PASSWORD 
        foreign key (USER_SUBJECT_ID) 
        references VALLOC.USER_SUBJECT;

    alter table VALLOC.PERMISSION 
        add constraint FKFE0FB1CF8FD7312F 
        foreign key (ID) 
        references VALLOC.PERMISSION;

    alter table VALLOC.PERMISSION 
        add constraint FK_PERMISSION_TYPE_ID 
        foreign key (PERMISSION_TYPE_ID) 
        references VALLOC.PERMISSION_TYPE;

    alter table VALLOC.PERMISSION_ACTION 
        add constraint FKA65CE6A697D3C85 
        foreign key (ID) 
        references VALLOC.PERMISSION_ACTION;

    alter table VALLOC.PERMISSION_ACTION 
        add constraint FK_PERMISSION_ID 
        foreign key (PERMISSION_ID) 
        references VALLOC.PERMISSION;

    alter table VALLOC.TARGET_HOST 
        add constraint FK_HOST_ID 
        foreign key (HOST_ID) 
        references VALLOC.HOST;

    alter table VALLOC.TARGET_HOST 
        add constraint FK_TARGET_ENVIRONMENT_ID 
        foreign key (TARGET_ENVIRONMENT_ID) 
        references VALLOC.TARGET_ENVIRONMENT;

    alter table VALLOC.USER_SUBJECT_ROLES 
        add constraint FK_USER_SUBJECT_ID 
        foreign key (ROLE_ID) 
        references VALLOC.ROLE;

    alter table VALLOC.USER_SUBJECT_ROLES 
        add constraint FK_ROLE_ID 
        foreign key (USER_SUBJECT_ID) 
        references VALLOC.USER_SUBJECT;

    create table VALLOC.hibernate_unique_key (
         next_hi integer 
    );

    insert into VALLOC.hibernate_unique_key values ( 0 );

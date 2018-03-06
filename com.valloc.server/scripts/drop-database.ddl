
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

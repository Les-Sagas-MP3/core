--liquibase formatted sql

--changeset lessagasmp3:create-table-role
CREATE TABLE IF NOT EXISTS role (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(255),
    user_id bigint NOT NULL,
    saga_id bigint
);
--rollback drop table roles;

--changeset lessagasmp3:add-fk-role
ALTER TABLE ONLY role
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    ADD CONSTRAINT fk_saga FOREIGN KEY (saga_id) REFERENCES saga(id)
--rollback alter table role drop constraint fk_user;
--rollback alter table role drop constraint fk_saga;

--changeset lessagasmp3:create-sequence-role
CREATE SEQUENCE IF NOT EXISTS role_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence role_seq;

--changeset lessagasmp3:migrate-admin-authority-to-role
INSERT INTO role(id, user_id, name)
    SELECT DISTINCT nextval('role_seq'), id, 'ADMINISTRATOR' FROM users WHERE email = 'admin@les-sagas-mp3.fr';
--rollback delete from role where name = 'ADMINISTRATOR';

--changeset lessagasmp3:migrate-user-authority-to-role
INSERT INTO role(id, user_id, name)
    SELECT DISTINCT nextval('role_seq'), id, 'MEMBER' FROM users;
--rollback delete from role where name = 'MEMBER';

--changeset lessagasmp3:drop-authority_seq
DROP SEQUENCE authority_seq;

--changeset lessagasmp3:drop-table-user_authority
DROP TABLE user_authority;

--changeset lessagasmp3:drop-table-authority
DROP TABLE authority;

--changeset lessagasmp3:add-users-fields
ALTER TABLE users
    ADD avatar_url character varying(255),
    ADD workspace character varying(255);
--rollback alter table users drop column avatar_url;
--rollback alter table users drop column workspace;

--changeset lessagasmp3:generate-users-uuid
UPDATE users SET workspace = gen_random_uuid() WHERE workspace IS NULL OR workspace = '';

--liquibase formatted sql

--changeset lessagasmp3:add-episode-file
ALTER TABLE episode ADD file_id int8 NULL;
--rollback alter table episode drop column file_id;

--changeset lessagasmp3:add-fk-episode-file
ALTER TABLE ONLY episode
    ADD CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES file(id);
--rollback alter table episode drop constraint fk_file;

--changeset lessagasmp3:add-saga-workspace
ALTER TABLE saga ADD workspace character varying(255);
--rollback alter table saga drop column workspace;

--changeset lessagasmp3:add-episode-workspace
ALTER TABLE episode ADD workspace character varying(255);
--rollback alter table episode drop column workspace;

--changeset lessagasmp3:generate-saga-uuid
UPDATE saga SET workspace = gen_random_uuid() WHERE workspace IS NULL OR workspace = '';

--changeset lessagasmp3:generate-episode-uuid
UPDATE episode SET workspace = gen_random_uuid() WHERE workspace IS NULL OR workspace = '';

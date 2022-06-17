--liquibase formatted sql

--changeset lesprojetscagnottes:add-episode-file
ALTER TABLE episode ADD file_id int8 NULL;
--rollback alter table episode drop column file_id;

--changeset lesprojetscagnottes:add-fk-episode-file
ALTER TABLE ONLY episode
    ADD CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES file(id);
--rollback alter table episode drop constraint fk_file;

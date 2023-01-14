--liquibase formatted sql

--changeset lessagasmp3:create-sequence-anecdote
CREATE SEQUENCE IF NOT EXISTS anecdote_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence anecdote_seq;

--changeset lessagasmp3:create-sequence-authority
CREATE SEQUENCE IF NOT EXISTS authority_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence authority_seq;

--changeset lessagasmp3:create-sequence-category
CREATE SEQUENCE IF NOT EXISTS category_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence category_seq;

--changeset lessagasmp3:create-sequence-creator
CREATE SEQUENCE IF NOT EXISTS creator_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence creator_seq;

--changeset lessagasmp3:create-sequence-distribution_entry
CREATE SEQUENCE IF NOT EXISTS distribution_entry_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence distribution_entry_seq;

--changeset lessagasmp3:create-sequence-episode
CREATE SEQUENCE IF NOT EXISTS episode_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence episode_seq;

--changeset lessagasmp3:create-sequence-event_log
CREATE SEQUENCE IF NOT EXISTS event_log_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence event_log_seq;

--changeset lessagasmp3:create-sequence-file
CREATE SEQUENCE IF NOT EXISTS file_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence file_seq;

--changeset lessagasmp3:create-sequence-rss_message
CREATE SEQUENCE IF NOT EXISTS rss_message_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence rss_message_seq;

--changeset lessagasmp3:create-sequence-saga
CREATE SEQUENCE IF NOT EXISTS saga_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence saga_seq;

--changeset lessagasmp3:create-sequence-season
CREATE SEQUENCE IF NOT EXISTS season_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence season_seq;

--changeset lessagasmp3:create-sequence-users
CREATE SEQUENCE IF NOT EXISTS users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence users_seq;

--changeset lessagasmp3:set-sequence-value-anecdote
SELECT setval('anecdote_seq', (select max(id) from anecdote) + 1, true);
--rollback select setval('anecdote_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-authority
SELECT setval('authority_seq', (select max(id) from authority) + 1, true);
--rollback select setval('authority_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-category
SELECT setval('category_seq', (select max(id) from category) + 1, true);
--rollback select setval('category_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-creator
SELECT setval('creator_seq', (select max(id) from creator) + 1, true);
--rollback select setval('creator_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-distribution_entry
SELECT setval('distribution_entry_seq', (select max(id) from distribution_entry) + 1, true);
--rollback select setval('distribution_entry_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-episode
SELECT setval('episode_seq', (select max(id) from episode) + 1, true);
--rollback select setval('episode_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-event_log
SELECT setval('event_log_seq', (select max(id) from event_log) + 1, true);
--rollback select setval('event_log_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-file
SELECT setval('file_seq', (select max(id) from file) + 1, true);
--rollback select setval('file_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-rss_message
SELECT setval('rss_message_seq', (select max(id) from rss_message) + 1, true);
--rollback select setval('rss_message_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-saga
SELECT setval('saga_seq', (select max(id) from saga) + 1, true);
--rollback select setval('saga_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-season
SELECT setval('season_seq', (select max(id) from season) + 1, true);
--rollback select setval('season_seq', 0, true);

--changeset lessagasmp3:set-sequence-value-users
SELECT setval('users_seq', (select max(id) from users) + 1, true);
--rollback select setval('users_seq', 0, true);

--changeset lessagasmp3:drop-sequence-hibernate_sequence
DROP SEQUENCE hibernate_sequence;
--rollback create sequence hibernate_sequence start with 1 increment by 1 no minvalue no maxvalue cache 1;

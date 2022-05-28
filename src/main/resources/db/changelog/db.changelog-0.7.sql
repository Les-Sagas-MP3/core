--liquibase formatted sql

--changeset lesprojetscagnottes:create-sequence-hibernate_sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--rollback drop sequence hibernate_sequence;

--changeset lesprojetscagnottes:create-table-anecdote
CREATE TABLE IF NOT EXISTS anecdote (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    anecdote text,
    saga_id bigint
);
--rollback drop table anecdote;

--changeset lesprojetscagnottes:create-table-authority
CREATE TABLE IF NOT EXISTS authority (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(50)
);
--rollback drop table authority;

--changeset lesprojetscagnottes:create-table-category
CREATE TABLE IF NOT EXISTS category (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(255),
    nb_sagas integer
);
--rollback drop table category;

--changeset lesprojetscagnottes:create-table-creator
CREATE TABLE IF NOT EXISTS creator (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(255),
    nb_sagas integer,
    user_id bigint
);
--rollback drop table creator;

--changeset lesprojetscagnottes:create-table-distribution_entry
CREATE TABLE IF NOT EXISTS distribution_entry (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    roles text,
    actor_id bigint,
    saga_id bigint
);
--rollback drop table distribution_entry;

--changeset lesprojetscagnottes:create-table-episode
CREATE TABLE IF NOT EXISTS episode (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    displayed_number character varying(255),
    infos character varying(255),
    number integer,
    title character varying(255),
    season_id bigint
);
--rollback drop table episode;

--changeset lesprojetscagnottes:create-table-event_log
CREATE TABLE IF NOT EXISTS event_log (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(50)
);
--rollback drop table event_log;

--changeset lesprojetscagnottes:create-table-file
CREATE TABLE IF NOT EXISTS file (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    content text,
    directory character varying(255),
    name character varying(255),
    path character varying(255),
    url character varying(255),
    format character varying(255)
);
--rollback drop table file;

--changeset lesprojetscagnottes:create-table-rss_message
CREATE TABLE IF NOT EXISTS rss_message (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    author character varying(255),
    description text,
    feed_title character varying(255),
    guid character varying(255),
    link character varying(255),
    pubdate timestamp without time zone,
    title character varying(255)
);
--rollback drop table rss_message;

--changeset lesprojetscagnottes:create-table-saga
CREATE TABLE IF NOT EXISTS saga (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    awards text,
    banner_url character varying(255),
    cover_url character varying(255),
    duration bigint,
    genese text,
    level_art integer,
    level_tech integer,
    nb_bravos integer,
    nb_reviews integer,
    origin character varying(255),
    start_date timestamp without time zone,
    status character varying(50),
    synopsis text,
    title character varying(255),
    url character varying(255),
    url_reviews character varying(255),
    url_wiki character varying(255)
);
--rollback drop table saga;

--changeset lesprojetscagnottes:create-table-saga_authors
CREATE TABLE IF NOT EXISTS saga_authors (
    sagas_written_id bigint NOT NULL,
    authors_id bigint NOT NULL,
    PRIMARY KEY (sagas_written_id, authors_id)
);
--rollback drop table saga_authors;

--changeset lesprojetscagnottes:create-table-saga_categories
CREATE TABLE IF NOT EXISTS saga_categories (
    sagas_id bigint NOT NULL,
    categories_id bigint NOT NULL,
    PRIMARY KEY (sagas_id, categories_id)
);
--rollback drop table saga_categories;

--changeset lesprojetscagnottes:create-table-saga_composers
CREATE TABLE IF NOT EXISTS saga_composers (
    sagas_composed_id bigint NOT NULL,
    composers_id bigint NOT NULL,
    PRIMARY KEY (sagas_composed_id, composers_id)
);
--rollback drop table saga_composers;

--changeset lesprojetscagnottes:create-table-season
CREATE TABLE IF NOT EXISTS season (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    name character varying(255),
    number integer,
    saga_id bigint
);
--rollback drop table season;

--changeset lesprojetscagnottes:create-table-user_authority
CREATE TABLE IF NOT EXISTS user_authority (
    user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    PRIMARY KEY (user_id, authority_id)
);
--rollback drop table user_authority;

--changeset lesprojetscagnottes:create-table-users
CREATE TABLE IF NOT EXISTS users (
    id bigint primary key,
    created_at timestamp without time zone DEFAULT now(),
    created_by character varying(255) DEFAULT 'System'::character varying,
    updated_at timestamp without time zone DEFAULT now(),
    updated_by character varying(255) DEFAULT 'System'::character varying,
    email character varying(255),
    enabled boolean,
    last_password_reset_date timestamp without time zone,
    password character varying(255),
    username character varying(255)
);
--rollback drop table users;

--changeset lesprojetscagnottes:add-fk-anecdote
ALTER TABLE ONLY anecdote
    ADD CONSTRAINT fk_saga FOREIGN KEY (saga_id) REFERENCES saga(id);
--rollback alter table anecdote drop constraint fk_saga;

--changeset lesprojetscagnottes:add-fk-creator
ALTER TABLE ONLY creator
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id);
--rollback alter table creator drop constraint fk_user;

--changeset lesprojetscagnottes:add-fk-episode
ALTER TABLE ONLY episode
    ADD CONSTRAINT fk_season FOREIGN KEY (season_id) REFERENCES season(id);
--rollback alter table episode drop constraint fk_season;

--changeset lesprojetscagnottes:add-fk-saga_authors
ALTER TABLE ONLY saga_authors
    ADD CONSTRAINT fk_author FOREIGN KEY (authors_id) REFERENCES creator(id),
    ADD CONSTRAINT fk_saga FOREIGN KEY (sagas_written_id) REFERENCES saga(id);
--rollback alter table saga_authors drop constraint fk_saga;
--rollback alter table saga_authors drop constraint fk_author;

--changeset lesprojetscagnottes:add-fk-saga_categories
ALTER TABLE ONLY saga_categories
    ADD CONSTRAINT fk_category FOREIGN KEY (categories_id) REFERENCES category(id),
    ADD CONSTRAINT fk_saga FOREIGN KEY (sagas_id) REFERENCES saga(id);
--rollback alter table saga_categories drop constraint fk_saga;
--rollback alter table saga_categories drop constraint fk_category;

--changeset lesprojetscagnottes:add-fk-saga_composers
ALTER TABLE ONLY saga_composers
    ADD CONSTRAINT fk_composer FOREIGN KEY (composers_id) REFERENCES creator(id),
    ADD CONSTRAINT fk_saga FOREIGN KEY (sagas_composed_id) REFERENCES saga(id);
--rollback alter table saga_composers drop constraint fk_saga;
--rollback alter table saga_composers drop constraint fk_composer;

--changeset lesprojetscagnottes:add-fk-season
ALTER TABLE ONLY season
    ADD CONSTRAINT fk_saga FOREIGN KEY (saga_id) REFERENCES saga(id);
--rollback alter table season drop constraint fk_saga;

--changeset lesprojetscagnottes:add-fk-user_authority
ALTER TABLE ONLY user_authority
    ADD CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authority(id),
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id);
--rollback alter table user_authority drop constraint fk_user;
--rollback alter table user_authority drop constraint fk_authority;

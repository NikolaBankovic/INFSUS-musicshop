--liquibase formatted sql
--changeset nbankovic:category-table-23052025-01
CREATE TABLE public.category (
                               id bigserial NOT NULL,
                               name varchar(255) NULL,
                               CONSTRAINT category_pkey PRIMARY KEY (id)
);
--rollback DROP TABLE public.category
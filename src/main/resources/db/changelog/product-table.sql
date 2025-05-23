--liquibase formatted sql
--changeset nbankovic:product-table-22072024-01
CREATE TABLE public.product (
                                id bigserial NOT NULL,
                                "name" varchar(255) NULL,
                                description text NULL,
                                image text NULL,
                                price float8 NOT NULL,
                                times_visited int8 NULL,
                                category_id int8 NOT NULL,
                                CONSTRAINT product_pkey PRIMARY KEY (id),
                                CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES public.category(id)
);
--rollback DROP TABLE public.product
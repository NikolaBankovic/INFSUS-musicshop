--liquibase formatted sql
--changeset nbankovic:product-table-23052025-01
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

--changeset nbankovic:product-table-24052025-01
INSERT INTO public.product
(name, description, image, price, times_visited, category_id)
VALUES
    ('Električna gitara Yamaha Pacifica', 'Odlična električna gitara za početnike i napredne svirače.', NULL, 2999.99, 0, 1),
    ('Akustični bubnjevi Pearl Roadshow', 'Komplet bubnjeva za ozbiljne početke u svijetu bubnjara.', NULL, 4599.00, 0, 2);
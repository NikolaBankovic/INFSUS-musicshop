--liquibase formatted sql
--changeset nbankovic:order-item-table-23052025-01
CREATE TABLE public.order_item (
                                   id bigserial NOT NULL,
                                   price float8 NOT NULL,
                                   quantity int4 NOT NULL,
                                   order_id int8 NULL,
                                   product_id int8 NULL,
                                   CONSTRAINT order_item_pkey PRIMARY KEY (id),
                                   CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.product(id),
                                   CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES public."order"(id)
);
--rollback DROP TABLE public.order_item

--changeset nbankovic:order-item-table-23052025-02
ALTER TABLE public.order_item
    DROP CONSTRAINT product_id_fk,
    ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE SET NULL;

ALTER TABLE public.order_item
    DROP CONSTRAINT order_id_fk,
    ADD CONSTRAINT order_id_fk FOREIGN KEY (order_id) REFERENCES public."order"(id) ON DELETE CASCADE;

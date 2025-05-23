--liquibase formatted sql
--changeset nbankovic:cart-table-23052025-01
CREATE TABLE public.cart (
                             id bigserial NOT NULL,
                             user_id int8 NULL,
                             CONSTRAINT cart_pkey PRIMARY KEY (id),
                             CONSTRAINT user_id_unique UNIQUE (user_id),
                             CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
--rollback DROP TABLE public.cart

--changeset nbankovic:cart-table-23052025-02
ALTER TABLE public.cart
    DROP CONSTRAINT user_id_fk,
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE CASCADE;
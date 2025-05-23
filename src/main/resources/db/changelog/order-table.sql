--liquibase formatted sql
--changeset nbankovic:order-table-23052025-01
CREATE TABLE public."order" (
                                id bigserial NOT NULL,
                                total_price float8 NOT NULL,
                                user_id int8 NULL,
                                credit_card_number varchar(8) NOT NULL DEFAULT 'unknown',
                                order_date TIMESTAMP NOT NULL DEFAULT NOW(),
                                delivery_address varchar(255) NOT NULL,
                                CONSTRAINT order_pkey PRIMARY KEY (id),
                                CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);
--rollback DROP TABLE public."order"

--changeset nbankovic:order-table-23052025-02
ALTER TABLE public."order"
    DROP CONSTRAINT user_id_fk,
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE SET NULL;
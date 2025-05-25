--liquibase formatted sql
--changeset nbankovic:review-table-23052025-01
CREATE TABLE public.review (
                               id bigserial NOT NULL,
                               rating int4 NOT NULL,
                               comment text NOT NULL,
                               user_id int8 NULL,
                               product_id int8 NULL,
                               CONSTRAINT review_pkey PRIMARY KEY (id),
                               CONSTRAINT rating_check CHECK (rating BETWEEN 1 AND 5),
                               CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.product(id),
                               CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);

--changeset nbankovic:review-table-23052025-02
ALTER TABLE public.review
    DROP CONSTRAINT product_id_fk,
    DROP CONSTRAINT user_id_fk,
    ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE CASCADE,
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE SET NULL;

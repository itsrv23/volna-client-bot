--liquibase formatted sql

--changeset samael:1

CREATE TABLE public.a_samael_telegram_client_answers (
                                                         id BIGSERIAL,
                                                         name VARCHAR(255),
                                                         answer TEXT,
                                                         CONSTRAINT pk_a_samael_telegram_client_answers PRIMARY KEY(id)
)
    WITH (oids = false);

COMMENT ON TABLE public.a_samael_telegram_client_answers
    IS 'taxi_volna_bot Ответы';

COMMENT ON COLUMN public.a_samael_telegram_client_answers.id
    IS 'id';

COMMENT ON COLUMN public.a_samael_telegram_client_answers.name
    IS 'Имя';

COMMENT ON COLUMN public.a_samael_telegram_client_answers.answer
    IS 'Ответ';

ALTER TABLE public.a_samael_telegram_client_answers
    OWNER TO postgres;


CREATE TABLE public.a_samael_telegram_client_user_state (
                                                            id SERIAL,
                                                            name VARCHAR(255),
                                                            comment VARCHAR(255),
                                                            CONSTRAINT pk_a_samael_telegram_client_user_state PRIMARY KEY(id)
)
    WITH (oids = false);

COMMENT ON TABLE public.a_samael_telegram_client_user_state
    IS 'taxi_volna_bot Статусы';

COMMENT ON COLUMN public.a_samael_telegram_client_user_state.id
    IS 'id';

COMMENT ON COLUMN public.a_samael_telegram_client_user_state.name
    IS 'Название';

COMMENT ON COLUMN public.a_samael_telegram_client_user_state.comment
    IS 'Описание';

ALTER TABLE public.a_samael_telegram_client_user_state
    OWNER TO postgres;



CREATE TABLE public.a_samael_telegram_client_users (
                                                       id BIGSERIAL,
                                                       user_id BIGINT,
                                                       phone VARCHAR(255),
                                                       user_state_id INTEGER,
                                                       dt_create TIMESTAMP WITH TIME ZONE DEFAULT now(),
                                                       CONSTRAINT pk_a_samael_telegram_client_users PRIMARY KEY(id),
                                                       CONSTRAINT user_id_uniq UNIQUE(user_id),
                                                       CONSTRAINT fk_a_samael_telegram_client_users_on_user_state FOREIGN KEY (user_state_id)
                                                           REFERENCES public.a_samael_telegram_client_user_state(id)
                                                           ON DELETE NO ACTION
                                                           ON UPDATE NO ACTION
                                                           NOT DEFERRABLE
)
    WITH (oids = false);

COMMENT ON TABLE public.a_samael_telegram_client_users
    IS 'taxi_volna_bot Пользователи';

COMMENT ON COLUMN public.a_samael_telegram_client_users.id
    IS 'id';

COMMENT ON COLUMN public.a_samael_telegram_client_users.user_id
    IS 'user_id';

COMMENT ON COLUMN public.a_samael_telegram_client_users.phone
    IS 'Телефон';

COMMENT ON COLUMN public.a_samael_telegram_client_users.user_state_id
    IS 'Статус';


ALTER TABLE public.a_samael_telegram_client_users
    OWNER TO postgres;


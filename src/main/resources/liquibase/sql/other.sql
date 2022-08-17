
CREATE OR REPLACE FUNCTION public.tg_a_samael_telegram_client_users (
)
    RETURNS trigger AS
    $body$
DECLARE

    BEGIN
    IF(NEW.phone is not null and NOT EXISTS(select 1 from client where phone = new.phone)) THEN
-- Создаем запись в справочнике bonusaccount, если до этого ее не существовало
INSERT INTO bonusaccount(clientid)  values (new.phone);
insert into client (phone) values (new.phone);

END IF;
return new;
END;
$body$
    LANGUAGE 'plpgsql'
    VOLATILE
    CALLED ON NULL INPUT
SECURITY INVOKER
    COST 100;

ALTER FUNCTION public.tg_a_samael_telegram_client_users ()
    OWNER TO postgres;

CREATE TRIGGER tg_a_samael_telegram_client_users
    AFTER UPDATE
    ON public.a_samael_telegram_client_users

    FOR EACH ROW
EXECUTE PROCEDURE public.tg_a_samael_telegram_client_users();
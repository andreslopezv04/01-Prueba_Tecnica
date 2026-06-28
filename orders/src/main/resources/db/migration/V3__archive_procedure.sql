CREATE OR REPLACE PROCEDURE archive_old_rejected_orders(days_threshold INT)
LANGUAGE plpgsql
AS $$
DECLARE
archived_count INT;
BEGIN
UPDATE orders
SET status = 'ARCHIVED', updated_at = now()
WHERE status = 'REJECTED'
  AND updated_at < now() - make_interval(days => days_threshold);

GET DIAGNOSTICS archived_count = ROW_COUNT;

IF days_threshold <= 0 THEN
        RAISE WARNING 'days_threshold inválido (%); revirtiendo', days_threshold;
ROLLBACK;
ELSE
        RAISE NOTICE 'Órdenes archivadas: %', archived_count;
COMMIT;
END IF;
END;
$$;
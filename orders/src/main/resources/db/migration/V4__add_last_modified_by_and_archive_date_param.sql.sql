ALTER TABLE orders ADD COLUMN last_modified_by BIGINT;

CREATE OR REPLACE FUNCTION log_order_status_change()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status THEN
        INSERT INTO order_status_log (order_id, old_status, new_status, changed_by)
        VALUES (NEW.id, OLD.status, NEW.status, NEW.last_modified_by);
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Eliminar sp porque recibía días
DROP PROCEDURE IF EXISTS archive_old_rejected_orders(INT);

CREATE OR REPLACE PROCEDURE archive_old_rejected_orders(cutoff_date DATE)
LANGUAGE plpgsql
AS $$
DECLARE
archived_count INT;
BEGIN
    IF cutoff_date IS NULL THEN
        RAISE EXCEPTION 'cutoff_date no puede ser NULL';
END IF;

UPDATE orders
SET status = 'ARCHIVED', updated_at = now(), last_modified_by = NULL
WHERE status = 'REJECTED'
  AND updated_at < cutoff_date;

GET DIAGNOSTICS archived_count = ROW_COUNT;
RAISE NOTICE 'Ordenes archivadas: %', archived_count;
COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$$;
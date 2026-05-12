ALTER TABLE task
    ADD COLUMN start_date DATE NULL,
    ADD COLUMN end_date DATE NULL;

CREATE INDEX idx_task_date_range ON task (start_date, end_date);

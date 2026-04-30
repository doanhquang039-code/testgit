-- Fix invalid TaskType enum values
-- This migration updates any task_type values that don't match the Java enum

-- Update any invalid task_type values to 'OTHER'
UPDATE task 
SET task_type = 'OTHER' 
WHERE task_type IS NOT NULL 
AND task_type NOT IN (
    'DAILY', 'WEEKLY', 'MONTHLY', 'CREATIVE', 'DEVELOPMENT', 'DESIGN', 
    'TESTING', 'BUGFIX', 'FEATURE', 'MAINTENANCE', 'RESEARCH', 
    'DOCUMENTATION', 'MEETING', 'REVIEW', 'DEPLOYMENT', 'OTHER'
);

-- Log the fix
SELECT CONCAT('Fixed ', ROW_COUNT(), ' invalid task_type values') AS migration_result;

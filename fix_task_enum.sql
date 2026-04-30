-- Fix invalid TaskType enum values in database
-- Run this SQL in your MySQL database

-- Check current invalid values
SELECT DISTINCT task_type FROM task WHERE task_type NOT IN (
    'DAILY', 'WEEKLY', 'MONTHLY', 'CREATIVE', 'DEVELOPMENT', 'DESIGN', 
    'TESTING', 'BUGFIX', 'FEATURE', 'MAINTENANCE', 'RESEARCH', 
    'DOCUMENTATION', 'MEETING', 'REVIEW', 'DEPLOYMENT', 'OTHER'
);

-- Update invalid values to OTHER
UPDATE task 
SET task_type = 'OTHER' 
WHERE task_type NOT IN (
    'DAILY', 'WEEKLY', 'MONTHLY', 'CREATIVE', 'DEVELOPMENT', 'DESIGN', 
    'TESTING', 'BUGFIX', 'FEATURE', 'MAINTENANCE', 'RESEARCH', 
    'DOCUMENTATION', 'MEETING', 'REVIEW', 'DEPLOYMENT', 'OTHER'
);

-- Verify fix
SELECT task_type, COUNT(*) as count 
FROM task 
GROUP BY task_type 
ORDER BY task_type;


SET SCHEMA VALLOC;

-- must be in reverse order of contraints
DELETE FROM TARGET_HOST;
DELETE FROM HOST;
DELETE FROM TARGET_ENVIRONMENT;

-- must be in reverse order of creation
DROP TABLE TARGET_HOST;
DROP TABLE HOST;
DROP TABLE TARGET_ENVIRONMENT;

-- whether/not autocommit
COMMIT; 

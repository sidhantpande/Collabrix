-- Databases required by the relational Collabrix services.
SELECT 'CREATE DATABASE collabrix_auth'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'collabrix_auth'
)\gexec

SELECT 'CREATE DATABASE collabrix_user'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'collabrix_user'
)\gexec

SELECT 'CREATE DATABASE collabrix_workspace'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'collabrix_workspace'
)\gexec

SELECT 'CREATE DATABASE collabrix_task'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'collabrix_task'
)\gexec

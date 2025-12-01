
SELECT 'CREATE DATABASE migration OWNER postgres ENCODING ''UTF8'' LC_COLLATE=''ru_RU.utf8'' LC_CTYPE=''ru_RU.utf8'' TEMPLATE=''template0'' CONNECTION LIMIT 10' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'migration')\gexec
;
GRANT ALL PRIVILEGES ON DATABASE migration TO postgres;
BEGIN TRANSACTION;
CREATE TABLE configuration (
    key STRING UNIQUE NOT NULL,
    value STRING NOT NULL);
CREATE TABLE workers (
    worker_id INTEGER PRIMARY KEY ASC,
    host_port STRING NOT NULL);
CREATE TABLE alive_workers (
    worker_id INTEGER PRIMARY KEY ASC REFERENCES workers(worker_id));
CREATE TABLE masters (
    master_id INTEGER PRIMARY KEY ASC,
    host_port STRING NOT NULL);
CREATE TABLE relations (
    user_name STRING NOT NULL,
    program_name STRING NOT NULL,
    relation_name STRING NOT NULL,
    PRIMARY KEY (user_name,program_name,relation_name));
CREATE TABLE relation_schema (
    user_name STRING NOT NULL,
    program_name STRING NOT NULL,
    relation_name STRING NOT NULL,
    col_index INTEGER NOT NULL,
    col_name STRING,
    col_type STRING NOT NULL,
    FOREIGN KEY (user_name,program_name,relation_name) REFERENCES relations);
CREATE TABLE stored_relations (
    stored_relation_id INTEGER PRIMARY KEY ASC,
    user_name STRING NOT NULL,
    program_name STRING NOT NULL,
    relation_name STRING NOT NULL,
    num_shards INTEGER NOT NULL,
    how_partitioned STRING NOT NULL,
    FOREIGN KEY (user_name,program_name,relation_name) REFERENCES relations);
CREATE TABLE shards (
    stored_relation_id INTEGER NOT NULL REFERENCES stored_relations(stored_relation_id),
    shard_index INTEGER NOT NULL,
    worker_id INTEGER NOT NULL REFERENCES workers(worker_id));
CREATE TABLE queries (
    query_id INTEGER NOT NULL PRIMARY KEY ASC,
    raw_query TEXT NOT NULL,
    logical_ra TEXT NOT NULL);
COMMIT TRANSACTION;

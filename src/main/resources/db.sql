-- fts.docs definition

-- Drop table

-- DROP TABLE fts.docs;

CREATE TABLE fts.docs
(
    id        int4 NOT NULL,
    "content" text NULL,
    tokens    tsvector NULL
);
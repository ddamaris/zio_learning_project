-- This migration file creates tables for each model defined in our application the rows of which match the model's parameters

CREATE TABLE IF NOT EXISTS client
(
    id         varchar PRIMARY KEY NOT NULL,
    first_name varchar NOT NULL,
    last_name  varchar NOT NULL,
    address    varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    id        varchar PRIMARY KEY NOT NULL,
    kind      varchar NOT NULL,
    balance   int8 NOT NULL,
    client_id varchar NOT NULL REFERENCES client (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "transaction"
(
    id        varchar PRIMARY KEY NOT NULL,
    acc_from      varchar NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    acc_to        varchar NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    amount    int8 NOT NULL
);
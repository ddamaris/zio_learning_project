INSERT INTO client (id, first_name, last_name, address)
VALUES ('cln_1', 'Vasya', 'Pupkin', 'Voronezh'),
       ('cln_2', 'Ivan', 'Petrov', 'Moscow'),
       ('cln_3', 'Gena', 'Sidoroff', 'Urupinsk');

INSERT INTO account (id, kind, balance, client_id)
VALUES ('acc_1', 'deposit', 1000, 'cln_1'),
       ('acc_2', 'credit', 2000, 'cln_1'),
       ('acc_3', 'deposit', 1000, 'cln_2'),
       ('acc_4', 'deposit', 1000, 'cln_3');

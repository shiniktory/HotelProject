CONNECT 'jdbc:derby://localhost:1527/ST4DB;create=true;user=admin;password=pass';

INSERT INTO roles VALUES (0, 'admin');
INSERT INTO roles VALUES (1, 'client');

INSERT INTO users VALUES (DEFAULT , 'admin', '21232f297a57a5a743894a0e4a801fc3', 0, 'Artem', 'Kozin','admin@m.ua');
INSERT INTO users VALUES (DEFAULT , 'user1', '24c9e15e52afc47c225b757e7bee1f9d', 1, 'Anna', 'Melnik','hanna@m.ua');
INSERT INTO users VALUES (DEFAULT , 'user2', '7e58d63b60197ceb55a1c487989a3720', 1, 'Маша', 'Петрова','masha1@m.ua');

INSERT INTO apartment_states VALUES (0, 'free');
INSERT INTO apartment_states VALUES (1, 'reserved');
INSERT INTO apartment_states VALUES (2, 'occupied');
INSERT INTO apartment_states VALUES (3, 'unavailable');

INSERT INTO apartment_classes VALUES (DEFAULT , 'economy');
INSERT INTO apartment_classes VALUES (DEFAULT , 'standard');
INSERT INTO apartment_classes VALUES (DEFAULT , 'deluxe');
INSERT INTO apartment_classes VALUES (DEFAULT , 'luxury');

INSERT INTO hotel VALUES (101, 2, 1, 0, 200);
INSERT INTO hotel VALUES (102, 1, 1, 0, 100);
INSERT INTO hotel VALUES (103, 3, 1, 0, 300);
INSERT INTO hotel VALUES (104, 1, 1, 0, 100);
INSERT INTO hotel VALUES (105, 1, 1, 0, 100);
INSERT INTO hotel VALUES (106, 1, 1, 0, 100);
INSERT INTO hotel VALUES (201, 1, 2, 0, 700);
INSERT INTO hotel VALUES (202, 2, 2, 0, 800);
INSERT INTO hotel VALUES (203, 1, 2, 0, 700);
INSERT INTO hotel VALUES (204, 1, 2, 0, 700);
INSERT INTO hotel VALUES (301, 1, 3, 1, 900);
INSERT INTO hotel VALUES (302, 2, 3, 0, 1000);
INSERT INTO hotel VALUES (303, 1, 3, 0, 900);
INSERT INTO hotel VALUES (304, 1, 3, 0, 900);
INSERT INTO hotel VALUES (401, 1, 4, 1, 1400);
INSERT INTO hotel VALUES (402, 1, 4, 0, 1400);
INSERT INTO hotel VALUES (403, 1, 4, 0, 1400);

INSERT INTO order_states VALUES (0, 'new');
INSERT INTO order_states VALUES (1, 'confirmed');
INSERT INTO order_states VALUES (2, 'paid');
INSERT INTO order_states VALUES (3, 'closed');

INSERT INTO orders VALUES (DEFAULT, 2, 301, 0, 0, '2017-01-01', '2017-01-11', '2017-01-12');
INSERT INTO orders VALUES (DEFAULT, 3, 401, 0, 0, '2017-01-01', '2017-01-11', '2017-01-12');

insert into tables (id) values (1);
insert into tables (id) values (2);
insert into tables (id) values (3);
insert into tables (id) values (4);
insert into tables (id) values (5);
insert into tables (id) values (6);
insert into tables (id) values (7);
insert into tables (id) values (8);
insert into tables (id) values (9);
insert into tables (id) values (10);

insert into users (user_id, email, first_name, last_name, password_hash, phone_number, user_role, user_status, failed_login_attempts, created_at) values
(-1, 'max@mustermann.com','max', 'mustermann', '$2a$10$7Qy8s9v1Z5e6f8g9h0j1k2l3m4n5o6p7q8r9s0t1u2v3w4x5y6z7A', '+491234567890', 'USER', 'UNVERIFIED', 0, '2024-06-15 12:00:00+00');

insert into reservations (id, table_id, user_id, party_size, start_date, end_date, status, created_at) values
(-1, 1, -1, 4,'2026-02-15 17:30:00+00', '2026-02-15 19:30:00+00', 'RESERVED', '2026-02-15 14:45:00+00');
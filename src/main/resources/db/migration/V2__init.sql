insert into tables (id) values (1);

insert into users (user_id, email, first_name, last_name, password_hash, phone_number, user_role, user_status, failed_login_attempts, created_at) values
(1, 'max@mustermann.com','max', 'mustermann', '$2a$10$7Qy8s9v1Z5e6f8g9h0j1k2l3m4n5o6p7q8r9s0t1u2v3w4x5y6z7A', '+491234567890', 'USER', 'UNVERIFIED', 0, '2024-06-15 12:00:00+00');

insert into reservations (id, table_id, user_id, party_size, start_date, end_date, status, created_at) values
(1, 1, 1, 4, '2024-07-01 18:00:00+00', '2024-07-01 20:00:00+00', 'AVAILABLE', '2024-06-15 12:00:00+00'),
(2, 1, 1, 4,'2024-07-02 19:00:00+00', '2024-07-02 21:00:00+00', 'AVAILABLE',   '2024-06-16 13:30:00+00'),
(3, 1, 1, 4,'2024-07-03 17:30:00+00', '2024-07-03 19:30:00+00', 'AVAILABLE', '2024-06-17 14:45:00+00');
insert into tables (id) values (1);

insert into reservations (id, table_id, party_size, start_date, end_date, status, created_at) values
(1, 1, 4, '2024-07-01 18:00:00+00', '2024-07-01 20:00:00+00', 'CONFIRMED', '2024-06-15 12:00:00+00'),
(2, 1, 2, '2024-07-02 19:00:00+00', '2024-07-02 21:00:00+00', 'PENDING',   '2024-06-16 13:30:00+00'),
(3, 1, 6, '2024-07-03 17:30:00+00', '2024-07-03 19:30:00+00', 'CANCELLED', '2024-06-17 14:45:00+00');

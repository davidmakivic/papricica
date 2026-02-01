create table if not exists tables (
    id bigserial primary key
);

create table if not exists reservations (
    id bigserial primary key,
    table_id bigint not null references tables(id),
    party_size int not null,
    start_date timestamptz not null,
    end_date timestamptz not null,
    status varchar(30) not null,
    created_at timestamptz not null default now()
    );

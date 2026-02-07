create table if not exists tables (
    id bigserial primary key
);

create table if not exists users (
    user_id bigserial primary key,
    email varchar(50) not null unique,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    password_hash varchar(255) not null,
    phone_number varchar(50) not null unique,
    user_role varchar(30) not null,
    user_status varchar(30) not null,
    failed_login_attempts int not null default 0,
    created_at timestamptz default now()
    );

create table if not exists reservations (
    id bigserial primary key,
    table_id bigint not null references tables(id),
    user_id bigint not null references users(user_id),
    party_size int not null,
    start_date timestamptz not null,
    end_date timestamptz not null,
    status varchar(30) not null,
    created_at timestamptz not null default now()
    );

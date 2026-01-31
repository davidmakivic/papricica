create table if not exists reservation (
                                           id bigserial primary key,
                                           customer_name varchar(200) not null,
    contact_phone varchar(50),
    contact_email varchar(200),
    party_size int not null,
    start_time timestamptz not null,
    end_time timestamptz not null,
    status varchar(30) not null,
    created_at timestamptz not null default now()
    );

create index if not exists idx_reservation_start_time on reservation(start_time);

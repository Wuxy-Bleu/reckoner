create table loan
(
    id                 uuid                     default uuid_generate_v4() primary key,
    principal          numeric(18, 8)           default 0                 not null,
    interest           numeric(18, 8)           default 0,
    from_acct          uuid,
    currency           varchar                  default 'CNY'::character varying,
    to_cny             numeric(18, 8),
    created_at         timestamp with time zone default now()             not null,
    last_updated_at    timestamp with time zone default CURRENT_TIMESTAMP not null,
    trans_date         timestamp with time zone default now()             not null,
    status             varchar,
    loan_type          varchar                                            not null,
    interest_type      varchar,
    installment_number smallint,
    image_link         varchar,
    descr              text,
    tags               jsonb
);

create table loan_schedule
(
    id              uuid                     default uuid_generate_v4() primary key,
    loan_id         uuid                                               not null,
    principal       numeric(18, 8)           default 0                 not null,
    interest        numeric(18, 8),
    created_at      timestamp with time zone default now()             not null,
    last_updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    due_date        date,
    reckoner_id     uuid,
    status          varchar                                            not null

)
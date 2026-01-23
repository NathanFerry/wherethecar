-- Create user-defined enum types
do $$ begin
    create type energy as enum ('gasoline', 'diesel', 'electric', 'hybrid', 'natural_gas', 'none', 'not_specified');
exception
    when duplicate_object then null;
end $$;

do $$ begin
    create type vehicle_status as enum ('available', 'reserved', 'maintenance');
exception
    when duplicate_object then null;
end $$;

do $$ begin
    create type reservation_status as enum ('pending', 'confirmed', 'cancelled', 'completed');
exception
    when duplicate_object then null;
end $$;

-- Create tables
create table if not exists agent (
    uuid uuid primary key,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    is_admin bool default false
);

create table if not exists vehicle (
    uuid uuid primary key,
    license_plate varchar(12) not null unique,
    manufacturer varchar(255) not null,
    model varchar(255) not null,
    energy energy not null,
    power int not null,
    seats int not null,
    capacity int not null,
    utility_weight int not null,
    color varchar(20) not null,
    kilometers int not null,
    acquisition_date timestamp not null,
    status vehicle_status not null
);

create table if not exists reservation (
    uuid uuid primary key,
    agent_uuid uuid references agent(uuid) on delete cascade,
    vehicle_uuid uuid references vehicle(uuid) on delete cascade,
    start_date timestamp not null,
    end_date timestamp not null,
    status reservation_status not null
);

create table if not exists maintenance_operation (
    uuid uuid primary key,
    vehicle_uuid uuid references vehicle(uuid) on delete cascade,
    name varchar(255) not null,
    description text not null,
    operation_date timestamp not null,
    cost numeric(10, 2) not null
);

-- This part is still work in progress. It will not be hydrated yet.

create table if not exists maintenance_document (
    uuid uuid primary key,
    maintenance_operation_uuid uuid references maintenance_operation(uuid) on delete cascade,
    name varchar(255) not null,
    filepath varchar(255) not null
);

create table if not exists picture (
    uuid uuid primary key,
    vehicle_uuid uuid references vehicle(uuid) on delete cascade,
    name varchar(255) not null,
    filepath varchar(255) not null
);
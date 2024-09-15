CREATE TABLE guests (
    id serial primary key,
    name text,
    available_seat integer
);

CREATE TABLE confirmations (
   id serial primary key,
   guest_id integer REFERENCES guests(id) UNIQUE,
   confirmed_at timestamp,
   reserved_seat integer,
   will_attend text check (will_attend in ('Yes', 'No', 'To Confirm'))
);

CREATE TABLE comments (
  id serial primary key,
  guest_id integer REFERENCES guests(id),
  description text,
  image bytea
);

INSERT INTO guests (name, available_seat)
VALUES ('Family 1', 5),
       ('Family 2', 2)

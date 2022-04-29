CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TEXT,
   visible BOOLEAN default false,
   city_id integer
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TEXT,
   visible BOOLEAN default false,
   photo bytea
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email TEXT,
  password TEXT
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TEXT,
   visible BOOLEAN default false,
   photo bytea
);

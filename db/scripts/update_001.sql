CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TEXT,
   visible BOOLEAN default false,
   city_id integer
);

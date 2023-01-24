CREATE TABLE IF NOT EXISTS participates (
   id serial PRIMARY KEY,
   task_id int not null REFERENCES tasks(id),
   category_id int not null REFERENCES categories(id)
);




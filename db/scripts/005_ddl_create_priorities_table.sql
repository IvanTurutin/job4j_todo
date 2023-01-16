CREATE TABLE IF NOT EXISTS priorities
(
   id SERIAL PRIMARY KEY,
   name VARCHAR UNIQUE NOT NULL,
   position INT
);

comment on table priorities is 'Приоритеты';
comment on column priorities.id is 'Идентификатор приоритета';
comment on column priorities.name is 'Название приоритета';
comment on column priorities.position is 'Позиция приоритета';

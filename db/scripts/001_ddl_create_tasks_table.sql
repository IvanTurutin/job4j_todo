CREATE TABLE IF NOT EXISTS tasks
(
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  description TEXT NOT NULL,
  created TIMESTAMP NOT NULL,
  done BOOLEAN NOT NULL
);

comment on table tasks is 'Задачи';
comment on column tasks.id is 'Идентификатор задачи';
comment on column tasks.name is 'Название задачи';
comment on column tasks.description is 'Описание задачи';
comment on column tasks.created is 'Время создания задачи';
comment on column tasks.done is 'Статус выполнения задачи';

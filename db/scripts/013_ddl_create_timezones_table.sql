CREATE TABLE IF NOT EXISTS timezones
(
   id SERIAL PRIMARY KEY,
   zoneid VARCHAR UNIQUE NOT NULL
);

comment on table timezones is 'Временная зона';
comment on column timezones.id is 'Идентификатор временной зоны';
comment on column timezones.zoneid is 'Обозначение идентификатоа зоны, соответствует идентификатору, указанному в
TimeZone.getID()';
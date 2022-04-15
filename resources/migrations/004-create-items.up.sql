CREATE TABLE items
(id serial primary key,
title varchar,
description varchar,
link varchar,
channel_id integer,
constraint fk_channel foreign key (channel_id) references channels(id));

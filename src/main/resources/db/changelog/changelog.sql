
CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL NOT NULL,
    login character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)


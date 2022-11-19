CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    user_full_name VARCHAR(300) NOT NULL,
    user_email VARCHAR(300) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uq_email UNIQUE (user_email)

);

CREATE TABLE IF NOT EXISTS items
(
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    owner_id    BIGINT,
    item_name VARCHAR(255) NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    item_availability BOOLEAN NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (item_id),
    CONSTRAINT fk_item_owner FOREIGN KEY (owner_id) REFERENCES users (user_id) ON UPDATE CASCADE,
    CONSTRAINT item_name_descr_not_blank CHECK (LENGTH(item_name) > 0 AND LENGTH(item_description) > 0)
);
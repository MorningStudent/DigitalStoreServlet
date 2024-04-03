CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name varchar(100) not null,
    image varchar(100) not null,
    file varchar(100) not null,
    price_amount int not null,
    price_currency varchar(4) not null
);

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name varchar(100) not null,
    email varchar(100) not null,
    phone varchar(30) not null
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    client_id int not null,
    product_id int not null,
    quantity int not null,
    cost_amount int not null,
    cost_currency varchar(4) not null,
    payed boolean default false,
    canceled boolean default false,
    completed boolean default false,

    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

ALTER TABLE orders
ADD COLUMN hashed_id varchar(200);

ALTER TABLE products
ADD COLUMN hashed_id varchar(200);
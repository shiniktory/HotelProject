CONNECT 'jdbc:derby://localhost:1527/ST4DB;create=true;user=admin;password=pass';

DROP TABLE forgot_password;
DROP TABLE feedbacks;
DROP TABLE requests;
DROP TABLE orders;
DROP TABLE order_states;
DROP TABLE hotel;
DROP TABLE apartment_classes;
DROP TABLE apartment_states;
DROP TABLE users;
DROP TABLE roles;

CREATE TABLE roles (
id INT NOT NULL PRIMARY KEY,
name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
login VARCHAR(25) NOT NULL UNIQUE,
password VARCHAR(300) NOT NULL,
role_id INT NOT NULL,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
email VARCHAR(40) NOT NULL UNIQUE,
FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE INDEX user_index ON users(id, login);

CREATE TABLE apartment_states (
id INT NOT NULL PRIMARY KEY,
state VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE apartment_classes (
id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
class VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE hotel (
number INT NOT NULL PRIMARY KEY,
place_count INT NOT NULL,
class_id INT NOT NULL REFERENCES apartment_classes(id),
state_id INT NOT NULL REFERENCES apartment_states(id),
price DOUBLE NOT NULL
);

CREATE INDEX hotel_index ON hotel(number);

CREATE TABLE order_states (
id INT NOT NULL PRIMARY KEY,
state VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE orders (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id INT NOT NULL REFERENCES users(id),
room_number INT NOT NULL REFERENCES hotel(number),
bill DOUBLE NOT NULL DEFAULT 0,
order_state INT NOT NULL REFERENCES order_states(id),
date_created DATE NOT NULL,
arrival_date DATE,
leaving_date DATE
);

CREATE INDEX order_index ON orders(id, user_id);

CREATE TABLE requests (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id INTEGER NOT NULL REFERENCES users(id),
place_count INT NOT NULL,
class_id INT NOT NULL REFERENCES apartment_classes(id),
arrival_date DATE,
leaving_date DATE,
request_state INT NOT NULL REFERENCES order_states(id)
);

CREATE INDEX request_index ON requests(id, user_id);

CREATE TABLE feedbacks (
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id INTEGER NOT NULL REFERENCES users(id),
f_date DATE NOT NULL,
feedback VARCHAR(200) NOT NULL
);

CREATE TABLE forgot_password (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email VARCHAR(40) NOT NULL,
  token VARCHAR(100) NOT NULL,
  date_exp TIMESTAMP NOT NULL,
  reset BOOLEAN DEFAULT FALSE
);
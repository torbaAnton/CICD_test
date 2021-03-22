--liquibase formatted sql
--changeset anton_torba:insert_products_values
INSERT INTO products (name, price) VALUES ('meat', '5'), ('cheese', '2'), ('eggs', '3'), ('milk', '1'), ('bread', '1');
--changeset anton_torba:insert_categories_values
INSERT INTO categories (name, product_count) VALUES ('food', '0'), ('beverages', '0');
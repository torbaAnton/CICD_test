DROP TABLE IF EXISTS `PRODUCTS_CATEGORIES`;
DROP TABLE IF EXISTS `PRODUCTS`;
DROP TABLE IF EXISTS `CATEGORIES`;

CREATE TABLE CATEGORIES
(
    ID BIGINT IDENTITY NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PRODUCT_COUNT INT NOT NULL,
    CONSTRAINT CONSTRAINT_6 PRIMARY KEY (ID)
);

CREATE TABLE PRODUCTS
(
    ID BIGINT IDENTITY NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PRICE DECIMAL(19, 2) NOT NULL,
    CONSTRAINT CONSTRAINT_F PRIMARY KEY (ID)
);

CREATE TABLE PRODUCTS_CATEGORIES
(
    PRODUCT_ID BIGINT NOT NULL,
    CATEGORY_ID BIGINT NOT NULL
);

CREATE INDEX CATEGORY_ID_KEY_INDEX_6 ON PRODUCTS_CATEGORIES(CATEGORY_ID);

CREATE INDEX PRODUCT_ID_KEY_INDEX_6 ON PRODUCTS_CATEGORIES(PRODUCT_ID);

ALTER TABLE PRODUCTS_CATEGORIES
ADD CONSTRAINT CATEGORY_ID_KEY
FOREIGN KEY (CATEGORY_ID)
REFERENCES CATEGORIES (ID)
ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE PRODUCTS_CATEGORIES
ADD CONSTRAINT PRODUCT_ID_KEY
FOREIGN KEY (PRODUCT_ID)
REFERENCES PRODUCTS (ID)
ON UPDATE RESTRICT ON DELETE RESTRICT;

--initial data
INSERT INTO products (name, price) VALUES ('meat', '5'), ('cheese', '2'), ('eggs', '3'), ('milk', '1');
INSERT INTO categories (name, product_count) VALUES ('food', '0'), ('beverages', '0');

DROP TABLE IF EXISTS `PRODUCTS_CATEGORIES`;
DROP TABLE IF EXISTS `PRODUCTS`;
DROP TABLE IF EXISTS `CATEGORIES`;

CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_86472248_E186_4880_8231_F7B10BE95C69" START WITH 5 BELONGS_TO_TABLE;
CREATE SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_92E3FD62_6851_4A88_9D2E_A63F824A7731" START WITH 3 BELONGS_TO_TABLE;
CREATE CACHED TABLE "PUBLIC"."CATEGORIES"(
    "ID" BIGINT DEFAULT NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_92E3FD62_6851_4A88_9D2E_A63F824A7731" NOT NULL NULL_TO_DEFAULT SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_92E3FD62_6851_4A88_9D2E_A63F824A7731",
    "NAME" VARCHAR(255) NOT NULL,
    "PRODUCT_COUNT" INTEGER NOT NULL
);

truncate table CATEGORIES restart identity;

ALTER TABLE "PUBLIC"."CATEGORIES" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6" PRIMARY KEY("ID");
CREATE CACHED TABLE "PUBLIC"."PRODUCTS"(
    "ID" BIGINT DEFAULT NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_86472248_E186_4880_8231_F7B10BE95C69" NOT NULL NULL_TO_DEFAULT SEQUENCE "PUBLIC"."SYSTEM_SEQUENCE_86472248_E186_4880_8231_F7B10BE95C69",
    "NAME" VARCHAR(255) NOT NULL,
    "PRICE" DECIMAL(19, 2) NOT NULL
);

truncate table PRODUCTS restart identity;

ALTER TABLE "PUBLIC"."PRODUCTS" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_F" PRIMARY KEY("ID");
CREATE CACHED TABLE "PUBLIC"."PRODUCTS_CATEGORIES"(
    "PRODUCT_ID" BIGINT NOT NULL,
    "CATEGORY_ID" BIGINT NOT NULL
);

--truncate table PRODUCTS_CATEGORIES restart identity;

ALTER TABLE "PUBLIC"."PRODUCTS_CATEGORIES" ADD CONSTRAINT "PUBLIC"."FKTJ1VDEA8QWERBJQIE4XLDL1EL" FOREIGN KEY("PRODUCT_ID") REFERENCES "PUBLIC"."PRODUCTS"("ID") NOCHECK;
ALTER TABLE "PUBLIC"."PRODUCTS_CATEGORIES" ADD CONSTRAINT "PUBLIC"."FKQT6M2O5DLY3LUQCM00F5T4H2P" FOREIGN KEY("CATEGORY_ID") REFERENCES "PUBLIC"."CATEGORIES"("ID") NOCHECK;

--initial data
INSERT INTO products (name, price) VALUES ('meat', '5'), ('cheese', '2'), ('eggs', '3'), ('milk', '1');
INSERT INTO categories (name, product_count) VALUES ('food', '0'), ('beverages', '0');
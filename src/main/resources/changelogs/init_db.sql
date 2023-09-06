CREATE TABLE AUTHOR
(
    id            SERIAL8 PRIMARY KEY,
    name          VARCHAR(255) NOT NULL
);

CREATE TABLE CATEGORY
(
    id                 SERIAL8 PRIMARY KEY,
    title              VARCHAR(255) NOT NULL,
    parent_category_id BIGINT,
    FOREIGN KEY (parent_category_id) REFERENCES CATEGORY (id)
);

CREATE TABLE BOOK
(
    id               SERIAL8 PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author_id        BIGINT NOT NULL,
    category_id      BIGINT,
    FOREIGN KEY (author_id) REFERENCES Author (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES CATEGORY (id) ON DELETE SET NULL
);
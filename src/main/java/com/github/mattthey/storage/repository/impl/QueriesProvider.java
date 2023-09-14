package com.github.mattthey.storage.repository.impl;

public class QueriesProvider {
    private QueriesProvider()
    {
    }

    static final String AUTHOR_ID = "AUTHOR_ID";
    static final String AUTHOR_NAME = "AUTHOR_NAME";
    static final String CATEGORY_ID = "CATEGORY_ID";
    static final String CATEGORY_TITLE = "CATEGORY_TITLE";
    static final String CATEGORY_PARENT_CATEGORY_ID = "CATEGORY_PARENT_CATEGORY_ID";
    static final String BOOK_ID = "BOOK_ID";
    static final String BOOK_TITLE = "BOOK_TITLE";

    private static final String CTE_FOR_SUBCATEGORIES = """
            WITH RECURSIVE CATEGORY_HIERARCHY AS
            	(SELECT ID,
            			TITLE,
            			PARENT_CATEGORY_ID
            		FROM CATEGORY
            		WHERE ID = ?
            		UNION SELECT C.ID,
            			C.TITLE,
            			C.PARENT_CATEGORY_ID
            		FROM CATEGORY C
            		INNER JOIN CATEGORY_HIERARCHY CH ON C.PARENT_CATEGORY_ID = CH.ID)
            """;

    /**
     * SQL запрос для Postgresql для получения всех подкатегорий
     */
    static final String GET_ALL_SUBCATEGORIES = """
            %s
            SELECT ID AS CATEGORY_ID,
            	TITLE AS CATEGORY_TITLE,
            	PARENT_CATEGORY_ID AS CATEGORY_PARENT_CATEGORY_ID
            FROM CATEGORY_HIERARCHY
            WHERE ID <> ?
            """.formatted(CTE_FOR_SUBCATEGORIES);

    static final String GET_BOOKS_FROM_SUBCATEGORIES = """
            %s
            SELECT BO.ID AS BOOK_ID,
            	BO.TITLE AS BOOK_TITLE,
            	BO.AUTHOR_ID AS AUTHOR_ID,
            	AU.NAME AS AUTHOR_NAME,
            	BO.CATEGORY_ID AS CATEGORY_ID,
            	CA.TITLE AS CATEGORY_TITLE,
            	CA.PARENT_CATEGORY_ID AS CATEGORY_PARENT_CATEGORY_ID
            FROM BOOK BO
            INNER JOIN AUTHOR AU ON AU.ID = BO.AUTHOR_ID
            LEFT JOIN CATEGORY CA ON BO.CATEGORY_ID = CA.ID
            WHERE BO.CATEGORY_ID IN
            		(SELECT CHH.ID
            			FROM CATEGORY_HIERARCHY CHH)
            """.formatted(CTE_FOR_SUBCATEGORIES);

    static final String DELETE_CATEGORY_WITH_SUBCATEGORIES = """
            %s
            DELETE FROM CATEGORY
            WHERE ID IN (SELECT CHH.ID FROM CATEGORY_HIERARCHY CHH)
            """.formatted(CTE_FOR_SUBCATEGORIES);;

    static final String GET_ALL_AUTHOR_ENTITY = """
            SELECT ID AS AUTHOR_ID,
            	NAME AS AUTHOR_NAME
            FROM AUTHOR
            """;

    static final String GET_AUTHOR_BY_ID = """
            SELECT ID AS AUTHOR_ID,
            	NAME AS AUTHOR_NAME
            FROM AUTHOR
            WHERE ID = ?
            """;

    static final String INSERT_AUTHOR = """
            INSERT INTO AUTHOR(NAME)
            VALUES (?)
            RETURNING ID AS AUTHOR_ID;
            """;

    static final String DELETE_AUTHOR = """
            DELETE
            FROM AUTHOR
            WHERE ID = ?
            """;

    static final String UPDATE_AUTHOR = """
            UPDATE AUTHOR
            SET NAME = ?
            WHERE ID = ?
            """;

    static final String INSERT_CATEGORY = """
            INSERT INTO CATEGORY(TITLE, PARENT_CATEGORY_ID)
            VALUES(?, ?)
            RETURNING ID AS CATEGORY_ID
            """;

    static final String GET_ALL_CATEGORY = """
            SELECT ID AS CATEGORY_ID,
            	TITLE AS CATEGORY_TITLE,
            	PARENT_CATEGORY_ID AS CATEGORY_PARENT_CATEGORY_ID
            FROM CATEGORY
            """;

    static final String GET_CATEGORY_BY_ID = """
            SELECT ID AS CATEGORY_ID,
            	TITLE AS CATEGORY_TITLE,
            	PARENT_CATEGORY_ID AS CATEGORY_PARENT_CATEGORY_ID
            FROM CATEGORY
            WHERE ID = ?
            """;

    static final String UPDATE_CATEGORY = """
            UPDATE CATEGORY
            SET PARENT_CATEGORY_ID = ?,
            	TITLE = ?
            WHERE ID = ?
            """;

    static final String GET_ALL_BOOKS_WITH_AUTHOR_AND_CATEGORY = """
            SELECT BO.ID AS BOOK_ID,
            	BO.TITLE AS BOOK_TITLE,
            	BO.AUTHOR_ID AS AUTHOR_ID,
            	AU.NAME AS AUTHOR_NAME,
            	BO.CATEGORY_ID AS CATEGORY_ID,
            	CA.TITLE AS CATEGORY_TITLE,
            	CA.PARENT_CATEGORY_ID AS CATEGORY_PARENT_CATEGORY_ID
            FROM BOOK BO
            INNER JOIN AUTHOR AU ON AU.ID = BO.AUTHOR_ID
            LEFT JOIN CATEGORY CA ON BO.CATEGORY_ID = CA.ID
            """;

    static final String INSERT_BOOK = """
            INSERT INTO BOOK(TITLE, AUTHOR_ID, CATEGORY_ID)
            VALUES(?, ?, ?)
            RETURNING ID AS BOOK_ID
            """;

    static final String FIND_BOOK_BY_ID = """
            %s
            WHERE BO.ID = ?
            """.formatted(GET_ALL_BOOKS_WITH_AUTHOR_AND_CATEGORY);

    static final String UPDATE_BOOK = """
            UPDATE BOOK
            SET TITLE = ?,
            	AUTHOR_ID = ?,
            	CATEGORY_ID = ?
            WHERE ID = ?
            """;

    static final String DELETE_BOOK = """
            DELETE
            FROM BOOK
            WHERE ID = ?
            """;

    static final String FIND_BOOKS_BY_AUTHOR_ID = """
            %s
            WHERE BO.AUTHOR_ID = ?
            """.formatted(GET_ALL_BOOKS_WITH_AUTHOR_AND_CATEGORY);
}

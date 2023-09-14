package com.github.mattthey.storage.repository.impl;

public class QueriesProvider {
    private QueriesProvider()
    {
    }

    private static final String CTE_FOR_SUBCATEGORIES = """
            WITH RECURSIVE CATEGORY_HIERARCHY AS
            	(SELECT ID,
            			TITLE,
            			PARENT_CATEGORY_ID
            		FROM CATEGORY
            		WHERE ID = :id
            		UNION SELECT C.ID,
            			C.TITLE,
            			C.PARENT_CATEGORY_ID
            		FROM CATEGORY C
            		INNER JOIN CATEGORY_HIERARCHY CH ON C.PARENT_CATEGORY_ID = CH.ID)
            """;

    /**
     * SQL запрос для Postgresql для получения всех подкатегорий
     */
    public static final String GET_ALL_SUBCATEGORIES = CTE_FOR_SUBCATEGORIES + """
            
            SELECT ID,
            	TITLE,
            	PARENT_CATEGORY_ID
            FROM CATEGORY_HIERARCHY
            WHERE ID <> :id
            """;

    public static final String GET_BOOKS_FROM_SUBCATEGORIES = CTE_FOR_SUBCATEGORIES + """
            
            SELECT BO.ID AS ID,
            	BO.TITLE AS TITLE,
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
            """;

    public static final String DELETE_CATEGORY_WITH_SUBCATEGORIES = CTE_FOR_SUBCATEGORIES + """
            
            DELETE FROM CATEGORY
            WHERE ID IN (SELECT CHH.ID FROM CATEGORY_HIERARCHY CHH)
            """;
}

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
            		WHERE ID = ?
            		UNION SELECT C.ID,
            			C.TITLE,
            			C.PARENT_CATEGORY_ID
            		FROM CATEGORY C
            		INNER JOIN CATEGORY_HIERARCHY CH ON C.PARENT_CATEGORY_ID = CH.ID)
            """;
}

package com.github.mattthey.storage.repository.impl;

import org.jooq.CommonTableExpression;
import org.jooq.Param;
import org.jooq.Record3;

import static com.github.mattthey.gen.public_.Tables.CATEGORY;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.name;

public class QueriesProvider {
    private QueriesProvider()
    {
    }

    @SuppressWarnings("unused")
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

    static final String ID = "id";

    static CommonTableExpression<Record3<Long, String, Long>> getCategoryHierarchy(Param<Long> idParam) {
        final var categoryHierarchy = "CATEGORY_HIERARCHY";
        return name(categoryHierarchy).as(
                select(CATEGORY.ID, CATEGORY.TITLE, CATEGORY.PARENT_CATEGORY_ID)
                        .from(CATEGORY)
                        .where(CATEGORY.ID.eq(idParam))
                        .union(
                                select(CATEGORY.ID, CATEGORY.TITLE, CATEGORY.PARENT_CATEGORY_ID)
                                        .from(CATEGORY)
                                        .join(name(categoryHierarchy))
                                        .on(CATEGORY.PARENT_CATEGORY_ID.eq(field(name(categoryHierarchy, "id"), Long.class)))
                        )
        );
    }
}

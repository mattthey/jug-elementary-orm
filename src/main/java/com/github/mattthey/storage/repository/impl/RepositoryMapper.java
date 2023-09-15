package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.gen.public_.tables.records.AuthorRecord;
import com.github.mattthey.gen.public_.tables.records.BookRecord;
import com.github.mattthey.gen.public_.tables.records.CategoryRecord;
import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.entity.CategoryEntity;
import org.jooq.Record3;

public class RepositoryMapper {
    private RepositoryMapper()
    {
    }

    public static AuthorEntity toEntity(AuthorRecord record) {
        if (record.getId() == null) {
            return null;
        }
        return new AuthorEntity(record.getId(), record.getName());
    }

    public static AuthorRecord toRecord(AuthorEntity entity) {
        return new AuthorRecord(entity.getId(), entity.getName());
    }

    public static CategoryEntity toEntity(CategoryRecord record) {
        if (record.getId() == null) {
            return null;
        }
        return new CategoryEntity(record.getId(), record.getTitle(), record.getParentCategoryId());
    }

    public static CategoryRecord toRecord(CategoryEntity entity) {
        return new CategoryRecord(entity.getId(), entity.getTitle(), entity.getParentCategoryId());
    }

    public static BookRecord toRecord(BookEntity bookEntity) {
        final AuthorEntity author = bookEntity.getAuthor();
        final CategoryEntity category = bookEntity.getCategory();
        return new BookRecord(bookEntity.getId(), bookEntity.getTitle(),
                author != null ? author.getId() : null,
                category != null ? category.getId() : null
        );
    }

    public static BookEntity toEntity(Record3<BookRecord, AuthorRecord, CategoryRecord> record3) {
        final BookRecord bookRecord = record3.component1();
        final AuthorRecord authorRecord = record3.component2();
        final CategoryRecord categoryRecord = record3.component3();
        return new BookEntity(bookRecord.getId(), bookRecord.getTitle(), toEntity(authorRecord), toEntity(categoryRecord));
    }
}

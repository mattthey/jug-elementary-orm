package com.github.mattthey;

import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.api.dto.BookDto;
import com.github.mattthey.api.dto.CategoryDto;
import com.github.mattthey.service.AuthorService;
import com.github.mattthey.service.BookService;
import com.github.mattthey.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void afterEach() {
        authorService.getAllAuthors().forEach(author ->
                authorService.deleteAuthor(author.id()));
        categoryService.getAllCategories().forEach(categoryDto ->
                categoryService.deleteCategory(categoryDto.id()));
    }

    @Test
    @Rollback
    void testCrateBooksWithAnyAuthors() {
        // создаем 3 авторов
        final List<AuthorDto> authorDtos = List.of(
                new AuthorDto(0L, "Author1"),
                new AuthorDto(1L, "Author2"),
                new AuthorDto(2L, "Author3")
        );
        final List<AuthorDto> createdAuthors = authorDtos.stream().map(authorService::createAuthor).toList();

        final List<AuthorDto> allAuthorsFromRepo = authorService.getAllAuthors();
        Assertions.assertEquals(authorDtos.size(), allAuthorsFromRepo.size());

        // создаем 3 книги
        final List<BookDto> bookDtos = List.of(
                new BookDto(1L, "Book1", createdAuthors.get(0), null),
                new BookDto(2L, "Book2", createdAuthors.get(1), null),
                new BookDto(3L, "Book3", createdAuthors.get(2), null),
                new BookDto(3L, "Book4", createdAuthors.get(0), null)
        );
        bookDtos.forEach(bookService::createBook);

        final List<BookDto> allBooksFromRepo = bookService.getAllBooks();
        Assertions.assertEquals(allBooksFromRepo.size(), bookDtos.size());
    }

    @Test
    @Rollback
    void testAuthorsCreateAuthor() {
        final AuthorDto author1 = new AuthorDto(0L, "Author1");
        final AuthorDto createdAuthor = authorService.createAuthor(author1);
        Assertions.assertEquals(author1.name(), createdAuthor.name());

        final List<AuthorDto> allAuthorsFromDb = authorService.getAllAuthors();
        Assertions.assertEquals(1, allAuthorsFromDb.size());
        Assertions.assertEquals(createdAuthor, allAuthorsFromDb.get(0));
    }

    @Test
    @Rollback
    void testUpdateAuthor() {
        final AuthorDto author1 = new AuthorDto(0L, "Author1");
        final AuthorDto createdAuthor = authorService.createAuthor(author1);

        // изменяем автора
        final AuthorDto authorForUpdate = new AuthorDto(createdAuthor.id(), "new Author name");
        authorService.updateAuthor(authorForUpdate.id(), authorForUpdate);
        final List<AuthorDto> allAuthorsFromDbAfterUpdate = authorService.getAllAuthors();
        Assertions.assertEquals(1, allAuthorsFromDbAfterUpdate.size());
        Assertions.assertEquals(authorForUpdate, allAuthorsFromDbAfterUpdate.get(0));

        // изменяем автора с указанием ID, которого не существует
        Assertions.assertThrowsExactly(RuntimeException.class, () ->
                authorService.updateAuthor(100L, authorForUpdate));
    }

    @Test
    @Rollback
    void testDeleteAuthor() {
        final AuthorDto author1 = new AuthorDto(0L, "Author1");
        final AuthorDto createdAuthor = authorService.createAuthor(author1);

        // создаем книгу с автором
        final BookDto book1 = new BookDto(null, "Book1", createdAuthor, null);
        bookService.createBook(book1);

        // удаляем автора и ожилдаем, что удалится автор и книга
        authorService.deleteAuthor(createdAuthor.id());
        final List<AuthorDto> allAuthorsFromDbAfterDelete = authorService.getAllAuthors();
        Assertions.assertEquals(0, allAuthorsFromDbAfterDelete.size());

        final List<BookDto> allBooksFromDbAfterDelete = bookService.getAllBooks();
        Assertions.assertEquals(0, allBooksFromDbAfterDelete.size());
    }

    @Test
    @Rollback
    void testCreateBookWithoutAuthor() {
        final BookDto bookWithoutAuthor = new BookDto(null, "Book1", null, null);
        Assertions.assertThrows(NullPointerException.class, () -> bookService.createBook(bookWithoutAuthor));
    }

    @Test
    @Rollback
    void testCreateBookWithExistingAuthor() {
        final AuthorDto author1 = new AuthorDto(null, "Author1");
        final AuthorDto createdAuthor = authorService.createAuthor(author1);

        final BookDto book1 = new BookDto(null, "Book1", createdAuthor, null);
        final BookDto createdBook = bookService.createBook(book1);
        Assertions.assertNotNull(createdBook.id());
        final List<BookDto> allBooksFromDb = bookService.getAllBooks();
        Assertions.assertEquals(1, allBooksFromDb.size());
        Assertions.assertEquals(createdBook, allBooksFromDb.get(0));
    }

    @Test
    @Rollback
    void testCreateBookAndAuthor() {
        final AuthorDto author1 = new AuthorDto(null, "Author1");
        final BookDto book1 = new BookDto(null, "Book1", author1, null);
        final BookDto createdBook = bookService.createBook(book1);

        final List<BookDto> allBooksFromDb = bookService.getAllBooks();
        Assertions.assertEquals(1, allBooksFromDb.size());
        Assertions.assertEquals(createdBook, allBooksFromDb.get(0));

        final List<AuthorDto> allAuthorsFromDb = authorService.getAllAuthors();
        Assertions.assertEquals(1, allAuthorsFromDb.size());
        Assertions.assertEquals(author1.name(), allAuthorsFromDb.get(0).name());
    }

    @Test
    @Rollback
    void testUpdateBookAndCreateAuthor() {
        final AuthorDto author1 = new AuthorDto(null, "Author1");
        final BookDto book1 = new BookDto(null, "Book1", author1, null);
        final BookDto createdBook = bookService.createBook(book1);

        final AuthorDto author2 = new AuthorDto(null, "Author2");
        final BookDto book2 = new BookDto(createdBook.id(), "Book2", author2, null);
        bookService.updateBook(book2.id(), book2);

        final List<AuthorDto> allAuthors = authorService.getAllAuthors();
        Assertions.assertEquals(2, allAuthors.size());

        final AuthorDto createdAuthor2 = allAuthors.stream()
                .filter(a -> a.name().equals("Author2"))
                .findAny()
                .orElseThrow();
        final BookDto expectedUpdatedBook = new BookDto(createdBook.id(), "Book2", createdAuthor2, null);

        final List<BookDto> allBooksFromDb = bookService.getAllBooks();
        Assertions.assertEquals(1, allBooksFromDb.size());
        Assertions.assertEquals(expectedUpdatedBook, allBooksFromDb.get(0));
    }


    @Test
    @Rollback
    void testUpdateBookToExistingAuthor() {
        final AuthorDto author1 = authorService.createAuthor(new AuthorDto(null, "Author1"));
        final AuthorDto author2 = authorService.createAuthor(new AuthorDto(null, "Author2"));

        final BookDto book1 = new BookDto(null, "Book1", author1, null);
        final BookDto createdBook = bookService.createBook(book1);

        final BookDto book2 = new BookDto(createdBook.id(), "Book2", author2, null);
        bookService.updateBook(book2.id(), book2);

        final List<BookDto> allBooks = bookService.getAllBooks();
        Assertions.assertEquals(1, allBooks.size());
        Assertions.assertEquals(book2, allBooks.get(0));
    }

    @Test
    @Rollback
    void testDeleteBookWithoutDeleteAuthor() {
        final AuthorDto author1 = authorService.createAuthor(new AuthorDto(null, "Author1"));
        final BookDto book1 = bookService.createBook(new BookDto(null, "Book1", author1, null));
        bookService.deleteBook(book1.id());

        final List<BookDto> allBooks = bookService.getAllBooks();
        Assertions.assertEquals(0, allBooks.size());

        final List<AuthorDto> allAuthors = authorService.getAllAuthors();
        Assertions.assertEquals(1, allAuthors.size());
    }

    @Test
    @Rollback
    void testGetAuthorsBooks() {
        final AuthorDto author1 = authorService.createAuthor(new AuthorDto(null, "Author1"));
        final AuthorDto author2 = authorService.createAuthor(new AuthorDto(null, "Author2"));

        final List<BookDto> bookFromAuthor1 = new ArrayList<>(List.of(
                bookService.createBook(new BookDto(null, "Book1", author1, null)),
                bookService.createBook(new BookDto(null, "Book2", author1, null)),
                bookService.createBook(new BookDto(null, "Book3", author1, null))
        ));
        final List<BookDto> authorsBooks = bookService.getAuthorsBooks(author1.id());
        Assertions.assertEquals(Set.copyOf(bookFromAuthor1), Set.copyOf(authorsBooks));

        final BookDto bookForChangeAuthor = bookFromAuthor1.remove(0);
        final BookDto bookWithChangedAuthor = new BookDto(bookForChangeAuthor.id(), "Book1", author2, null);
        bookService.updateBook(bookForChangeAuthor.id(), bookWithChangedAuthor);

        final List<BookDto> authorsBooksAfterUpdate = bookService.getAuthorsBooks(author1.id());
        Assertions.assertEquals(Set.copyOf(bookFromAuthor1), Set.copyOf(authorsBooksAfterUpdate));

        final List<BookDto> authors2Book = bookService.getAuthorsBooks(author2.id());
        Assertions.assertEquals(Set.of(bookWithChangedAuthor), Set.copyOf(authors2Book));
    }

    @Test
    @Rollback
    void testCreateCategory() {
        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        final CategoryDto category6 = categoryService.createCategory(new CategoryDto(null, "Category6", null));

        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));
        final CategoryDto category3 = categoryService.createCategory(new CategoryDto(null, "Category3", category1.id()));

        final CategoryDto category4 = categoryService.createCategory(new CategoryDto(null, "Category4", category2.id()));
        final CategoryDto category5 = categoryService.createCategory(new CategoryDto(null, "Category5", category2.id()));

        final List<CategoryDto> allCategories = categoryService.getAllCategories();
        Assertions.assertEquals(6, allCategories.size());
        final Set<CategoryDto> expectedAllCategories = Set.of(category1, category2, category3, category4, category5, category6);
        Assertions.assertEquals(expectedAllCategories, Set.copyOf(allCategories));

        final List<CategoryDto> subcategoriesFor2 = categoryService.getSubcategories(category2.id());
        Assertions.assertEquals(Set.of(category4, category5), Set.copyOf(subcategoriesFor2));

        final List<CategoryDto> subcategoriesFor6 = categoryService.getSubcategories(category6.id());
        Assertions.assertEquals(0, subcategoriesFor6.size());

        final List<CategoryDto> subcategoriesFor1 = categoryService.getSubcategories(category1.id());
        Assertions.assertEquals(Set.of(category2, category3, category4, category5), Set.copyOf(subcategoriesFor1));
    }

    @Test
    @Rollback
    void testUpdateCategory() {
        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        final CategoryDto category6 = categoryService.createCategory(new CategoryDto(null, "Category6", null));

        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));
        final CategoryDto category3 = categoryService.createCategory(new CategoryDto(null, "Category3", category1.id()));

        final CategoryDto category4 = categoryService.createCategory(new CategoryDto(null, "Category4", category2.id()));
        final CategoryDto category5 = categoryService.createCategory(new CategoryDto(null, "Category5", category2.id()));

        final List<CategoryDto> subcategoriesFrom6 = categoryService.getSubcategories(category6.id());
        Assertions.assertEquals(0, subcategoriesFrom6.size());

        final CategoryDto updatedCategory2 = new CategoryDto(category2.id(), "Category2-updated", category6.id());
        categoryService.updateCategory(category2.id(), updatedCategory2);

        final List<CategoryDto> subcategoriesFrom1 = categoryService.getSubcategories(category1.id());
        Assertions.assertEquals(Set.of(category3), Set.copyOf(subcategoriesFrom1));

        final List<CategoryDto> subcategoriesFrom6AfterUpdate = categoryService.getSubcategories(category6.id());
        Assertions.assertEquals(Set.of(updatedCategory2, category4, category5), Set.copyOf(subcategoriesFrom6AfterUpdate));
    }

    @Test
    @Rollback
    void testRecursiveDependenciesForbidden() {
        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));

        final CategoryDto category1ForUpdate = new CategoryDto(category1.id(), "Category1-updated", category2.id());
        Assertions.assertThrows(RuntimeException.class, () -> categoryService.updateCategory(category1ForUpdate.id(), category1ForUpdate));
    }

    @Test
    @Rollback
    void testRecursiveDependenciesForbidden2() {
        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        categoryService.createCategory(new CategoryDto(null, "Category6", null));

        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));
        categoryService.createCategory(new CategoryDto(null, "Category3", category1.id()));

        categoryService.createCategory(new CategoryDto(null, "Category4", category2.id()));
        final CategoryDto category5 = categoryService.createCategory(new CategoryDto(null, "Category5", category2.id()));

        final CategoryDto category2ForUpdate = new CategoryDto(category2.id(), "Category2-updated", category5.id());
        Assertions.assertThrows(RuntimeException.class, () -> categoryService.updateCategory(category2ForUpdate.id(), category2ForUpdate));
    }

    @Test
    @Rollback
    void testRecursiveDeleteCategories() {
        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        final CategoryDto category6 = categoryService.createCategory(new CategoryDto(null, "Category6", null));

        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));
        categoryService.createCategory(new CategoryDto(null, "Category3", category1.id()));

        categoryService.createCategory(new CategoryDto(null, "Category4", category2.id()));
        categoryService.createCategory(new CategoryDto(null, "Category5", category2.id()));

        categoryService.deleteCategory(category1.id());
        final List<CategoryDto> allCategoriesAfterDelete = categoryService.getAllCategories();
        Assertions.assertEquals(1, allCategoriesAfterDelete.size());
        Assertions.assertEquals(category6, allCategoriesAfterDelete.get(0));
    }

    @Test
    @Rollback
    void testGetBooksWithSpecifiedCategory() {
        final AuthorDto author1 = authorService.createAuthor(new AuthorDto(null, "Author1"));

        final CategoryDto category1 = categoryService.createCategory(new CategoryDto(null, "Category1", null));
        final CategoryDto category6 = categoryService.createCategory(new CategoryDto(null, "Category6", null));

        final CategoryDto category2 = categoryService.createCategory(new CategoryDto(null, "Category2", category1.id()));
        final CategoryDto category3 = categoryService.createCategory(new CategoryDto(null, "Category3", category1.id()));

        final CategoryDto category4 = categoryService.createCategory(new CategoryDto(null, "Category4", category2.id()));
        final CategoryDto category5 = categoryService.createCategory(new CategoryDto(null, "Category5", category2.id()));

        final BookDto book1 = bookService.createBook(new BookDto(null, "Book1", author1, category1));
        final BookDto book11 = bookService.createBook(new BookDto(null, "Book11", author1, category1));
        final BookDto book2 = bookService.createBook(new BookDto(null, "Book2", author1, category2));
        final BookDto book3 = bookService.createBook(new BookDto(null, "Book3", author1, category3));
        final BookDto book4 = bookService.createBook(new BookDto(null, "Book4", author1, category4));
        final BookDto book5 = bookService.createBook(new BookDto(null, "Book5", author1, category5));
        final BookDto book6 = bookService.createBook(new BookDto(null, "Book6", author1, category6));

        final List<BookDto> actualResultForSubcategoriesFrom1 = bookService.getBooksWithSpecifiedCategory(category1.id());
        Assertions.assertEquals(Set.of(book1, book11, book2, book3, book4, book5), Set.copyOf(actualResultForSubcategoriesFrom1), () ->
                "Has next books: " + actualResultForSubcategoriesFrom1.stream().map(BookDto::title).collect(Collectors.joining(", ")));

        final List<BookDto> actualBooksWithSpecifiedCategoryFrom2 = bookService.getBooksWithSpecifiedCategory(category2.id());
        Assertions.assertEquals(Set.of(book2, book4, book5), Set.copyOf(actualBooksWithSpecifiedCategoryFrom2));

        // remove category from book5
        final BookDto book5Update = new BookDto(book5.id(), "Book5-updated", author1, null);
        bookService.updateBook(book5Update.id(), book5Update);

        final List<BookDto> actualBooksWithSpecifiedCategoryFrom2AfterUpdate = bookService.getBooksWithSpecifiedCategory(category2.id());
        Assertions.assertEquals(Set.of(book2, book4), Set.copyOf(actualBooksWithSpecifiedCategoryFrom2AfterUpdate));

        // удалим вторую категорию, тогда дожны также удалиться 4 и 5
        categoryService.deleteCategory(category2.id());

        final List<BookDto> booksWithSpecifiedCategoryAfterRemoveCategory2 = bookService.getBooksWithSpecifiedCategory(category1.id());
        Assertions.assertEquals(Set.of(book1, book11, book3), Set.copyOf(booksWithSpecifiedCategoryAfterRemoveCategory2));


        final List<BookDto> allBooks = List.of(book1, book11, book2, book3, book4, book5, book6);
        allBooks.forEach(book -> Assertions.assertNotNull(bookService.getBookById(book.id())));
    }
}

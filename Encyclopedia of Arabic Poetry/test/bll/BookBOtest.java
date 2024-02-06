package bll;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.logging.log4j.core.config.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bll.bookbll.BookBO;
import dal.DALFacade;
import dal.IDALFacade;
import dal.Tokenize.ITokenizeDAO;
import dal.bookdal.IBookDAO;
import dal.poemdal.importPoem.IParsePoemDAO;
import dal.poemdal.manualAdd.IPoemDAO;
import dal.rootdal.IRootDao;
import dal.versedal.IVerseDAO;
import stub.BookStub;
import stub.ParsePoemStub;
import stub.PoemStub;
import stub.RootStub;
import stub.TokenStub;
import stub.VerseStub;

class BookBOtest {

	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
	static IRootDao root;
	static IParsePoemDAO parsepoem;

	static BookBO bo;
	static IDALFacade dal;

	@BeforeAll
	static void coldStart() throws SQLException, ParseException {
		poemDAO = new PoemStub();
		verseDAO = new VerseStub();
		bookDAO = new BookStub();
		root = new RootStub();
		token = new TokenStub();
		parsepoem = new ParsePoemStub();

		dal = new DALFacade(root, parsepoem, bookDAO, poemDAO, verseDAO, token);
		bo = new BookBO(dal);

	}

	@Test
    @DisplayName("Select Books - Valid Selection")
    void testSelectBooksValidSelection() {
       
        List<dal.bookdal.Book> books = bo.selectBooks();

       
        assertNotNull(books, "List of books should not be null");
        assertFalse(books.isEmpty(), "List of books should not be empty");
    }

    
    @Test
    @DisplayName("Select Books - Valid Selection with Book Details Check")
    void testSelectBooksValidSelectionWithDetails() {
       
        List<dal.bookdal.Book> books = bo.selectBooks();

       
        assertNotNull(books, "List of books should not be null");
        assertFalse(books.isEmpty(), "List of books should not be empty");

        
        dal.bookdal.Book firstBook = books.get(0);
        assertNotNull(firstBook, "First book should not be null");
        assertEquals("عندما أصبحنا أحرارًا", firstBook.getTitle(), "Title should match expected value");
        assertEquals("أحمد مطر", firstBook.getAuthor(), "Author should match expected value");
        
    }


    @Test
    
    @DisplayName("Select Books - Valid Selection with Multiple Books")
    void testSelectBooksValidSelectionWithMultipleBooks() {
       
        List<dal.bookdal.Book> books = bo.selectBooks();

        
        assertNotNull(books, "List of books should not be null");
        assertFalse(books.isEmpty(), "List of books should not be empty");
        assertTrue(books.size() > 1, "List of books should contain multiple books");
    }
    @Test
    @DisplayName("Get Book ID By Title - Valid Title")
    void testGetBookIdByTitleValidTitle() {
     
        String validTitle = "عندما أصبحنا أحرارًا";

        
        int bookId = bo.getBookIdByTitle(validTitle);

        
        assertTrue(bookId > 0, "Book ID should be greater than 0");
    }

    @Test
    @DisplayName("Get Book ID By Title - Non-Existing Title")
    void testGetBookIdByTitleNonExistingTitle() {
       
        String nonExistingTitle = "Non-Existing Title";

       
        int bookId = bo.getBookIdByTitle(nonExistingTitle);

       
        assertEquals(-1, bookId, "Book ID should be -1 for non-existing title");
    }

   

    @Test
    @DisplayName("Get Book ID By Title - Valid Title with Multiple Books")
    void testGetBookIdByTitleValidTitleWithMultipleBooks() {
       
        String validTitle = "جريمة وعقوبة";

      
        int bookId = bo.getBookIdByTitle(validTitle);

      
        assertTrue(bookId > 0, "Book ID should be greater than 0");
    }

    @Test
    @DisplayName("Get Book ID By Title - Valid Title with Special Characters")
    void testGetBookIdByTitleValidTitleWithSpecialCharacters() {
            String validTitle = "هاري بوتر وحجر الساحر";

        int bookId = bo.getBookIdByTitle(validTitle);

        
        assertTrue(bookId > 0, "Book ID should be greater than 0");
    }
    
    @Test

	@DisplayName("Update Book - Valid Update")
	void testUpdateBookValidUpdate() {

		String title = "عندما أصبحنا أحرارًا";
		String author = "Updated Author";
		Date publishDate = new Date(0);
		Date deathDate = new Date(0);

		bo.updateBook(title, author, publishDate, deathDate);

	}

	@Test
	@DisplayName("Update Book - Invalid Update (Null Title)")
	void testUpdateBookInvalidUpdateNullTitle() {

		String title = null;
		String author = "Updated Author";
		Date publishDate = new Date(0);
		Date deathDate = new Date(0);

		assertThrows(IllegalArgumentException.class, () -> bo.updateBook(title, author, publishDate, deathDate));
	}

	@Test
	@DisplayName("Update Book - Valid Update With Death Date Null")
	void testUpdateBookValidUpdateNullDeathDate() {

		String title = "عندما أصبحنا أحرارًا";
		String author = "Updated Author";
		Date publishDate = new Date(0);
		Date deathDate = null;

		assertDoesNotThrow(() -> bo.updateBook(title, author, publishDate, deathDate));

	}

	@Test
	@DisplayName("Delete Book - Valid Title")
	void testDeleteBookValidTitle() {

		String validTitle = "عندما أصبحنا أحرارًا";

		assertDoesNotThrow(() -> bo.deleteBook(validTitle));

	}

	@Test
	@DisplayName("Delete Book - Non-Existing Title")
	void testDeleteBookNonExistingTitle() {

		String nonExistingTitle = "Non-Existing Title";

		assertDoesNotThrow(() -> bo.deleteBook(nonExistingTitle));

	}

	@Test
	@DisplayName("Delete Book - Invalid Title (Null)")
	void testDeleteBookInvalidTitleNull() {

		String nullTitle = null;

		assertThrows(Exception.class, () -> bo.deleteBook(nullTitle));
	}

	@Test
	@DisplayName("Insert Book - Valid Insert")
	void testInsertBookValidInsert() {

		String title = "New Book";
		String author = "Author";
		Date publishDate = new Date(0);
		Date deathDate = null;

		assertDoesNotThrow(() -> bo.insertBook(title, author, publishDate, deathDate));
	}

	@Test
	@DisplayName("Insert Book - Invalid Insert (Null Title)")
	void testInsertBookInvalidInsertNullTitle() {

		String title = null;
		String author = "Author";
		Date publishDate = new Date(0);
		Date deathDate = null;

		assertThrows(Exception.class, () -> bo.insertBook(title, author, publishDate, deathDate));
	}






}
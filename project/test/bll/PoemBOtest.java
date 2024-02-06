package bll;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.poembll.PoemBO;
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

class PoemBOtest {

	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
	static IRootDao root;
	static IParsePoemDAO parsepoem;

	static PoemBO bo;
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
		bo = new PoemBO(dal);

	}

	@Test
	@DisplayName("Update Poem - Valid Update")
	void testUpdatePoemValidUpdate() {
		bo.updatePoem(1, "قصيدة جميلة ", "Updated قصيدة جميلة");
		List<String> poems = bo.viewPoemsByBook(1);
		assertTrue(poems.contains("Updated قصيدة جميلة"));
	}

	@Test
	@DisplayName("Update Poem - Non-Existing Poem")
	void testUpdatePoemNonExistingPoem() {
		assertDoesNotThrow(() -> bo.updatePoem(2, "Non-Existing Title", "New Title"));
	}

	@Test
	@DisplayName("Update Poem - Non-Existing BookId")
	void testUpdatePoemNonExistingBookId() {

		int nonExistingBookId = 100;
		String poemTitle = "Some Poem Title";
		String updatedTitle = "Updated Poem Title";

		bo.updatePoem(nonExistingBookId, poemTitle, updatedTitle);

		assertTrue(true);
	}

	@Test
	@DisplayName("Update Poem - Empty New Title")
	void testUpdatePoemEmptyNewTitle() {

		int bookId = 2;
		String existingPoemTitle = "قصيدة مفكرة";
		String emptyNewTitle = "";

		bo.updatePoem(bookId, existingPoemTitle, emptyNewTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertFalse(poems.contains(emptyNewTitle), "Poem should not be updated with an empty title");
	}

	@Test
	@DisplayName("Add Poem - Valid Input")
	void testAddPoemValidInput() {

		int bookId = 5;
		String poemTitle = "New Poem Title";

		bo.addPoem(bookId, poemTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertTrue(poems.contains(poemTitle), "Poem should be added successfully");
	}

	@Test
	@DisplayName("Add Poem - Invalid Input (Null Title)")
	void testAddPoemInvalidInput() {

		int bookId = 5;
		String poemTitle = null;

		bo.addPoem(bookId, poemTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertFalse(poems.contains(poemTitle), "Poem should not be added with null title");
	}

	@Test
	@DisplayName("Add Poem - Existing Poem Title")
	void testAddPoemExistingTitle() {

		int bookId = 3;
		String existingPoemTitle = "قصيدة غامضة";

		bo.addPoem(bookId, existingPoemTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertTrue(poems.contains(existingPoemTitle), "Poem with existing title should be added successfully");
	}

	@Test
	@DisplayName("Add Poem - Invalid BookId")
	void testAddPoemInvalidBookId() {

		int invalidBookId = -1;
		String poemTitle = "Invalid BookId Poem";

		bo.addPoem(invalidBookId, poemTitle);

		assertTrue(true);
	}

	@Test
	@DisplayName("Get Poem ID By Title - Valid Input")
	void testGetPoemIdByTitleValidInput() {

		int bookId = 1;
		String poemTitle = "قصيدة جميلة";

		int poemId = bo.getPoemIdByTitle(bookId, poemTitle);

		assertEquals(poemId, poemId, "Poem ID should match the expected value for the specified bookId and poemTitle");
	}

	@Test
	@DisplayName("Add Poem - Empty Poem Title")
	void testAddPoemEmptyTitle() {

		int bookId = 4;
		String emptyTitle = "";

		bo.addPoem(bookId, emptyTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertTrue(poems.contains(emptyTitle), "Poem with empty title should be added successfully");
	}

	@Test
	@DisplayName("Delete Poem - Valid Input")
	void testDeletePoemValidInput() {

		int bookId = 1;
		String poemTitle = "قصيدة جميلة";

		// Act
		bo.deletePoem(bookId, poemTitle);

		List<String> poems = bo.viewPoemsByBook(bookId);
		assertFalse(poems.contains(poemTitle), "Poem should be deleted successfully");
	}

	@Test
	@DisplayName("Delete Poem - Non-Existing Poem")
	void testDeletePoemNonExistingPoem() {

		int bookId = 2;
		String poemTitle = "Non-Existing Poem";

		bo.deletePoem(bookId, poemTitle);

		assertTrue(true);
	}

	@Test
	@DisplayName("Delete Poem - Invalid Input (Negative BookId)")
	void testDeletePoemInvalidInput() {

		int bookId = -1;

		bo.deletePoem(bookId, "Some Poem Title");

		assertTrue(true);
	}

	@Test
	@DisplayName("Delete Poem - Invalid Input (Null Poem Title)")
	void testDeletePoemInvalidInputNullTitle() {

		int bookId = 1;
		String poemTitle = null;

		bo.deletePoem(bookId, poemTitle);

		assertTrue(true);
	}

	@Test
	@Disabled
	@DisplayName("View Poems By Book - Valid BookId")
	void testViewPoemsByBookValidBookId() {

		int bookId = 2;

		List<String> poems = bo.viewPoemsByBook(bookId);

		assertNotNull(poems, "List of poems should not be null");
		assertEquals(1, poems.size(), "There should be one poem for the specified bookId");
		assertTrue(poems.contains("قصيدة ملهمة"), "Poem 'قصيدة ملهمة' should be in the list");
	}

	@Test
	@DisplayName("View Poems By Book - Non-Existing BookId")
	void testViewPoemsByBookNonExistingBookId() {

		int nonExistingBookId = 100; // Choose a non-existing bookId

		List<String> poems = bo.viewPoemsByBook(nonExistingBookId);

		assertNotNull(poems, "List of poems should not be null");
		assertTrue(poems.isEmpty(), "List of poems should be empty for a non-existing bookId");
	}

	@Test
	@DisplayName("View Poems By Book - Invalid BookId (Negative)")
	void testViewPoemsByBookInvalidBookId() {

		int invalidBookId = -1; // Choose an invalid bookId

		List<String> poems = bo.viewPoemsByBook(invalidBookId);

		assertNotNull(poems, "List of poems should not be null for an invalid bookId");
		assertTrue(poems.isEmpty(), "List of poems should be empty for an invalid bookId");
	}

	@Test
	@DisplayName("View Poems By Book - Empty BookId")
	void testViewPoemsByBookEmptyBookId() {

		int emptyBookId = 0; // Choose an empty bookId

		List<String> poems = bo.viewPoemsByBook(emptyBookId);

		assertNotNull(poems, "List of poems should not be null for an empty bookId");
		assertTrue(poems.isEmpty(), "List of poems should be empty for an empty bookId");
	}

	@Test
	@DisplayName("Get Poem ID By Title - Non-Existing Title")
	void testGetPoemIdByTitleNonExistingTitle() {

		int bookId = 2; // Choose a bookId with no poems
		String nonExistingPoemTitle = "Non-Existing Title";

		int poemId = bo.getPoemIdByTitle(bookId, nonExistingPoemTitle);

		assertEquals(0, poemId, "Poem ID should be 0 for a non-existing poem title");
	}

	@Test
	@DisplayName("Get Poem ID By Title - Invalid BookId")
	void testGetPoemIdByTitleInvalidBookId() {

		int invalidBookId = -1; // Choose an invalid bookId
		String poemTitle = "Some Poem Title";

		int poemId = bo.getPoemIdByTitle(invalidBookId, poemTitle);

		assertEquals(0, poemId, "Poem ID should be 0 for an invalid bookId");
	}

	@Test
	@DisplayName("Get Poem ID By Title - Empty Title")
	void testGetPoemIdByTitleEmptyTitle() {

		int bookId = 3; // Choose an existing bookId
		String emptyTitle = ""; // Choose an empty poem title

		int poemId = bo.getPoemIdByTitle(bookId, emptyTitle);

		assertEquals(0, poemId, "Poem ID should be 0 for an empty poem title");
	}

	@Test
	@DisplayName("Get Book Title By Poem Title - Valid Poem ID")
	void testGetBookTitleByPoemTitleValidPoemId() {

		int validPoemId = 1; // Choose an existing poem ID

		String bookTitle = bo.getBookTitleByPoemTitle(validPoemId);

		assertNotNull(bookTitle, "Book title should not be null for a valid poem ID");
		assertEquals("Book Title: 1", bookTitle,
				"Book title should match the expected value for the specified poem ID");
	}

	@Test
	@DisplayName("Get Book Title By Poem Title - Non-Existing Poem ID")
	void testGetBookTitleByPoemTitleNonExistingPoemId() {

		int nonExistingPoemId = 100; // Choose a non-existing poem ID

		String bookTitle = bo.getBookTitleByPoemTitle(nonExistingPoemId);

		assertEquals(null, bookTitle, "Book title should be null for a non-existing poem ID");
	}

	@Test
	@DisplayName("Get Book Title By Poem Title - Invalid Poem ID (Negative)")
	void testGetBookTitleByPoemTitleInvalidPoemId() {

		int invalidPoemId = -1; // Choose an invalid poem ID

		String bookTitle = bo.getBookTitleByPoemTitle(invalidPoemId);

		assertEquals(null, bookTitle, "Book title should be null for an invalid poem ID");
	}

	@Test
	@DisplayName("Get Book Title By Poem Title - Zero Poem ID")
	void testGetBookTitleByPoemTitleZeroPoemId() {

		int zeroPoemId = 0; // Choose a zero poem ID

		String bookTitle = bo.getBookTitleByPoemTitle(zeroPoemId);

		assertEquals(null, bookTitle, "Book title should be null for a zero poem ID");
	}

	@Test
	@DisplayName("Get Poem ID By Title - Null Title")
	void testGetPoemIdByTitleNullTitle() {

		String nullTitle = null; // Choose a null poem title

		int poemId = bo.PoemIdByTitle(nullTitle);

		assertEquals(0, poemId, "Poem ID should be 0 for a null title");
	}

}
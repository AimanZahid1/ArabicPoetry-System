package bll;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.poembll.ImportPoemBO;
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

class PasePoemBOtest {

	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
     static IRootDao root;
     static IParsePoemDAO parsepoem;
	
	static ImportPoemBO bo;
	static IDALFacade dal;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		poemDAO = new PoemStub();
		verseDAO=new VerseStub();
		 bookDAO=new BookStub();
		 root=new RootStub();
		 token = new TokenStub();
		 parsepoem= new ParsePoemStub();
		//bookDAO = new BookStub();
		
		dal = new DALFacade(root, parsepoem,bookDAO,poemDAO,verseDAO,token);
		bo = new ImportPoemBO(dal);
	}
	@Test
	@DisplayName("Import Poem - Valid Path and Book Name")
	void ValidPathAndBook() {
	    // Arrange
	    String validPath = "valid/path/to/poems.txt";
	    String validBookName = "Valid Book Name";

	    // Act
	    boolean result = bo.importPoems(validPath, validBookName);

	    // Assert
	    assertTrue(result, "Importing poems should be successful for a valid path and book name.");
	}

	@Test
	@DisplayName("Import Poem - Invalid Path and Invalid Book Name")
	void InvalidPathAndBook() {
	    // Arrange
	    String invalidPath = null; // or any invalid path
	    String invalidBookName = null; // or any invalid book name

	    // Act
	    boolean result = bo.importPoems(invalidPath, invalidBookName);

	    // Assert
	    assertFalse(result, "Importing poems should fail for an invalid path and book name.");
	}

	@Test
	@DisplayName("Import Poem - Valid Path and Invalid Book Name")
	void ValidPathAndInvalidBook() {
	    // Arrange
	    String validPath = "valid/path/to/poems.txt";
	    String invalidBookName = null; // or any invalid book name

	    // Act
	    boolean result = bo.importPoems(validPath, invalidBookName);

	    // Assert
	    assertFalse(result, "Importing poems should fail for a valid path and invalid book name.");
	}
	@Test
	@DisplayName("Import Poem - Invalid Path and Valid Book Name")
	void InvalidPathAndValidBook() {
	    // Arrange
	    String invalidPath = null; // or any invalid path
	    String validBookName = "Valid Book Name";

	    // Act
	    boolean result = bo.importPoems(invalidPath, validBookName);

	    // Assert
	    assertFalse(result, "Importing poems should fail for an invalid path and valid book name.");
	}

	}



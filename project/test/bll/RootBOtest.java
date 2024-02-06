package bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.rootbll.RootBO;
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

class RootBOtest {


	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
     static IRootDao root;
     static IParsePoemDAO parsepoem;
	
	static RootBO bo;
	static IDALFacade dal;
	@BeforeAll
	static void coldStart() throws SQLException, ParseException {
		poemDAO = new PoemStub();
		verseDAO=new VerseStub();
		 bookDAO=new BookStub();
		 root=new RootStub();
		 token = new TokenStub();
		 parsepoem= new ParsePoemStub();
		
		dal = new DALFacade(root, parsepoem,bookDAO,poemDAO,verseDAO,token);
		bo = new RootBO(dal);

	}
	
	
	@Test
    @DisplayName("Add Route  - Valid Insert")
	void testValidAddRoute() throws SQLException {
		
		String validRoot = "مرحبا";
        int validTokenId = 1;
        int validVerseId = 101;
        String validStatus = "verified";
        boolean result=bo.addRoute(validRoot, validTokenId, validVerseId, validStatus);
		
        assertTrue(result, "root added");
}

	@Test
	@DisplayName("Add Route  - InValid Insert")
	void testInValidAddRoute() throws SQLException {

	    int validVerseId = 101;
	    String validStatus = "verified";
	    
	    // Pass null for tokenId
	    boolean result = bo.addRoute(null, 1, validVerseId, validStatus);

	    assertFalse(result, "root not added");
	}


	@Test
	@DisplayName("Delete Route - Valid Deletion")
	void testValidDelRoute() throws SQLException {
	    // Arrange
	    String validRoot = "مرحبا";
	    int validTokenId = 1;
	    int validVerseId = 101;
	    String validStatus = "Active";

	    // Act
	    boolean addResult = bo.addRoute(validRoot, validTokenId, validVerseId, validStatus);
	    boolean delResult = bo.deleteroute(validRoot);

	    // Assert
	    assertTrue(addResult, "Adding route should be successful for valid parameters.");
	    assertTrue(delResult, "Deleting route should be successful for valid root.");

	}
	
	@Test
	@DisplayName("Delete Route - Invalid Deletion")
	void testInvalidDelRoute() throws SQLException {
	    // Arrange
	    String validRoot = "مرحبا";
	    int validTokenId = 1;
	    int validVerseId = 101;
	    String validStatus = "Active";
	    String invalidRoot = "غير صالح";

	    // Act
	    boolean addResult = bo.addRoute(validRoot, validTokenId, validVerseId, validStatus);
	    boolean delResult = bo.deleteroute(invalidRoot); 

	    // Assert
	    assertTrue(addResult, "Adding route should be successful for valid parameters.");
	    assertFalse(delResult, "Deleting route should be unsuccessful for invalid root.");
	}
	
	@Test
	@DisplayName("Update Route - Valid Update")
	void testValidUpdateRoute() throws SQLException {
	    // Arrange
	    String validRoot = "مرحبا";
	    int validTokenId = 1;
	    int validVerseId = 101;
	    String validStatus = "Active";
	    String newRootWord = "تحديث";

	    // Act
	    boolean addResult = bo.addRoute(validRoot, validTokenId, validVerseId, validStatus);
	    boolean updateResult = bo.updateroute(validRoot, newRootWord, validStatus);

	    // Assert
	    assertTrue(addResult, "Adding route should be successful for valid parameters.");
	    assertTrue(updateResult, "Updating route should be successful for valid parameters.");


	}
	@Test
	@DisplayName("Update Route - Invalid Update")
	void testInvalidUpdateRoute() throws SQLException {
	    // Arrange
	    String validRoot = "مرحبا";
	    int validTokenId = 1;
	    int validVerseId = 101;
	    String validStatus = "Active";
	    String newRootWord = null; // Invalid newRootWord

	    // Act
	    boolean addResult = bo.addRoute(validRoot, validTokenId, validVerseId, validStatus);
	    boolean updateResult = bo.updateroute(validRoot, newRootWord, validStatus);

	    // Assert
	    assertTrue(addResult, "Adding route should be successful for valid parameters.");
	    assertFalse(updateResult, "Updating route should be unsuccessful for invalid parameters.");
	}

	@Test
	@DisplayName("View All Routes with Status")
	void testViewAllRouteWithStatus() throws SQLException {
	    // Arrange
	    String validRoot1 = "جذر1";
	    int validTokenId1 = 201;
	    int validVerseId1 = 101;
	    String validStatus1 = "verified";

	    String validRoot2 = "جذر2";
	    int validTokenId2 = 202;
	    int validVerseId2 = 102;
	    String validStatus2 = "verified";

	    // Act
	    bo.addRoute(validRoot1, validTokenId1, validVerseId1, validStatus1);
	    bo.addRoute(validRoot2, validTokenId2, validVerseId2, validStatus2);

	    List<String[]> result = bo.viewAllRouteWithStatus();

	    // Assert
	    assertNotNull(result, "Result should not be null.");
	    assertFalse(result.isEmpty(), "Result should not be empty.");
	    assertEquals(7, result.size(), "Result size should be 7.");
	    
	}
	@Test
	@DisplayName("RootId from Name - Valid Name")
	void testValidRootIdFromName() throws SQLException {
	    String validRoot = "جذر1";
	    
	    int result = bo.roodIdFromName(validRoot);

	    assertEquals(1, result, "RootId should be 1 for the valid root name.");
	}

	@Test
	@DisplayName("RootId from Name - Invalid Name")
	void testInvalidRootIdFromName() throws SQLException {
	    String invalidRoot = "InvalidRootName";
	    
	    int result = bo.roodIdFromName(invalidRoot);

	    assertEquals(-1, result, "RootId should be -1 for an invalid root name.");
	}

	}

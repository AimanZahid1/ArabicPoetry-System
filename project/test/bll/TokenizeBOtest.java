package bll;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.tokenize.TokenizeBO;
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

class TokenizeBOtest {

	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
     static IRootDao root;
     static IParsePoemDAO parsepoem;
	
	static TokenizeBO bo;
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
		bo = new TokenizeBO(dal);

	}
	

    @Test
    @DisplayName("Check If Existing VerseId is Available - Valid")
    void testValidIsExistingVerseIdAvailable() {
        // Arrange
        TokenStub tokenStub = new TokenStub();
        int existingVerseId = 101;

        // Act
        boolean resultExistingVerseId = tokenStub.isVerseIdAvailable(existingVerseId);

        // Assert
        assertTrue(resultExistingVerseId, "Existing verseId should be available.");
    }

    @Test
    @DisplayName("Check If Non-existing VerseId is Not Available - Invalid")
    void testInvalidIsNonExistingVerseIdAvailable() {
        // Arrange
        TokenStub tokenStub = new TokenStub();
        int nonExistingVerseId = 103;

        // Act
        boolean resultNonExistingVerseId = tokenStub.isVerseIdAvailable(nonExistingVerseId);

        // Assert
        assertFalse(resultNonExistingVerseId, "Non-existing verseId should not be available.");
    }


    @Test
    @DisplayName("Get ids From tokens - valid")
    void testGetIdsFromTokensValid() {
    	
    	ArrayList<String> ele = new ArrayList<String>();
    			ele.add("كلمة2");
    			ele.add("كلمة3");

        List<Integer> ids = bo.getidFromTokens(ele);
      
        assertTrue(!ids.isEmpty(), "List of tokens should  not be empty  for  valid tokens");
    }
   
    

    @Test
    @DisplayName("Get ids From tokens - Invalid")
    void testGetIdsFromTokensInValid() {
    	
    	ArrayList<String> ele = new ArrayList<String>();

        List<Integer> ids = bo.getidFromTokens(ele);
       
        assertTrue(ids.isEmpty(), "List of tokens should be empty  for no tokens");
    }
   
    
    
    
    
    
}

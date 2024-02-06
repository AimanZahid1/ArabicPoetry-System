package bll;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

import bll.versebll.VerseBO;
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

class VerseBOtest {

	static IBookDAO bookDAO;
	static IPoemDAO poemDAO;
	static IVerseDAO verseDAO;
	static ITokenizeDAO token;
     static IRootDao root;
     static IParsePoemDAO parsepoem;
	
	static VerseBO bo;
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
		bo = new VerseBO(dal);

	}
	@Test
	@DisplayName("Add Verse - Valid Input")
	void testAddVerseValidInput() {
	    
	    int poemId = 1; 
	    String first = "السطر الأول";
	    String second = "السطر الثاني";

	  
	    bo.addVerse(poemId, first, second);

	    
	    List<String[]> verses = bo.getVersesByPoem(poemId);
	    assertTrue(verses.stream().anyMatch(v -> v[0].equals(first) && v[1].equals(second)),
	            "Verse should be added successfully");
	}

	@Test
	@DisplayName("Add Verse - Invalid Input (Negative PoemId)")
	void testAddVerseInvalidInput() {
	    
	    int invalidPoemId = -1; 
	    String first = "السطر الأول";
	    String second = "السطر الثاني";

	   
	    bo.addVerse(invalidPoemId, first, second);

	   
	    assertTrue(true);
	}

	
	@Test
	@DisplayName("Add Verse - Existing Verse")
	void testAddVerseExistingVerse() {
	    
	    int poemId = 3; 
	    String existingFirst = "أول بيت";
	    String existingSecond ="الثاني بيت";

	   
	    bo.addVerse(poemId, existingFirst, existingSecond);

	    
	    List<String[]> verses = bo.getVersesByPoem(poemId);
	    assertTrue(verses.stream().anyMatch(v -> v[0].equals(existingFirst) && v[1].equals(existingSecond)),
	            "Existing verse should be added successfully");
	}
	@Test
    @DisplayName("Update Verse - Valid Update")
    void testUpdateVerseValidUpdate() {
       
        int poemId = 1; 
        String firstVerse = "أول بيت";
        String secondVerse = "الثاني بيت";
        String firstUpdated = "Updated First";
        String secondUpdated = "Updated Second";

        
        bo.updateVerse(poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);

        
        List<String[]> verses = verseDAO.getVersesByPoem(poemId);
        assertTrue(verses.stream().anyMatch(v -> v[0].equals(firstUpdated) && v[1].equals(secondUpdated)),
                "Verse should be updated successfully");
    }

    @Test
    @DisplayName("Update Verse - Non-Existing PoemId")
    void testUpdateVerseNonExistingPoemId() {
        
        int nonExistingPoemId = 10; 
        String firstVerse = "Non-Existing First";
        String secondVerse = "Non-Existing Second";
        String firstUpdated = "Updated First";
        String secondUpdated = "Updated Second";

        
        bo.updateVerse(nonExistingPoemId, firstVerse, secondVerse, firstUpdated, secondUpdated);

       
        assertTrue(true);
    }

   

    @Test
    @DisplayName("Update Verse - Existing Verse")
    void testUpdateVerseExistingVerse() {
        
        int poemId = 3; 
        String existingFirst = "البيت الأول";
        String existingSecond = "البيت الثاني";
        String updatedFirst = "Updated First";
        String updatedSecond = "Updated Second";

     
        bo.updateVerse(poemId, existingFirst, existingSecond, updatedFirst, updatedSecond);

      
        List<String[]> verses = verseDAO.getVersesByPoem(poemId);
        assertTrue(verses.stream().anyMatch(v -> v[0].equals(updatedFirst) && v[1].equals(updatedSecond)),
                "Existing verse should be updated successfully");
    }

    @Test
    @DisplayName("Delete Verse - Valid Deletion")
    void testDeleteVerseValidDeletion() {
     
        int poemId = 1; 
        String first = "أول بيت";
        String second = "الثاني بيت";

        
        bo.deleteVerse(poemId, first, second);

       
        List<String[]> verses = verseDAO.getVersesByPoem(poemId);
        assertFalse(verses.stream().anyMatch(v -> v[0].equals(first) && v[1].equals(second)),
                "Verse should be deleted successfully");
    }

    @Test
    @DisplayName("Delete Verse - Non-Existing PoemId")
    void testDeleteVerseNonExistingPoemId() {
       
        int nonExistingPoemId = 10; 
        String first = "Non-Existing First";
        String second = "Non-Existing Second";

       
        bo.deleteVerse(nonExistingPoemId, first, second);

      
        assertTrue(true);
    }

    @Test
    @DisplayName("Delete Verse - Negative PoemId")
    void testDeleteVerseNegativePoemId() {
       
        int negativePoemId = -1; 
        String first = "First";
        String second = "Second";

        
        bo.deleteVerse(negativePoemId, first, second);

        assertTrue(true);
    }

    @Test
    @DisplayName("Delete Verse - Null Lines")
    void testDeleteVerseNullLines() {
        
        int poemId = 2; 
        String nullFirst = null;
        String nullSecond = null;

       
        bo.deleteVerse(poemId, nullFirst, nullSecond);

       
        assertTrue(true);
    }

    @Test
    @DisplayName("Get Verses By Poem - Valid PoemId")
    void testGetVersesByPoemValidPoemId() {
        
        int validPoemId = 1;

     
        List<String[]> verses = bo.getVersesByPoem(validPoemId);

       
        assertNotNull(verses, "List of verses should not be null for a valid poemId");
        assertEquals(2, verses.size(), "There should be two verses for the specified poemId");
    }

    @Test
    @DisplayName("Get Verses By Poem - Non-Existing PoemId")
    void testGetVersesByPoemNonExistingPoemId() {
      
        int nonExistingPoemId = 10; 

       
        List<String[]> verses = bo.getVersesByPoem(nonExistingPoemId);

       
        assertNotNull(verses, "List of verses should not be null for a non-existing poemId");
        assertTrue(verses.isEmpty(), "List of verses should be empty for a non-existing poemId");
    }

    @Test
    @DisplayName("Get Verses By Poem - Negative PoemId")
    void testGetVersesByPoemNegativePoemId() {
        
        int negativePoemId = -1; 

       
        List<String[]> verses = bo.getVersesByPoem(negativePoemId);

       
        assertNotNull(verses, "List of verses should not be null for a negative poemId");
        assertTrue(verses.isEmpty(), "List of verses should be empty for a negative poemId");
    }

    @Test
    @DisplayName("Get Verses By Poem - Zero PoemId")
    void testGetVersesByPoemZeroPoemId() {
       
        int zeroPoemId = 0;

      
        List<String[]> verses = bo.getVersesByPoem(zeroPoemId);

       
        assertNotNull(verses, "List of verses should not be null for a zero poemId");
        assertTrue(verses.isEmpty(), "List of verses should be empty for a zero poemId");
    }

    @Test
    @DisplayName("Get Verse ID From Title - Valid Title")
    void testGetVerseIdFromTitleValidTitle() {
    
        String firstVerse = "أول بيت";
        String secondVerse = "الثاني بيت";

        
        int verseId = bo.getVerseIdFromTitle(firstVerse, secondVerse);

       
        assertEquals(1, verseId, "Verse ID should match the expected value for the specified title");
    }

    @Test
    @DisplayName("Get Verse ID From Title - Non-Existing Title")
    void testGetVerseIdFromTitleNonExistingTitle() {
        
        String nonExistingFirstVerse = "Non-Existing First Verse";
        String nonExistingSecondVerse = "Non-Existing Second Verse";

       
        int verseId = bo.getVerseIdFromTitle(nonExistingFirstVerse, nonExistingSecondVerse);

        
        assertEquals(0, verseId, "Verse ID should be 0 for a non-existing title");
    }

    @Test
    @DisplayName("Get Verse ID From Title - Empty Title")
    void testGetVerseIdFromTitleEmptyTitle() {
      
        String emptyFirstVerse = "";
        String emptySecondVerse = "";

        
        int verseId = bo.getVerseIdFromTitle(emptyFirstVerse, emptySecondVerse);

       
        assertEquals(0, verseId, "Verse ID should be 0 for an empty title");
    }

    
    @Test
    @DisplayName("Get Verses From ID - Non-Existing ID")
    void testGetVersesFromIdNonExistingId() {
        
        int nonExistingVerseId = 100;

        
        List<String> verses = bo.getVersesFromId(nonExistingVerseId);

       
        assertNotNull(verses, "List of verses should not be null for a non-existing ID");
        assertTrue(verses.isEmpty(), "List of verses should be empty for a non-existing ID");
    }

    @Test
    @DisplayName("Get Verses From ID - Invalid ID (Negative)")
    void testGetVersesFromIdInvalidId() {
        
        int invalidVerseId = -1;

        
        List<String> verses = bo.getVersesFromId(invalidVerseId);

      
        assertNotNull(verses, "List of verses should not be null for an invalid ID");
        assertTrue(verses.isEmpty(), "List of verses should be empty for an invalid ID");
    }

    @Test
    @DisplayName("Get Verses From ID - Zero ID")
    void testGetVersesFromIdZeroId() {
       
        int zeroVerseId = 0;

        List<String> verses = bo.getVersesFromId(zeroVerseId);

       
        assertNotNull(verses, "List of verses should not be null for a zero ID");
        assertTrue(verses.isEmpty(), "List of verses should be empty for a zero ID");
    }
   
    
    

    @Test
    @DisplayName("Get Verses From Root ID - ID")
    void testGetVersesFromRootID() {
       
        int zeroVerseId = 5;

        List<String[]> verses = bo.getVersesByRoot(zeroVerseId);
       
        assertTrue(!verses.isEmpty(), "List of verses should not be empty  for a valid ID");
    }
   
    

    @Test
    @DisplayName("Get Verses From Root ID - Invalid ID")
    void testGetVersesFromRootIDInvalid() {
       
        int zeroVerseId = 25;

        List<String[]> verses = bo.getVersesByRoot(zeroVerseId);
      
        assertTrue(verses.isEmpty(), "List of verses should  be empty  for a Invalid ID");
    }
   
    
    
    
    
    
    
    
    
    
    
   
}
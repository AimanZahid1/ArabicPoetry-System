package bll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.bookbll.BookBO;
import bll.poembll.ImportPoemBO;
import bll.poembll.PoemBO;
import bll.rootbll.RootBO;
import bll.tokenize.TokenizeBO;
import bll.versebll.VerseBO;
import dal.IDALFacade;
import dal.bookdal.Book;

/**
 * The BLLFacade class implements the IBLLFacade interface and provides the
 * business logic layer functionalities.
 */
public class BLLFacade implements IBLLFacade {
	private IDALFacade df;
	private RootBO root;
	BookBO book;
	ImportPoemBO Import;
	PoemBO poem;
	TokenizeBO token;
	VerseBO verse;
	private static final Logger logger = LogManager.getLogger(BLLFacade.class);

	/**
	 * Constructs a BLLFacade object.
	 *
	 * @param df The data access layer facade.
	 */
	public BLLFacade(IDALFacade df) {
		this.df = df;
		this.root= new RootBO(df);
		book = new BookBO(df);
		Import = new ImportPoemBO(df);
	 poem = new PoemBO(df);
	  token = new TokenizeBO(df);
	  verse = new VerseBO(df);
	}

	/**
	 * Adds a route with specified details.
	 *
	 * @param root    The root of the route.
	 * @param tokenid The token ID for the route.
	 * @param verseId The verse ID for the route.
	 * @param status  The status of the route.
	 * @return True if the route is added successfully, false otherwise.
	 * @throws SQLException If an SQL exception occurs.  
	 */
	@Override
	public boolean addRoute(String root, int tokenid, int verseId, String status) throws SQLException {
		logger.debug("Adding route - Root: {}, TokenId: {}, VerseId: {}, Status: {}", root, tokenid, verseId, status);
		return this.root.addRoute(root, tokenid, verseId, status);
	}

	@Override
	public boolean updateroute(String name, String root, String status) throws SQLException {
		logger.debug("Updating route - Name: {}, Root: {}, Status: {}", name, root, status);
		return this.root.updateroute(name, root, status);
	}

	@Override
	public boolean deleteroute(String name) throws SQLException {
		logger.debug("Deleting route - Name: {}", name);
		return this.root.deleteroute(name);
	}

	@Override
	public List<String[]> viewAllRouteWithStatus() throws SQLException {
		logger.debug("Viewing all routes with status");
		return this.root.viewAllRouteWithStatus();
	}

	/**
	 * Imports poems from a file.
	 *
	 * @param filePath The path of the file containing poems.
	 * @param bookName The name of the book containing the poems.
	 * @return A string representing the imported poems' data.
	 */
	@Override
	public boolean importPoems(String filePath, String bookName) {
		logger.debug("Importing poems - FilePath: {}, BookName: {}", filePath, bookName);
		return Import.importPoems(filePath, bookName);
	}

	
	/**
	 * Inserts a book into the database.
	 *
	 * @param title       The title of the book.
	 * @param author      The author of the book.
	 * @param publishDate The publish date of the book.
	 * @param deathDate   The death date of the author.
	 */
	@Override
	public void insertBook(String title, String author, Date publishDate, Date deathDate) {
		logger.debug("Inserting book - Title: {}, Author: {}, PublishDate: {}, DeathDate: {}", title, author,
				publishDate, deathDate);
		book.insertBook(title, author, publishDate, deathDate);
	}

	@Override
	public void updateBook(String title, String author, Date publishDate, Date deathDate) {
		logger.debug("Updating book - Title: {}, Author: {}, PublishDate: {}, DeathDate: {}", title, author,
				publishDate, deathDate);
		book.updateBook(title, author, publishDate, deathDate);
	}

	@Override
	public void deleteBook(String title) {
		logger.debug("Deleting book - Title: {}", title);
		book.deleteBook(title);
	}

	@Override
	public List<Book> selectBooks() {
		logger.debug("Selecting books");
		return book.selectBooks();
	}

	@Override
	public void addPoem(int bookId, String poemTitle) {
		logger.debug("Adding poem - BookId: {}, PoemTitle: {}", bookId, poemTitle);
		poem.addPoem(bookId, poemTitle);
	}

	@Override
	public void deletePoem(int bookId, String poemTitle) {
		logger.debug("Deleting poem - BookId: {}, PoemTitle: {}", bookId, poemTitle);
		poem.deletePoem(bookId, poemTitle);
	}

	@Override
	public List<String> viewPoemsByBook(int bookId) {
		logger.debug("Viewing poems by book - BookId: {}", bookId);
		return poem.viewPoemsByBook(bookId);
	}

	@Override
	public int getBookIdByTitle(String title) {
		logger.debug("Getting book ID by title - Title: {}", title);
		return book.getBookIdByTitle(title);
	}

	@Override
	public void updatePoem(int bookId, String previousTitle, String newTitle) {
		logger.debug("Updating poem - BookId: {}, PreviousTitle: {}, NewTitle: {}", bookId, previousTitle, newTitle);
		poem.updatePoem(bookId, previousTitle, newTitle);
	}

	@Override
	public void addVerse(int poemId, String first, String snd) throws SQLException {
		logger.debug("Adding verse - PoemId: {}, First: {}, Second: {}", poemId, first, snd);
		verse.addVerse(poemId, first, snd);
	}

	@Override
	public List<String[]> getVersesByPoem(int poemId) {
		logger.debug("Getting verses by poem - PoemId: {}", poemId);
		return verse.getVersesByPoem(poemId);
	}

	@Override
	public int getPoemIdByTitle(int bookId, String poemTitle) {
		logger.debug("Getting poem ID by title - BookId: {}, PoemTitle: {}", bookId, poemTitle);
		return poem.getPoemIdByTitle(bookId, poemTitle);
	}

	@Override
	public void deleteVerse(int poemId, String first, String snd) {
		logger.debug("Deleting verse - PoemId: {}, First: {}, Second: {}", poemId, first, snd);
		verse.deleteVerse(poemId, first, snd);
	}

	@Override
	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated,
			String secondUpdated) {
		logger.debug(
				"Updating verse - PoemId: {}, FirstVerse: {}, SecondVerse: {}, FirstUpdated: {}, SecondUpdated: {}",
				poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);
		verse.updateVerse(poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);
	}

	@Override
	public String getBookTitleByPoemTitle(int poemid) {
		logger.debug("Getting book title by poem title - PoemId: {}", poemid);
		return poem.getBookTitleByPoemTitle(poemid);
	}

	@Override
	public void insertToken(ArrayList<String> tokens, int verseid) {
		logger.debug("Inserting tokens - Tokens: {}, VerseId: {}", tokens, verseid);
		token.insertToken(tokens, verseid);
	}

	@Override
	public int getVerseIdFromTitle(String firstVerse, String secondVerse) {
		logger.debug("Getting verse ID from title - FirstVerse: {}, SecondVerse: {}", firstVerse, secondVerse);
		return verse.getVerseIdFromTitle(firstVerse, secondVerse);
	}

	@Override
	public boolean isVerseIdAvailable(int verseId) {
		logger.debug("Checking if verse ID is available - VerseId: {}", verseId);
		return token.isVerseIdAvailable(verseId);
	}

	@Override
	public List<String> getVersesFromId(int verseId) {
		logger.debug("Getting verses from ID - VerseId: {}", verseId);
		return verse.getVersesFromId(verseId);
	}

	@Override
	public ArrayList<String> getTokensByVerseId(int verseId) {
		logger.debug("Getting tokens by verse ID - VerseId: {}", verseId);
		return token.getTokensByVerseId(verseId);
	}

	@Override
	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens) {
		logger.debug("Getting IDs from tokens - Tokens: {}", tokens);
		return token.getidFromTokens(tokens);
	}

	@Override
	public int roodIdFromName(String name) {
		logger.debug("Getting root ID from name - Name: {}", name);
		return this.root.roodIdFromName(name);
	}

	@Override
	public List<String[]> getVersesByRoot(int rootId) {
		logger.debug("Getting verses by root ID - RootId: {}", rootId);
		return verse.getVersesByRoot(rootId);
	}

	@Override
	public int PoemIdByTitle(String poemTitle) {
		logger.debug("Getting poem ID by title - PoemTitle: {}", poemTitle);
		return poem.PoemIdByTitle(poemTitle);
	}

	@Override
	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException {
		logger.debug("Adding or updating route - Root: {}, TokenId: {}, VerseId: {}, Status: {}", root, tokenid,
				verseId, status);
		return this.root.addOrUpdateRoute(root, tokenid, verseId, status);
	}
}
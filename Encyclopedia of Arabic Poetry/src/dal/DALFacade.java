package dal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dal.Tokenize.ITokenizeDAO;
import dal.bookdal.Book;
import dal.bookdal.IBookDAO;
import dal.poemdal.importPoem.IParsePoemDAO;
import dal.poemdal.importPoem.ParsePoemDAO.Poems;
import dal.poemdal.manualAdd.IPoemDAO;
import dal.rootdal.IRootDao;
import dal.versedal.IVerseDAO;

public class DALFacade implements IDALFacade {
	private static final Logger LOGGER = LogManager.getLogger(DALFacade.class);
	IRootDao root;
	IParsePoemDAO parsepoem;
	private IBookDAO bookDAO;
	private IPoemDAO poemDAO;
	private IVerseDAO verseDAO;
	private ITokenizeDAO token;

	public DALFacade(IRootDao root, IParsePoemDAO parsepoem, IBookDAO bookDAO, IPoemDAO poemDAO, IVerseDAO verseDAO,
			ITokenizeDAO token) {
		this.root = root;
		this.parsepoem = parsepoem;
		this.bookDAO = bookDAO;
		this.poemDAO = poemDAO;
		this.verseDAO = verseDAO;
		this.token = token;
	}

	@Override
	public boolean addRoute(String roots, int tokenid, int verseId, String status) throws SQLException {
		LOGGER.debug("Adding route with roots: {}, tokenid: {}, verseId: {}, status: {}", roots, tokenid, verseId,
				status);
		return root.addRoute(roots, tokenid, verseId, status);

	}

	@Override
	public boolean delRoute(String rootName) throws SQLException {
		LOGGER.debug("Deleting route with rootName: {}", rootName);
		return root.delRoute(rootName);
	}

	@Override
	public void updateBook(String title, String author, Date publishDate, Date deathDate) {
		// TODO Auto-generated method stub
		LOGGER.debug("Updating book with title: {}, author: {}, publishDate: {}, deathDate: {}", title, author,
				publishDate, deathDate);
		bookDAO.updateBook(title, author, publishDate, deathDate);
	}

	@Override
	public void deleteBook(String title) {
		// TODO Auto-generated method stub
		bookDAO.deleteBook(title);
	}

	@Override
	public boolean updRoute(String rootName, String newRootWord, String status) throws SQLException {
		return root.updRoute(rootName, newRootWord, status);
	}

	@Override
	public List<String[]> viewAllRouteWithStatus() throws SQLException {

		return root.viewAllRouteWithStatus();
	}

	@Override
	public boolean getPoemsData(String filePath, String bookName) {
		return parsepoem.getPoemsData(filePath, bookName);

	}

	

	@Override
	public void insertBook(String title, String author, Date publishDate, Date deathDate) {
		// TODO Auto-generated method stub
		bookDAO.insertBook(title, author, publishDate, deathDate);

	}

	@Override
	public List<Book> selectBooks() {
		// TODO Auto-generated method stub
		return bookDAO.selectBooks();

	}

	@Override
	public void addPoem(int bookId, String poemTitle) {
		// TODO Auto-generated method stub
		poemDAO.addPoem(bookId, poemTitle);
	}

	@Override
	public List<String> viewPoemsByBook(int bookId) {
		// TODO Auto-generated method stub
		return poemDAO.viewPoemsByBook(bookId);
	}

	@Override
	public void updatePoem(int bookId, String previousTitle, String newTitle) {
		poemDAO.updatePoem(bookId, previousTitle, newTitle);
	}

	@Override
	public void deletePoem(int bookId, String poemTitle) {
		// TODO Auto-generated method stub
		poemDAO.deletePoem(bookId, poemTitle);
	}

	public int getBookIdByTitle(String bookTitle) {
		// Delegate to the BookDAO to get the book_id by title
		return bookDAO.getBookIdByTitle(bookTitle);
	}

	@Override
	public void addVerse(int poemId, String firstverse, String secondverse) throws SQLException {
		// TODO Auto-generated method stub
		verseDAO.addVerse(poemId, firstverse, secondverse);

	}

	@Override
	public List<String[]> getVersesByPoem(int poemId) {
		// TODO Auto-generated method stub
		return verseDAO.getVersesByPoem(poemId);
	}

	public int getPoemIdByTitle(int bookId, String poemTitle) {
		return poemDAO.getPoemIdByTitle(bookId, poemTitle);
	}

	@Override
	public void deleteVerse(int poemId, String FirstVerse, String SecondVerse) {
		// TODO Auto-generated method stub
		verseDAO.deleteVerse(poemId, FirstVerse, SecondVerse);
	}

	@Override
	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated,
			String secondUpdated) {
		// TODO Auto-generated method stub
		verseDAO.updateVerse(poemId, firstVerse, secondVerse, firstUpdated, secondUpdated);
	}

	@Override
	public String getBookTitleByPoemId(int poemTitle) {
		// TODO Auto-generated method stub
		return poemDAO.getBookTitleByPoemId(poemTitle);
	}

	@Override
	public void insertTokens(ArrayList<String> tokens, int verseid) {
		LOGGER.debug("Inserting tokens with verseid: {}", verseid);
		token.insertTokens(tokens, verseid);
	}

	@Override
	public int getVerseIdFromTitle(String firstVerse, String secondVerse) {
		// TODO Auto-generated method stub
		return verseDAO.getVerseIdFromTitle(firstVerse, secondVerse);
	}

	@Override
	public boolean isVerseIdAvailable(int verseId) {
		// TODO Auto-generated method stub
		return token.isVerseIdAvailable(verseId);
	}

	@Override
	public List<String> getVersesFromId(int verseId) {
		// TODO Auto-generated method stub
		return verseDAO.getVersesFromId(verseId);
	}

	@Override
	public ArrayList<String> getTokensByVerseId(int verseId) {
		// TODO Auto-generated method stub
		return token.getTokensByVerseId(verseId);
	}

	@Override
	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens) {
		// TODO Auto-generated method stub
		return token.getidFromTokens(tokens);
	}

	@Override
	public int roodIdFromName(String name) {
		// TODO Auto-generated method stub
		return root.roodIdFromName(name);
	}

	@Override
	public List<String[]> getVersesByRoot(int rootId) {
		// TODO Auto-generated method stub
		return verseDAO.getVersesByRoot(rootId);
	}

	@Override
	public int PoemIdByTitle(String poemTitle) {
		// TODO Auto-generated method stub
		return poemDAO.PoemIdByTitle(poemTitle);
	}

	@Override
	public boolean addOrUpdateRoute(String roots, int tokenid, int verseId, String status) throws SQLException {
		// TODO Auto-generated method stub
		return root.addOrUpdateRoute(roots, tokenid, verseId, status);
	}

}

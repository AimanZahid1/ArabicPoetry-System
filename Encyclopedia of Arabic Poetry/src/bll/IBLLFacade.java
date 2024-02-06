package bll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dal.bookdal.Book;

/**
 * The interface IBLLFacade serves as a facade combining multiple interfaces
 * related to business logic. It extends various other interfaces providing a
 * comprehensive set of methods for different functionalities.
 */
public interface IBLLFacade {
	public void addVerse(int poemId, String first,String snd) throws SQLException;
	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated, String secondUpdated);
	 public void deleteVerse(int poemId,String First,String snd);
	 public  List<String[]> getVersesByPoem(int poemId);
	 public int getVerseIdFromTitle(String firstVerse, String secondVerse) ;
	 public List<String> getVersesFromId(int verseId);

		public List<String[]> getVersesByRoot(int rootId);

	public void insertToken(ArrayList<String> tokens, int verseid);

	public boolean isVerseIdAvailable(int verseId);

	public ArrayList<String> getTokensByVerseId(int verseId);

	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens);

	/**
	 * Adds a new poem to a book.
	 *
	 * @param bookId    The ID of the book to which the poem belongs.
	 * @param poemTitle The title of the new poem.
	 */
	public void addPoem(int bookId, String poemTitle);

	/**
	 * Updates an existing poem title in a book.
	 *
	 * @param bookId        The ID of the book containing the poem.
	 * @param previousTitle The previous title of the poem.
	 * @param newTitle      The new title for the poem.
	 */
	public void updatePoem(int bookId, String previousTitle, String newTitle);

	/**
	 * Deletes a poem from a book.
	 *
	 * @param bookId    The ID of the book containing the poem.
	 * @param poemTitle The title of the poem to be deleted.
	 */
	public void deletePoem(int bookId, String poemTitle);

	/**
	 * Retrieves a list of poems associated with a book.
	 *
	 * @param bookId The ID of the book.
	 * @return A list of poem titles associated with the specified book.
	 */
	public List<String> viewPoemsByBook(int bookId);

	/**
	 * Retrieves the ID of a poem by its title in a specific book.
	 *
	 * @param bookId    The ID of the book containing the poem.
	 * @param poemTitle The title of the poem.
	 * @return The ID of the poem.
	 */
	int getPoemIdByTitle(int bookId, String poemTitle);

	/**
	 * Retrieves the book title by poem title.
	 *
	 * @param poemTitle The title of the poem.
	 * @return The title of the book associated with the specified poem.
	 */
	public String getBookTitleByPoemTitle(int poemTitle);

	/**
	 * Retrieves the ID of a poem by its title.
	 *
	 * @param poemTitle The title of the poem.
	 * @return The ID of the poem.
	 */
	int PoemIdByTitle(String poemTitle);

	/**
	 * Imports poems from a file.
	 *
	 * @param filePath The file path from which poems are imported.
	 * @param bookName The name of the book associated with the poems.
	 * @return A message indicating the result of the import operation.
	 */
	public boolean importPoems(String filePath, String bookName);

	// Method to add a route
	public boolean addRoute(String root, int tokenid, int verseId, String status) throws SQLException;

	// Method to update a route
	public boolean updateroute(String name, String root, String status) throws SQLException;

	// Method to delete a route
	public boolean deleteroute(String name) throws SQLException;

	// Method to view all routes with status
	public List<String[]> viewAllRouteWithStatus() throws SQLException;

	// Method to get root ID from name
	public int roodIdFromName(String name);

	/**
	 * Adds or updates a route with the specified parameters.
	 *
	 * @param root    The root of the route.
	 * @param tokenid The token ID associated with the route.
	 * @param verseId The verse ID associated with the route.
	 * @param status  The status of the route.
	 * @return True if the operation is successful, otherwise false.
	 * @throws SQLException If a database access error occurs.
	 */
	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException;

	/**
	 * Retrieves a list of books.
	 *
	 * @return A list of books.
	 */
	public List<Book> selectBooks();

	/**
	 * Retrieves the ID of a book by its title.
	 *
	 * @param bookTitle The title of the book.
	 * @return The ID of the book.
	 */
	int getBookIdByTitle(String bookTitle);

	/**
	 * Updates the details of a book.
	 *
	 * @param title       The title of the book.
	 * @param author      The author of the book.
	 * @param publishDate The publish date of the book.
	 * @param deathDate   The death date of the author (if applicable).
	 */
	public void updateBook(String title, String author, Date publishDate, Date deathDate);

	/**
	 * Deletes a book by its title.
	 *
	 * @param title The title of the book to be deleted.
	 */
	public void deleteBook(String title);

	/**
	 * Inserts a new book.
	 *
	 * @param title       The title of the new book.
	 * @param author      The author of the new book.
	 * @param publishDate The publish date of the new book.
	 * @param deathDate   The death date of the author (if applicable) of the new
	 *                    book.
	 */
	public void insertBook(String title, String author, Date publishDate, Date deathDate);
}
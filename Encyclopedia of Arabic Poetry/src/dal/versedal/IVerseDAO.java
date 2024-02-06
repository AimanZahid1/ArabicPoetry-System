package dal.versedal;

import java.sql.SQLException;
import java.util.List;

/**
 * The IVerseDAO interface defines methods for interacting with the data access
 * layer to manage verses in the Encyclopedia of Arabic Poetry application.
 */
public interface IVerseDAO {
	/**
	 * Adds a verse to a poem.
	 *
	 * @param poemId      The ID of the poem.
	 * @param firstverse  The first verse.
	 * @param secondverse The second verse.
	 * @throws SQLException If a SQL exception occurs.
	 */
	public void addVerse(int poemId, String firstverse, String secondverse) throws SQLException;

	/**
	 * Updates a verse in a poem.
	 *
	 * @param poemId        The ID of the poem.
	 * @param firstVerse    The original first verse.
	 * @param secondVerse   The original second verse.
	 * @param firstUpdated  The updated first verse.
	 * @param secondUpdated The updated second verse.
	 */
	public void updateVerse(int poemId, String firstVerse, String secondVerse, String firstUpdated,
			String secondUpdated);

	/**
	 * Deletes a verse from a poem.
	 *
	 * @param poemId      The ID of the poem.
	 * @param FirstVerse  The first verse.
	 * @param SecondVerse The second verse.
	 */
	public void deleteVerse(int poemId, String FirstVerse, String SecondVerse);

	/**
	 * Retrieves a list of verses for a given poem.
	 *
	 * @param poemId The ID of the poem.
	 * @return A list of String arrays representing verses.
	 */
	public List<String[]> getVersesByPoem(int poemId);

	/**
	 * Retrieves the ID of a verse based on its title.
	 *
	 * @param firstVerse  The first verse.
	 * @param secondVerse The second verse.
	 * @return The ID of the verse.
	 */
	public int getVerseIdFromTitle(String firstVerse, String secondVerse);

	/**
	 * Retrieves a list of verses based on their ID.
	 *
	 * @param verseId The ID of the verse.
	 * @return A list of verses as strings.
	 */
	public List<String> getVersesFromId(int verseId);

	/**
	 * Retrieves a list of verses for a given root.
	 *
	 * @param rootId The ID of the root.
	 * @return A list of String arrays representing verses.
	 */
	public List<String[]> getVersesByRoot(int rootId);

}
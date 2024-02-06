package dal.Tokenize;

import java.util.ArrayList;
import java.util.List;

/**
 * The ITokenizeDAO interface provides methods for interacting with the data
 * access layer related to tokenization in the Encyclopedia of Arabic Poetry
 * application.
 */
public interface ITokenizeDAO {

	/**
	 * Inserts tokens associated with a verse into the database.
	 *
	 * @param tokens  The list of tokens to be inserted.
	 * @param verseid The ID of the verse.
	 */
	public void insertTokens(ArrayList<String> tokens, int verseid);

	/**
	 * Checks if a verse ID is available in the database.
	 *
	 * @param verseId The ID of the verse to check.
	 * @return True if the verse ID is available, false otherwise.
	 */
	public boolean isVerseIdAvailable(int verseId);

	/**
	 * Retrieves tokens associated with a verse from the database.
	 *
	 * @param verseId The ID of the verse.
	 * @return The list of tokens associated with the verse.
	 */
	public ArrayList<String> getTokensByVerseId(int verseId);

	/**
	 * Retrieves verse IDs associated with a list of tokens from the database.
	 *
	 * @param tokens The list of tokens.
	 * @return The list of verse IDs associated with the tokens.
	 */
	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens);
}

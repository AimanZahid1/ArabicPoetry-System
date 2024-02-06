package dal.rootdal;

import java.sql.SQLException;
import java.util.List;

/**
 * The IRootDao interface defines methods for interacting with root-related data
 * in the data access layer of the Encyclopedia of Arabic Poetry application.
 */
public interface IRootDao {
	/**
	 * Adds a root route to the database.
	 *
	 * @param root    The root word.
	 * @param tokenid The ID of the associated token.
	 * @param verseId The ID of the associated verse.
	 * @param status  The status of the root route.
	 * @return True if the addition is successful, false otherwise.
	 * @throws SQLException If a SQL exception occurs.
	 */
	public boolean addRoute(String root, int tokenid, int verseId, String status) throws SQLException;

	/**
	 * Deletes a root route from the database.
	 *
	 * @param root The root word to be deleted.
	 * @return True if the deletion is successful, false otherwise.
	 * @throws SQLException If a SQL exception occurs.
	 */
	public boolean delRoute(String root) throws SQLException;

	/**
	 * Updates a root route in the database.
	 *
	 * @param root        The root word to be updated.
	 * @param newRootWord The new root word.
	 * @param status      The new status of the root route.
	 * @return True if the update is successful, false otherwise.
	 * @throws SQLException If a SQL exception occurs.
	 */
	public boolean updRoute(String root, String newRootWord, String status) throws SQLException;

	public List<String[]> viewAllRouteWithStatus() throws SQLException;

	public int roodIdFromName(String name);

	/**
	 * Adds or updates a root route in the database.
	 *
	 * @param root    The root word.
	 * @param tokenid The ID of the associated token.
	 * @param verseId The ID of the associated verse.
	 * @param status  The status of the root route.
	 * @return True if the addition or update is successful, false otherwise.
	 * @throws SQLException If a SQL exception occurs.
	 */

	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException;
}

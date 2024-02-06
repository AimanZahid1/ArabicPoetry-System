package dal.rootdal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dal.ConnectionClass;

/**
 * The RootDAO class implements the IRootDao interface and provides methods for
 * interacting with root-related data in the data access layer of the
 * Encyclopedia of Arabic Poetry application.
 */
public class RootDAO implements IRootDao {
	Connection connection;

	/**
	 * Constructor for the RootDAO class. Initializes the database connection.
	 */
	public RootDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

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
	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException {
		int existingRootId = roodIdFromName(root);
		if (existingRootId != -1) {
			return updRoute(root, root, status);
		} else {
			return addRoute(root, tokenid, verseId, status);
		}
	}

	/**
	 * Adds a new root route to the database.
	 *
	 * @param root    The root word.
	 * @param tokenid The ID of the associated token.
	 * @param verseId The ID of the associated verse.
	 * @param status  The status of the root route.
	 * @return True if the addition is successful, false otherwise.
	 * @throws SQLException If a SQL exception occurs.
	 */
	public boolean addRoute(String root, int tokenid, int verseId,String status) throws SQLException {
	    ResultSet generatedKeys = null;

	    try {
	        connection.setAutoCommit(false); // Start a transaction

	        String addQuery = "INSERT INTO root (root_Word) VALUES (?)";

	        try (PreparedStatement pStatement = connection.prepareStatement(addQuery,
	                PreparedStatement.RETURN_GENERATED_KEYS)) {
	            pStatement.setString(1, root);
	            pStatement.executeUpdate();

	            // Retrieve the generated keys
	            generatedKeys = pStatement.getGeneratedKeys();

	            if (generatedKeys.next()) {
	                int rootId = generatedKeys.getInt(1);
	                System.out.println("Generated Root ID: " + rootId);

	                // Add the root ID and token ID into the rootsoftokens table
	                addIntoRootsOfTokens(rootId, tokenid);

	                // Add the root ID and verse ID into the rootofverses table
	                addIntoRootsOfVerses(rootId, verseId, status);

	                connection.commit(); // Commit the transaction
	                return true;
	            } else {
	                System.out.println("Failed to retrieve generated root ID.");
	                connection.rollback(); // Rollback the transaction
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Consider logging the error instead
	        connection.rollback(); // Rollback the transaction in case of an exception
	        return false;
	    } finally {
	        if (generatedKeys != null) {
	            generatedKeys.close();
	        }
	        if (connection != null) {
	            connection.setAutoCommit(true); // Reset auto-commit to true
	        }
	    }
	}

	/**
	 * Retrieves the ID of a root from its name.
	 *
	 * @param name The name of the root.
	 * @return The ID of the root.
	 */
	public int roodIdFromName(String name) {
		try {
			String selectQuery = "SELECT root.id AS root_id FROM root JOIN rootsoftokens ON root.id = rootsoftokens.root_id WHERE root_Word = ?";
			try (PreparedStatement pStatement = connection.prepareStatement(selectQuery)) {
				pStatement.setString(1, name);
				try (ResultSet resultSet = pStatement.executeQuery()) {
					if (resultSet.next()) {
						// Use the alias "root_id" instead of "id"
						return resultSet.getInt("root_id");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // Return -1 if the root ID is not found
	}

	/**
	 * Adds a root ID and token ID into the rootsoftokens table.
	 *
	 * @param rootId  The ID of the root.
	 * @param tokenId The ID of the token.
	 */
	public void addIntoRootsOfTokens(int rootId, int tokenId) {
		try {
			String insertQuery = "INSERT INTO rootsoftokens (root_id, token_id) VALUES (?, ?)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setInt(1, rootId);
				preparedStatement.setInt(2, tokenId);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a root ID and verse ID into the rootsofverses table.
	 *
	 * @param rootId  The ID of the root.
	 * @param verseId The ID of the verse.
	 */
	public void addIntoRootsOfVerses(int rootId, int verseId, String status) {
	    try {
	        String insertQuery = "INSERT INTO rootsofverses (root_id, verseId, status) VALUES (?, ?, ?)";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	            preparedStatement.setInt(1, rootId);
	            preparedStatement.setInt(2, verseId);
	            preparedStatement.setString(3, status);
	            preparedStatement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception appropriately
	    }
	}

	/**
	 * Adds a root ID and verse ID into the rootofverses table.
	 *
	 * @param rootId  The ID of the root.
	 * @param verseId The ID of the verse.
	 * @return True if the addition is successful, false otherwise.
	 */
	public boolean delRoute(String root) throws SQLException {
		try {
			String deleteQuery = "DELETE FROM root WHERE root_Word = ?";
			try (PreparedStatement pStatement = connection.prepareStatement(deleteQuery)) {
				pStatement.setString(1, root);
				int rowsDeleted = pStatement.executeUpdate();

				return rowsDeleted > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updRoute(String root, String newRootWord, String newStatus) {
	    try {
	        int rootId = roodIdFromName(root);

	        if (rootId != -1) {
	           
	            String updateRootQuery = "UPDATE root SET root_Word = ? WHERE id = ?";
	            try (PreparedStatement rootStatement = connection.prepareStatement(updateRootQuery)) {
	                rootStatement.setString(1, newRootWord);
	                rootStatement.setInt(2, rootId);

	                int rowsUpdatedRoot = rootStatement.executeUpdate();

	              
	                String updateRootsOfVersesQuery = "UPDATE rootsofverses SET status = ? WHERE root_id = ?";
	                try (PreparedStatement rootsOfVersesStatement = connection.prepareStatement(updateRootsOfVersesQuery)) {
	                    rootsOfVersesStatement.setString(1, newStatus);
	                    rootsOfVersesStatement.setInt(2, rootId);

	                    int rowsUpdatedRootsOfVerses = rootsOfVersesStatement.executeUpdate();

	                   
	                    if (rowsUpdatedRoot > 0 && rowsUpdatedRootsOfVerses > 0) {
	                        return true;
	                    } else {
	                        connection.rollback();
	                        return false;
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            connection.rollback();
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	    }
	    return false;
	}


	public List<String[]> viewAllRouteWithStatus() throws SQLException {
	    List<String[]> routeList = new ArrayList<>();
	    try (PreparedStatement statement = connection.prepareStatement(
	            "SELECT root.root_Word, rootsofverses.status " +
	            "FROM root " +
	            "JOIN rootsofverses ON root.id = rootsofverses.root_id");
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {
	            String rootWord = resultSet.getString("root_Word");
	            String rootStatus = resultSet.getString("status");

	         
	            String[] routeData = { rootWord, rootStatus };
	            routeList.add(routeData);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return routeList;
	}

}

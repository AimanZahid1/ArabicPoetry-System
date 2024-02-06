package dal.Tokenize;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dal.ConnectionClass;
import net.oujda_nlp_team.entity.Result;

/**
 * The TokenizeDAO class implements the ITokenizeDAO interface and provides
 * methods for interacting with the data access layer related to tokenization in
 * the Encyclopedia of Arabic Poetry application.
 */
public class TokenizeDAO implements ITokenizeDAO {
	private static final Logger LOGGER = LogManager.getLogger(TokenizeDAO.class);

	private Connection connection;

	/**
	 * Constructs a new instance of TokenizeDAO and initializes the database
	 * connection.
	 */
	public TokenizeDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

	/**
	 * Inserts tokens associated with a verse into the database.
	 *
	 * @param tokens  The list of tokens to be inserted.
	 * @param verseId The ID of the verse.
	 */
	public void insertTokens(ArrayList<String> tokens, int verseId) {

		String insertTokenQuery = "INSERT INTO tokenize (token, partsOfSpeech) VALUES (?, ?)";
		String selectTokenQuery = "SELECT id FROM tokenize WHERE token = ?";
		List<Integer> generatedIds = new ArrayList<>();

		try (PreparedStatement insertStatement = connection.prepareStatement(insertTokenQuery,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement selectStatement = connection.prepareStatement(selectTokenQuery)) {

			for (String token : tokens) {
				
				selectStatement.setString(1, token);
				try (ResultSet resultSet = selectStatement.executeQuery()) {
					if (resultSet.next()) {
				
						generatedIds.add(resultSet.getInt("id"));
						continue;
					}
				}

				// Token doesn't exist, insert it
				String partsOfSpeech = "";

				// Call the part-of-speech tagger and get the results
				List<Result> pos = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(token)
				        .getAllResults();

				// Check if the part-of-speech tagger returned any results
				if (!pos.isEmpty()) {
				    // Get the first part-of-speech tag
				    String firstPartOfSpeech = pos.get(0).getPartOfSpeech();

				    
				    String[] parts = firstPartOfSpeech.split("\\|");
				    if (parts.length > 0) {
				        partsOfSpeech = parts[0];
				    }
				}


				insertStatement.setString(1, token);
				insertStatement.setString(2, partsOfSpeech);

				insertStatement.executeUpdate();

				
				try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedIds.add(generatedKeys.getInt(1));
					} else {
						LOGGER.error("Failed to retrieve generated keys.");
						throw new SQLException("Failed to retrieve generated keys.");
					}
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Error occurred while inserting tokens: " + e.getMessage(), e);
			// Handle the exception appropriately
		}

		insertTokenVerseRelationship(verseId, generatedIds);
	}

	private void insertTokenVerseRelationship(int verseId, List<Integer> tokenIds) {
		String insertRelationshipQuery = "INSERT INTO tokensofverses (verse_id, token_id) VALUES (?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(insertRelationshipQuery)) {

			for (int tokenId : tokenIds) {
				preparedStatement.setInt(1, verseId);
				preparedStatement.setInt(2, tokenId);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			LOGGER.error("Error occurred while inserting token-verse relationships: " + e.getMessage(), e);
			// Handle the exception appropriately
		}
	}

	/**
	 * Checks if a token ID is available in the database.
	 *
	 * @param id The ID of the token to check.
	 * @return True if the token ID is available, false otherwise.
	 */
	public boolean isTokenIdAvailable(int id) {
		boolean idAvailable = false;

		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT id FROM tokensofverses WHERE token_id = ?")) {
			pStatement.setInt(1, id);
			ResultSet resultSet = pStatement.executeQuery();
			idAvailable = resultSet.next();
		} catch (SQLException e) {
			LOGGER.error("Error occurred while checking token ID availability: " + e.getMessage(), e);
		
		}
		return idAvailable;
	}

	public boolean isVerseIdAvailable(int verseId) {
		boolean idAvailable = false;

		try (PreparedStatement pStatement = connection
				.prepareStatement("SELECT verse_id FROM tokensofverses WHERE id = ?")) {
			pStatement.setInt(1, verseId);
			ResultSet resultSet = pStatement.executeQuery();
			idAvailable = resultSet.next(); // If next() returns true, the ID exists
		} catch (SQLException e) {
			LOGGER.error("Error occurred while checking verse ID availability: " + e.getMessage(), e);
			// Handle the exception appropriately
		}
		return idAvailable;
	}

	public ArrayList<Integer> getidFromTokens(ArrayList<String> tokens) {
		ArrayList<Integer> tokenIds = new ArrayList<>();

		String selectIdQuery = "SELECT id FROM tokenize WHERE token = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(selectIdQuery)) {

			for (String token : tokens) {
				preparedStatement.setString(1, token);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						int tokenId = resultSet.getInt("id");
						tokenIds.add(tokenId);
					} else {
						
						LOGGER.warn("Token not found in the database: " + token);
					}
				}
			}

		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting token IDs: " + e.getMessage(), e);
			
		}

		return tokenIds;
	}

	/**
	 * Retrieves tokens associated with a verse ID from the database.
	 *
	 * @param verseId The ID of the verse.
	 * @return The list of tokens associated with the verse.
	 */
	public ArrayList<String> getTokensByVerseId(int verseId) {
		ArrayList<String> tokens = new ArrayList<>();

		try (PreparedStatement pStatement = connection.prepareStatement("SELECT token FROM tokenize t "
				+ "JOIN tokensofverses tv ON t.id = tv.token_id " + "WHERE tv.verse_id = ?")) {
			pStatement.setInt(1, verseId);
			ResultSet resultSet = pStatement.executeQuery();

			while (resultSet.next()) {
				String token = resultSet.getString("token");
				tokens.add(token);
			}
		} catch (SQLException e) {
			LOGGER.error("Error occurred while getting tokens by verse ID: " + e.getMessage(), e);
		}

		return tokens;
	}
}

package dal.poemdal.importPoem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import dal.ConnectionClass;
import dal.poemdal.importPoem.ParsePoemDAO.Poems;
import net.oujda_nlp_team.entity.Result;

public class ParsePoemDAO implements IParsePoemDAO {
	StringBuilder data;
	Connection connection;

	public ParsePoemDAO() {
		connection = ConnectionClass.getInstance().getConnection();
	}

	public class Poems {
		public String title;
		public ArrayList<Misra> verses;

		public Poems(String title) {
			this.title = title;
			this.verses = new ArrayList<>();
		}

		void addVerse(Misra misra) {
			verses.add(misra);
		}
	}

	public class Misra {
		public ArrayList<String> misraList = new ArrayList<>();

		void addMisra(String m) {
			misraList.add(m);
		}
	}

	public boolean getPoemsData(String filePath, String bookName) {
		data = new StringBuilder();
		try {
			File file = new File(filePath);
			String line;
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("___")) {
					while (!line.contains("====")) {
						line = bufferedReader.readLine();
					}
					line = bufferedReader.readLine();
				}
				if (line != null) {

					data.append(line).append("\n");
				}
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();

			ArrayList<Poems> parsedData = new ArrayList<>();

			parsedData = filterData(data.toString(), parsedData);

			if (parsedData.isEmpty()) {
				return false;
			}

			storeDb(parsedData, bookName);
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private ArrayList<Poems> filterData(String data, ArrayList<Poems> parsedData) {

		try (BufferedReader reader = new BufferedReader(new StringReader(data))) {
			String line;
			Poems currentArbi = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("[")) {
					String storeTitle = line.substring(1, line.length() - 1);
					currentArbi = null;
					for (Poems Poems : parsedData) {
						if (Poems.title.equals(storeTitle)) {
							currentArbi = Poems;
							break;
						}
					}
					if (currentArbi == null) {
						currentArbi = new Poems(storeTitle);
						parsedData.add(currentArbi);
					}
				} else if (line.startsWith("(") && currentArbi != null) {
					String storeMisra = line.substring(1, line.length() - 2);
					String[] parts = storeMisra.split("\\.\\.\\.");
					Misra misra = new Misra();
					for (int i = 0; i < parts.length; i++) {
						misra.addMisra(parts[i].trim());
					}
					currentArbi.addVerse(misra);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parsedData;
	}

	private boolean storeDb(ArrayList<Poems> parsedData, String bookName) {
		PreparedStatement insertPoem = null;
		PreparedStatement insertVerse = null;

		try {

			connection.setAutoCommit(false);

			int book_id = getBookId(bookName);

			if (book_id == -1) {

				return false;
			}

			String insertPoemSQL = "INSERT INTO Poem (book_id, poem_Title) VALUES (?, ?)";
			String insertVerseSQL = "INSERT INTO verses (poem_id, firstVerse, secondVerse) VALUES (?, ?, ?)";

			insertPoem = connection.prepareStatement(insertPoemSQL, PreparedStatement.RETURN_GENERATED_KEYS);

			insertVerse = connection.prepareStatement(insertVerseSQL, PreparedStatement.RETURN_GENERATED_KEYS);

			for (Poems arbi : parsedData) {
				insertPoem.setInt(1, book_id);
				insertPoem.setString(2, arbi.title);
				insertPoem.addBatch();
			}

			int[] poemBatchResults = insertPoem.executeBatch();

			if (containsError(poemBatchResults)) {
				throw new SQLException("Poem batch execution failed.");
			}

			try (ResultSet generatedPoemKeys = insertPoem.getGeneratedKeys()) {

			}

			try (ResultSet generatedPoemKeys = insertPoem.getGeneratedKeys()) {
				int i = 0;
				while (generatedPoemKeys.next()) {
					int poemId = generatedPoemKeys.getInt(1);
					insertVerse.setInt(1, poemId);

					for (Misra misra : parsedData.get(i).verses) {
						insertVerse.setString(2, misra.misraList.size() >= 1 ? misra.misraList.get(0) : "");
						insertVerse.setString(3, misra.misraList.size() >= 2 ? misra.misraList.get(1) : "");
						insertVerse.addBatch(); // Add to batch
					}

					i++;
				}
			}

			// Execute batch for insertVerse
			int[] verseBatchResults = insertVerse.executeBatch();

			// Check for errors in verse batch execution
			if (containsError(verseBatchResults)) {
				throw new SQLException("Verse batch execution failed.");
			}

			// Get generated keys for verse batch
			try (ResultSet generatedVerseKeys = insertVerse.getGeneratedKeys()) {
				int i = 0;
				while (generatedVerseKeys.next()) {
					int verseId = generatedVerseKeys.getInt(1);
					for (Misra misra : parsedData.get(i).verses) {
						StringTokenizer tokenizer = new StringTokenizer(
								misra.misraList.get(0) + " " + misra.misraList.get(1));
						StringBuilder tokenizedVerses = new StringBuilder("");
						ArrayList<String> tokens = new ArrayList<>();
						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken();
							tokenizedVerses.append(token).append("\n");
							tokens.add(token);
						}

//						insertTokens(tokens, verseId);

						ArrayList<Integer> ids = getidFromTokens(tokens);

						int j = 0;
						for (String token : tokens) {
							String rawRoot = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(token)
									.getAllRootString();
							String cleanedRoot = rawRoot.replaceAll("[:-]", "");
							cleanedRoot = cleanedRoot.trim();
							int id = ids.get(j);

//							addOrUpdateRoute(cleanedRoot, id, verseId, "Not verified");

							j++;
						}
					}
					i++;
				}
			}

			// Commit the transaction
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			return false;
		} finally {
			try {
				if (insertVerse != null) {
					insertVerse.close();
				}
				if (insertPoem != null) {
					insertPoem.close();
				}
				if (connection != null) {
					connection.setAutoCommit(true);
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean containsError(int[] batchResults) {
		for (int result : batchResults) {
			if (result == PreparedStatement.EXECUTE_FAILED || result == Statement.SUCCESS_NO_INFO) {
				return true;
			}
		}
		return false;
	}

	private int getBookId(String bookName) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT id FROM book WHERE book_Name = ?")) {
			preparedStatement.setString(1, bookName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("id");
				} else {

					return -1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

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

				String partsOfSpeech = "";

				List<Result> pos = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(token)
						.getAllResults();

				if (!pos.isEmpty()) {

					String firstPartOfSpeech = pos.get(0).getPartOfSpeech();

					String[] parts = firstPartOfSpeech.split("\\|");
					if (parts.length > 0) {
						partsOfSpeech = parts[0];
					}
				}

				insertStatement.setString(1, token);
				insertStatement.setString(2, partsOfSpeech);

				insertStatement.executeUpdate();

				// Retrieve the generated keys and add them to the list
				try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						generatedIds.add(generatedKeys.getInt(1));
					} else {
						throw new SQLException("Failed to retrieve generated keys.");
					}
				}
			}

		} catch (SQLException e) {
			// Handle the exception appropriately
		}

//		insertTokenVerseRelationship(verseId, generatedIds);
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
			e.printStackTrace();
			// Handle the exception appropriately
		}
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
						// Token not found in the database, handle accordingly
						System.out.println("Token not found in the database: " + token);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception appropriately
		}

		return tokenIds;
	}

	public boolean addOrUpdateRoute(String root, int tokenid, int verseId, String status) throws SQLException {
		int existingRootId = roodIdFromName(root);
		if (existingRootId != -1) {
			return updRoute(root, root, status);
		} else {
			return addRoute(root, tokenid, verseId, status);
		}
	}

	public boolean addRoute(String root, int tokenid, int verseId, String status) throws SQLException {
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

	public boolean updRoute(String root, String newRootWord, String newStatus) {
		try {
			int rootId = roodIdFromName(root);

			if (rootId != -1) {
				// Update the root table
				String updateRootQuery = "UPDATE root SET root_Word = ? WHERE id = ?";
				try (PreparedStatement rootStatement = connection.prepareStatement(updateRootQuery)) {
					rootStatement.setString(1, newRootWord);
					rootStatement.setInt(2, rootId);

					int rowsUpdatedRoot = rootStatement.executeUpdate();

					// Update the rootsofverses table
					String updateRootsOfVersesQuery = "UPDATE rootsofverses SET status = ? WHERE root_id = ?";
					try (PreparedStatement rootsOfVersesStatement = connection
							.prepareStatement(updateRootsOfVersesQuery)) {
						rootsOfVersesStatement.setString(1, newStatus);
						rootsOfVersesStatement.setInt(2, rootId);

						int rowsUpdatedRootsOfVerses = rootsOfVersesStatement.executeUpdate();

						// Commit the transaction if both updates are successful
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
			// Handle the exception appropriately
		}
	}

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

}
package dal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionClass {
	private static ConnectionClass instance;
	private Connection connection;

	private ConnectionClass() {
		try {
			
			Properties properties = loadProperties();

			
			String dbUrl = properties.getProperty("db.url");
			String dbUser = properties.getProperty("db.user");
			String dbPassword = properties.getProperty("db.password");

			
			Class.forName("com.mysql.cj.jdbc.Driver");

			
			this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}
	}

	// Method to get the singleton instance
	public static ConnectionClass getInstance() {
		if (instance == null) {
			synchronized (ConnectionClass.class) {
				if (instance == null) {
					instance = new ConnectionClass();
				}
			}
		}
		return instance;
	}

	
	public Connection getConnection() {
		return connection;
	}

	private Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find db.properties");
				return properties;
			}

			properties.load(input);
		}
		return properties;
	}
}

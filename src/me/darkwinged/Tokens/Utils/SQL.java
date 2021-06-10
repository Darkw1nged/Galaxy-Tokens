package me.darkwinged.Tokens.Utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class SQL {
	
	final Plugin plugin;
	private Connection connection;
	private String host, database, username, password;
	private int port;

	protected SQL(Plugin plugin) {
		this.plugin = plugin;
		host = getConfig().getString("SQL.Host", "localhost");
		port = getConfig().getInt("SQL.Port", 3306);
		database = getConfig().getString("SQL.Database", "minecraft");
		username = getConfig().getString("SQL.Username", "root");
		password = getConfig().getString("SQL.Password", "password");
		try {
			openConnection();
			if (!doesTableExist("galaxytokens"))
				setupDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			plugin.getLogger().warning("An error occured when trying to connect to the MySQL database.");
			plugin.getLogger().warning("Make sure the MySQL settings are correct in the config.yml file, and that the MySQL server is online!");
			plugin.getLogger().warning("If this error persists please report the following Error code to the plugin developer: ERSQL-100-A");
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					DbUtils.close(connection);
				}
			} catch (SQLException e) {
				plugin.getLogger().warning("An error occured when trying to close the MySQL database connection.");
				plugin.getLogger().warning("If this error persists please report the following Error code to the plugin developer: ERSQL-101-A");
			}
		}
	}

	public FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	public void setupDatabase() throws SQLException {
		if (connection == null || connection.isClosed()) {
			Bukkit.getConsoleSender().sendMessage(Utils.chat("&cAn error occured while trying to generate database tables. " + "Please notify the Developer!"));
			return;
		}
		String query = "CREATE TABLE IF NOT EXISTS galaxytokens (player_uuid VARCHAR(36) NOT NULL, tokens INT(11) DEFAULT 0";
		PreparedStatement ps = connection.prepareStatement(query + ", PRIMARY KEY (player_uuid));");
		ps.executeUpdate();
		DbUtils.close(ps);
	}

	public void openConnection() throws SQLException, ClassNotFoundException {
		synchronized (this) {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + String.valueOf(port) + "/" + this.database, this.username, this.password);
			if (connection == null || connection.isClosed()) {
				plugin.getLogger().warning(Utils.chat("&cAn error occured while trying" + " to connect to the MySQL/SQLite server/database."));
				return;
			}
		}
	}

	public Connection getConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
			try {
				openConnection();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private boolean doesTableExist(String tableName) throws SQLException {
		DatabaseMetaData dbm = getConnection().getMetaData();
		ResultSet rs = dbm.getTables(null, null, tableName, null);
		if (rs.next())
			return true;
		else
			return false;
	}

	private boolean doesColumnExist(String tableName, String columnName) throws SQLException {
		DatabaseMetaData dbm = getConnection().getMetaData();
		ResultSet rs = dbm.getColumns(null, null, tableName, columnName);
		if (rs.next())
			return true;
		else
			return false;
	}

	@SuppressWarnings("unused")
	private List<String> getColumns(String tableName) throws SQLException {
		DatabaseMetaData dbm = getConnection().getMetaData();
		ResultSet rs = dbm.getColumns(null, null, tableName, null);
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount(); // number of column
		String columnName[] = new String[count];
		List<String> columns = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			columnName[i - 1] = metaData.getColumnLabel(i);
			columns.add(columnName[i - 1]);
		}
		return columns;
	}

}

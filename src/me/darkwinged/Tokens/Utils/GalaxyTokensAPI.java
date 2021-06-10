package me.darkwinged.Tokens.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.darkwinged.Tokens.Main;

public class GalaxyTokensAPI {
	
	private Main plugin;
	private SQL bd;

	public boolean playerExists(OfflinePlayer player) {
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("SELECT * FROM galaxytokens WHERE UUID=?");
			statement.setString(1, player.getUniqueId().toString());

			ResultSet results = statement.executeQuery();
			if (results.next()) {
				plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Player Found");
				return true;
			}
			plugin.getServer().broadcastMessage(ChatColor.RED + "Player NOT Found");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void createAccount(OfflinePlayer player) {
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("SELECT * FROM galaxytokens WHERE UUID=?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet results = statement.executeQuery();
			results.next();
			if (playerExists(player) != true) {
				PreparedStatement insert = bd.getConnection()
						.prepareStatement("INSERT INTO galaxytokens (UUID,NAME,TOKENS) VALUES (?,?,?)");
				insert.setString(1, player.getUniqueId().toString());
				insert.setString(2, player.getName());
				insert.setInt(3, 0);
				insert.executeUpdate();

				plugin.getServer().broadcastMessage(ChatColor.GREEN + "Player Inserted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getPlayerTokens(OfflinePlayer player) {
		int bal = 0;
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("SELECT * FROM galaxytokens WHERE UUID=?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet results = statement.executeQuery();
			results.next();
			bal = results.getInt("TOKENS");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bal;
	}
	
	public void setPlayerToken(OfflinePlayer player, Integer amount) {
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("UPDATE galaxytokens SET TOKENS=? WHERE UUID=?");
			statement.setInt(1, amount);
			statement.setString(2, player.getUniqueId().toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayerToken(OfflinePlayer player, Integer amount) {
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("UPDATE galaxytokens SET TOKENS=? WHERE UUID=?");
			statement.setInt(1, getPlayerTokens(player) + amount);
			statement.setString(2, player.getUniqueId().toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removePlayerToken(OfflinePlayer player, Integer amount) {
		try {
			PreparedStatement statement = bd.getConnection()
					.prepareStatement("UPDATE galaxytokens SET TOKENS=? WHERE UUID=?");
			statement.setInt(1, getPlayerTokens(player) - amount);
			statement.setString(2, player.getUniqueId().toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

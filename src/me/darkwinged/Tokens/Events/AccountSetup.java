package me.darkwinged.Tokens.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.darkwinged.Tokens.Utils.GalaxyTokensAPI;

public class AccountSetup implements Listener {
	
	private GalaxyTokensAPI api;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!api.playerExists(player)) {
			api.createAccount(player);
		}
	}

}

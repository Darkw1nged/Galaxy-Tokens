package me.darkwinged.Tokens.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.darkwinged.Tokens.Main;
import me.darkwinged.Tokens.Utils.GalaxyTokensAPI;
import me.darkwinged.Tokens.Utils.Utils;

public class cmd_Token implements CommandExecutor {
	
	private Main plugin;
	private GalaxyTokensAPI api;

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("token")) {
			if (!(sender instanceof Player)) {
				if (args.length != 3) {
					sender.sendMessage(Utils.chat("&cError! You can only do the following commands:"));
					sender.sendMessage(Utils.chat("&6/token add <player> <amount>"));
					sender.sendMessage(Utils.chat("&6/token remove <player> <amount>"));
					return true;
				}
				if (args[0].equalsIgnoreCase("add")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Utils.chat("&cError! That player could not be found."));
						return true;
					}
					if (api.playerExists(target)) {
						api.addPlayerToken(target, Integer.valueOf(args[2]));
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Utils.chat("&cError! That player could not be found."));
						return true;
					}
					if (api.playerExists(target)) {
						api.removePlayerToken(target, Integer.valueOf(args[2]));
					}
				}
				return true;
			}
			Player player = (Player)sender;
			if (args[0].equalsIgnoreCase("balance")) {
				if (player.hasPermission("GTokens.Balance")) {
					if (args.length != 2) {
						if (api.playerExists(player)) {
							player.sendMessage(Utils.chat(plugin.getConfig().getString("Messages.Balance")
								.replaceAll("%balance%", String.valueOf(api.getPlayerTokens(player)))));
						}
					} else {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							sender.sendMessage(Utils.chat("&cError! That player could not be found."));
							return true;
						}
						if (api.playerExists(target)) {
							player.sendMessage(Utils.chat(plugin.getConfig().getString("Messages.Balance")
									.replaceAll("%balance%", String.valueOf(api.getPlayerTokens(target)))
									.replaceAll("%player%", target.getName())));
						}
					}
				}
			} else if (args[0].equalsIgnoreCase("add")) {
				if (player.hasPermission("GTokens.Add")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Utils.chat("&cError! That player could not be found."));
						return true;
					}
					if (api.playerExists(target)) {
						api.addPlayerToken(target, Integer.valueOf(args[2]));
					}
				}				
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (player.hasPermission("GTokens.Remove")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Utils.chat("&cError! That player could not be found."));
						return true;
					}
					if (api.playerExists(target)) {
						api.removePlayerToken(target, Integer.valueOf(args[2]));
					}
				}
			} else if (args[0].equalsIgnoreCase("pay")) {
				if (player.hasPermission("GTokens.Pay")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(Utils.chat("&cError! That player could not be found."));
						return true;
					}
					if (api.playerExists(player)) {
						if (api.playerExists(target)) {
							api.addPlayerToken(target, Integer.valueOf(args[2]));
							api.removePlayerToken(player, Integer.valueOf(args[2]));
						}
					}
					
				}
			}
		}
		return false;
	}
	
}

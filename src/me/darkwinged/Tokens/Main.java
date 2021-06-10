package me.darkwinged.Tokens;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.darkwinged.Tokens.Commands.cmd_Token;
import me.darkwinged.Tokens.Events.AccountSetup;
import me.darkwinged.Tokens.Events.MobDeath;
import me.darkwinged.Tokens.Utils.SQL;
public class Main extends JavaPlugin {
	
	private SQL db;
	
	public void onEnable() {
		loadConfig();
		try {
			db.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		getServer().getPluginManager().registerEvents(new MobDeath(), this);
		getServer().getPluginManager().registerEvents(new AccountSetup(), this);
		getCommand("token").setExecutor(new cmd_Token());
		reset();
	}
	
	private FileConfiguration config;
    private File cfile;
    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        cfile = new File(getDataFolder(), "config.yml");
    }
    
    // Limit Token gain
    public Map<UUID, Integer> Limit = new HashMap<>();
    public Map<UUID, Integer> Max_Limit = new HashMap<>();
    
    public void limited() {
    	new BukkitRunnable() {
            public void run() {
            	for (Player player : Bukkit.getOnlinePlayers()) {
		    		if (player.hasPermission("GTokens.Limit.Tier1")) {
		    			if (Limit.get(player.getUniqueId()) >= getConfig().getInt("Limit Tokens.Tier 1")) {
		    				Max_Limit.put(player.getUniqueId(), 86400);
		    				reset();
		    			}
		    		} else if (player.hasPermission("GTokens.Limit.Tier2")) {
		    			if (Limit.get(player.getUniqueId()) >= getConfig().getInt("Limit Tokens.Tier 2")) {
		    				Max_Limit.put(player.getUniqueId(), 86400);
		    				reset();
		    			}
		    		} else if (player.hasPermission("GTokens.Limit.Tier3")) {
		    			if (Limit.get(player.getUniqueId()) >= getConfig().getInt("Limit Tokens.Tier 3")) {
		    				Max_Limit.put(player.getUniqueId(), 86400);
		    				reset();
		    			} 
		    		} else if (player.hasPermission("GTokens.Limit.Tier4")) {
		    			if (Limit.get(player.getUniqueId()) >= getConfig().getInt("Limit Tokens.Tier 4")) {
		    				Max_Limit.put(player.getUniqueId(), 86400);
		    				reset();
		    			}
		    		} else if (player.hasPermission("GTokens.Limit.Tier5")) {
		    			if (Limit.get(player.getUniqueId()) >= getConfig().getInt("Limit Tokens.Tier 5")) {
		    				Max_Limit.put(player.getUniqueId(), 86400);
		    				reset();
		    			}
		    		}
            	}
            }
        }.runTaskTimer(this, 0L, 20L);
    }
    
    public void reset() {
    	for (UUID uuid : Max_Limit.keySet()) {
    		if (Max_Limit.containsKey(uuid)) {
    			new BukkitRunnable() {
    	            public void run() {
    	            	if (!Max_Limit.containsKey(uuid)) return;
    	            	if (Max_Limit.get(uuid) <= 0) {
    	            		Max_Limit.remove(uuid);
    	            		return;
    	            	}
    	            	Max_Limit.put(uuid, Max_Limit.get(uuid) - 1);
    	            }
    	        }.runTaskTimer(this, 0L, 20L);
    		}
    	}
    }
	
}

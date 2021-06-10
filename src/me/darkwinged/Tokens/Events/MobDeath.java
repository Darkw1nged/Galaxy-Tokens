package me.darkwinged.Tokens.Events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.darkwinged.Tokens.Main;
import me.darkwinged.Tokens.Utils.GalaxyTokensAPI;
import me.darkwinged.Tokens.Utils.Utils;

public class MobDeath implements Listener {
	
	private Main plugin;
	private GalaxyTokensAPI api;
	
	@EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        String entityName = event.getEntityType().name();
        if (!plugin.getConfig().contains("Mobs.Chance." + entityName.toUpperCase())) return;
        
        Player player = event.getEntity().getKiller();
        if (plugin.Max_Limit.containsKey(player.getUniqueId())) return;
        
        int chance = this.getMobChance(entityName);
        Random random = new Random();
        
        if (random.nextInt(100) < chance) {
            api.addPlayerToken(player, plugin.getConfig().getInt("Mobs.Amount." + entityName.toUpperCase()));
            
            // Playing a sound when the mob dies
            String getSound = plugin.getConfig().getString("Mobs.Sound.sound");
            Sound sound = Sound.valueOf(getSound.toUpperCase());
            float volume = plugin.getConfig().getInt("Mobs.Sound.volume");
            float pitch = plugin.getConfig().getInt("Mobs.Sound.pitch");

            player.playSound(player.getLocation(), sound, volume, pitch);
            
            // Spawning the hologram
            Location loc = event.getEntity().getLocation();
            ArmorStand Hologram = loc.getWorld().spawn(loc, ArmorStand.class);
            Hologram.setGravity(false);
            Hologram.setCustomNameVisible(true);
            Hologram.setCustomName(Utils.chat(plugin.getConfig().getString("Mobs.Hologram." + entityName.toUpperCase())));
            new BukkitRunnable() {
                public void run() {
                	Hologram.remove();
                }
            }.runTaskTimer(plugin, 0L, 20 * 3);
        }
    }

    private int getMobChance(String entity) {
        if (!plugin.getConfig().contains("Mobs.Chance." + entity.toUpperCase())) {
            return 0;
        }
        return plugin.getConfig().getInt("Mobs.Chance." + entity.toUpperCase());
    }

}

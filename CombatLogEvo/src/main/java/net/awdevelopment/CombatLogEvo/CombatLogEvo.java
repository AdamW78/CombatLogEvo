package net.awdevelopment.CombatLogEvo;

import java.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatLogEvo extends JavaPlugin {
    
	final FileConfiguration config = getConfig();
    private List<String> commands;
    private CombatMap map;
    private double version;
    
    public CombatLogEvo() {
        commands = new ArrayList<String>();
        version = 1.01;
    }
    @Override
    public void onEnable() {
        commands.add("/tempban %player% 1d &cCombatLogging is not allowed on this server");
        //Add configuration options here
        config.addDefault("prefix", "&4[&cCombat Log Evo&4] &r");
        config.addDefault("secondsForCombat", Integer.valueOf(15));
        config.addDefault("combatEntryMessage", "&cYou are now in combat. Do NOT log out for %seconds% seconds or you will be killed and banned for a day");
        config.addDefault("killPlayer", Boolean.valueOf(true));
        config.addDefault("executeCommands", Boolean.valueOf(true));
        config.addDefault("commands", commands);
        config.addDefault("debugMode", false);
        config.options().copyDefaults(true);
        this.saveConfig();
        getServer().getPluginManager().registerEvents(new DamageEventListener(this), this);
        getServer().getPluginManager().registerEvents(new OnJoinListener(map), this);
        getServer().getPluginManager().registerEvents(new OnQuitListener(this, map), this);
        this.getCommand("cle").setExecutor(new CLE(version));
        map = new CombatMap(this);
        for (Player p : getServer().getOnlinePlayers()) {
        	map.addPlayer(p);
        }
    }
    @Override
    public void onDisable() {
    }

    public CombatMap getMap() {
        return map;
    }
}

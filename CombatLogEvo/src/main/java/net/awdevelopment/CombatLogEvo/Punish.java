package net.awdevelopment.CombatLogEvo;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Punish implements Callable<Boolean> {
    private CombatLogEvo plugin;
    private Player p;
    ConsoleCommandSender console;
    
    //Instantiate the console to send commands from the console
    //Instiantiate Player p to get player to kill
    //Instantiate CombatLogEvo plugin to fetch config values (commands to run)
    public Punish(Player player, CombatLogEvo instance)
    {
        console = Bukkit.getServer().getConsoleSender();
        p = player;
        plugin = instance;
    }
    //kills the player by setting his health to 0
    public void kill()
    {
        if(plugin.getConfig().getBoolean("killPlayer"))
            p.setHealth(0.0D);
    }
    //runs the commands fetched from the configuration with a for-each loop
    public void commands()
    {
        if(plugin.getConfig().getBoolean("executeCommands"))
        {
            for (String command : plugin.getConfig().getStringList("commands")) {
            	Bukkit.dispatchCommand(console, command);  	
            }
        }
    }
    //kills the player, runs configured commands
	public Boolean call() throws Exception {
		kill();
		commands();
		return true;
	}
}
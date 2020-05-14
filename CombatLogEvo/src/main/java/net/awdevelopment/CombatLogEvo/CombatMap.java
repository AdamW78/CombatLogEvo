package net.awdevelopment.CombatLogEvo;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class CombatMap {

    private HashMap<Player, Boolean> booleanMap;
    private HashMap<Player, ScheduledThreadPoolExecutor> timerMap;
    private HashMap<Player, ScheduledFuture<Boolean>> taskMap;
    private TextComponent msg;
    private TextComponent warnMsg;
    private CombatLogEvo plugin;
    private int seconds;
    private boolean debug;
    
    public CombatMap(CombatLogEvo instance) {
        //create a hash map that stores a boolean value for whether or not a player is in combat
    	booleanMap = new HashMap<Player, Boolean>();
    	//create a hash map of scheduled thread pool executors - only used once a player enters combat
        timerMap = new HashMap<Player, ScheduledThreadPoolExecutor>();
       //create a map of scheduled futures - only used once a player enters combat
        taskMap = new HashMap<Player, ScheduledFuture<Boolean>>();
        plugin = instance;
        debug = plugin.getConfig().getBoolean("debugMode");
        //fetch config values and bulld the enter combat message
        seconds = plugin.getConfig().getInt("secondsForCombat");
        msg = new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix")));
        warnMsg = new TextComponent((new StringBuilder("You are now in combat. Do not leave for ")).append(seconds).append(" or you will be killed and banned for a day.").toString());
        warnMsg.setColor(ChatColor.RED);
        msg.addExtra(warnMsg);
    }
    //returns a boolean for whether or not a player is in combat by fetching a value from booleanMap
    public boolean isInCombat(Player p) {
        return ((Boolean)booleanMap.get(p)).booleanValue();
    }

    public void enterCombat(Player p, int seconds) {
    	//Check if player isn't in combat
        if (booleanMap.replace(p, Boolean.valueOf(false), Boolean.valueOf(true))) {
        	if (debug) plugin.getLogger().info("Combat entered"); //debug
        	//send message about entering combat to player
            p.spigot().sendMessage(msg);
            //create a new ScheduledThreadpoolExecutor and add it to the timerMap for the player
            ScheduledThreadPoolExecutor playerTimer = new ScheduledThreadPoolExecutor(2);
            timerMap.put(p, playerTimer);
            //schedule a new ScheduleFuture to cancel the timer after specific seconds from config and add it to the taskMap for the player
            taskMap.put(p, playerTimer.schedule(new Cancel(playerTimer, p, plugin, booleanMap), seconds, TimeUnit.SECONDS));
            
        } 
        //This runs if the player is already in combat
        else {
        	//fetch the player's ScheduledFuture (to cancel itself) and cancels it early because the player has been hit
        	taskMap.get(p).cancel(true);
        	//fetch the player's ScheduledThreadpoolExecutor and reschedule it to cancel itself
        	ScheduledThreadPoolExecutor playerTimer = timerMap.get(p);
        	taskMap.put(p, playerTimer.schedule(new Cancel(playerTimer, p, plugin, booleanMap), seconds, TimeUnit.SECONDS));
        	if (debug) plugin.getLogger().info("Combat re-entered"); //debug
        	
        }
    }
    public boolean removeFromCombat(Player p) {
    	if(!booleanMap.get(p)) return false;
    	timerMap.get(p).submit(new Cancel(timerMap.get(p), p, plugin, booleanMap));
    	return true;
    }
    //Adds a player to the booleanMap, called on join
    public void addPlayer(Player p) {
        booleanMap.put(p, Boolean.valueOf(false));
    }
    //Accessor method to fetch booleanMap
    public HashMap<Player, Boolean> getBooleanMap() {
        return booleanMap;
    }
  //Accessor method to fetch timerMap
    public HashMap<Player, ScheduledThreadPoolExecutor> getTimerMap() {
        return timerMap;
    }
  //Accessor method to fetch taskMap
    public HashMap<Player, ScheduledFuture<Boolean>> getTaskMap() {
        return taskMap;
    }
    //Removes a player from booleanMap, called on quit
    public void removePlayer(Player p) {
        booleanMap.remove(p);
    }
}
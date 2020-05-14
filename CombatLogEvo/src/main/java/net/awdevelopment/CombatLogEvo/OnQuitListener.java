package net.awdevelopment.CombatLogEvo;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnQuitListener implements Listener {
    private CombatLogEvo plugin;
    private CombatMap combatMap;
    
    //Instantiates CombatLogEvo plugin to pass into the Punish Callable
    //Instantiaties CombatMap combatMap to remove player from the combatMap
    public OnQuitListener(CombatLogEvo instance, CombatMap map) {
        plugin = instance;
        combatMap = map;
    }
    //Runs on PlayerQuitEvent
    public void onQuit(PlayerQuitEvent e) {
       //Fetches player who just quit
    	Player p = e.getPlayer();
        //Checks if the player is in combat
        if (combatMap.getBooleanMap().get(p)) {
        	//Cancels the player's ScheduledFuture Cancel that is counting down
        	combatMap.getTaskMap().get(p).cancel(true);
        	//Submits the Punish Callable to the player's ScheduledThreadPool
        	combatMap.getTimerMap().get(p).submit(new Punish(p, plugin));
        	//Removes the player from the combat map
        	combatMap.removePlayer(p);
        }
    }
}

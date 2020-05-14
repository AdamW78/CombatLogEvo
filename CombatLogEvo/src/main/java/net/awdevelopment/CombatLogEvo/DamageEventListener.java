package net.awdevelopment.CombatLogEvo;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEventListener implements Listener {
	
    CombatLogEvo plugin;
    private int seconds;
    private CombatMap playerMap;
    private boolean debug;
    
    public DamageEventListener(CombatLogEvo instance) {
    	//pass in a CombatLogEvo instance to fetch secondsForCombat from the config.yml
        plugin = instance;
        debug = plugin.getConfig().getBoolean("debugMode");
        seconds = plugin.getConfig().getInt("secondsForCombat");
        //Fetch the CombatMap for putting players into combat

    }
    @EventHandler (priority = EventPriority.NORMAL, ignoreCancelled=true)
    public void onCombat(EntityDamageByEntityEvent e) {
    	playerMap = plugin.getMap();
    	if (debug) plugin.getLogger().info("Found an entity damaged by another entity, proceeding to check if combat should be entered for either"); //debug
    	//Check to make sure the entity damaged was a player and was damaged either by another player or by an arrow
    	//Otherwise, no action should be taken
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player || e.getDamager() instanceof Projectile)) return;
        //Store the damaged entity as a Player object so no casts are necessary in the future
        Player damagedPlayer = (Player) e.getEntity();
        if (debug) plugin.getLogger().info("Confirmed damaged entity was a player and was damaged by either another player or a projectile"); //debug
        //Defines the entity attacker with nested ternary operators
        //If attacker is a Projectile, and the Projectile was shot by a player, we return the player
        //Otherwise, we return the attacking Entity (which will either be a Player or a Projectile)
        Entity attacker = e.getDamager() instanceof Projectile 
        		? (((Projectile) e.getDamager()).getShooter() instanceof Player
        		? (Player) ((Projectile) e.getDamager()).getShooter() : e.getDamager()) : e.getDamager();
        //Case for when the attackerPlayer method returns a Player object
        if(attacker instanceof Player) {
        	//Creating a new player variable and casting attacker to a player and storing it
        	Player attackerPlayer = (Player) attacker;
        	//Store the attacking entity in a Player object so no casts are necessary in the future
        	if (debug) plugin.getLogger().info("Confirmed both entities are players, will check to make sure damaged Player was not killed"); //debug
            //Check to make sure damaged Player wasn't killed by the hit
            if(damagedPlayer.getHealth() - e.getDamage() > 0.0D) {
            	//Confirmed damaged Player wasn't killed, put them in combat
                playerMap.enterCombat(damagedPlayer, seconds);
                if (debug) plugin.getLogger().info("Confirmed damaged Player survived being hit, put them into combat"); //debug
            }
            //Case for if the damaged Player was killed, check if they were already in combat
            else if (playerMap.isInCombat(damagedPlayer)) {
            	//Remove the damaged Player from combat on death
            	playerMap.removeFromCombat(damagedPlayer);
            	if (debug) plugin.getLogger().info("Damaged Player was killed by the hit, removed them from combat"); //debug
            }
            //Put attacker Player in combat regardless of whether or not they killed damaged Player
            playerMap.enterCombat(attackerPlayer, seconds);
            if (debug) plugin.getLogger().info("Confirmed attacking player hit damaged Player, placed them in combat"); //debug
        }
    }
}
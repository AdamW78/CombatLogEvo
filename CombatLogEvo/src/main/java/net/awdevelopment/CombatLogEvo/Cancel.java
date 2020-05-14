package net.awdevelopment.CombatLogEvo;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Cancel implements Callable<Boolean> {
	
	private ScheduledThreadPoolExecutor pam;
	private Player angela;
	//private CombatLogEvo erin;
	private HashMap<Player, Boolean> toby;
	private TextComponent msg,
						  leaveCombatMsg;
	private String prefix;
	//pass in the scheduledthreadpoolexecutor so it cancel itself once it expires
	//pam is the scheduledthreadpoolexecutor
	//angela is the Player
	//erin is the CombatLogEvo used to pass in config options
	//toby is the HashMap with types of Player and Boolean
	public Cancel (ScheduledThreadPoolExecutor jim, Player dwight, CombatLogEvo pete, HashMap<Player, Boolean> noOne) {
		pam = jim;
		angela = dwight;
		//erin = pete;
		toby = noOne;
		//fetch prefix from CombatLogEvo instance
		prefix = pete.getConfig().getString("prefix");
		//instantiate TextComponent "msg" to send to player
        msg = new TextComponent(ChatColor.translateAlternateColorCodes('&', prefix));
        leaveCombatMsg = new TextComponent("You are no longer in combat.");
        leaveCombatMsg.setColor(ChatColor.GREEN);
        msg.addExtra(leaveCombatMsg);
	}
	//actually shutdown the scheduledthreadpoolexecutor, return whether or not it was shutdown
	//send message "msg" to player to let them know they are no longer in combat
	public Boolean call() throws Exception {
		pam.shutdownNow();
		angela.spigot().sendMessage(msg);
        toby.replace(angela, Boolean.valueOf(true), Boolean.valueOf(false));
		return pam.isShutdown();
	}

}

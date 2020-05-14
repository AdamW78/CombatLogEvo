package net.awdevelopment.CombatLogEvo;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.*;

public class CLE implements CommandExecutor {
    private double ver;
    private TextComponent msgLineOne;

    public CLE(double version) {
        ver = version;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Create a new textComponent, set its color and send it to the player - return true after execution
    	msgLineOne = new TextComponent((new StringBuilder("[CombatLogEvo] Version ")).append(ver).toString());
        msgLineOne.setColor(ChatColor.LIGHT_PURPLE);
        sender.spigot().sendMessage(msgLineOne);
        return true;
    }
}

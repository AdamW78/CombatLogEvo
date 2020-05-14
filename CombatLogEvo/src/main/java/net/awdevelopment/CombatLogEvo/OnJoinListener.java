package net.awdevelopment.CombatLogEvo;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class OnJoinListener implements Listener {

    public OnJoinListener(CombatMap combatMap) {
        combatBooleanMap = combatMap;
    }

    public void onJoin(PlayerJoinEvent e) {
        combatBooleanMap.addPlayer(e.getPlayer());
    }

    private CombatMap combatBooleanMap;
}
package com.github.AndrewAlbizati;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEventListener implements Listener {
    private final Main main;
    public QuitEventListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        main.getConfig().set(player.getUniqueId().toString(), System.currentTimeMillis());
        main.saveConfig();
    }
}
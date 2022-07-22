package com.github.AndrewAlbizati;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig(); // Create the config file if it doesn't exist

        // Register /seen command
        getCommand("seen").setExecutor(new SeenCommand(this));

        // Register events
        getServer().getPluginManager().registerEvents(new QuitEventListener(this), this);
    }
}
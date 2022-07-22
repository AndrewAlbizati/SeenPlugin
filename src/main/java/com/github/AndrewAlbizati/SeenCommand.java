package com.github.AndrewAlbizati;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class SeenCommand implements CommandExecutor {
    private final Main main;
    public SeenCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.equalsIgnoreCase("seen")) {
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Please provide a valid username to be searched.");
            return true;
        }

        String username = args[0];
        UUID uuid;
        try {
            uuid = getUUID(username);
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "The user " + username + " could not be found.");
            return true;
        }

        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "The user " + username + " could not be found.");
            return true;
        }


        if (Bukkit.getServer().getPlayerExact(username) != null) {
            sender.sendMessage(ChatColor.GREEN + "The user " + username + " is currently online.");
            return true;
        }

        if (!main.getConfig().contains(uuid.toString())) {
            sender.sendMessage(ChatColor.RED + "The user " + username + " has not logged into the server.");
            return true;
        }


        long quitTime = main.getConfig().getLong(uuid.toString());

        Date currentDate = new Date(quitTime);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        sender.sendMessage(ChatColor.GREEN + username + " was last seen on " + df.format(currentDate) + " GMT.");
        return true;
    }

    private static UUID getUUID(String username) throws IOException {
        URL nameURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + username.toLowerCase());
        URLConnection urlConnection = nameURL.openConnection();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        if (!stringBuilder.toString().startsWith("{")) {
            return null;
        }

        String uuidTemp = stringBuilder.substring(stringBuilder.toString().indexOf("\"id\":\"") + 6, stringBuilder.length() - 2);

        StringBuilder uuid = new StringBuilder();

        for (int i = 0; i <= 31; i++) {
            uuid.append(uuidTemp.charAt(i));
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                uuid.append("-");
            }
        }

        return UUID.fromString(uuid.toString());
    }
}
package me.jacestratton;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

/**
 * The main class to initialize the plugin and run commands.
 * 
 * @author JaceStratton
 * 
 * @version 1.0
 */
public class JacesPlugin extends JavaPlugin {
    private Guide tips;
    private String message;

    /**
     * Initializes fields and logs the plugin initialization.
     */
    @Override
    public void onEnable() {
        getLogger().info("JacesPlugin is online!");
        tips = new Guide(this);
        new WorldBorder(this);
    }

    /**
     * Closes any open processes and stops the plugin.
     */
    @Override
    public void onDisable() {

    }

    /**
     * Catches commands.
     * 
     * @param sender
     *            The player who sent the command.
     * @param cmd
     *            The command that was entered by the player.
     * @param label
     *            ???
     * @param args
     *            The arguments included for the command.
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("tip")) {
                return tip(player, args);
            }
            if (cmd.getName().equalsIgnoreCase("addtip")) {
                return addtip(player, args);
            }
        }
        return false;
    }

    /**
     * Prints any error from GuideBook methods to the logger.
     * 
     * @param e
     *            The error to print.
     */
    public void printError(Exception e) {
        getLogger().info(e.getMessage());
    }

    /**
     * Returns a tip to the player, either random or searched for.
     * 
     * @param player
     *            The player who sent the command.
     * @param args
     *            The number or keyword used to search for a tip.
     * @return True if the player sent a valid command. False if the player
     *         included too many arguments.
     */
    public boolean tip(Player player, String[] args) {
        if (args.length == 0) {
            message = tips.randomTip();
            getServer().broadcastMessage("[Server] " + player.getName() + " got tip " + message);
            return true;
        }
        else {
            Exception exception = null;
            int parse = 0;
            try {
                parse = Integer.parseInt(args[0]);
            }
            catch (Exception e) {
                exception = e;
            }
            // If there wasn't a problem getting an integer from the string, the
            // exception will be null and the player wants to search by number.
            if (exception == null) {
                message = tips.searchTip(parse);
                if (message == null) {
                    player.sendMessage(ChatColor.RED + "You must specify a number between 1 and " +
                            tips.getNumberOfTips() + ".");
                    return false;
                }
                getServer().broadcastMessage("[Server] " + player.getName() + " searched for tip " + message);
            }
            // If an integer could not be retrieved from the string, the method
            // will return an exception and the argument is a keyword.
            else {
                StringBuilder string = new StringBuilder(args[0]);
                for (int i = 1; i < args.length; i++) {
                    string.append(" " + args[i]);
                }
                message = tips.searchTip(string.toString());
                if (message == null) {
                    player.sendMessage(ChatColor.RED + "No matches found.");
                    return false;
                }
                getServer().broadcastMessage("[Server] " + player.getName() + " searched for tip " + message);
            }
            return true;
        }
    }

    /**
     * Adds a tip to the guidebook.
     * 
     * @param player
     *            The player who wants to add a tip.
     * @param args
     *            The tip the player wants to add.
     * @return True if the tip was not empty. False if the tip was empty.
     */
    public boolean addtip(Player player, String[] args) {
        if (args.length != 0) {
            StringBuilder string = new StringBuilder(args[0]);
            for (int i = 1; i < args.length; i++) {
                string.append(" " + args[i]);
            }
            tips.addTip(string.toString());
            message = tips.searchTip(tips.getNumberOfTips());
            getServer().broadcastMessage("[Server] " + player.getName() + " added tip " + message);
            return true;
        }
        player.sendMessage(ChatColor.RED + "You can't add an empty tip.");
        return false;
    }

}

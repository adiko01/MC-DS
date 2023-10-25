package de.adiko01.mcds.commands;

import de.adiko01.mcds.storage.MariaDB;
import de.adiko01.mcds.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.*;

/**
 * Klasse des Befehls /mcds
 * @author adiko01
 * @version 1.0
 */
public class MCDS implements CommandExecutor, TabCompleter {

    /** Der Storage **/
    private Storage store;

    public MCDS(Storage store) {
        this.store = store;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        boolean showHELP = false;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("link")) {
                //Linker
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    if (!p.hasPermission("mcds.link")) {
                        getPermError(commandSender, "mcds.link");
                        return false;
                    }
                }
                if (args.length < 2) {
                    commandSender.sendMessage("Bitte gebe einen Dienst an: /mcds link [service]");
                    return false;
                }
                link(commandSender, args[1]);
                return true;
            } else if (args[0].equalsIgnoreCase("about")) {
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    if (!p.hasPermission("mcds.about")) {
                        getPermError(commandSender, "mcds.about");
                        return false;
                    }
                }
                commandSender.sendMessage("Minecraft Directory Service\n"
                        + "GitHub: " + ChatColor.RED + ChatColor.UNDERLINE + "https://github.com/adiko01/MC-DS" + ChatColor.RESET + "\n"
                        + "Wiki: " + ChatColor.RED + ChatColor.UNDERLINE + "https://github.com/adiko01/MC-DS/wiki" + ChatColor.RESET + "\n"
                        + "Bugtracker: " + ChatColor.RED + ChatColor.UNDERLINE + "https://github.com/adiko01/MC-DS/issues" + ChatColor.RESET + "\n"
                        + "Bukkit: " + ChatColor.RED + ChatColor.UNDERLINE + "" + ChatColor.RESET + "\n" //TODO LINK
                );
                return false;
            } else if (args[0].equalsIgnoreCase("help")) {
                showHELP = true;
            } else {
                showHELP = true;
            }
        } else {
            showHELP = true;
        }

        if (showHELP) {
            //Prüfe, ob der Spieler atp.help besitzt
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if (!p.hasPermission("mcds.help")) {
                    getPermError(commandSender, "mcds.help");
                    return false;
                }
            }

            commandSender.sendMessage(
                    ChatColor.YELLOW +"-------------- Help: /mcds ----------------------------" + ChatColor.RESET + "\n"
                            + ChatColor.GOLD + "Description:" + ChatColor.RESET + " Below is a list of all /mcds commands:" + "\n"
                            + ChatColor.GOLD + "/mcds about :" + ChatColor.RESET + " Displays information about the plugin." + "\n"
                            + ChatColor.GOLD + "/mcds help :" + ChatColor.RESET + " Displays this page." + "\n"
                            + ChatColor.GOLD + "/mcds link [service]:" + ChatColor.RESET + " Generates the Config and an Manual to connect an other webservice." + "\n"
            );
            return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        String[][] Commands = {};
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("link")) {
                Commands = new String[][] {
                        //command snippet , permission
                        {"dynmap", "mcds.link"}
                };
            }
        } else {
            //Liste aller Argumente des Commmand
            Commands = new String[][] {
                    //command snippet , permission
                    {"about" , "mcds.about"},
                    {"help" , "mcds.help"},
                    {"link" , "mcds.link"}
            };
        }

        //Liste, welche zurückgegeben werden soll
        ArrayList<String> Ret = new ArrayList<>();
        //Aktueller Snippet des Arg
        String CurrentCommand = args[args.length-1];

        Player p;
        if (commandSender instanceof Player) {
            p = (Player) commandSender;
        } else {
            p = null;
        }

        for (String MCom[] : Commands) {
            if (MCom[0].startsWith(CurrentCommand)) {
                if ((p != null) && !p.hasPermission(MCom[1])) {
                    continue;
                }
                Ret.add(MCom[0]);
            }
        }

        return Ret;
    }

    /**
     * Zeigt einen Fehler, dass eine Permission fehlt
     * @param commandSender Der Sender
     * @param permission Die fehlende permission
     * @since 1.0
     */
    private void getPermError (CommandSender commandSender, String permission) {
        commandSender.sendMessage(ChatColor.RED + "This is not allowed! - You need " + ChatColor.BLUE + permission + ChatColor.RESET);
    }

    /**
     * Generiert im Pluginordner die passenden Configurationsdatein für einen Dienst
     * @param Type
     */
    private void link (CommandSender s, String Type) {
        if (Type.equalsIgnoreCase("dynmap")) {
            File dataFolder = Bukkit.getPluginManager().getPlugin("MC-DS").getDataFolder();
            copyResource("dynmap/README.md", dataFolder.toPath());
            copyResource("dynmap/login.html", dataFolder.toPath());
            copyResource("dynmap/MCDS_login.php", dataFolder.toPath());

            //Setze Default Werte in der MCDS_login.php
            MariaDB st = (MariaDB) store;

            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator + "MCDS_login.php",
                    "\t$DSdbname = '';",
                    "\t$DSdbname = '" + st.getDatabase() + "';"
            );
            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator +"MCDS_login.php",
                    "\t$DSdbhost = '';",
                    "\t$DSdbhost = '" + st.getHost() + "';"
            );
            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator +"MCDS_login.php",
                    "\t$DSdbport = 3306;",
                    "\t$DSdbport = " + st.getPort() + ";"
            );
            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator +"MCDS_login.php",
                    "\t$DSdbuserid = '';",
                    "\t$DSdbuserid = '" + st.getUsername() + "';"
            );
            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator +"MCDS_login.php",
                    "\t$DSdbpassword = '';",
                    "\t$DSdbpassword = '" + st.getPassword() + "';"
            );
            replaceLine(dataFolder.getPath() + File.separator +"dynmap" + File.separator +"MCDS_login.php",
                    "\t$DSdbprefix = '';",
                    "\t$DSdbprefix = '" + st.getPrefix() + "';"
            );
            s.sendMessage("Die Konfigurationsdateien für die dynmap sind erstellt worden.");
            getLogger().info("Die Konfigurationsdateien für die dynmap sind erstellt worden.");
        } else {
            s.sendMessage("Der Dienst " + Type + " existiert nicht.");
            getLogger().warning("Der Dienst " + Type + " existiert nicht.");
        }
    }

    /**
     * Kopiert die Recource in den Pluginordner
     * @param resourcePath
     * @param targetPath
     * @since 1.0
     */
    private void copyResource(String resourcePath, Path targetPath) {
        try {
            InputStream inputStream = Bukkit.getPluginManager().getPlugin("MC-DS").getResource(resourcePath);
            if (inputStream != null) {
                if (!Files.exists(targetPath.resolve(resourcePath))) {
                    Files.createDirectories(targetPath.resolve(resourcePath).getParent());
                    Files.copy(inputStream, targetPath.resolve(resourcePath), StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                getLogger().warning("Resource folder " + resourcePath + " not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ersetzt eine Zeile in der übergebenen Datei
     * @param filePath Pfad als String
     * @param oldLine Alte Zeile
     * @param newLine Neue Zeile
     * @since 1.0
     */
    private void replaceLine (String filePath, String oldLine, String newLine) {
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains(oldLine)) {
                    lines.set(i, newLine);
                }
            }

            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
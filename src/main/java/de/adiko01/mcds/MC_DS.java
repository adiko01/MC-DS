package de.adiko01.mcds;

import de.adiko01.mcds.commands.Account;
import de.adiko01.mcds.commands.DS;
import de.adiko01.mcds.storage.MariaDB;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import de.adiko01.mcds.storage.Storage;

/**
 * Main Class
 * @author adiko01
 * @version 1.0
 */
public final class MC_DS extends JavaPlugin {

    /** Die Pluginbeschreibungs-Datei
     * @since 1.0
     **/
    PluginDescriptionFile pdf;

    private Storage store;

    @Override
    public void onEnable() {
        // Plugin startup logic
        pdf = this.getDescription();

        if (!handleConfig()) {
            //Laden der Config fehlgeschlagen, stoppe Plugin
            getServer().getPluginManager().disablePlugin(this);
        } else {
            //Laden der Config erfolgreich!
            initCommands();


            getLogger().info("MC-DS " + getVersion() + " is enabled.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MC-DS is disabled.");
    }

    /**
     * Loading the Config
     * @since 1.0
     */
    private boolean handleConfig() {
        //Erstelle eine Config, wenn keine da ist
        saveDefaultConfig();

        //Lade die Engin
        if (!getConfig().isSet("storage")) {
            //Es gibt die Einstellung storage nicht
            getLogger().warning("Die Konfiguration ist fehlerhaft!\n" +
                    " Der Parameter 'storage' wurde nicht gefunden!");
            return false;
        } else if (getConfig().getString("storage").equalsIgnoreCase("mariadb") || getConfig().getString("storage").equalsIgnoreCase("mysql")) {
            //mariaDB oder MySql
            getLogger().info("Lade MariaDB bzw. MySQL");

            String[] params = {
                    "host",
                    "port",
                    "database",
                    "user",
                    "password",
                    "prefix"
            };

            for (String param : params) {
                if (!getConfig().isSet("mariadb." + param)) {
                    getLogger().warning(
                            "storage steht auf mariadb oder mysql.\n" +
                            "Unter mariadb wurde die Einstellung " + param + " nicht gefunden");
                    return false;
                }
            }

            store = new MariaDB(getConfig().getString("mariadb.host"),
                    getConfig().getInt("mariadb.port"),
                    getConfig().getString("mariadb.database"),
                    getConfig().getString("mariadb.user"),
                    getConfig().getString("mariadb.password"),
                    getConfig().getString("mariadb.prefix")
                    );

            if (store.createConnection()) {
                return true;
            } else {
                getLogger().warning("Verbindung zur Datenbank fehlgeschlagen! - " +
                        "Pruefe die Konfiguration");
                return false;
            }
        }
        getLogger().warning("Die Konfiguration ist fehlerhaft! - " +
                "Unbekannter Fehler!");
        return false;
    }

    /**
     * Melde die Befehle in Spigot an
     * @since 1.0
     */
    private void initCommands() {
        getCommand("ds").setExecutor(new DS());
        getCommand("account").setExecutor(new Account());
    }

    /**
     * @return Die Version des Plugins
     * @since 1.0
     */
    public String getVersion() {
        return pdf.getVersion();
    }
}

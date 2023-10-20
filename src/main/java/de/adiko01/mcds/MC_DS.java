package de.adiko01.mcds;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginDescriptionFile pdf = this.getDescription();



        getLogger().info("MC-DS " + pdf.getVersion() + " is enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MC-DS is disabled.");
    }

}

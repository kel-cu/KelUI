package ru.kelcuprum.kelmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.config.Config;

public class KelMenu implements ClientModInitializer {
    public static final Logger LOG = LogManager.getLogger("KelMenu");
    public static void log(String message) { log(message, Level.INFO);}
    public static void log(String message, Level level) { LOG.log(level, "[" + LOG.getName() + "] " + message); }
    public static Config config = new Config("config/KelMenu.json");
    //
    public static Boolean modmenu = FabricLoader.getInstance().getModContainer("modmenu").isPresent();
    public static Boolean isLoaded = false;
    @Override
    public void onInitializeClient() {
        config.load();
        log("Hello, world!");
    }

    public static void executeCommand(LocalPlayer player, String command){
        if (command.startsWith("/")) {
            command = command.substring(1);
            player.connection.sendCommand(command);
        } else {
            player.connection.sendChat(command);
        }
    }
}
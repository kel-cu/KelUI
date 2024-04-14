package ru.kelcuprum.kelui;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.config.Config;

public class KelUIServer implements DedicatedServerModInitializer {
    public static MinecraftServer MINECRAFT_SERVER;
    public static final Logger LOG = LogManager.getLogger("KelUI/Server");
    public static Config config = new Config("config/KelUI.server.json");

    public static void log(String message) {
        log(message, Level.INFO);
    }

    public static void log(String message, Level level) {
        LOG.log(level, "[" + LOG.getName() + "] " + message);
    }

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MINECRAFT_SERVER = server;
            log("Hello, moderator");
        });

    }
}

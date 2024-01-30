package ru.kelcuprum.kelui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.config.Config;

public class KelUI implements ClientModInitializer {
    public static final Logger LOG = LogManager.getLogger("KelUI");
    public static void log(String message) { log(message, Level.INFO);}
    public static void log(String message, Level level) { LOG.log(level, "[" + LOG.getName() + "] " + message); }
    public static Config config = new Config("config/KelUI.json");
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
    public static Screen getOptionScreen(Screen parent){
        return new OptionsScreen(parent, Minecraft.getInstance().options);
    }
    public static String getStringVersion(){
        return switch (KelUI.config.getNumber("VERSION_TYPE", 0).intValue()){
            case 1 -> String.format("Minecraft %s (Mods: %s)", Minecraft.getInstance().getLaunchedVersion(), FabricLoader.getInstance().getAllMods().size());
            case 2 -> String.format("Minecraft %s (%s)", Minecraft.getInstance().getLaunchedVersion(), Minecraft.getInstance().getVersionType());
            case 3 -> KelUI.config.getString("VERSION_TYPE.CUSTOM", "Modpack v1.0.0");
            default -> String.format("Minecraft %s", Minecraft.getInstance().getLaunchedVersion());
        };
    }
    public static String getStringCredits(){
        return switch (KelUI.config.getNumber("CREDITS", 0).intValue()){
            case 1 -> KelUI.config.getString("CREDITS.CUSTOM", "Made with ❤ by Kel");
            case 2 -> "";
            default -> Component.translatable("title.credits").getString();
        };
    }
    public interface ICONS{
        ResourceLocation LANGUAGE = new ResourceLocation("kelui", "textures/gui/sprites/icon/language.png");
        ResourceLocation ACCESSIBILITY = new ResourceLocation("kelui", "textures/gui/sprites/icon/accessibility.png");
    }
}
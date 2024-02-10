package ru.kelcuprum.kelui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.alinlib.config.Localization;

import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.SEADRIVE;

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
    public static String getArmorDamage(ItemStack item){
        return switch (config.getNumber("HUD.ARMOR_INFO.DAMAGE.TYPE", 0).intValue()){
            case 1 -> (item.getMaxDamage() == 0 ? "" : String.valueOf(item.getMaxDamage()-item.getDamageValue()));// Full damage
            case 2 -> (item.getMaxDamage() == 0 ? "" : Localization.getRounding(((double) (item.getMaxDamage()-item.getDamageValue()) /item.getMaxDamage())*100, config.getBoolean("HUD.ARMOR_INFO.DAMAGE.TYPE.CUT", true))+"%");
            default -> (item.getMaxDamage() == 0 ? "" : item.getMaxDamage() == (item.getMaxDamage() - item.getDamageValue()) ? String.format("%s", item.getMaxDamage()) : String.format("%s/%s", item.getMaxDamage() - item.getDamageValue(), item.getMaxDamage()));
        };
    }
    public static Component createTimestamp() {
        return Component.literal(
                new SimpleDateFormat(config.getString("CHAT.TIMESTAMP.PATTERN", "HH:mm")).format(new Date())
        ).setStyle(Style.EMPTY.withColor(config.getNumber("CHAT.TIMESTAMP.COLOR", SEADRIVE).intValue()));
    }

    public interface ICONS{
        ResourceLocation LANGUAGE = new ResourceLocation("kelui", "textures/gui/sprites/icon/language.png");
        ResourceLocation ACCESSIBILITY = new ResourceLocation("kelui", "textures/gui/sprites/icon/accessibility.png");
    }
}
package ru.kelcuprum.kelui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.api.events.alinlib.LocalizationEvents;
import ru.kelcuprum.alinlib.api.events.client.ClientLifecycleEvents;
import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.styles.FlatStyle;
import ru.kelcuprum.kelui.gui.screen.SkinCustomScreen;
import ru.kelcuprum.kelui.gui.style.SodiumLikeStyle;
import ru.kelcuprum.kelui.gui.style.VanillaLikeStyle;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static ru.kelcuprum.alinlib.gui.Colors.*;

public class KelUI implements ClientModInitializer {
    public static final FlatStyle flatStyle = new FlatStyle();
    public static final Logger LOG = LogManager.getLogger("KelUI");

    public static void log(String message) {
        log(message, Level.INFO);
    }

    public static void log(String message, Level level) {
        LOG.log(level, "[" + LOG.getName() + "] " + message);
    }

    public static Config config = new Config("config/KelUI.json");
    public static Minecraft MINECRAFT = Minecraft.getInstance();
    public static final String MINECRAFT_LAUNCHED_VERSION = MINECRAFT.getLaunchedVersion();
    public static IconStorageHelper iconStorageHelper = new IconStorageHelper();
    public static final boolean isSodiumExtraEnable = FabricLoader.getInstance().isModLoaded("sodium-extra");
    public static PlayerSkin playerSkin;
    public static UUID SillyUUID = UUID.randomUUID();
    public static boolean localizationInited = false;
    //
    public static VanillaLikeStyle vanillaLikeStyle = new VanillaLikeStyle();

    @Override
    public void onInitializeClient() {
        onInitialize();
        ClientLifecycleEvents.CLIENT_FULL_STARTED.register((s) -> playerSkin = s.getSkinManager().getInsecureSkin(s.getGameProfile()));
        LocalizationEvents.DEFAULT_PARSER_INIT.register((s) ->
            localizationInited = true
        );
        GuiUtils.registerStyle(vanillaLikeStyle);
        GuiUtils.registerStyle(new SodiumLikeStyle());
        iconStorageHelper.init();
    }

    public static void onInitialize() {
        log("Hello, world!");
    }

    public static void executeCommand(LocalPlayer player, String command) {
        if (command.startsWith("/")) {
            command = command.substring(1);
            player.connection.sendCommand(command);
        } else {
            player.connection.sendChat(command);
        }
    }

    public static Screen getOptionScreen(Screen parent) {
        return new OptionsScreen(parent, MINECRAFT.options);
    }

    public static String getStringVersion() {
        return switch (KelUI.config.getNumber("VERSION_TYPE", 0).intValue()) {
            case 1 ->
                    String.format("Minecraft %s (Mods: %s)", MINECRAFT.getLaunchedVersion(), FabricLoader.getInstance().getAllMods().size());
            case 2 -> String.format("Minecraft %s (%s)", MINECRAFT.getLaunchedVersion(), MINECRAFT.getVersionType());
            case 3 ->
                    AlinLib.starScript != null ? AlinLib.localization.getParsedText(KelUI.config.getString("VERSION_TYPE.CUSTOM", "Modpack v1.0.0")) : KelUI.config.getString("VERSION_TYPE.CUSTOM", "Modpack v1.0.0");
            default -> String.format("Minecraft %s", MINECRAFT.getLaunchedVersion());
        };
    }

    public static String getStringCredits() {
        return switch (KelUI.config.getNumber("CREDITS", 0).intValue()) {
            case 1 ->
                    AlinLib.starScript != null ? AlinLib.localization.getParsedText(KelUI.config.getString("CREDITS.CUSTOM", "Made with ❤ by Kel")) : KelUI.config.getString("CREDITS.CUSTOM", "Made with ❤ by Kel");
            case 2 -> "";
            default -> Component.translatable("title.credits").getString();
        };
    }

    public static String getArmorDamage(ItemStack item) {
        return switch (config.getNumber("HUD.ARMOR_INFO.DAMAGE.TYPE", 0).intValue()) {
            case 1 ->
                    (item.getMaxDamage() == 0 ? "" : String.valueOf(item.getMaxDamage() - item.getDamageValue()));// Full damage
            case 2 ->
                    (item.getMaxDamage() == 0 ? "" : Localization.getRounding(((double) (item.getMaxDamage() - item.getDamageValue()) / item.getMaxDamage()) * 100, config.getBoolean("HUD.ARMOR_INFO.DAMAGE.TYPE.CUT", true)) + "%");
            case 3 -> "";
            default ->
                    (item.getMaxDamage() == 0 ? "" : item.getMaxDamage() == (item.getMaxDamage() - item.getDamageValue()) ? String.format("%s", item.getMaxDamage()) : String.format("%s/%s", item.getMaxDamage() - item.getDamageValue(), item.getMaxDamage()));
        };
    }

    public static boolean isAprilFool() {
        return LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 1;
    }

    public static int getPingColor(int ping) {
        int color = 0xFFFFFFFF;
        if (config.getBoolean("TAB.PING_TO_TEXT.COLOR_ENABLE", true))
            color = ping < 150 ? SEADRIVE : ping < 300 ? CLOWNFISH : GROUPIE;
        return color;
    }

    public static Component createTimestamp() {
        return Component.literal(
                new SimpleDateFormat(config.getString("CHAT.TIMESTAMP.PATTERN", "HH:mm")).format(new Date())
        ).setStyle(Style.EMPTY.withColor(config.getNumber("CHAT.TIMESTAMP.COLOR", SEADRIVE).intValue()));
    }

    public static Screen getSkinCustom(Screen parent){
        return KelUI.config.getBoolean("CUSTOM_SKIN", true) ? new SkinCustomScreen(parent, AlinLib.MINECRAFT.options) : new SkinCustomizationScreen(parent, AlinLib.MINECRAFT.options);
    }
    public static boolean isModMenuInstalled(){
        return FabricLoader.getInstance().isModLoaded("modmenu") || FabricLoader.getInstance().isModLoaded("menulogue");
    }
    public static boolean isFlashbackInstalled(){
        return FabricLoader.getInstance().isModLoaded("flashback");
    }
    public static boolean isCatalogueInstalled(){
        return FabricLoader.getInstance().isModLoaded("catalogue");
    }

    public interface ICONS {
        ResourceLocation LOADING_ICON = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/loading/icon.png");
        ResourceLocation LANGUAGE = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/language.png");
        ResourceLocation HAT_SMALL = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/hat_small.png");
        ResourceLocation MULTIPLAYER = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/multiplayer.png");
        ResourceLocation SINGLEPLAYER = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/singleplayer.png");
        ResourceLocation REALMS = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/realms.png");
        ResourceLocation ACCESSIBILITY = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/accessibility.png");
        ResourceLocation CAPES = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/icon/capes.png");


        ResourceLocation RECORD = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/flashback/record.png");
        ResourceLocation PAUSE = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/flashback/pause.png");
        ResourceLocation PLAY = ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/sprites/flashback/play.png");
    }

    public interface TEXTS {
        Component NAME = Component.translatable("kelui.name");

        interface TITLE {
            Component MENU_CONFIG = Component.translatable("kelui.config.title.main_menu");
            Component PAUSE_CONFIG = Component.translatable("kelui.config.title.pause_menu");
            Component SCREENS_CONFIG = Component.translatable("kelui.config.title.screens_config");
            Component HUD_CONFIG = Component.translatable("kelui.config.title.hud");
            Component PLAYER_LIST_CONFIG = Component.translatable("kelui.config.title.player_list");
            Component OPTIONS_CONFIG = Component.translatable("kelui.config.title.options_config");
            Component LOADING_CONFIG = Component.translatable("kelui.config.title.loading");
            Component OTHER_CONFIG = Component.translatable("kelui.config.title.other");
        }
    }
}
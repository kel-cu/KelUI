package ru.kelcuprum.kelui.gui.screen.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorIntegerButton;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class MenuConfigScreen {

    public Screen build(Screen parent) {
        String[] realmsTypes = {
                "Skin",
                "Sounds"
        };
        String[] types = {
                "KelUI",
                "OneShot"
        };
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME, KelUI.configDesignType)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build().setActive(!FabricLoader.getInstance().isModLoaded("controlify")))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build().setActive(!FabricLoader.getInstance().isModLoaded("controlify")))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.MENU_CONFIG, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu"), true).setConfig(KelUI.config, "MAIN_MENU").build())
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.main_menu.type")).setValue(0).setList(types).setConfig(KelUI.config, "MAIN_MENU.TYPE").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.enable_realms"), false).setConfig(KelUI.config, "MAIN_MENU.ENABLE_REALMS").build())
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.main_menu.realms_small_button")).setList(realmsTypes).setValue(0).setConfig(KelUI.config, "MAIN_MENU.REALMS_SMALL_BUTTON").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.info"), true).setConfig(KelUI.config, "MAIN_MENU.INFO").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.version"), true).setConfig(KelUI.config, "MAIN_MENU.VERSION").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.credits"), true).setConfig(KelUI.config, "MAIN_MENU.CREDITS").build())
                .build();
    }
}

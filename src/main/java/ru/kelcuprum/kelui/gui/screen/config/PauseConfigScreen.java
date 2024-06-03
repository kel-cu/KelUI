package ru.kelcuprum.kelui.gui.screen.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

import static ru.kelcuprum.kelui.gui.screen.config.MenuConfigScreen.types;

public class PauseConfigScreen {

    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME, KelUI.configDesignType)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.PAUSE_CONFIG, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu"), true).setConfig(KelUI.config, "PAUSE_MENU").build())
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.pause_menu.type")).setValue(0).setList(types).setConfig(KelUI.config, "PAUSE_MENU.TYPE").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.alpha"), true).setConfig(KelUI.config, "PAUSE_MENU.ALPHA").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.info"), true).setConfig(KelUI.config, "PAUSE_MENU.INFO").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.version"), true).setConfig(KelUI.config, "PAUSE_MENU.VERSION").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.credits"), false).setConfig(KelUI.config, "PAUSE_MENU.CREDITS").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.player"), true).setConfig(KelUI.config, "PAUSE_MENU.PLAYER").build())
                .addWidget(new CategoryBox(Component.translatable("kelui.config.pause_menu.short_command"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.enable_short_command"), false).setConfig(KelUI.config, "PAUSE_MENU.ENABLE_SHORT_COMMAND").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.name")).setValue("Lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.NAME").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.command")).setValue("/lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.COMMAND").build())
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.pause_menu.oneshot"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.niko_roomba"), false).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.NIKO_ROOMBA").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.other"), true).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.OTHER").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.quit_question"), true).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.QUIT_QUESTION").build())
                )
                .build();
    }
}

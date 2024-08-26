package ru.kelcuprum.kelui.gui.screen.config;

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

public class PauseConfigScreen {

    public static String[] types = {
            "KelUI",
            "OneShot"
    };

    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OPTIONS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new SettingsConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.PAUSE_CONFIG, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu"), true).setConfig(KelUI.config, "PAUSE_MENU"))
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.pause_menu.type")).setValue(0).setList(types).setConfig(KelUI.config, "PAUSE_MENU.TYPE"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.info"), true).setConfig(KelUI.config, "PAUSE_MENU.INFO"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.version"), true).setConfig(KelUI.config, "PAUSE_MENU.VERSION"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.credits"), false).setConfig(KelUI.config, "PAUSE_MENU.CREDITS"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.player"), true).setConfig(KelUI.config, "PAUSE_MENU.PLAYER"))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.pause_menu.short_command"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.enable_short_command"), false).setConfig(KelUI.config, "PAUSE_MENU.ENABLE_SHORT_COMMAND"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.name")).setValue("Lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.NAME"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.command")).setValue("/lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.COMMAND"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.pause_menu.oneshot"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.niko_roomba"), false).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.NIKO_ROOMBA"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.other"), true).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.OTHER"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.oneshot.quit_question"), true).setConfig(KelUI.config, "PAUSE_MENU.ONESHOT.QUIT_QUESTION"))
                )
                .build();
    }
}

package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class PlayerListConfigScreen {

    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OPTIONS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new SettingsConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.forever_render_hat"), true).setConfig(KelUI.config, "TAB.FOREVER_RENDER_HAT"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.forever_render_heads"), true).setConfig(KelUI.config, "TAB.FOREVER_RENDER_HEADS"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text"), true).setConfig(KelUI.config, "TAB.PING_TO_TEXT"))
                .addWidget(new EditBoxBuilder(Component.translatable("kelui.config.tab.ping_to_text.format")).setValue("%sms").setConfig(KelUI.config, "TAB.PING_TO_TEXT.FORMAT"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text.color_enable"), true).setConfig(KelUI.config, "TAB.PING_TO_TEXT.COLOR_ENABLE"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text.render_icon"), false).setConfig(KelUI.config, "TAB.PING_TO_TEXT.RENDER_ICON"))
                .build();
    }
}

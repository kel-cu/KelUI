package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.screen.config.demo.PreviewLoadingOverlay;

public class LoadingConfigScreen {
    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME, KelUI.configDesignType)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.LOADING_CONFIG, true))
                .addWidget(new ButtonBuilder(Component.translatable("kelui.config.loading.preview"), (OnPress) -> Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(1000, () -> {}))).build())
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.loading.vanilla"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading"), true).setConfig(KelUI.config, "LOADING").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.background")).setColor(0xff1b1b1b).setConfig(KelUI.config, "LOADING.BACKGROUND").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.bar")).setColor(0xffff4f4f).setConfig(KelUI.config, "LOADING.BAR_COLOR").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.bar.border")).setColor(0xffffffff).setConfig(KelUI.config, "LOADING.BAR_COLOR.BORDER").build())
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.loading.new"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new"), true).setConfig(KelUI.config, "LOADING.NEW").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.background")).setColor(0xff030C03).setConfig(KelUI.config, "LOADING.NEW.BACKGROUND").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.bar.background")).setColor(0xff1FA48C).setConfig(KelUI.config, "LOADING.NEW.BAR_BACKGROUND").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.bar")).setColor(0x7f05241E).setConfig(KelUI.config, "LOADING.NEW.BAR_COLOR").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new.icon"), true).setConfig(KelUI.config, "LOADING.NEW.ENABLE_ICON").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new.icon_kelui"), true).setConfig(KelUI.config, "LOADING.NEW.ICON_KELUI").build())
                )
                .build();
    }
}

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

public class ScreenConfigScreen {
    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.SCREENS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new ScreenConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.SCREENS_CONFIG, true))
                .addWidget(new CategoryBox(KelUI.TEXTS.TITLE.LOADING_CONFIG)
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new"), false).setConfig(KelUI.config, "LOADING.NEW"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.background_color")).setColor(0xFFB4B4B4).setConfig(KelUI.config, "LOADING.NEW.BACKGROUND_C0LOR"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_color")).setColor(0xFF000000).setConfig(KelUI.config, "LOADING.NEW.BORDER_C0LOR"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_background_color")).setColor(0xFFD9D9D9).setConfig(KelUI.config, "LOADING.NEW.BORDER_BACKGROUND_C0LOR"))
                        .addValue(new ButtonBuilder(Component.translatable("kelui.config.loading.preview"), (OnPress) -> Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(2000, () -> {}))))
                )
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.custom_skin"), true).setConfig(KelUI.config, "CUSTOM_SKIN"))
                .build();
    }
}

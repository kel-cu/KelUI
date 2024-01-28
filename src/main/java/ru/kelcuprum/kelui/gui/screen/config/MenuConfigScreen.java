package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class MenuConfigScreen {
    private static final Component MainConfigCategory = Localization.getText("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Localization.getText("kelui.config.title.pause_menu");
    private static final Component LoadingConfigCategory = Localization.getText("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Localization.getText("kelui.config.title.other");
    private static final InterfaceUtils.DesignType designType = InterfaceUtils.DesignType.FLAT;

    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, Component.translatable("kelui.name"), InterfaceUtils.DesignType.FLAT)
                .addPanelWidget(
                        new Button(10,40, designType, MainConfigCategory, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10,65, designType, PauseConfigCategory, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10,90, designType, LoadingConfigCategory, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10,115, designType, OtherConfigCategory, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent)))
                )

                .addWidget(new TextBox(140, 5, MainConfigCategory, true))
                .addWidget(new ButtonConfigBoolean(140, 30, designType, KelUI.config, "MAIN_MENU", true, Localization.getText("kelui.config.main_menu")))
                .addWidget(new ButtonConfigBoolean(140, 55, designType, KelUI.config, "MAIN_MENU.PANORAMA", true, Localization.getText("kelui.config.main_menu.panorama")))
                .addWidget(new ButtonConfigBoolean(140, 80, designType, KelUI.config, "MAIN_MENU.VERSION", true, Localization.getText("kelui.config.main_menu.version")))
                .build();
    }
}
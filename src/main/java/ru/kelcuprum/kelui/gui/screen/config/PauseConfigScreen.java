package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxConfigString;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class PauseConfigScreen {
    private static final Component MainConfigCategory = Localization.getText("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Localization.getText("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Localization.getText("kelui.config.title.hud");
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
                        new Button(10,90, designType, HUDConfigCategory, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10,115, designType, LoadingConfigCategory, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10,140, designType, OtherConfigCategory, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent)))
                )

                .addWidget(new TextBox(140, 5, PauseConfigCategory, true))
                .addWidget(new ButtonConfigBoolean(140, 30, designType, KelUI.config, "PAUSE_MENU", true, Localization.getText("kelui.config.pause_menu")))
                .addWidget(new ButtonConfigBoolean(140, 55, designType, KelUI.config, "PAUSE_MENU.ALPHA", true, Localization.getText("kelui.config.pause_menu.alpha")))
                .addWidget(new ButtonConfigBoolean(140, 80, designType, KelUI.config, "PAUSE_MENU.VERSION", true, Localization.getText("kelui.config.pause_menu.version")))
                .addWidget(new ButtonConfigBoolean(140, 105, designType, KelUI.config, "PAUSE_MENU.PLAYER", true, Localization.getText("kelui.config.pause_menu.player")))
                .addWidget(new TextBox(140, 130, Localization.getText("kelui.config.pause_menu.short_command"), false))
                .addWidget(new ButtonConfigBoolean(140, 155, designType, KelUI.config, "PAUSE_MENU.ENABLE_SHORT_COMMAND", false, Localization.getText("kelui.config.pause_menu.enable_short_command")))
                .addWidget(new EditBoxConfigString(140, 180, false, designType, KelUI.config, "PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby", Localization.getText("kelui.config.pause_menu.short_command.name")))
                .addWidget(new EditBoxConfigString(140, 205, false, designType, KelUI.config, "PAUSE_MENU.SHORT_COMMAND.COMMAND" ,"/lobby", Localization.getText("kelui.config.pause_menu.short_command.command")))
                .build();
    }
}

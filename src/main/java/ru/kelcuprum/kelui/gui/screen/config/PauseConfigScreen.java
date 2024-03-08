package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxConfigString;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class PauseConfigScreen {
    private static final Component MainConfigCategory = Component.translatable("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Component.translatable("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Component.translatable("kelui.config.title.hud");
    private static final Component LoadingConfigCategory = Component.translatable("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Component.translatable("kelui.config.title.other");
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

                .addWidget(new TextBox(PauseConfigCategory, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu"), true).setConfig(KelUI.config, "PAUSE_MENU").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.alpha"), true).setConfig(KelUI.config, "PAUSE_MENU.ALPHA").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.info"), true).setConfig(KelUI.config, "PAUSE_MENU.INFO").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.version"), true).setConfig(KelUI.config, "PAUSE_MENU.VERSION").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.credits"), true).setConfig(KelUI.config, "PAUSE_MENU.CREDITS").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.player"), true).setConfig(KelUI.config, "PAUSE_MENU.PLAYER").build())
                .addWidget(new CategoryBox(Component.translatable("kelui.config.pause_menu.short_command"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.pause_menu.enable_short_command"), false).setConfig(KelUI.config, "PAUSE_MENU.ENABLE_SHORT_COMMAND").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.name")).setValue("Lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.NAME").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.pause_menu.short_command.command")).setValue("/lobby").setConfig(KelUI.config, "PAUSE_MENU.SHORT_COMMAND.COMMAND").build())
                )
                .build();
    }
}

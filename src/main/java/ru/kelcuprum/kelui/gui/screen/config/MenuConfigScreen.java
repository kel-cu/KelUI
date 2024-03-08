package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class MenuConfigScreen {
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

                .addWidget(new TextBox(MainConfigCategory, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu"), true).setConfig(KelUI.config, "MAIN_MENU").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.panorama"), true).setConfig(KelUI.config, "MAIN_MENU.PANORAMA").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.texture_background"), false).setConfig(KelUI.config, "MAIN_MENU.TEXTURE_BACKGROUND").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.info"), true).setConfig(KelUI.config, "MAIN_MENU.INFO").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.version"), true).setConfig(KelUI.config, "MAIN_MENU.VERSION").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.credits"), true).setConfig(KelUI.config, "MAIN_MENU.CREDITS").build())
                .build();
    }
}

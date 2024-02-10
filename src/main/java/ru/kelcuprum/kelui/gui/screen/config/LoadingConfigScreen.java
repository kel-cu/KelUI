package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxColor;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.screen.config.demo.PreviewLoadingOverlay;

public class LoadingConfigScreen {
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

                .addWidget(new TextBox(140, 5, MainConfigCategory, true))
                .addWidget(new CategoryBox(140 , 30, Component.translatable("kelui.config.title.loading.vanilla"))
                        .addValue(new ButtonConfigBoolean(140, 55, designType, KelUI.config, "LOADING", true, Component.translatable("kelui.config.loading")))
                        .addValue(new EditBoxColor(140, 80, designType, KelUI.config, "LOADING.BACKGROUND", 0xff1b1b1b, Component.translatable("kelui.config.loading.background")))
                        .addValue(new EditBoxColor(140, 105, designType, KelUI.config, "LOADING.BAR_COLOR", 0xffff4f4f, Component.translatable("kelui.config.loading.bar")))
                        .addValue(new EditBoxColor(140, 130, designType, KelUI.config, "LOADING.BAR_COLOR.BORDER", 0xffffffff, Component.translatable("kelui.config.loading.bar.border")))
                )
                .addWidget(new CategoryBox(140 , 155, Component.translatable("kelui.config.title.loading.new"))
                        .addValue(new ButtonConfigBoolean(140, 180, designType, KelUI.config, "LOADING.NEW", true, Component.translatable("kelui.config.loading.new")))
                        .addValue(new EditBoxColor(140, 205, designType, KelUI.config, "LOADING.NEW.BACKGROUND", 0xff030C03, Component.translatable("kelui.config.loading.new.background")))
                        .addValue(new EditBoxColor(140, 230, designType, KelUI.config, "LOADING.NEW.BAR_BACKGROUND", 0x7f05241E, Component.translatable("kelui.config.loading.new.bar")))
                        .addValue(new EditBoxColor(140, 255, designType, KelUI.config, "LOADING.NEW.BAR", 0xff1FA48C, Component.translatable("kelui.config.loading.new.bar.background")))
                        .addValue(new ButtonConfigBoolean(140, 280, designType, KelUI.config, "LOADING.NEW.ENABLE_ICON", true, Component.translatable("kelui.config.loading.new.icon")))
                        .addValue(new ButtonConfigBoolean(140, 305, designType, KelUI.config, "LOADING.NEW.ICON_KELUI", true, Component.translatable("kelui.config.loading.new.icon_kelui")))
                )

                .addWidget(new Button(140, 330, designType, Component.translatable("kelui.config.loading.preview"), (OnPress) ->{
                    Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(1000, () -> {}));
                }))
                .build();
    }
}

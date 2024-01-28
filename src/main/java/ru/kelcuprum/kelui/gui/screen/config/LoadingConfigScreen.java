package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxColor;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.screen.config.demo.PreviewLoadingOverlay;

public class LoadingConfigScreen {
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
                .addWidget(new ButtonConfigBoolean(140, 30, designType, KelUI.config, "LOADING", true, Localization.getText("kelui.config.loading")))
                .addWidget(new EditBoxColor(140, 55, designType, KelUI.config, "LOADING.BACKGROUND", 0xff1b1b1b, Localization.getText("kelui.config.loading.background")))
                .addWidget(new EditBoxColor(140, 80, designType, KelUI.config, "LOADING.BAR_COLOR", 0xffff4f4f, Localization.getText("kelui.config.loading.bar")))
                .addWidget(new EditBoxColor(140, 105, designType, KelUI.config, "LOADING.BAR_COLOR.BORDER", 0xffffffff, Localization.getText("kelui.config.loading.bar.border")))
                .addWidget(new Button(140, 130, designType, Localization.getText("kelui.config.loading.preview"), (OnPress) ->{
                    Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(1000, () -> {}));
                }))
                .build();
    }
}
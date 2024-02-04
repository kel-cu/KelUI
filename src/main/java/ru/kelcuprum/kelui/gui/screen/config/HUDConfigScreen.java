package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorIntegerButton;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class HUDConfigScreen {
    private static final Component MainConfigCategory = Localization.getText("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Localization.getText("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Localization.getText("kelui.config.title.hud");
    private static final Component LoadingConfigCategory = Localization.getText("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Localization.getText("kelui.config.title.other");
    private static final InterfaceUtils.DesignType designType = InterfaceUtils.DesignType.FLAT;

    public Screen build(Screen parent) {
        String[] type = {
                "Default",
                "Only damage",
                "Percent"
        };
        return new ConfigScreenBuilder(parent, Component.translatable("kelui.name"), InterfaceUtils.DesignType.FLAT)
                .addPanelWidget(
                        new Button(10,40, designType, MainConfigCategory, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent)))
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
                .addWidget(new ButtonConfigBoolean(140, 30, designType, KelUI.config, "HUD.ARMOR_INFO", true, Localization.getText("kelui.config.hud.armor_info")))
                .addWidget(new ButtonConfigBoolean(140, 55, designType, KelUI.config, "HUD.ARMOR_INFO.DAMAGE", true, Localization.getText("kelui.config.hud.armor_info.damage")))
                .addWidget(new SelectorIntegerButton(140, 80, designType, type, KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE", 0, Localization.getText("kelui.config.hud.armor_info.damage.type")))
                .addWidget(new ButtonConfigBoolean(140, 105, designType, KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE.CUT", false, Localization.getText("kelui.config.hud.armor_info.damage.type.cut")))
                .addWidget(new ButtonConfigBoolean(140, 130, designType, KelUI.config, "HUD.NEW_HOTBAR", false, Localization.getText("kelui.config.hud.new_hotbar")))
                .addWidget(new ButtonConfigBoolean(140, 155, designType, KelUI.config, "HUD.NEW_EFFECTS", false, Localization.getText("kelui.config.hud.new_effects")))
                .addWidget(new ButtonConfigBoolean(140, 180, designType, KelUI.config, "HUD.NEW_EFFECTS.TIME", true, Localization.getText("kelui.config.hud.new_effects.time")))
                .build();
    }
}

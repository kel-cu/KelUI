package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorIntegerButton;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class HUDConfigScreen {
    private static final Component MainConfigCategory = Component.translatable("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Component.translatable("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Component.translatable("kelui.config.title.hud");
    private static final Component LoadingConfigCategory = Component.translatable("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Component.translatable("kelui.config.title.other");
    private static final InterfaceUtils.DesignType designType = InterfaceUtils.DesignType.FLAT;

    public Screen build(Screen parent) {
        String[] type = {
                "Default",
                "Only damage",
                "Percent"
        };
        String[] hudPosition = {
                "Left",
                "Center"
        };
        String[] stateType = {
                "Default",
                "Vanilla-like",
        };
        //"HUD.NEW_HOTBAR.POSITION"
        return new ConfigScreenBuilder(parent, Component.translatable("kelui.name"), InterfaceUtils.DesignType.FLAT)
                .addPanelWidget(
                        new Button(10, 40, designType, MainConfigCategory, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10, 65, designType, PauseConfigCategory, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10, 90, designType, HUDConfigCategory, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10, 115, designType, LoadingConfigCategory, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent)))
                )
                .addPanelWidget(
                        new Button(10, 140, designType, OtherConfigCategory, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent)))
                )

                .addWidget(new TextBox(140, 5, HUDConfigCategory, true))
                .addWidget(new ButtonConfigBoolean(140, 30, designType, KelUI.config, "HUD.DEBUG_OVERLAY", false, Component.translatable("kelui.config.hud.debug_overlay")))
                .addWidget(new CategoryBox(140, 55, Component.translatable("kelui.config.hud.armor_info.title"))
                        .addValue(new ButtonConfigBoolean(140, 80, designType, KelUI.config, "HUD.ARMOR_INFO", true, Component.translatable("kelui.config.hud.armor_info")))
                        .addValue(new ButtonConfigBoolean(140, 105, designType, KelUI.config, "HUD.ARMOR_INFO.DAMAGE", true, Component.translatable("kelui.config.hud.armor_info.damage")))
                        .addValue(new SelectorIntegerButton(140, 130, designType, type, KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE", 0, Component.translatable("kelui.config.hud.armor_info.damage.type")))
                        .addValue(new ButtonConfigBoolean(140, 155, designType, KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE.CUT", true, Component.translatable("kelui.config.hud.armor_info.damage.type.cut"))))
                .addWidget(new CategoryBox(140, 180, Component.translatable("kelui.config.new_interface"))
                        .addValue(new ButtonConfigBoolean(140, 205, designType, KelUI.config, "HUD.NEW_HOTBAR", false, Component.translatable("kelui.config.hud.new_hotbar")))
                        .addValue(new SelectorIntegerButton(140, 230, designType, hudPosition, KelUI.config, "HUD.NEW_HOTBAR.POSITION", 0, Component.translatable("kelui.config.hud.new_hotbar.position")))
                        .addValue(new SelectorIntegerButton(140, 255, designType, stateType, KelUI.config, "HUD.NEW_HOTBAR.STATE_TYPE", 0, Component.translatable("kelui.config.hud.new_hotbar.state_type")))
                        .addValue(new ButtonConfigBoolean(140, 280, designType, KelUI.config, "HUD.NEW_EFFECTS", false, Component.translatable("kelui.config.hud.new_effects")))
                        .addValue(new ButtonConfigBoolean(140, 305, designType, KelUI.config, "HUD.NEW_EFFECTS.TIME", true, Component.translatable("kelui.config.hud.new_effects.time"))))
                .addWidget(new CategoryBox(140, 330, Component.translatable("kelui.config.debug_overlay"))
                        .addValue(new ButtonConfigBoolean(140, 355, designType, KelUI.config, "HUD.DARK_DEBUG_OVERLAY", false, Component.translatable("kelui.config.hud.dark_debug_overlay")))
                        .addValue(new ButtonConfigBoolean(140, 380, designType, KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_GAME_INFO", false, Component.translatable("kelui.config.hud.debug_overlay.remove_game_info")))
                        .addValue(new ButtonConfigBoolean(140, 405, designType, KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_SYSTEM_INFO", false, Component.translatable("kelui.config.hud.debug_overlay.remove_system_info"))))
                .build();
    }
}

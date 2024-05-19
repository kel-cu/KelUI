package ru.kelcuprum.kelui.gui.screen.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class HUDConfigScreen {

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
//                "Modern"
        };
        //"HUD.NEW_HOTBAR.POSITION"
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME, KelUI.configDesignType)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build().setActive(!FabricLoader.getInstance().isModLoaded("controlify")))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build().setActive(!FabricLoader.getInstance().isModLoaded("controlify")))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.HUD_CONFIG, true))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY").build())
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.paper_doll"), false).setConfig(KelUI.config, "HUD.PAPER_DOLL").build())
                .addWidget(new CategoryBox(Component.translatable("kelui.config.hud.armor_info.title"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.damage"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE").build())
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.armor_info.damage.type")).setValue(0).setList(type).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.damage.type.cut"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE.CUT").build()))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.new_interface"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_hotbar"), false).setConfig(KelUI.config, "HUD.NEW_HOTBAR").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_hotbar.colored_bar"), false).setConfig(KelUI.config, "HUD.NEW_HOTBAR.COLORED_BAR").build())
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.new_hotbar.position")).setValue(0).setList(hudPosition).setConfig(KelUI.config, "HUD.NEW_HOTBAR.POSITION").build())
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.new_hotbar.state_type")).setValue(0).setList(stateType).setConfig(KelUI.config, "HUD.NEW_HOTBAR.STATE_TYPE").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_effects"), false).setConfig(KelUI.config, "HUD.NEW_EFFECTS").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_effects.time"), true).setConfig(KelUI.config, "HUD.NEW_EFFECTS.TIME").build()))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.debug_overlay"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.debug.dark_graph"), true).setConfig(KelUI.config, "DEBUG.DARK_GRAPH").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.dark_debug_overlay"), false).setConfig(KelUI.config, "HUD.DARK_DEBUG_OVERLAY").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay.remove_game_info"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_GAME_INFO").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay.remove_system_info"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_SYSTEM_INFO").build()))
                .build();
    }
}

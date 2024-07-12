package ru.kelcuprum.kelui.gui.screen.config;

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
                "Center",
                "Right"
        };
        String[] aiPosition = {
                "Left",
                "Right"
        };
        String[] stateType = {
                "Default",
                "Vanilla-like"
        };
        ConfigScreenBuilder builder = new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OPTIONS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new SettingsConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PlayerListConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.HUD_CONFIG, true));
        if (KelUI.isSodiumExtraEnable)
            builder.addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.sodium_extra_debug"), false).setConfig(KelUI.config, "HUD.SODIUM_EXTRA_DEBUG").build());
        builder.addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.paper_doll"), false).setConfig(KelUI.config, "HUD.PAPER_DOLL").build())
                .addWidget(new CategoryBox(Component.translatable("kelui.config.hud.armor_info.title"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.selected"), false).setConfig(KelUI.config, "HUD.ARMOR_INFO.SELECTED").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.off_hand"), false).setConfig(KelUI.config, "HUD.ARMOR_INFO.OFF_HAND").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.damage"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.warning"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.WARNING").build())
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.armor_info.position")).setValue(0).setList(aiPosition).setConfig(KelUI.config, "HUD.ARMOR_INFO.POSITION").build())
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
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay.remove_system_info"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_SYSTEM_INFO").build()));
        return builder.build();
    }
}

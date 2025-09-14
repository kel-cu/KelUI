package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
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
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.SCREENS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new ScreenConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .setCategoryTitle(KelUI.TEXTS.TITLE.HUD_CONFIG);
        if (KelUI.isSodiumExtraEnable)
            builder.addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.sodium_extra_debug"), false).setConfig(KelUI.config, "HUD.SODIUM_EXTRA_DEBUG"));
        builder.addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.paper_doll"), false).setConfig(KelUI.config, "HUD.PAPER_DOLL"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.item_info"), true).setConfig(KelUI.config, "HUD.ITEM_INFO"))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.hud.armor_info.title"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.selected"), false).setConfig(KelUI.config, "HUD.ARMOR_INFO.SELECTED"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.off_hand"), false).setConfig(KelUI.config, "HUD.ARMOR_INFO.OFF_HAND"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.damage"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.warning"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.WARNING"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.armor_info.position")).setValue(0).setList(aiPosition).setConfig(KelUI.config, "HUD.ARMOR_INFO.POSITION"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.armor_info.damage.type")).setValue(0).setList(type).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.armor_info.damage.type.cut"), true).setConfig(KelUI.config, "HUD.ARMOR_INFO.DAMAGE.TYPE.CUT")))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.new_interface"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.toasts"), true).setConfig(KelUI.config, "TOASTS"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_hotbar"), false).setConfig(KelUI.config, "HUD.NEW_HOTBAR"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_hotbar.colored_bar"), false).setConfig(KelUI.config, "HUD.NEW_HOTBAR.COLORED_BAR"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.new_hotbar.position")).setValue(1).setList(hudPosition).setConfig(KelUI.config, "HUD.NEW_HOTBAR.POSITION"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.hud.new_hotbar.state_type")).setValue(1).setList(stateType).setConfig(KelUI.config, "HUD.NEW_HOTBAR.STATE_TYPE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_effects"), false).setConfig(KelUI.config, "HUD.NEW_EFFECTS"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.new_effects.time"), true).setConfig(KelUI.config, "HUD.NEW_EFFECTS.TIME")))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.debug_overlay"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.debug.dark_graph"), true).setConfig(KelUI.config, "DEBUG.DARK_GRAPH"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.dark_debug_overlay"), false).setConfig(KelUI.config, "HUD.DARK_DEBUG_OVERLAY"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay.remove_game_info"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_GAME_INFO"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.debug_overlay.remove_system_info"), false).setConfig(KelUI.config, "HUD.DEBUG_OVERLAY.REMOVE_SYSTEM_INFO")))
                .addWidget(new CategoryBox(KelUI.TEXTS.TITLE.PLAYER_LIST_CONFIG, true)
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.forever_render_hat"), true).setConfig(KelUI.config, "TAB.FOREVER_RENDER_HAT"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.forever_render_heads"), true).setConfig(KelUI.config, "TAB.FOREVER_RENDER_HEADS"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text"), true).setConfig(KelUI.config, "TAB.PING_TO_TEXT"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.tab.ping_to_text.format")).setValue("%sms").setConfig(KelUI.config, "TAB.PING_TO_TEXT.FORMAT"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text.color_enable"), true).setConfig(KelUI.config, "TAB.PING_TO_TEXT.COLOR_ENABLE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.ping_to_text.render_icon"), false).setConfig(KelUI.config, "TAB.PING_TO_TEXT.RENDER_ICON"))
                );
        return builder.build();
    }
}

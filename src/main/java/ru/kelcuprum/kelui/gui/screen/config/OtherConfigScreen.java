package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.SEADRIVE;

public class OtherConfigScreen {

    public Screen build(Screen parent) {
        String[] versionTypes = {
                "Minecraft",
                "Minecraft + Mods loaded",
                "Minecraft + Loader",
                "Custom",
        };
        String[] creditsTypes = {
                "Mojang AB",
                "Custom",
                "Nothing",
        };
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME, KelUI.configDesignType)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG, (s) -> Minecraft.getInstance().setScreen(new LoadingConfigScreen().build(parent))).build())
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))).build())

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.OTHER_CONFIG, true))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.render"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.tab.forever_render_heads"), true).setConfig(KelUI.config, "TAB.FOREVER_RENDER_HEADS").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.map_slot"), true).setConfig(KelUI.config, "HUD.MAP_SLOT").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.debug.dark_graph"), true).setConfig(KelUI.config, "DEBUG.DARK_GRAPH").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.smooth_menu"), false).setConfig(KelUI.config, "UI.SMOOTH_MENU").build())
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.chat"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.edgeless_screen"), true).setConfig(KelUI.config, "CHAT.EDGELESS_SCREEN").build())
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.timestamp"), true).setConfig(KelUI.config, "CHAT.TIMESTAMP").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.pattern")).setValue("HH:mm").setConfig(KelUI.config, "CHAT.TIMESTAMP.PATTERN").build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.color")).setColor(SEADRIVE).setConfig(KelUI.config, "CHAT.TIMESTAMP.COLOR").build())
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.info"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.version")).setValue(0).setConfig(KelUI.config, "VERSION_TYPE").setList(versionTypes).build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.version.custom")).setValue("Modpack v1.0.0").setConfig(KelUI.config, "VERSION_TYPE.CUSTOM").build())
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.credits")).setValue(0).setConfig(KelUI.config, "CREDITS").setList(creditsTypes).build())
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.credits.custom")).setValue("Made with ❤ by Kel").setConfig(KelUI.config, "CREDITS.CUSTOM").build())
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.fix"))
                        .addValue(new TextBox(Component.translatable("kelui.fix.connect_screen"), false))
                        .addValue(new TextBox(Component.translatable("kelui.fix.selection_list"), false))
                        .addValue(new TextBox(Component.translatable("kelui.fix.tab_navigation_bar"), false))
                )

                .build();
    }
}

package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxColor;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxConfigString;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorIntegerButton;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.SEADRIVE;

public class OtherConfigScreen {
    private static final Component MainConfigCategory = Component.translatable("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Component.translatable("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Component.translatable("kelui.config.title.hud");
    private static final Component LoadingConfigCategory = Component.translatable("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Component.translatable("kelui.config.title.other");
    private static final InterfaceUtils.DesignType designType = InterfaceUtils.DesignType.FLAT;
    public Screen build(Screen parent){
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

                 .addWidget(new TextBox(OtherConfigCategory, true))
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

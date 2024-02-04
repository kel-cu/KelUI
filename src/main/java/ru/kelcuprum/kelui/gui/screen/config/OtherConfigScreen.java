package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonConfigBoolean;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxConfigString;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorIntegerButton;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class OtherConfigScreen {
    private static final Component MainConfigCategory = Localization.getText("kelui.config.title.main_menu");
    private static final Component PauseConfigCategory = Localization.getText("kelui.config.title.pause_menu");
    private static final Component HUDConfigCategory = Localization.getText("kelui.config.title.hud");
    private static final Component LoadingConfigCategory = Localization.getText("kelui.config.title.loading");
    private static final Component OtherConfigCategory = Localization.getText("kelui.config.title.other");
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

                 .addWidget(new TextBox(140, 5, OtherConfigCategory, true))
                 .addWidget(new CategoryBox(140, 30, Component.translatable("kelui.config.title.other.render"))
                         .addValue(new ButtonConfigBoolean(140, 55, designType, KelUI.config, "TAB.FOREVER_RENDER_HEADS", true, Localization.getText("kelui.config.tab.forever_render_heads")))
                         .addValue(new ButtonConfigBoolean(140, 80, designType, KelUI.config, "HUD.MAP_SLOT", true, Localization.getText("kelui.config.hud.map_slot")))
                         .addValue(new ButtonConfigBoolean(140, 105, designType, KelUI.config, "CHAT.EDGELESS_SCREEN", true, Localization.getText("kelui.config.edgeless_chat_screen")))
                         .addValue(new ButtonConfigBoolean(140, 130, designType, KelUI.config,  "DEBUG.DARK_GRAPH", true, Localization.getText("kelui.config.debug.dark_graph"))))
                 .addWidget(new CategoryBox(140, 155, Component.translatable("kelui.config.title.other.info"))
                         .addValue(new SelectorIntegerButton(140, 180, designType, versionTypes, KelUI.config, "VERSION_TYPE", 0, Localization.getText("kelui.config.version")))
                         .addValue(new EditBoxConfigString(140, 205, false , designType, KelUI.config, "VERSION_TYPE.CUSTOM", "Modpack v1.0.0", Localization.getText("kelui.config.version.custom")))
                         .addValue(new SelectorIntegerButton(140, 230, designType, creditsTypes, KelUI.config, "CREDITS", 0, Localization.getText("kelui.config.credits")))
                         .addValue(new EditBoxConfigString(140, 255, false, designType, KelUI.config, "CREDITS.CUSTOM", "Made with ❤ by Kel", Localization.getText("kelui.config.credits.custom"))))
                 .addWidget(new CategoryBox(140, 280, Component.translatable("kelui.config.title.other.fix"))
                         .addValue(new TextBox(140, 305, Component.translatable("kelui.fix.connect_screen"), false))
                         .addValue(new TextBox(140, 330, Component.translatable("kelui.fix.selection_list"), false))
                         .addValue(new TextBox(140, 355, Component.translatable("kelui.fix.tab_navigation_bar"), false))
                 )

                 .build();
    }
}

package ru.kelcuprum.kelui.gui.screen.config;

import com.mojang.blaze3d.platform.IconSet;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

import java.io.IOException;

import static ru.kelcuprum.alinlib.gui.Colors.SEADRIVE;

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
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.SCREENS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new ScreenConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.OTHER_CONFIG, true))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.render"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.map_slot"), true).setConfig(KelUI.config, "HUD.MAP_SLOT"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.smooth_menu"), false).setConfig(KelUI.config, "UI.SMOOTH_MENU"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.ui.tooltip_type")).setValue(0).setList(new String[]{"Vanilla", "KelUI", "KelUI [Textured]"}).setConfig(KelUI.config, "UI.TOOLTIP_TYPE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.alinlib_style"), false).setConfig(KelUI.config, "UI.ALINLIB_STYLE"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.chat"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.edgeless_screen"), true).setConfig(KelUI.config, "CHAT.EDGELESS_SCREEN"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.timestamp"), true).setConfig(KelUI.config, "CHAT.TIMESTAMP"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.pattern")).setValue("HH:mm").setConfig(KelUI.config, "CHAT.TIMESTAMP.PATTERN"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.color")).setColor(SEADRIVE).setConfig(KelUI.config, "CHAT.TIMESTAMP.COLOR"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.info"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.version")).setValue(0).setConfig(KelUI.config, "VERSION_TYPE").setList(versionTypes))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.version.custom")).setValue("Modpack v1.0.0").setConfig(KelUI.config, "VERSION_TYPE.CUSTOM"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.credits")).setValue(0).setConfig(KelUI.config, "CREDITS").setList(creditsTypes))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.credits.custom")).setValue("Made with ❤ by Kel").setConfig(KelUI.config, "CREDITS.CUSTOM"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.global"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.global.enable_custom_name"), false).setConfig(KelUI.config, "GLOBAL.ENABLE_CUSTOM_NAME"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.global.custom_name")).setValue("KelUI").setConfig(KelUI.config, "GLOBAL.CUSTOM_NAME"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.global.enable_custom_version_type"), false).setConfig(KelUI.config, "GLOBAL.ENABLE_CUSTOM_VERSION_TYPE"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.global.custom_version_type")).setValue("KelUI").setConfig(KelUI.config, "GLOBAL.CUSTOM_VERSION_TYPE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.global.enable_custom_version"), false).setConfig(KelUI.config, "GLOBAL.ENABLE_CUSTOM_VERSION"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.global.custom_version")).setValue(KelUI.MINECRAFT_LAUNCHED_VERSION).setConfig(KelUI.config, "GLOBAL.CUSTOM_VERSION"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.global.enable_custom_icon"), false).setConfig(KelUI.config, "GLOBAL.ENABLE_CUSTOM_ICON"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.global.enable_custom_icon.mod"), true).setConfig(KelUI.config, "GLOBAL.ENABLE_CUSTOM_ICON.MOD"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.global.custom_icon.path")).setValue("config/KelUI/icons/").setConfig(KelUI.config, "GLOBAL.CUSTOM_ICON_PATH"))
                        .addValue(new ButtonBuilder(Component.translatable("kelui.config.global.custom_icon.update"), (s) -> {
                            try {
                                KelUI.MINECRAFT.getWindow().setIcon(KelUI.MINECRAFT.getVanillaPackResources(), SharedConstants.getCurrentVersion().isStable() ? IconSet.RELEASE : IconSet.SNAPSHOT);
                            } catch (IOException e) {
                                KelUI.log(e.getLocalizedMessage(), Level.ERROR);
                            }
                        }))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.fix"))
                        //
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.fix.connect_screen"), true).setConfig(KelUI.config, "FIX.CONNECTION_SCREEN"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.fix.selection_list"), true).setConfig(KelUI.config, "FIX.RENDER_SELECTION"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.fix.disabled_blur"), true).setConfig(KelUI.config, "FIX.DISABLED_BLUR").build()
                                .setDescription(Component.translatable("kelui.fix.disabled_blur.description")))
                )

                .build();
    }
}

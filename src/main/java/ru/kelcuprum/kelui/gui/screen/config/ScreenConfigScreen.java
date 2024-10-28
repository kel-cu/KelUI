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
import ru.kelcuprum.alinlib.info.Player;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.screen.config.demo.PreviewLoadingOverlay;

import static ru.kelcuprum.alinlib.gui.Colors.SEADRIVE;

public class ScreenConfigScreen {
    public Screen build(Screen parent) {
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.SCREENS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new ScreenConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .addWidget(new TextBox(KelUI.TEXTS.TITLE.SCREENS_CONFIG, true))
                .addWidget(new CategoryBox(KelUI.TEXTS.TITLE.LOADING_CONFIG)
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new"), false).setConfig(KelUI.config, "LOADING.NEW"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.background_color")).setColor(0xFFB4B4B4).setConfig(KelUI.config, "LOADING.NEW.BACKGROUND_C0LOR"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_color")).setColor(0xFF000000).setConfig(KelUI.config, "LOADING.NEW.BORDER_C0LOR"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_background_color")).setColor(0xFFD9D9D9).setConfig(KelUI.config, "LOADING.NEW.BORDER_BACKGROUND_C0LOR"))
                        .addValue(new ButtonBuilder(Component.translatable("kelui.config.loading.preview"), (OnPress) -> Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(2000, () -> {}))))
                )
                .addWidget(new CategoryBox(KelUI.TEXTS.TITLE.OPTIONS_CONFIG)
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.custom_skin"), true).setConfig(KelUI.config, "CUSTOM_SKIN"))
                )
                .addWidget(new TextBox(Component.translatable("kelui.config.title.death")))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.death"), true).setConfig(KelUI.config, "DEATH"))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.death.niko"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.death.niko", Player.getName()), false).setConfig(KelUI.config, "DEATH.NIKO"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.death.niko.only_hardcore"), true).setConfig(KelUI.config, "DEATH.NIKO.ONLY_HARDCORE"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.death.niko.function")).setValue(0).setList(new String[]{Component.translatable("deathScreen.spectate").getString(), Component.translatable("deathScreen.titleScreen").getString(), "Crash"}).setConfig(KelUI.config, "DEATH.NIKO.FUNCTION"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.render"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.map_slot"), true).setConfig(KelUI.config, "HUD.MAP_SLOT"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.smooth_menu"), false).setConfig(KelUI.config, "UI.SMOOTH_MENU"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.ui.tooltip_type")).setValue(0).setList(new String[]{"Vanilla", "KelUI", "kelcuprum.ru [blockquote]", "KelUI [Textured]"}).setConfig(KelUI.config, "UI.TOOLTIP_TYPE"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.alinlib_style"), false).setConfig(KelUI.config, "UI.ALINLIB_STYLE"))
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.chat"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.edgeless_screen"), true).setConfig(KelUI.config, "CHAT.EDGELESS_SCREEN"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.timestamp"), true).setConfig(KelUI.config, "CHAT.TIMESTAMP"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.pattern")).setValue("HH:mm").setConfig(KelUI.config, "CHAT.TIMESTAMP.PATTERN"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.color")).setColor(SEADRIVE).setConfig(KelUI.config, "CHAT.TIMESTAMP.COLOR"))
                )
                .build();
    }
}

package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.editbox.EditBoxBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.HorizontalRuleBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.alinlib.info.Player;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.Util;
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

                .setCategoryTitle(KelUI.TEXTS.TITLE.SCREENS_CONFIG)
                .addWidget(new HorizontalRuleBuilder(KelUI.TEXTS.TITLE.LOADING_CONFIG))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.new"), false).setConfig(KelUI.config, "LOADING_OVERLAY.NEW"))
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.loading.type"), selectorButton -> KelUI.config.setString("LOADING_OVERLAY.TYPE", Util.getOverlayByName(selectorButton.getList()[selectorButton.getPosition()]).id))
                        .setList(Util.getOverlaysName())
                        .setValue(Util.getPositionOnOverlaysID(Util.getSelectedOverlay().title.getString())))
                .addWidget(new ButtonBuilder(Component.translatable("kelui.config.loading.preview"), (OnPress) -> Minecraft.getInstance().setOverlay(new PreviewLoadingOverlay(2000, () -> {
                }))))
                .addWidget(new CategoryBox(Component.translatable("kelui.loading.default"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.background_color")).setColor(0xFFB4B4B4).setConfig(KelUI.config, "LOADING_OVERLAY.TYPE.DEFAULT.BACKGROUND_C0LOR"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_color")).setColor(0xFF000000).setConfig(KelUI.config, "LOADING_OVERLAY.TYPE.DEFAULT.BORDER_C0LOR"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.loading.type.default.border"), true).setConfig(KelUI.config, "LOADING_OVERLAY.TYPE.DEFAULT.BORDER"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.new.border_background_color")).setColor(0xFFD9D9D9).setConfig(KelUI.config, "LOADING_OVERLAY.TYPE.DEFAULT.BORDER_BACKGROUND_C0LOR"))
                        .changeState(false)
                )
                .addWidget(new CategoryBox(Component.translatable("kelui.loading.lite"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.loading.type.lite.background_color")).setColor(0xFF000000).setConfig(KelUI.config, "LOADING_OVERLAY.TYPE.LITE.BACKGROUND_C0LOR"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.loading.type.lite.icon")).setList(new String[]{
                                Component.translatable("kelui.config.loading.type.lite.icon.creeper").getString(),
                                Component.translatable("kelui.config.loading.type.lite.icon.tree").getString(),
                                Component.translatable("kelui.config.loading.type.lite.icon.lamp").getString(),
                                Component.translatable("kelui.config.loading.type.lite.icon.pepe").getString()
                        }).setValue(0).setConfig(KelUI.config, "LOADING.WHITE.ICON"))
                        .changeState(false)
                )
                .addWidget(new HorizontalRuleBuilder(Component.translatable("kelui.config.title.death")))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.death"), true).setConfig(KelUI.config, "DEATH"))
                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.death.niko"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.death.niko", Player.getName()), false).setConfig(KelUI.config, "DEATH.NIKO"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.death.niko.only_hardcore"), true).setConfig(KelUI.config, "DEATH.NIKO.ONLY_HARDCORE"))
                        .addValue(new SelectorBuilder(Component.translatable("kelui.config.death.niko.function")).setValue(0).setList(new String[]{Component.translatable("deathScreen.spectate").getString(), Component.translatable("deathScreen.titleScreen").getString(), "Crash"}).setConfig(KelUI.config, "DEATH.NIKO.FUNCTION"))
                        .changeState(false)
                )
                .addWidget(new HorizontalRuleBuilder(Component.translatable("kelui.config.title.other.render")))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.custom_skin"), true).setConfig(KelUI.config, "CUSTOM_SKIN"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.hud.map_slot"), true).setConfig(KelUI.config, "HUD.MAP_SLOT"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.smooth_menu"), false).setConfig(KelUI.config, "UI.SMOOTH_MENU"))
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.ui.tooltip_type")).setValue(0).setList(new String[]{"Vanilla", "KelUI", "kelcuprum.ru [blockquote]", "KelUI [Textured]"}).setConfig(KelUI.config, "UI.TOOLTIP_TYPE"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.ui.alinlib_style"), false).setConfig(KelUI.config, "UI.ALINLIB_STYLE"))

                .addWidget(new CategoryBox(Component.translatable("kelui.config.title.other.chat"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.edgeless_screen"), true).setConfig(KelUI.config, "CHAT.EDGELESS_SCREEN"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("kelui.config.chat.timestamp"), true).setConfig(KelUI.config, "CHAT.TIMESTAMP"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.pattern")).setValue("HH:mm").setConfig(KelUI.config, "CHAT.TIMESTAMP.PATTERN"))
                        .addValue(new EditBoxBuilder(Component.translatable("kelui.config.chat.timestamp.color")).setColor(SEADRIVE).setConfig(KelUI.config, "CHAT.TIMESTAMP.COLOR"))
                        .changeState(false)
                )
                .build();
    }
}

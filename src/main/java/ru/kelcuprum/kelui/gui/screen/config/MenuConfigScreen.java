package ru.kelcuprum.kelui.gui.screen.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.KelUI;

public class MenuConfigScreen {

    public static String[] types = {
            "KelUI",
            "KelUI V2",
            "OneShot"
    };

    public Screen build(Screen parent) {
        String[] realmsTypes = {
                "Skin",
                "Sounds"
        };
        return new ConfigScreenBuilder(parent, KelUI.TEXTS.NAME)
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.MENU_CONFIG, (s) -> Minecraft.getInstance().setScreen(new MenuConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.PAUSE_CONFIG, (s) -> Minecraft.getInstance().setScreen(new PauseConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.SCREENS_CONFIG, (s) -> Minecraft.getInstance().setScreen(new ScreenConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.HUD_CONFIG, (s) -> Minecraft.getInstance().setScreen(new HUDConfigScreen().build(parent))))
                .addPanelWidget(new ButtonBuilder(KelUI.TEXTS.TITLE.OTHER_CONFIG, (s) -> Minecraft.getInstance().setScreen(new OtherConfigScreen().build(parent))))

                .setCategoryTitle(KelUI.TEXTS.TITLE.MENU_CONFIG)
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu"), true).setConfig(KelUI.config, "MAIN_MENU"))
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.main_menu.type")).setValue(0).setList(types).setConfig(KelUI.config, "MAIN_MENU.TYPE"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.enable_realms"), false).setConfig(KelUI.config, "MAIN_MENU.ENABLE_REALMS"))
                .addWidget(new SelectorBuilder(Component.translatable("kelui.config.main_menu.realms_small_button")).setList(realmsTypes).setValue(0).setConfig(KelUI.config, "MAIN_MENU.REALMS_SMALL_BUTTON"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.player"), true).setConfig(KelUI.config, "MAIN_MENU.PLAYER"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.info"), true).setConfig(KelUI.config, "MAIN_MENU.INFO"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.version"), true).setConfig(KelUI.config, "MAIN_MENU.VERSION"))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("kelui.config.main_menu.credits"), true).setConfig(KelUI.config, "MAIN_MENU.CREDITS"))
                .build();
    }
}

package ru.kelcuprum.kelui.gui.components.comp;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.kelui.gui.components.OneShotButton;
import ru.kelcuprum.kelui.gui.components.OneShotPauseButton;
import ru.kelcuprum.kelui.gui.components.OneShotTitleButton;

public class ModMenuButtons {
    public static Screen getModScreen(){
        return com.terraformersmc.modmenu.api.ModMenuApi.createModsScreen(AlinLib.MINECRAFT.screen);
    }
    public static Component getModText(){
        Component text = com.terraformersmc.modmenu.api.ModMenuApi.createModsButtonText();
        return FabricLoader.getInstance().isModLoaded("catalogue") ? Component.translatable("catalogue.gui.mod_list") : text;
    }

    public static ButtonBuilder getModMenuButton(){
        return new ButtonBuilder(getModText())
                .setOnPress((s) -> AlinLib.MINECRAFT.setScreen(getModScreen()));
    }

    public static OneShotButton getModMenuOneShotButton(int x, int y, int width, int height, OneShotButton.OnPress onPress){
        return new OneShotButton(x, y, width, height, getModText(), onPress);
    }

    public static OneShotPauseButton getModMenuOneShotButtonPause(int x, int y, int width, int height, OneShotPauseButton.OnPress onPress){
        return new OneShotPauseButton(x, y, width, height, getModText(), onPress);
    }
    public static OneShotTitleButton getModMenuOneShotButtonTitle(int x, int y, int width, int height, OneShotTitleButton.OnPress onPress){
        return new OneShotTitleButton(x, y, width, height, getModText(), onPress);
    }
}

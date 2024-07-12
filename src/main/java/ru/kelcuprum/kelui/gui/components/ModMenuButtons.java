package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;

public class ModMenuButtons {
    public static Screen getModScreen(){
        return com.terraformersmc.modmenu.api.ModMenuApi.createModsScreen(AlinLib.MINECRAFT.screen);
    }
    public static Component getModText(){
        return com.terraformersmc.modmenu.api.ModMenuApi.createModsButtonText();
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

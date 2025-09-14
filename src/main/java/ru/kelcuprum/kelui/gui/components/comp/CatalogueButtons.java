package ru.kelcuprum.kelui.gui.components.comp;

import com.mrcrayfish.catalogue.client.screen.CatalogueModListScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.kelui.gui.components.oneshot.OneShotButton;
import ru.kelcuprum.kelui.gui.components.oneshot.OneShotPauseButton;
import ru.kelcuprum.kelui.gui.components.oneshot.OneShotTitleButton;

public class CatalogueButtons {
    public static Screen getModScreen(){
        return new CatalogueModListScreen(AlinLib.MINECRAFT.screen);
    }
    public static ButtonBuilder getModMenuButton(){
        return new ButtonBuilder(Component.translatable("catalogue.gui.mod_list"))
                .setOnPress((s) -> AlinLib.MINECRAFT.setScreen(getModScreen()));
    }

    public static OneShotButton getModMenuOneShotButton(int x, int y, int width, int height, OneShotButton.OnPress onPress){
        return new OneShotButton(x, y, width, height, Component.translatable("catalogue.gui.mod_list"), onPress);
    }

    public static OneShotPauseButton getModMenuOneShotButtonPause(int x, int y, int width, int height, OneShotPauseButton.OnPress onPress){
        return new OneShotPauseButton(x, y, width, height, Component.translatable("catalogue.gui.mod_list"), onPress);
    }
    public static OneShotTitleButton getModMenuOneShotButtonTitle(int x, int y, int width, int height, OneShotTitleButton.OnPress onPress){
        return new OneShotTitleButton(x, y, width, height, Component.translatable("catalogue.gui.mod_list"), onPress);
    }
}

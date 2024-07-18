package ru.kelcuprum.kelui.gui.components.comp;

import com.mineblock11.skinshuffle.client.gui.GeneratedScreens;
import net.minecraft.client.gui.screens.Screen;
import ru.kelcuprum.alinlib.AlinLib;

public class SSButtons {
    public static Screen getScreen(){
        return GeneratedScreens.getCarouselScreen(AlinLib.MINECRAFT.screen);
    }
}

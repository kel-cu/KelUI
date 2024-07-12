package ru.kelcuprum.kelui.gui.components;

import com.mineblock11.skinshuffle.client.gui.CarouselScreen;
import com.mineblock11.skinshuffle.client.gui.GeneratedScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;

import static ru.kelcuprum.kelui.KelUI.ICONS.CAPES;

public class SSButtons {
    public static Screen getScreen(){
        return GeneratedScreens.getCarouselScreen(AlinLib.MINECRAFT.screen);
    }
}

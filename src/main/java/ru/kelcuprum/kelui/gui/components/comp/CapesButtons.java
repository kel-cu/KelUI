package ru.kelcuprum.kelui.gui.components.comp;

import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;

import static ru.kelcuprum.kelui.KelUI.ICONS.CAPES;

public class CapesButtons {
    public static ButtonBuilder getCapesButton(){
        return new ButtonBuilder(Component.translatable("options.capes.title")).setIcon(CAPES).setOnPress((s) -> AlinLib.MINECRAFT.setScreen(new me.cael.capes.menu.SelectorMenu(AlinLib.MINECRAFT.screen, AlinLib.MINECRAFT.options)));
    }
}

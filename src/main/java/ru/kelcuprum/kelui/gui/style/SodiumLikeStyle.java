package ru.kelcuprum.kelui.gui.style;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.styles.AbstractStyle;

public class SodiumLikeStyle extends AbstractStyle {
    public SodiumLikeStyle() {
        super("sodium", Component.literal("Sodium (KelUI)"));
    }

    @Override
    public void renderBackground$widget(GuiGraphics guiGraphics, int x, int y, int width, int height, boolean active, boolean isHoveredOrFocused) {
        float state = !active ? 3.0F : (isHoveredOrFocused ? 2.0F : 1.0F);
        float f = state / 2.0F * 0.9F + 0.1F;
        int background = (int)(255.0F * f);
        guiGraphics.fill(x, y, x+width, y+height-1, background / 2 << 24);
        if(isHoveredOrFocused) guiGraphics.fill(x, y+height-1, x+width, y+height, Colors.SODIUM);
    }

    @Override
    public void renderBackground$slider(GuiGraphics guiGraphics, int x, int y, int width, int height, boolean active, boolean isHoveredOrFocused, double position) {
        this.renderBackground$widget(guiGraphics, x, y, width, height, active, isHoveredOrFocused);
        if(isHoveredOrFocused){
            int xS = x + (int)(position * (double)(width - 4));
            int yS = y+(height - 8) / 2;
            guiGraphics.fill(xS, yS, xS+4, yS+ AlinLib.MINECRAFT.font.lineHeight, Colors.CLOWNFISH);
        }
    }
}

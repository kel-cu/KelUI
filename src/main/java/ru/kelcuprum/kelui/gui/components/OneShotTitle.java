package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;

public class OneShotTitle extends AbstractWidget {
    public OneShotTitle(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.active = false;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2.5F, 2.5F, 2.5F);
        guiGraphics.drawString(AlinLib.MINECRAFT.font, getMessage(), (int) (getX()/2.5F), (int) (getY()/2.5F), -1);
        guiGraphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

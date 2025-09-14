package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

public class PlayerHeadWidget extends AbstractWidget {
    public PlayerHeadWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        PlayerFaceRenderer.draw(guiGraphics, KelUI.MINECRAFT.getSkinManager().getInsecureSkin(KelUI.MINECRAFT.getGameProfile()), getX(), getY(), getHeight());
        if(isHovered()){
            guiGraphics.setTooltipForNextFrame(KelUI.MINECRAFT.font, Component.literal(KelUI.MINECRAFT.getUser().getName()), i, j);
        }
    }

    @Override
    public void onClick(double d, double e) {
        AlinLib.MINECRAFT.setScreen(KelUI.getSkinCustom(AlinLib.MINECRAFT.screen));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

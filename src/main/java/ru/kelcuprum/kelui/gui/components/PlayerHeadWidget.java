package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class PlayerHeadWidget extends AbstractWidget {
    public PlayerHeadWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.active = false;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        PlayerFaceRenderer.draw(guiGraphics, Minecraft.getInstance().getSkinManager().getInsecureSkin(Minecraft.getInstance().getGameProfile()), getX(), getY(), getHeight());
        if(isHovered()){
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(Minecraft.getInstance().getUser().getName()), i, j);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

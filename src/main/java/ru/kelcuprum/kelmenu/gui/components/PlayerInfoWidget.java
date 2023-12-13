package ru.kelcuprum.kelmenu.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class PlayerInfoWidget extends AbstractWidget {
    public final Boolean enableMessage;
    public final String name;
    public PlayerInfoWidget(int x, int y, int width, int height, String name, Boolean enableMessage, Component component) {
        super(x, y, width, height, component);
        this.enableMessage = enableMessage;
        this.name = name;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        PlayerFaceRenderer.draw(guiGraphics, Minecraft.getInstance().getSkinManager().getInsecureSkin(Minecraft.getInstance().getGameProfile()), getX(), getY(), height);
        if(enableMessage){
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), getX()+height+(getHeight() - 8) / 2, getY(), 0xffffff);
            guiGraphics.drawString(Minecraft.getInstance().font, this.name, getX()+height+(getHeight() - 8) / 2, getY()+height-Minecraft.getInstance().font.lineHeight, 0xffffff);
        }
        else guiGraphics.drawString(Minecraft.getInstance().font, this.name, getX()+height+(getHeight() - 8) / 2, getY()+(getHeight() - 8) / 2, 0xffffff);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.cicada.DummyClientPlayerEntity;
import ru.kelcuprum.kelui.gui.cicada.GuiEntityRenderer;

public class PlayerButton extends AbstractWidget {
    int size;
    public PlayerButton(int x, int y, int size) {
        super(x, y, size, size*2, Component.empty());
        this.size = size;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(KelUI.playerSkin != null) {
            KelUI.playerSkin = AlinLib.MINECRAFT.getSkinManager().getInsecureSkin(AlinLib.MINECRAFT.getGameProfile());
            int scale = size/45;
            float followX = (float) (this.getX() + (this.getWidth() / 2)) - mouseX;
            float followY = (float) (((float) (this.getY() + (this.height/2.5)) - mouseY)-(7.5*scale*AlinLib.MINECRAFT.options.guiScale().get()));
            float rotation = 0;

            guiGraphics.pose().pushPose();
            DummyClientPlayerEntity entity = new DummyClientPlayerEntity(null, KelUI.SillyUUID, KelUI.playerSkin);
            GuiEntityRenderer.drawEntity(
                    guiGraphics.pose(), this.getX() + (this.getWidth() / 2), this.getY()+this.height,
                    size, rotation, followX, followY, entity
            );
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.cicada.DummyClientPlayerEntity;
import ru.kelcuprum.kelui.gui.cicada.GuiEntityRenderer;

public class SkinButton extends Button {
    int size;
    public SkinButton(int x, int y, int width, int size) {
        super(new ButtonBuilder().setTitle("It's YOU!").setWidth(width).setPosition(x, y));
        this.size = size;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
        if(KelUI.playerSkin != null) {
            KelUI.playerSkin = AlinLib.MINECRAFT.getSkinManager().getInsecureSkin(AlinLib.MINECRAFT.getGameProfile());
            int scale = size/45;
            float followX = (float) (this.getX() + (this.getWidth() / 2)) - mouseX;
            float followY = (float) ((float) (this.getY() - (size*2)) - mouseY+(4.5*scale*AlinLib.MINECRAFT.options.guiScale().get()));
            float rotation = 0;

            guiGraphics.pose().pushPose();
            DummyClientPlayerEntity entity = new DummyClientPlayerEntity(null, KelUI.SillyUUID, KelUI.playerSkin);
            GuiEntityRenderer.drawEntity(
                    guiGraphics.pose(), this.getX() + (this.getWidth() / 2), this.getY() - 12,
                    size, rotation, followX, followY, entity
            );
            guiGraphics.pose().popPose();
        }
    }
}

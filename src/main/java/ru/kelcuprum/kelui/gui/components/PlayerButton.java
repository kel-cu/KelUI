package ru.kelcuprum.kelui.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.cicada.DummyClientPlayerEntity;
import ru.kelcuprum.kelui.gui.cicada.GuiEntityRenderer;

import static com.mojang.blaze3d.Blaze3D.getTime;

public class PlayerButton extends AbstractWidget {
    int size;
    boolean showItem;
    boolean rotate;
    boolean autoRotate;
    boolean followMouse;
    private final double currentTime;
    public PlayerButton(int x, int y, int size) {
        this(x, y, size, false);
    }

    public PlayerButton(int x, int y, int size, boolean showItem) {
        this(x, y, size, showItem, false);
    }
    public PlayerButton(int x, int y, int size, boolean showItem, boolean rotate) {
        this(x, y, size, showItem, rotate, true);
    }
    public PlayerButton(int x, int y, int size, boolean showItem, boolean rotate, boolean autoRotate) {
        this(x, y, size, showItem, rotate, autoRotate, true);
    }
    public PlayerButton(int x, int y, int size, boolean showItem, boolean rotate, boolean autoRotate, boolean followMouse) {
        super(x, y, size, size*2, Component.empty());
        this.size = size;
        this.rotate = rotate;
        this.showItem = showItem;
        this.currentTime = getTime();
        this.autoRotate = autoRotate;
        this.followMouse = followMouse;
    }
    float rotation = 0;
    public float getRotation() {
        if(autoRotate) rotation = (float) ((getTime() - currentTime) * 30.0f);
        return rotation;
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(KelUI.playerSkin != null) {
            KelUI.playerSkin = AlinLib.MINECRAFT.getSkinManager().getInsecureSkin(AlinLib.MINECRAFT.getGameProfile());
            int scale = size/45;
            float followX = (float) (this.getX() + (this.getWidth() / 2)) - mouseX;
            float followY = (float) (((float) (this.getY() + (this.height/2.5)) - mouseY)-(7.5*scale*AlinLib.MINECRAFT.options.guiScale().get()));
            float rotation = 0;
            if(!followMouse) followX = followY = 0;
            if(rotate){
                followX = followY = 0;
                rotation = getRotation();
            }

            guiGraphics.pose().pushPose();
            DummyClientPlayerEntity entity = new DummyClientPlayerEntity(null, KelUI.SillyUUID, KelUI.playerSkin, AlinLib.MINECRAFT.options, showItem);
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

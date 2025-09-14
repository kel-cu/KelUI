package ru.kelcuprum.kelui.gui.components.omori;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import ru.kelcuprum.alinlib.gui.GuiUtils;

public class OMORIButton extends AbstractButton {
    protected OnPress onPress;
    protected boolean isExit;
    protected boolean isInit = false;
    public OMORIButton(int i, int j, int k, int l, Component component, OnPress onPress) {
        this(i, j, k, l, component, false, onPress);
    }
    public OMORIButton(int i, int j, int k, int l, Component component, boolean isExit, OnPress onPress) {
        super(i, j, k, l, component);
        this.onPress = onPress;
        this.isExit = isExit;
    }

    public boolean isActive = true;

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        int k = this.isActive ? -1 : 0x7fFFFFFF;
        if(isExit){
            guiGraphics.fill(getX(), getY(), getRight(), getBottom(), 0xFFFFFFFF);
            guiGraphics.renderOutline(getX(), getY(), width, height, 0xFF000000);
            guiGraphics.fill(getX()+4, getY()+4, getRight()-4, getBottom()-4, 0xFF000000);
        }
        renderString(guiGraphics, Minecraft.getInstance().font, k);
        if(!isInit) isInit = true;
    }
    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int i){
        guiGraphics.drawCenteredString(font, this.getMessage(),this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() - 8) / 2, i);
        if(isFocused()) {
            long time = System.currentTimeMillis() % 1000;
            double d = (double) (time % 500) / 500;
            if(time > 500) d = 1 - d;
            int xw = (int) (5*d);
            int xh = isExit ? this.getX()-35 : this.getX()+(width/2)-(font.width(getMessage())/2)-35;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GuiUtils.getResourceLocation("kelui", "textures/gui/omori/right.png"), xh-xw, this.getY() + (this.getHeight() / 2) - 7, 0,0, 29, 14, 29,14);
        }
    }

    public boolean keyPressed(int i, int j, int k) {
        if (this.active && this.visible) {
            if (CommonInputs.selected(i)) {
                if(isActive) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_HAT.value(), 1.25F, 0.3F));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_HAT.value(), 1.5F, 0.3F));
                    this.onPress();
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 1.0F, 0.3F));
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 1.5F, 0.3F));
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onClick(double d, double e) {

    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return false;
    }

    @Override
    public boolean isHovered() {
        return false;
    }

    @Override
    public void setFocused(boolean bl) {
        super.setFocused(bl);
        if(bl && isInit) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BIT.value(), 1.0F, 0.3F));
        }
    }

    @Override
    public void onPress() {
        if(onPress != null && isActive) onPress.onPress(this);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public interface OnPress {
        void onPress(OMORIButton button);
    }

}

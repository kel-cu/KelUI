package ru.kelcuprum.kelui.gui.components;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.kelui.gui.TexturesHelper;

import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotButton extends Button {
    protected File screenshot;
    public ScreenshotButton(int x, int y, int width, File screenshot) {
        super(new ButtonBuilder(Component.empty()).setOnPress((s) -> Util.getPlatform().openFile(screenshot)).setPosition(x, y).setSize(width, 40));
        this.screenshot = screenshot;
    }
    public int imageWidth = 0;
    @Override
    public void renderText(GuiGraphics guiGraphics, int i, int j, float f) {
        if(getY() > guiGraphics.guiHeight()) return;
        ResourceLocation rl = TexturesHelper.getTexture(screenshot);
        if(!TexturesHelper.urlsImages.containsKey(screenshot)) return;
        BufferedImage huy = TexturesHelper.urlsImages.get(screenshot);
        int scale = huy.getHeight()/(getHeight()-6);
        int imageWidth = huy.getWidth()/scale;
        guiGraphics.blit(rl, this.getX()+3, this.getY()+3, 0.0F, 0.0F, imageWidth, this.getHeight()-6, imageWidth, this.getHeight()-6);
        renderString(guiGraphics, screenshot.getName(), getX()+9+imageWidth, getY() + 8);
        renderString(guiGraphics, screenshot.toPath().getParent().toFile().getName(), getX()+9+imageWidth, getY() + height - 8 - AlinLib.MINECRAFT.font.lineHeight);
    }

    protected void renderScrollingString(GuiGraphics guiGraphics, Font font, Component message, int x, int y, int color) {
        int k = this.getX() + imageWidth + x;
        int l = this.getX() + this.getWidth() - x;
        renderScrollingString(guiGraphics, font, message, k, y, l, y+font.lineHeight, color);
    }

    protected void renderString(GuiGraphics guiGraphics, Component text, int x, int y) {
        renderString(guiGraphics, text.getString(), x, y);
    }

    protected void renderString(GuiGraphics guiGraphics, String text, int x, int y) {
        if (getWidth() - 50 < AlinLib.MINECRAFT.font.width(text)) {
            renderScrollingString(guiGraphics, AlinLib.MINECRAFT.font, Component.literal(text), 5, y-1, -1);
        } else {
            guiGraphics.drawString(AlinLib.MINECRAFT.font, text, x, y, -1);
        }
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}

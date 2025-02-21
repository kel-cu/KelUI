package ru.kelcuprum.kelui.gui.loading;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public abstract class AbstractLoadingOverlay {
    public final String id;
    public final Component title;
    public AbstractLoadingOverlay(String id, Component title){
        this.id = id;
        this.title = title;
    }
    static int replaceAlpha(int i, int j) {
        return i & 16777215 | j << 24;
    }
    public abstract void drawProgressBar(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, float currentProgress);
    public abstract void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, float f, float currentProgress, int k, int kB);
    public abstract void registerTextures();
}

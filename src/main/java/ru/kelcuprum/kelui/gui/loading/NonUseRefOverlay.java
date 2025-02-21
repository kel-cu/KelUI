package ru.kelcuprum.kelui.gui.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;


import static ru.kelcuprum.kelui.KelUI.ICONS.LOADING_ICON;
/*
 * inabakumori - NON-USE reference
 * YouTube: https://www.youtube.com/watch?v=-5T-L0b43no
 */
public class NonUseRefOverlay extends AbstractLoadingOverlay{
    Minecraft minecraft = AlinLib.MINECRAFT;

    public NonUseRefOverlay() {
        super("non_use_ref", Component.translatable("kelui.loading.default"));
    }

    @Override
    public void drawProgressBar(GuiGraphics guiGraphics, int x, int y, int x1, int y2, float f, float currentProgress) {
        int kB = 255;
        if (f < 1.0F) {
            y2 += 5;
            guiGraphics.fill(x, y, x1, y2, replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BORDER_C0LOR", 0xFF000000).intValue(), kB));
            guiGraphics.fill(x + 2, y + 2, (x1 - 2), y2 - 2, replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BORDER_BACKGROUND_C0LOR", 0xFFD9D9D9).intValue(), kB));
            guiGraphics.fill(x + 3, y + 3, (int) (x + (((x1 - 3 - x) * currentProgress))), y2 - 3, replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BORDER_C0LOR", 0xFF000000).intValue(), kB));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, float f, float currentProgress, int k, int kB) {
        if (minecraft.level == null) guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BACKGROUND_C0LOR", 0xFFB4B4B4).intValue(), k));
        if(KelUI.config.getBoolean("LOADING_OVERLAY.TYPE.DEFAULT.BORDER", true)) {
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), 30, replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BORDER_C0LOR", 0xFF000000).intValue(), kB));
            guiGraphics.fill(RenderType.guiOverlay(), 0, guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight() - 30, replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BORDER_C0LOR", 0xFF000000).intValue(), kB));
        }
        // Icon
        if (minecraft.level == null) {
            guiGraphics.blit(RenderType::guiTextured, LOADING_ICON, guiGraphics.guiWidth() / 2 - 50, guiGraphics.guiHeight() / 2 - 50, 0, 0, 100, 100, 100, 100);
        }
        // Progress bar
        if (f < 1.0F) {
            int px = (guiGraphics.guiWidth() / 2) - 100;
            int py = guiGraphics.guiHeight() - 57;
            this.drawProgressBar(guiGraphics, px, py, px+200, py+10, f, currentProgress);
        }
    }

    @Override
    public void registerTextures() {

    }
}

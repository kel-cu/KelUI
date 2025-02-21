package ru.kelcuprum.kelui.gui.loading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

import static ru.kelcuprum.alinlib.gui.Colors.CPM_BLUE;
import static ru.kelcuprum.alinlib.gui.Colors.GROUPIE;
import static ru.kelcuprum.kelui.KelUI.ICONS.LOADING_ICON;
public class SafeOverlay extends AbstractLoadingOverlay{
    Minecraft minecraft = AlinLib.MINECRAFT;

    public SafeOverlay() {
        super("safe", Component.literal("safe"));
    }

    @Override
    public void drawProgressBar(GuiGraphics guiGraphics, int x, int y, int x1, int y2, float f, float currentProgress) {
        int kB = 255;
        if (f < 1.0F) {
            y2 += 5;
            guiGraphics.fill(x, y, x1, y2, replaceAlpha(0xFFFFFF, kB));
            guiGraphics.fill(x + 2, y + 2, (x1 - 2), y2 - 2, replaceAlpha(GROUPIE, kB));
            guiGraphics.fill(x + 3, y + 3, (int) (x + (((x1 - 3 - x) * currentProgress))), y2 - 3, replaceAlpha(0xFFFFFF, kB));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, float f, float currentProgress, int k, int kB) {
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), replaceAlpha(GROUPIE, k));
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

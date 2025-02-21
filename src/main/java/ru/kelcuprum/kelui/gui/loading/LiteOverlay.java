package ru.kelcuprum.kelui.gui.loading;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.kelui.KelUI;

public class LiteOverlay extends AbstractLoadingOverlay{
    public LiteOverlay() {
        super("lite", Component.translatable("kelui.loading.lite"));
    }

    @Override
    public void drawProgressBar(GuiGraphics guiGraphics, int x, int y, int x1, int y2, float f, float currentProgress) {
        int kB = 255;
        if (f < 1.0F) {
            y2 += 5;
            guiGraphics.fill(x, y, (int) (x+((x1-x) * currentProgress)), y2 - 3, replaceAlpha(0xFFFFFFFF, kB));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, float f, float currentProgress, int k, int kB) {
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), replaceAlpha(KelUI.config.getNumber("LOADING_OVERLAY.TYPE.LITE.BACKGROUND_C0LOR", 0xFF000000).intValue(), k));
        int i = KelUI.config.getNumber("LOADING.WHITE.ICON", 0).intValue();
        int x = (guiGraphics.guiWidth()/2) - 48;
        int y = (guiGraphics.guiHeight()/2) - 48;
        guiGraphics.blit(RenderType::guiTexturedOverlay, GuiUtils.getResourceLocation("kelui", String.format("textures/gui/loading/%s.png",
                i == 3 ? "pepe" : i == 2 ? "lamp" : i == 1 ? "tree" : "creeper")), x, y, 0,0 , 96, 96, 96, 96);
        // Progress bar
        if (f < 1.0F) {
            int py = guiGraphics.guiHeight() - 45;
            this.drawProgressBar(guiGraphics, 45, py, guiGraphics.guiWidth()-45, py+1, f, currentProgress);
        }
    }

    @Override
    public void registerTextures() {

    }
}

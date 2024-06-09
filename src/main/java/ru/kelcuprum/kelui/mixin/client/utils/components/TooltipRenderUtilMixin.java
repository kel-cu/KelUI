package ru.kelcuprum.kelui.mixin.client.utils.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin {
    @Inject(method = "renderTooltipBackground", at=@At("HEAD"), cancellable = true)
    private static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, CallbackInfo ci){
        if(KelUI.config.getNumber("UI.TOOLTIP_TYPE", 0).intValue() == 0) return;
        if(KelUI.config.getNumber("UI.TOOLTIP_TYPE", 0).intValue() == 1) {
            guiGraphics.fill(x-3, y-3, x+width+3, y+height+3, z, 0xCC000000);
            // ---------
            guiGraphics.fill(x-3, y-3, x+width+3, y-2, z, 0x7fFFFFFF);
            guiGraphics.fill(x-3, y+height+2, x+width+3, y+height+3, z, 0x7fFFFFFF);
            // |
            guiGraphics.fill(x-3, y-2, x-2, y+height+2, z, 0x7fFFFFFF);
            guiGraphics.fill(x+width+2, y-2, x+width+3, y+height+2, z, 0x7fFFFFFF);
        }
        else guiGraphics.blitSprite(new ResourceLocation("kelui", "tooltip/tooltip_background"), x-5, y-5, z, width+10, height+10);
        ci.cancel();
    }
}

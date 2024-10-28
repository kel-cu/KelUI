package ru.kelcuprum.kelui.mixin.client.utils.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

@Mixin(TooltipRenderUtil.class)
public class TooltipRenderUtilMixin {
    @Inject(method = "renderTooltipBackground", at=@At("HEAD"), cancellable = true)
    private static void renderTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, ResourceLocation location, CallbackInfo ci){
        if(KelUI.config.getNumber("UI.TOOLTIP_TYPE", 0).intValue() == 0) return;
        if(KelUI.config.getNumber("UI.TOOLTIP_TYPE", 0).intValue() == 1) {
            guiGraphics.fill(x-3, y-3, x+width+3, y+height+3, z, 0xCC000000);
            // ---------
            guiGraphics.fill(x-3, y-3, x+width+3, y-2, z, 0x7fFFFFFF);
            guiGraphics.fill(x-3, y+height+2, x+width+3, y+height+3, z, 0x7fFFFFFF);
            // |
            guiGraphics.fill(x-3, y-2, x-2, y+height+2, z, 0x7fFFFFFF);
            guiGraphics.fill(x+width+2, y-2, x+width+3, y+height+2, z, 0x7fFFFFFF);
        } else if(KelUI.config.getNumber("UI.TOOLTIP_TYPE", 0).intValue() == 2) {
            guiGraphics.fill(x-5, y-5, x+width+5, y+height+5, z, 0xF0171917);
            // ---------
            guiGraphics.fill(x-7, y-5, x-5, y+height+5, z, 0xFF7f916f);
        } else {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, (float)z);
            guiGraphics.blitSprite(RenderType::guiTextured, ResourceLocation.fromNamespaceAndPath("kelui", "tooltip/tooltip_background"), x-5, y-5, width+10, height+10);
            guiGraphics.pose().popPose();
        }
        ci.cancel();
    }
}

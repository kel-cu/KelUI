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
        if(!KelUI.config.getBoolean("UI.TEXTURED_TOOLTIP", false)) return;
        guiGraphics.blitSprite(new ResourceLocation("kelui", "tooltip/tooltip_background"), x-5, y-5, z, width+10, height+10);
        ci.cancel();
    }
}

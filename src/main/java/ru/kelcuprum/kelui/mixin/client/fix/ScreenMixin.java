package ru.kelcuprum.kelui.mixin.client.fix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    protected Minecraft minecraft;

    @Shadow
    public abstract void renderTransparentBackground(GuiGraphics guiGraphics);

    @Shadow
    protected abstract void renderPanorama(GuiGraphics guiGraphics, float partialTick);

    @Shadow
    protected abstract void renderMenuBackground(GuiGraphics partialTick);


    @Shadow protected abstract void renderBlurredBackground(GuiGraphics guiGraphics);

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("FIX.DISABLED_BLUR", true)) return;
        if (this.minecraft.level == null) {
            renderPanorama(guiGraphics, partialTick);
        }
        if (this.minecraft.options.getMenuBackgroundBlurriness() != 0) {
            this.renderBlurredBackground(guiGraphics);
            this.renderMenuBackground(guiGraphics);
        } else this.renderTransparentBackground(guiGraphics);
        ci.cancel();
    }
}

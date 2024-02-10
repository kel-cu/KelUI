package ru.kelcuprum.kelui.mixin.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {

    /** Changes the background color */
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;replaceAlpha(II)I"
            )
    )
    private void background(Args args) {
        if(!KelUI.config.getBoolean("LOADING", true)) return;
        args.set(0, KelUI.config.getNumber("LOADING.BACKGROUND", 0xff1b1b1b).intValue());
    }
    @Shadow
    private float currentProgress;

    @Inject(method = "drawProgressBar", at = @At("HEAD"), cancellable = true)
    private void drawProgressBar(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, CallbackInfo ci) {
        if(!KelUI.config.getBoolean("LOADING", true)) return;
        int m = Mth.ceil((float)(k - i - 2) * this.currentProgress);
        int o = KelUI.config.getNumber("LOADING.BAR_COLOR", 0xffff4f4f).intValue();
        int a = KelUI.config.getNumber("LOADING.BAR_COLOR.BORDER", 0xffffffff).intValue();
        guiGraphics.fill(i + 2, j + 2, i + m, l - 2, o);
        guiGraphics.fill(i + 1, j, k - 1, j + 1, a);
        guiGraphics.fill(i + 1, l, k - 1, l - 1, a);
        guiGraphics.fill(i, j, i + 1, l, a);
        guiGraphics.fill(k, j, k - 1, l, a);
        ci.cancel();
    }
}

package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @ModifyArgs(
            method = "renderLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
            )
    )
    private void darkDebugOverlayBack(Args args) {
        if(!KelUI.config.getBoolean("HUD.DARK_DEBUG_OVERLAY", true)) return;
        args.set(4, 0x7f000000);
    }
}

package ru.kelcuprum.kelui.mixin.client.screen.catalogue;

import com.mrcrayfish.catalogue.client.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.kelui.KelUI;

@Mixin(Config.class)
public class Catalogue$Config {
    @Inject(method = "isPauseMenuVisible", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void isPauseMenuVisible(CallbackInfoReturnable<Boolean> cir){
        if(KelUI.config.getBoolean("PAUSE_MENU", true)) cir.setReturnValue(false);
    }
    @Inject(method = "isTitleMenuVisible", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void isTitleMenuVisible(CallbackInfoReturnable<Boolean> cir){
        if(KelUI.config.getBoolean("MAIN_MENU", true)) cir.setReturnValue(false);
    }
}

package ru.kelcuprum.kelui.mixin.utils;

import com.mojang.blaze3d.platform.IconSet;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.kelcuprum.kelui.KelUI;

import java.io.InputStream;

@Mixin(IconSet.class)
public class IconsMixin {
    @Inject(method = "getFile", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void returnBetterFile(PackResources resourcePack, String fileName, CallbackInfoReturnable<IoSupplier<InputStream>> cir) {
        if(!KelUI.config.getBoolean("GLOBAL.ENABLE_CUSTOM_ICON", false)) return;
        InputStream stream = KelUI.iconStorageHelper.getResource(fileName);
        if(stream == null) return;

        cir.setReturnValue(() -> stream);
    }
}

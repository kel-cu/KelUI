package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.kelui.KelUI;

@Mixin({Minecraft.class})
public abstract class MinecraftMixin {
    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    protected void renderSelection(CallbackInfoReturnable<Integer> cir) {
        if(!KelUI.config.getBoolean("UI.SMOOTH_MENU", false)) return;
        cir.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
    }
}
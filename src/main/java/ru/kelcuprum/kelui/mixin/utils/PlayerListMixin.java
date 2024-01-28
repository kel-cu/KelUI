package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.kelcuprum.kelui.KelUI;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"))
    private boolean renderHeads(Minecraft instance) {
        if(KelUI.config.getBoolean("TAB.FOREVER_RENDER_HEADS", true)) return true;
        else return instance.isLocalServer();
    }
}

package ru.kelcuprum.kelui.mixin.client.utils.bars;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.kelcuprum.kelui.KelUI;

@Mixin(LocatorBarRenderer.class)
public class LocatorBarRendererMixin{
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/LocatorBarRenderer;top(Lcom/mojang/blaze3d/platform/Window;)I"))
    public int render(LocatorBarRenderer instance, Window window){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return instance.top(window);
        return 5;
    }
    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/LocatorBarRenderer;top(Lcom/mojang/blaze3d/platform/Window;)I"))
    public int renderBackground(LocatorBarRenderer instance, Window window){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return instance.top(window);
        return 5;
    }
}

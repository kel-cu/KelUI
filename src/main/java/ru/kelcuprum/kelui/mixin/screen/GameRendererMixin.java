package ru.kelcuprum.kelui.mixin.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.gui.PanoramaRenderHelper;

@Mixin(value = GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow @Nullable Minecraft minecraft;

    @Inject(method = "render", at=@At(value = "INVOKE",target = "Lnet/minecraft/client/Minecraft;getDeltaFrameTime()F",ordinal = 1))
    public void updatePanorama(float tickDelta, long startTime, boolean tick, CallbackInfo ci){
        PanoramaRenderHelper.getInstance().addPanoramaTime(minecraft.getDeltaFrameTime());
    }
}

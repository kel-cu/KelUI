package ru.kelcuprum.kelui.mixin.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PanoramaRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.gui.PanoramaRenderHelper;

@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin {

    @Inject(method = "render", at=@At("HEAD"),cancellable = true)
    public void renderBedrockIfyCubeMap(float delta, float alpha, CallbackInfo ci){
        PanoramaRenderHelper.getInstance().render(new GuiGraphics(Minecraft.getInstance(),Minecraft.getInstance().renderBuffers().bufferSource()),alpha, true);
        ci.cancel();
    }

}
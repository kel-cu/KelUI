package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.PanoramaRenderHelper;

@Mixin(value = Screen.class)
public abstract class ScreenMixin {
    @Shadow @Nullable protected Minecraft minecraft;

    @Shadow public abstract void renderTransparentBackground(GuiGraphics guiGraphics);

    @Inject(method = "renderDirtBackground", at = @At("HEAD"), cancellable = true)
    public void renderBackground(GuiGraphics guiGraphics, CallbackInfo ci){
        if(!KelUI.config.getBoolean("SCREEN.BETTER_BACKGROUND", true)) return;
        if(this.minecraft.level == null) PanoramaRenderHelper.getInstance().render(guiGraphics, 1, false);
        renderTransparentBackground(guiGraphics);
        ci.cancel();
    }
}


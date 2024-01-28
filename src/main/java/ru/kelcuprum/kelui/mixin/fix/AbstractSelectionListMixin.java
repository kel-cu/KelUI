package ru.kelcuprum.kelui.mixin.fix;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({AbstractSelectionList.class})
public abstract class AbstractSelectionListMixin {
    @Shadow public abstract int getRowLeft();
    @Shadow public abstract int getRowRight();
    @Inject(method = "renderSelection", at = @At("HEAD"), cancellable = true)
    protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor, CallbackInfo ci) {
        int i = getRowLeft() - 2;
        int j = getRowRight() - 2;
        guiGraphics.fill(i, top - 2, j, top + height + 2, outerColor);
        guiGraphics.fill(i + 1, top - 1, j - 1, top + height + 1, innerColor);
        ci.cancel();
    }
}
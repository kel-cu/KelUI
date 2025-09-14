package ru.kelcuprum.kelui.mixin.client.utils.bars;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.JumpableVehicleBarRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

import static ru.kelcuprum.alinlib.gui.Colors.CONVICT;
import static ru.kelcuprum.alinlib.gui.Colors.SEADRIVE;

@Mixin(JumpableVehicleBarRenderer.class)
public class JumpableVehicleBarRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    public void renderBackground(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 0) ? 0 : (guiGraphics.guiWidth() - 180) / 2;
        assert this.minecraft.player != null;
        float f = this.minecraft.player.getJumpRidingScale();
        guiGraphics.fill(pos, guiGraphics.guiHeight() - 26, (int) (pos + (180 * f)), guiGraphics.guiHeight() - 24, 0x7fffffff);
        int k = 180 / 18;
        guiGraphics.fill(pos, guiGraphics.guiHeight() - 26, pos + (k * 16), guiGraphics.guiHeight() - 24, 0x7F598392);
        guiGraphics.fill(pos + (k * 16), guiGraphics.guiHeight() - 26, pos + 180, guiGraphics.guiHeight() - 24, CONVICT - 0x7F000000);
        ci.cancel();
    }
}

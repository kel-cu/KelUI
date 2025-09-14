package ru.kelcuprum.kelui.mixin.client.utils.bars;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
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

import static ru.kelcuprum.alinlib.gui.Colors.SEADRIVE;

@Mixin(ExperienceBarRenderer.class)
public class ExperienceBarRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    public void renderBackground(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        renderBar(guiGraphics, getHotBarX(guiGraphics), getHotBarY(guiGraphics)-4, SEADRIVE, 180, this.minecraft.player.experienceProgress);

        ci.cancel();
    }
    @Unique
    void renderBar(GuiGraphics guiGraphics, int x, int y, int color, int size, double value){
        guiGraphics.fill(x, y, x+size, y+2, getAlphaBarColor(color));
        guiGraphics.fill(x, y, (int) (x+(size*value)), y+2, color);
    }
    @Unique
    int getAlphaBarColor(int color){
        return KelUI.config.getBoolean("HUD.NEW_HOTBAR.COLORED_BAR", false) ? color-0x75000000 : 0x75000000;
    }
    @Unique
    int getHotBarX(GuiGraphics guiGraphics) {
        int conf = KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue();
        int x = conf == 0 ? 2 : conf == 1 ? (guiGraphics.guiWidth() - 180) / 2 : guiGraphics.guiWidth()-182;
        if(this.minecraft.player == null) return x;
        ItemStack off_item = this.minecraft.player.getOffhandItem();
        if(!off_item.isEmpty()){
            if(this.minecraft.player.getMainArm().getOpposite() == HumanoidArm.LEFT && conf == 0) x+=22;
            else if(this.minecraft.player.getMainArm().getOpposite() == HumanoidArm.RIGHT && conf == 2) x-=22;
        }
        return x;
    }

    @Unique
    int getHotBarY(GuiGraphics guiGraphics) {
        return guiGraphics.guiHeight() - 22;
    }
}

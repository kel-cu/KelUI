package ru.kelcuprum.kelui.mixin.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at=@At("HEAD"))
    public void render(GuiGraphics guiGraphics, CallbackInfo ci){
        if (KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)){
            if(isExperienceBarVisible()) guiGraphics.pose().translate(0, 10);
        }
    }
    @Inject(method = "render", at=@At("RETURN"))
    public void render$end(GuiGraphics guiGraphics, CallbackInfo ci){
        if (KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)){
            if(isExperienceBarVisible()) guiGraphics.pose().translate(0, -10);
        }
    }

    @Unique
    public boolean isExperienceBarVisible() {
        return this.nextContextualInfoState() == Gui.ContextualInfo.LOCATOR;
    }

    @Unique
    private Gui.ContextualInfo nextContextualInfoState(){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return Gui.ContextualInfo.EMPTY;
        boolean bl = this.minecraft.player.connection.getWaypointManager().hasWaypoints();
        boolean bl2 = this.minecraft.player.jumpableVehicle() != null;
        boolean bl3 = this.minecraft.gameMode.hasExperience();
        Gui.ContextualInfo ret;
        if (bl) {
            ret = Gui.ContextualInfo.LOCATOR;
        } else if (bl2) {
            ret = Gui.ContextualInfo.JUMPABLE_VEHICLE;
        } else {
            ret = bl3 ? Gui.ContextualInfo.EXPERIENCE : Gui.ContextualInfo.EMPTY;
        }
        return ret;
    }
}

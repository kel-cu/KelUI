package ru.kelcuprum.kelui.mixin.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.kelui.KelUI;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo ci){
        if (KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)){
            if(isExperienceBarVisible()) guiGraphics.pose().translate(0, 5);
        }
    }
    @Inject(method = "render", at=@At("RETURN"), cancellable = true)
    public void render$end(GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo ci){
        if (KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)){
            if(isExperienceBarVisible()) guiGraphics.pose().translate(0, -5);
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

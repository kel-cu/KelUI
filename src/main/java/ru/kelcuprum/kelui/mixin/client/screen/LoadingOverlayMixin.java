package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.kelui.KelUI;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("LOADING_OVERLAY.NEW", false)) return;
        long l = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = l;
        }

        float t = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + t * 0.050000012F, 0.0F, 1.0F);
        // Alpha
        float f = this.fadeOutStart > -1L ? (float) (l - this.fadeOutStart) / 1000.0F : -1.0F;
        float g = this.fadeInStart > -1L ? (float) (l - this.fadeInStart) / 500.0F : -1.0F;
        int k;
        int kB;
        if (f >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(guiGraphics, 0, 0, partialTick);
            }
            k = kB = Mth.ceil((1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && g < 1.0F) {
                this.minecraft.screen.render(guiGraphics, mouseX, mouseY, partialTick);
            }
            k = kB = Mth.ceil(Mth.clamp(g, 0.15, 1.0) * 255.0);
        } else {
            k = KelUI.config.getNumber("LOADING_OVERLAY.TYPE.DEFAULT.BACKGROUND_C0LOR", 0xFFB4B4B4).intValue();
            kB = 255;
        }
        // Render
        ru.kelcuprum.kelui.gui.Util.getSelectedOverlay().render(guiGraphics, mouseX, mouseY, partialTick, f, this.currentProgress, k, kB);
        // End
        if (f >= 2.0F) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || g >= 2.0F)) {
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable var23) {
                this.onFinish.accept(Optional.of(var23));
            }

            this.fadeOutStart = Util.getMillis();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }
        }
        ci.cancel();
    }

    @Shadow
    private float currentProgress;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private long fadeOutStart;

    @Shadow
    @Final
    private ReloadInstance reload;

    @Shadow
    @Final
    private boolean fadeIn;

    @Shadow
    private long fadeInStart;

    @Shadow
    @Final
    private Consumer<Optional<Throwable>> onFinish;

    @Inject(method = "drawProgressBar", at = @At("HEAD"), cancellable = true)
    private void drawProgressBar(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, CallbackInfo ci) {
        if (KelUI.config.getBoolean("LOADING_OVERLAY.NEW", false)) {
            ru.kelcuprum.kelui.gui.Util.getSelectedOverlay().drawProgressBar(guiGraphics, i, j, k, l, f, currentProgress);
            ci.cancel();
        }
    }
}

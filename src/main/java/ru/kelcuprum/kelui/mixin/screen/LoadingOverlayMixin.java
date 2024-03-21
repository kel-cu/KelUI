package ru.kelcuprum.kelui.mixin.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

import java.util.Optional;
import java.util.function.Consumer;

import static ru.kelcuprum.kelui.KelUI.ICONS.LOADING_ICON;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {

    /** Changes the background color */
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;replaceAlpha(II)I"
            )
    )
    private void background(Args args) {
        if(!KelUI.config.getBoolean("LOADING", true) || KelUI.config.getBoolean("LOADING.NEW", false)) return;
        args.set(0, KelUI.config.getNumber("LOADING.BACKGROUND", 0xff1b1b1b).intValue());
    }


    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if(!KelUI.config.getBoolean("LOADING.NEW", true)) return;
        long m = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = m;
        }

        float g = this.fadeOutStart > -1L ? (float)(m - this.fadeOutStart) / 1000.0F : -1.0F;
        float h = this.fadeInStart > -1L ? (float)(m - this.fadeInStart) / 500.0F : -1.0F;

        float w = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + w * 0.050000012F, 0.0F, 1.0F);
        int n;
        float o;
        if (g >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(guiGraphics, 0, 0, f);
            }
            n = Mth.ceil((1.0F - Mth.clamp(g - 1.0F, 0.0F, 1.0F)) * 255.0F);
            o = 1.0F - Mth.clamp(g - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && h < 1.0F) {
                this.minecraft.screen.render(guiGraphics, i, j, f);
            }
            n = Mth.ceil(Mth.clamp(h, 0.15, 1.0) * 255.0);
            o = Mth.clamp(h, 0.0F, 1.0F);
        } else {
            n = KelUI.config.getNumber("LOADING.NEW.BACKGROUND", 0xff030C03).intValue();
            float p = (float)(n >> 16 & 255) / 255.0F;
            float q = (float)(n >> 8 & 255) / 255.0F;
            float r = (float)(n & 255) / 255.0F;
            GlStateManager._clearColor(p, q, r, 1.0F);
            GlStateManager._clear(16384, Minecraft.ON_OSX);
            o = 1.0F;
        }
        if(this.minecraft == null || this.minecraft.level == null){
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BACKGROUND", 0xff030C03).intValue(), n));
        }
        guiGraphics.fill(0, guiGraphics.guiHeight() - 10, guiGraphics.guiWidth(), guiGraphics.guiHeight(),  KelUI.config.getNumber("LOADING.NEW.BAR_BACKGROUND", 0x7f05241E).intValue());
        guiGraphics.fill(0, guiGraphics.guiHeight() - 10, (int) (guiGraphics.guiWidth()*currentProgress), guiGraphics.guiHeight(), KelUI.config.getNumber("LOADING.NEW.BAR", 0xff1FA48C).intValue());
        if(KelUI.config.getBoolean("LOADING.NEW.ENABLE_ICON", true)){
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 1);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, o);
            if(KelUI.config.getBoolean("LOADING.NEW.ICON_KELUI", true)){
                guiGraphics.blit(LOADING_ICON, guiGraphics.guiWidth()/2-50, guiGraphics.guiHeight()/2-50, 0, 0, 100, 100, 100, 100);
            } else {

                int k = (int)((double)guiGraphics.guiWidth() * 0.5);
                int p = (int)((double)guiGraphics.guiHeight() * 0.5);
                double d = Math.min((double)guiGraphics.guiWidth() * 0.75, guiGraphics.guiHeight()) * 0.25;
                int q = (int)(d * 0.5);
                double e = d * 4.0;
                int r = (int)(e * 0.5);
                guiGraphics.blit(MOJANG_STUDIOS_LOGO_LOCATION, k - r, p - q, r, (int)d, -0.0625F, 0.0F, 120, 60, 120, 120);
                guiGraphics.blit(MOJANG_STUDIOS_LOGO_LOCATION, k, p - q, r, (int)d, 0.0625F, 60.0F, 120, 60, 120, 120);
            }
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
        if (g >= 2.0F) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || h >= 2.0F)) {
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

    @Shadow @Final private Minecraft minecraft;

    @Shadow private long fadeOutStart;

    @Shadow @Final private ReloadInstance reload;

    @Shadow @Final private boolean fadeIn;

    @Shadow private long fadeInStart;

    @Shadow @Final private Consumer<Optional<Throwable>> onFinish;


    @Shadow
    private static int replaceAlpha(int i, int j) {
        return i & 16777215 | j << 24;
    }

    @Shadow @Final
    static ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION;

    @Inject(method = "drawProgressBar", at = @At("HEAD"), cancellable = true)
    private void drawProgressBar(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, CallbackInfo ci) {
        if(!KelUI.config.getBoolean("LOADING", true)) return;
        int m = Mth.ceil((float)(k - i - 2) * this.currentProgress);
        int o = KelUI.config.getNumber("LOADING.BAR_COLOR", 0xffff4f4f).intValue();
        int a = KelUI.config.getNumber("LOADING.BAR_COLOR.BORDER", 0xffffffff).intValue();
        guiGraphics.fill(i + 2, j + 2, i + m, l - 2, o);
        guiGraphics.fill(i + 1, j, k - 1, j + 1, a);
        guiGraphics.fill(i + 1, l, k - 1, l - 1, a);
        guiGraphics.fill(i, j, i + 1, l, a);
        guiGraphics.fill(k, j, k - 1, l, a);
        ci.cancel();
    }
}

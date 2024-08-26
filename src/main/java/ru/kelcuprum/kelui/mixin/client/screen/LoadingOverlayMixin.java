package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

import java.nio.file.Watchable;
import java.util.Optional;
import java.util.function.Consumer;

import static ru.kelcuprum.kelui.KelUI.ICONS.LOADING_ICON;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("LOADING.NEW", false)) return;
        long l = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = l;
        }

        float t = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + t * 0.050000012F, 0.0F, 1.0F);
        // Alpha
        float f = this.fadeOutStart > -1L ? (float) (l - this.fadeOutStart) / 1000.0F : -1.0F;
        float g = this.fadeInStart > -1L ? (float) (l - this.fadeInStart) / 500.0F : -1.0F;
        float h;
        int k;
        int kB;
        if (f >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(guiGraphics, 0, 0, partialTick);
            }
            k = kB = Mth.ceil((1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            h = 1.0F - Mth.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && g < 1.0F) {
                this.minecraft.screen.render(guiGraphics, mouseX, mouseY, partialTick);
            }
            k = kB = Mth.ceil(Mth.clamp(g, 0.15, 1.0) * 255.0);
            h = Mth.clamp(g, 0.0F, 1.0F);
        } else {
            k = KelUI.config.getNumber("LOADING.NEW.BACKGROUND_C0LOR", 0xFFB4B4B4).intValue();
            kB = 255;
            h = 1.0F;
        }
        // Render
        if (minecraft.level == null)
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BACKGROUND_C0LOR", 0xFFB4B4B4).intValue(), k));
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), 30, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
        guiGraphics.fill(RenderType.guiOverlay(), 0, guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight() - 30, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
        // Shit
        if (minecraft.level == null) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, h);
            // Т-Банк не спонсировал, честно
            int twidth = 100;
            int theight = 100;
            guiGraphics.blit(LOADING_ICON, guiGraphics.guiWidth() / 2 - (twidth / 2), guiGraphics.guiHeight() / 2 - (theight / 2), 0, 0, 100, 100, 100, 100);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        // Progress bar
        if (f < 1.0F) {
            int px = guiGraphics.guiWidth() / 2;
            int py = guiGraphics.guiHeight() - 57;
            int width = 100;
            int height = 15;
            guiGraphics.fill(px - width, py, px + width, py + height, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
            guiGraphics.fill(px - (width - 2), py + 2, px + (width - 2), py + height - 2, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_BACKGROUND_C0LOR", 0xFFD9D9D9).intValue(), kB));
            guiGraphics.fill(px - (width - 3), py + 3, (int) (px + ((width - 3) * this.currentProgress)), py + height - 3, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
        }
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


    @Shadow
    private static int replaceAlpha(int i, int j) {
        return i & 16777215 | j << 24;
    }

    @Inject(method = "drawProgressBar", at = @At("HEAD"), cancellable = true)
    private void drawProgressBar(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, CallbackInfo ci) {
        if (KelUI.config.getBoolean("LOADING.NEW", false)) {
            int kB = 255;
            if (f < 1.0F) {
                l += 5;
                guiGraphics.fill(i, j, k, l, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
                guiGraphics.fill(i + 2, j + 2, (k - 2), l - 2, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_BACKGROUND_C0LOR", 0xFFD9D9D9).intValue(), kB));
                guiGraphics.fill(i + 3, j + 3, (int) (i + (((k - 3 - i) * this.currentProgress))), l - 3, replaceAlpha(KelUI.config.getNumber("LOADING.NEW.BORDER_C0LOR", 0xFF000000).intValue(), kB));
            }
            ci.cancel();
        }
    }
}

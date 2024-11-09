package ru.kelcuprum.kelui.mixin.client.utils.toasts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

import java.util.List;

@Mixin(RecipeToast.class)
public abstract class RecipeToastMixin implements Toast {

    @Shadow private long lastChanged;

    @Shadow @Final private static long DISPLAY_TIME;

    @Shadow @Final private static Component TITLE_TEXT;

    @Shadow @Final private static Component DESCRIPTION_TEXT;

    @Shadow @Final private List<RecipeToast.Entry> recipeItems;

    @Shadow private int displayedRecipeIndex;

    @Override
    public int width() {
        if(!KelUI.config.getBoolean("TOASTS", true)) return Toast.super.width();
        int wid = 35;
        wid+=(Math.max(AlinLib.MINECRAFT.font.width(TITLE_TEXT), AlinLib.MINECRAFT.font.width(DESCRIPTION_TEXT)));
        return Math.max(Toast.super.width(), wid);
    }

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, Font font, long l, CallbackInfo ci){
        if(!KelUI.config.getBoolean("TOASTS", true)) return;
        double d = DISPLAY_TIME;
        long m = l - this.lastChanged;
        guiGraphics.fill(0, 0, width(), height() - 1, 0xB3000000);
        guiGraphics.fill(0, height() - 1, (int) (width()*(m/d)), height(), 0xFFcba6f7);

        guiGraphics.drawString(font, TITLE_TEXT, 30, 7, 0xFFcba6f7, false);
        guiGraphics.drawString(font, DESCRIPTION_TEXT, 30, 18, -1, false);
        RecipeToast.Entry entry = (RecipeToast.Entry)this.recipeItems.get(this.displayedRecipeIndex);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.6F, 0.6F, 1.0F);
        guiGraphics.renderFakeItem(entry.categoryItem(), 3, 3);
        guiGraphics.pose().popPose();
        guiGraphics.renderFakeItem(entry.unlockedItem(), 8, 8);
        ci.cancel();
    }
}

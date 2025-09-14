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
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
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
        GuiUtils.getSelected().renderToastBackground(new ToastBuilder(), guiGraphics, 0, 0, width()+1, height(), m/d);

        guiGraphics.drawString(font, TITLE_TEXT, 30, 7, 0xFFcba6f7, false);
        guiGraphics.drawString(font, DESCRIPTION_TEXT, 30, 18, GuiUtils.getSelected().getToastTextColor(), false);
        RecipeToast.Entry entry = (RecipeToast.Entry)this.recipeItems.get(this.displayedRecipeIndex);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(0.6F, 0.6F);
        guiGraphics.renderFakeItem(entry.categoryItem(), 3, 3);
        guiGraphics.pose().popMatrix();
        guiGraphics.renderFakeItem(entry.unlockedItem(), 8, 8);
        ci.cancel();
    }
}

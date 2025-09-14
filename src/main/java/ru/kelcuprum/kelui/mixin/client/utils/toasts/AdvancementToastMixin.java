package ru.kelcuprum.kelui.mixin.client.utils.toasts;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import ru.kelcuprum.kelui.KelUI;

import java.util.List;

@Mixin(AdvancementToast.class)
public abstract class AdvancementToastMixin implements Toast {

    @Shadow @Final private AdvancementHolder advancement;

    @Shadow @Final public static int DISPLAY_TIME;

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, Font font, long l, CallbackInfo ci){
        if(!KelUI.config.getBoolean("TOASTS", true)) return;
        DisplayInfo displayInfo = this.advancement.value().display().orElse(null);
        double d = DISPLAY_TIME;
        GuiUtils.getSelected().renderToastBackground(new ToastBuilder(), guiGraphics, 0, 0, width()+1, height(), l/d);
        if(displayInfo != null) {
            List<FormattedCharSequence> list = font.split(displayInfo.getTitle(), 125);
            int i = displayInfo.getType() == AdvancementType.CHALLENGE ? 0xFFcba6f7 : GuiUtils.getSelected().id.equals("windows") ? 0xFF000000 : -256;
            guiGraphics.fill(0, height() - 1, (int) (width()*(l/d)), height(), i);
            if (list.size() == 1) {
                guiGraphics.drawString(font, displayInfo.getType().getDisplayName(), 30, 7, i, false);
                guiGraphics.drawString(font, (FormattedCharSequence)list.get(0), 30, 18, GuiUtils.getSelected().getToastTextColor(), false);
            } else {
                int j = 1500;
                float f = 300.0F;
                if (l < 1500L) {
                    int k = Mth.floor(Mth.clamp((float)(1500L - l) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    guiGraphics.drawString(font, displayInfo.getType().getDisplayName(), 30, 11, GuiUtils.getSelected().getToastTextColor() | k, false);
                } else {
                    int k = Mth.floor(Mth.clamp((float)(l - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int m = this.height() / 2 - list.size() * 9 / 2;

                    for (FormattedCharSequence formattedCharSequence : list) {
                        guiGraphics.drawString(font, formattedCharSequence, 30, m, GuiUtils.getSelected().getToastTextColor() | k, false);
                        m += 9;
                    }
                }
            }

            guiGraphics.renderFakeItem(displayInfo.getIcon(), 8, 8);
        }
        ci.cancel();
    }
}

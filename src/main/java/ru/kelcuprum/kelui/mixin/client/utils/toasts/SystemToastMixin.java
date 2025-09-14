package ru.kelcuprum.kelui.mixin.client.utils.toasts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
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

@Mixin(SystemToast.class)
public abstract class SystemToastMixin implements Toast {
    @Shadow private List<FormattedCharSequence> messageLines;

    @Shadow private Component title;

    @Shadow public abstract int width();

    @Shadow public abstract int height();

    @Shadow @Final private SystemToast.SystemToastId id;

    @Shadow private long lastChanged;

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, Font font, long l, CallbackInfo ci){
        if(!KelUI.config.getBoolean("TOASTS", true)) return;
        double d = (double)this.id.displayTime;
        long m = l - this.lastChanged;
        GuiUtils.getSelected().renderToastBackground(new ToastBuilder(), guiGraphics, 0, 0, width()+1, height(), m/d);
        int color = GuiUtils.getSelected().id.equals("windows") ? 0xFF000000 : -256;
        guiGraphics.fill(7, 6, 11, height()-13, color);
        guiGraphics.fill(7, height()-11, 11, height()-7, color);

        // ------
        if (this.messageLines.isEmpty())
            guiGraphics.drawString(font, this.title, 18, 12, color, false);
        else {
            guiGraphics.drawString(font, this.title, 18, 7, color, false);
            for(int j = 0; j < this.messageLines.size(); ++j)
                guiGraphics.drawString(font, this.messageLines.get(j), 18, 18 + j * 12, GuiUtils.getSelected().getToastTextColor(), false);
        }
        ci.cancel();
    }
}

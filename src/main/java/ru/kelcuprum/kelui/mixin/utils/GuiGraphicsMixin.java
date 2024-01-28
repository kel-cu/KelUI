package ru.kelcuprum.kelui.mixin.utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.kelui.KelUI;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract PoseStack pose();

    @Shadow
    public abstract MultiBufferSource.BufferSource bufferSource();

    @Shadow
    public abstract void flush();

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "TAIL")
    )
    private void drawMap(Font font, ItemStack stack, int i, int j, String string, CallbackInfo ci) {
        if(!KelUI.config.getBoolean("HUD.MAP_SLOT", true)) return;
        if (!stack.is(Items.FILLED_MAP)) return;

        var mapId = MapItem.getMapId(stack);
        var savedData = MapItem.getSavedData(mapId, this.minecraft.level);

        if (savedData == null) return;

        this.pose().pushPose();
        this.pose().translate(i, j, 200F);
        this.pose().scale(0.125F, 0.125F, 1F);
        this.minecraft
                .gameRenderer
                .getMapRenderer()
                .render(this.pose(), this.bufferSource(), mapId, savedData, true, 15728880);
        this.flush();
        this.pose().popPose();
    }
    //////
    @ModifyArgs(
            method = "hLine(Lnet/minecraft/client/renderer/RenderType;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"
            )
    )
    private void hLine(Args args) {
        if(!KelUI.config.getBoolean("DEBUG.DARK_GRAPH", true)) return;
        args.set(5, -1873784742);
    }
    @ModifyArgs(
            method = "vLine(Lnet/minecraft/client/renderer/RenderType;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"
            )
    )
    private void vLine(Args args) {
        if(!KelUI.config.getBoolean("DEBUG.DARK_GRAPH", true)) return;
        args.set(5, -1873784742);
    }
}
package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.kelui.KelUI;

import java.util.ArrayList;
import java.util.List;

import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.GROUPIE;
import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.SEADRIVE;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow protected abstract Player getCameraPlayer();

    @Shadow private int screenHeight;

    @Shadow public abstract SpectatorGui getSpectatorGui();

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, float f, Player player, ItemStack itemStack, int k);

    @Shadow public abstract int getGuiTicks();

    @Shadow private int screenWidth;

    @Inject(method = "render", at = @At("HEAD"))
    void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        int y = screenHeight/2;
        List<ItemStack> items = new ArrayList<>();
        List<Component> text = new ArrayList<>();
        int maxText = 0;
        if(KelUI.config.getBoolean("HUD.ARMOR_INFO", true)) {
            for (int i = 0; i < 4; i++) {
                ItemStack item = getCameraPlayer().getInventory().getArmor(3 - i);
                if (!item.isEmpty()) {
                    items.add(item);
                    if(KelUI.config.getBoolean("HUD.ARMOR_INFO.DAMAGE", true)) {
                        Component itext = Component.literal(item.getMaxDamage() == (item.getMaxDamage() - item.getDamageValue()) ? String.format("%s", item.getMaxDamage()) : String.format("%s/%s", item.getMaxDamage() - item.getDamageValue(), item.getMaxDamage()));
                        if (Minecraft.getInstance().font.width(itext) > maxText)
                            maxText = Minecraft.getInstance().font.width(itext);
                        text.add(itext);
                    }
                }
            }
            if (!items.isEmpty()) {
                y -= ((20 * items.size()) / 2);
                int j = 0;
                guiGraphics.fill(0, y, 20, y + (18 * items.size()), 0x75000000);
                if (maxText > 0) {
                    guiGraphics.fill(20, y, 26 + maxText, y + (18 * items.size()), 0x75000000);
                }
                for (ItemStack item : items) {
                    guiGraphics.renderFakeItem(item, 2, y + (j * 18) + 2);
                    if(!text.isEmpty()) guiGraphics.drawString(Minecraft.getInstance().font, text.get(j), 22, y + (j * 18) + (20 / 2) - (minecraft.font.lineHeight / 2), 0xFFFFFFFF);
                    j++;
                }
            }
        }
    }
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    void renderHotbar(float f, GuiGraphics guiGraphics, CallbackInfo ci){
        if(!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int l = 1;
        int m;
        int n;
        int o = this.screenHeight - 22;
        for(m = 0; m < 9; ++m) {
            n = m * 20;
            this.renderSlot(guiGraphics, n, o, f, getCameraPlayer(), (ItemStack)getCameraPlayer().getInventory().items.get(m), l++);
        }
        int selected = getCameraPlayer().getInventory().selected * 20;
        guiGraphics.fill(selected, this.screenHeight-3, selected+20, this.screenHeight-1, InterfaceUtils.Colors.SEADRIVE);
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if(!itemStack.isEmpty()){
            this.renderSlot(guiGraphics, this.screenWidth-20, o, f, getCameraPlayer(), itemStack, l++);
        }
        ci.cancel();
    }
    @Inject(method = "renderSlot", at=@At("HEAD"), cancellable = true)
    void renderSlot(GuiGraphics guiGraphics, int i, int j, float f, Player player, ItemStack itemStack, int k, CallbackInfo ci){
        if(!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        guiGraphics.fill(i, j, i+20, j+20, 0x75000000);
        if (!itemStack.isEmpty()) {
            float g = (float)itemStack.getPopTime() - f;
            if (g > 0.0F) {
                float h = 1.0F + g / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float)(i + 8), (float)(j + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float)(-(i + 8)), (float)(-(j + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, itemStack, i+2, j+2, k);
            if (g > 0.0F) {
                guiGraphics.pose().popPose();
            }

            guiGraphics.renderItemDecorations(this.minecraft.font, itemStack, i+2, j+2);
        }
        ci.cancel();
    }
    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    void renderPlayrerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int i = this.screenHeight-22;
        // health
        double health = getCameraPlayer().getHealth() / getCameraPlayer().getAttributeValue(Attributes.MAX_HEALTH);
        guiGraphics.fill(182, i, 184, i+20, GROUPIE-0x75000000);
        guiGraphics.fill(182, i, 184, (int) (i+(20*health)), GROUPIE);

        double hunger = (double) getCameraPlayer().getFoodData().getFoodLevel() / 20;
        guiGraphics.fill(186, i, 188, i+20, 0x75ff9b54);
        guiGraphics.fill(186, i, 188, (int) (i+(20*hunger)), 0xFFff9b54);
        ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at=@At("HEAD"), cancellable = true)
    void renderExperienceBar(GuiGraphics guiGraphics, int j, CallbackInfo ci){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        double exp = this.minecraft.player.experienceProgress;
        int i = this.screenHeight-22;
        guiGraphics.fill(190, i, 192, i+20, SEADRIVE-0x75000000);
        guiGraphics.fill(190, i, 192, (int) (i+(20*exp)), SEADRIVE);
        guiGraphics.drawString(Minecraft.getInstance().font, "" + this.minecraft.player.experienceLevel, 194, i + (20 / 2) - (minecraft.font.lineHeight / 2), SEADRIVE);
        ci.cancel();
    }

}
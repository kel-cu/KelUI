package ru.kelcuprum.kelui.mixin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.kelcuprum.alinlib.gui.InterfaceUtils.Colors.*;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow protected abstract Player getCameraPlayer();

    @Shadow private int screenHeight;

    @Shadow @Final private Minecraft minecraft;

    @Shadow protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, float f, Player player, ItemStack itemStack, int k);


    @Shadow private int screenWidth;

    @Shadow private int toolHighlightTimer;

    @Shadow public abstract Font getFont();

    @Shadow private ItemStack lastToolHighlight;

    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow @Final private DebugScreenOverlay debugOverlay;

    @Inject(method = "render", at = @At("HEAD"))
    void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        int y = screenHeight/2;
        List<ItemStack> items = new ArrayList<>();
        List<Component> text = new ArrayList<>();
        int maxText = 0;
        if(this.debugOverlay.showDebugScreen()) return;
        if(KelUI.config.getBoolean("HUD.ARMOR_INFO", true)) {
            for (int i = 0; i < 4; i++) {
                ItemStack item = getCameraPlayer().getInventory().getArmor(3 - i);
                if (!item.isEmpty()) {
                    items.add(item);
                    if(KelUI.config.getBoolean("HUD.ARMOR_INFO.DAMAGE", true)) {
                        Component itext = Component.literal(KelUI.getArmorDamage(item));
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
        if(KelUI.config.getBoolean("HUD.DEBUG_OVERLAY", false)) {
            int x = 2;
            int y1 = 2;
            Component fps = Component.literal(String.format("%s FPS", this.minecraft.getFps()));
            guiGraphics.fill(x, y1,x+4+getFont().width(fps)+4 , y1+2+getFont().lineHeight+4, 0x7f000000);
            guiGraphics.drawString(getFont(), fps, x+4, y1+4, -1);
        }
    }
    // -=-=-=-=-=-=-=-=-=-
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
            )
    )
    private void message(Args args) {
        if(!KelUI.config.getBoolean("HUD.NEW_HOTBAR", true)) return;
        if((float) args.get(1) == (float) (this.screenHeight - 68)) {
            args.set(0, (float) (((this.screenWidth-200)/2)+200));
            args.set(1, (float) (this.screenHeight-18+ (20 / 2) - (minecraft.font.lineHeight / 2)));
        }
    }
    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    void renderEffects(GuiGraphics guiGraphics, CallbackInfo ci){
        if(!KelUI.config.getBoolean("HUD.NEW_EFFECTS", false)) return;
        if(!this.debugOverlay.showDebugScreen()) {
            assert this.minecraft.player != null;
            Collection<MobEffectInstance> collection = this.minecraft.player.getActiveEffects();
            MobEffectTextureManager mobEffectTextureManager = this.minecraft.getMobEffectTextures();
            if (collection.isEmpty()) return;
            int i = 0;
            int j = 0;
            for (MobEffectInstance effect : collection) {
                if (i > 5) {
                    i = 0;
                    j++;
                }
                guiGraphics.fill(this.screenWidth - (24 * i), 24 * j, this.screenWidth - (24 + (24 * i)), 24 + (24 * j), 0x7f000000);
                guiGraphics.fill(this.screenWidth - (24 * i), 22 + (24 * j), this.screenWidth - (24 + (24 * i)), 24 + (24 * j), effect.isAmbient() ? 0xff598392 : SEADRIVE);
                guiGraphics.blit(this.screenWidth - (4 + (24 * i)) - 16, 4 + (24 * j), 0, 16, 16, mobEffectTextureManager.get(effect.getEffect()));
                if (!effect.isInfiniteDuration() && KelUI.config.getBoolean("HUD.NEW_EFFECTS.TIME", true)) {
                    Component time = Component.literal(Util.getDurationAsString(effect.getDuration()));
                    guiGraphics.drawString(this.getFont(), time, this.screenWidth - (4 + (24 * i)) - 16, 20 - getFont().lineHeight + (24 * j), -1);
                }
                i++;
            }
        }
        ci.cancel();
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
            this.renderSlot(guiGraphics, n, o, f, getCameraPlayer(), getCameraPlayer().getInventory().items.get(m), l++);
        }
        int selected = getCameraPlayer().getInventory().selected * 20;
        guiGraphics.fill(selected, this.screenHeight-3, selected+20, this.screenHeight-1, InterfaceUtils.Colors.SEADRIVE);
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if(!itemStack.isEmpty()){
            this.renderSlot(guiGraphics, 182, o, f, getCameraPlayer(), itemStack, l);
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
        int x = 0;
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if(!itemStack.isEmpty()){
            x=22;
        }
        // health
        double health = getCameraPlayer().getHealth() / getCameraPlayer().getAttributeValue(Attributes.MAX_HEALTH);
        guiGraphics.fill(182+x, i, 184+x, i+20, GROUPIE-0x75000000);
        guiGraphics.fill(182+x, i, 184+x, (int) (i+(20*health)), GROUPIE);

        double armor = (double) getCameraPlayer().getArmorValue() / 20;
        guiGraphics.fill(186+x, i, 188+x, i+20, 0x75598392);
        guiGraphics.fill(186+x, i, 188+x, (int) (i+(20*armor)), 0xff598392);

        double hunger = (double) getCameraPlayer().getFoodData().getFoodLevel() / 20;
        guiGraphics.fill(190+x, i, 192+x, i+20, 0x75ff9b54);
        guiGraphics.fill(190+x, i, 192+x, (int) (i+(20*hunger)), 0xFFff9b54);
        if(getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()){
            double air = (double) Math.max(0, getCameraPlayer().getAirSupply()) /getCameraPlayer().getMaxAirSupply();
            guiGraphics.fill(194+x, i, 196+x, i+20, 0x7fcae9ff);
            guiGraphics.fill(194+x, i, 196+x, (int) (i+(20*air)), 0xffcae9ff);
        }
        ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at=@At("HEAD"), cancellable = true)
    void renderExperienceBar(GuiGraphics guiGraphics, int j, CallbackInfo ci){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        double exp = this.minecraft.player.experienceProgress;
        int i = this.screenHeight-22;
        int x = 0;
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if(!itemStack.isEmpty()){
            x=22;
        }
        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        if (livingEntity != null) {
            x+=4;
        }
        if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
            x+=4;
        }
        guiGraphics.fill(194+x, i, 196+x, i+20, SEADRIVE-0x75000000);
        guiGraphics.fill(194+x, i, 196+x, (int) (i+(20*exp)), SEADRIVE);
        guiGraphics.drawString(Minecraft.getInstance().font, "" + this.minecraft.player.experienceLevel, 198+x, i + (20 / 2) - (minecraft.font.lineHeight / 2), SEADRIVE);
        ci.cancel();
    }
    @Inject(method = "renderVehicleHealth", at=@At("HEAD"), cancellable = true)
    void renderVehicleHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int i = this.screenHeight-22;
        int x = 0;
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if(!itemStack.isEmpty()){
            x=22;
        }
        if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
            x+=4;
        }
        assert this.minecraft.gameMode != null;
        if (!this.minecraft.gameMode.canHurtPlayer()) {
            x-=12;
        }
        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        if (livingEntity != null) {
            double health = livingEntity.getHealth()/livingEntity.getMaxHealth();
            guiGraphics.fill(194+x, i, 196+x, i+20, CLOWNFISH-0x75000000);
            guiGraphics.fill(194+x, i, 196+x, (int) (i+(20*health)), CLOWNFISH);
        }
        ci.cancel();
    }
    @Inject(method = "renderJumpMeter", at=@At("HEAD"), cancellable = true)
    void renderJumpMeter(PlayerRideableJumping playerRideableJumping, GuiGraphics guiGraphics, int j, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        float f = this.minecraft.player.getJumpRidingScale();
        guiGraphics.fill(0, screenHeight-26, (int) (180*f), screenHeight-24, 0x7fffffff);
        int k = 180/18;
        guiGraphics.fill(0, screenHeight-26, k*16, screenHeight-24, 0x7F598392);
        guiGraphics.fill(k*16, screenHeight-26, 180, screenHeight-24, CONVICT-0x7F000000);
        ci.cancel();
    }


    @Inject(method = "renderSelectedItemName", at=@At("HEAD"), cancellable = true)
    void renderSelectedItemName(GuiGraphics guiGraphics, CallbackInfo ci){
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            MutableComponent mutableComponent = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color);
            int l = (int) ((float) this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            guiGraphics.drawString(getFont(), mutableComponent, 6, screenHeight - 22 - 4 - minecraft.font.lineHeight, 16777215 + (l << 24));
        }
        ci.cancel();
    }
}
package ru.kelcuprum.kelui.mixin.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Shadow
    protected abstract Player getCameraPlayer();

    @Unique
    int screenHeight;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, float f, Player player, ItemStack itemStack, int k);

    @Unique
    int screenWidth;

    @Shadow
    private int toolHighlightTimer;

    @Shadow
    public abstract Font getFont();

    @Shadow
    private ItemStack lastToolHighlight;

    @Shadow
    protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow
    @Final
    private DebugScreenOverlay debugOverlay;
    @Shadow
    @Final
    private LayeredDraw layers;

    @Shadow protected abstract boolean isExperienceBarVisible();

    @Shadow public abstract void render(GuiGraphics guiGraphics, float f);

    @Inject(method = "render", at = @At("HEAD"))
    void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        this.screenWidth = guiGraphics.guiWidth();
        this.screenHeight = guiGraphics.guiHeight();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    void init(Minecraft minecraft, CallbackInfo ci) {
        LayeredDraw kelUILayer = new LayeredDraw().add(this::renderDebugOverlay).add(this::renderPaperDoll).add(this::renderArmorInfo).add(this::renderModernStateOverlay);
        layers.add(kelUILayer, () -> !minecraft.options.hideGui);
    }
    @Unique
    public void renderModernStateOverlay(GuiGraphics guiGraphics, float f){
        if (this.debugOverlay.showDebugScreen()) return;
        if(KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() != 2) return;

        PlayerFaceRenderer.draw(guiGraphics, KelUI.MINECRAFT.getSkinManager().getInsecureSkin(KelUI.MINECRAFT.getGameProfile()), 5, 5, 20);
    }
    @Unique
    public void renderDebugOverlay(GuiGraphics guiGraphics, float f) {
        if (this.debugOverlay.showDebugScreen()) return;
        if (!KelUI.config.getBoolean("HUD.DEBUG_OVERLAY", false)) return;
        int x = 2;
        int y1 = 2;
        int size = 2 + getFont().lineHeight + 4;

        Component fps = Component.literal(String.format("%s FPS", this.minecraft.getFps()));
        guiGraphics.fill(x, y1, x + 4 + getFont().width(fps) + 4, y1 + size, 0x7f000000);
        guiGraphics.drawString(getFont(), fps, x + 4, y1 + 4, -1);

        y1+=size;

    }

    @Unique
    public void renderPaperDoll(GuiGraphics guiGraphics, float f) {
        if (this.debugOverlay.showDebugScreen()) return;
        if (!KelUI.config.getBoolean("HUD.PAPER_DOLL", false)) return;
        assert this.minecraft.player != null;
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, screenWidth - 130, 0, screenWidth, 150, 45, 0.0625F, (float) screenWidth / 2, 75, this.minecraft.player);

    }

    @Unique
    public void renderArmorInfo(GuiGraphics guiGraphics, float f) {
        if (this.debugOverlay.showDebugScreen()) return;
        if (!KelUI.config.getBoolean("HUD.ARMOR_INFO", true)) return;
        List<ItemStack> items = new ArrayList<>();
        List<Component> text = new ArrayList<>();
        int y = screenHeight / 2;
        int maxText = 0;
        if (getCameraPlayer() instanceof Player) {
            for (int i = 0; i < 4; i++) {
                ItemStack item = getCameraPlayer().getInventory().getArmor(3 - i);
                if (!item.isEmpty()) {
                    items.add(item);
                    if (KelUI.config.getBoolean("HUD.ARMOR_INFO.DAMAGE", true)) {
                        Component itext = Component.literal(KelUI.getArmorDamage(item));
                        if(!itext.getString().isBlank()) {
                            if (KelUI.MINECRAFT.font.width(itext) > maxText)
                                maxText = KelUI.MINECRAFT.font.width(itext);
                            text.add(itext);
                        } else text.add(Component.empty());
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
                    if (!text.isEmpty())
                        guiGraphics.drawString(minecraft.font, text.get(j), 22, y + (j * 18) + (20 / 2) - (minecraft.font.lineHeight / 2), 0xFFFFFFFF);
                    j++;
                }
            }
        }
    }

    // -=-=-=-=-=-=-=-=-=-
    @ModifyArgs(
            method = "renderOverlayMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
            )
    )
    private void message(Args args) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 1) return;
        if ((float) args.get(1) == (float) (this.screenHeight - 68)) {
            args.set(0, (float) (((this.screenWidth - 200) / 2) + 200));
            args.set(1, (float) (this.screenHeight - 18 + (20 / 2) - (minecraft.font.lineHeight / 2)));
        }
    }

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    void renderEffects(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_EFFECTS", false)) return;
        if (!this.debugOverlay.showDebugScreen()) {
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

    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    void renderItemHotbar(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        int l = 1;
        int m;
        int n;
        int o = this.screenHeight - 22;
        for (m = 0; m < 9; ++m) {
            n = m * 20;
            boolean isSelected = m == getCameraPlayer().getInventory().selected;
            kelUI$renderSlot(guiGraphics, pos + n, o, f, getCameraPlayer(), getCameraPlayer().getInventory().items.get(m), l++, isSelected);
        }
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if (!itemStack.isEmpty()) {
            kelUI$renderSlot(guiGraphics, pos + 182, o, f, getCameraPlayer(), itemStack, l, false);
        }
        ci.cancel();
    }
    @Unique
    void kelUI$renderSlot(GuiGraphics guiGraphics, int i, int j, float f, Player player, ItemStack itemStack, int k, boolean isSelected){
        int color = isSelected ? TETRA : 0xFF000000;
        if (!itemStack.isEmpty() && itemStack.isDamageableItem() && isSelected){
            color = (itemStack.getBarColor() | -16777216);
        }
        guiGraphics.fill(i, j, i + 20, j + 20, color-0x75000000);
        if (!itemStack.isEmpty()) {
            float g = (float) itemStack.getPopTime() - f;
            if (g > 0.0F) {
                float h = 1.0F + g / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (i + 8), (float) (j + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float) (-(i + 8)), (float) (-(j + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, itemStack, i + 2, j + 2, k);
            if (g > 0.0F) {
                guiGraphics.pose().popPose();
            }
            guiGraphics.renderItemDecorations(this.minecraft.font, itemStack, i + 2, j + 2);
        }
        if(isSelected) {
            guiGraphics.fill(i, this.screenHeight - 3, i + 20, this.screenHeight - 1, color == 0xFF000000 ? SEADRIVE : color);
        }
    }

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    void renderPlayrerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        // -=-=-=-

        double health = getCameraPlayer().getHealth() / getCameraPlayer().getAttributeValue(Attributes.MAX_HEALTH);
        double armor = (double) getCameraPlayer().getArmorValue() / 20;
        double hunger = (double) getCameraPlayer().getFoodData().getFoodLevel() / 20;
        double air = (double) Math.max(0, getCameraPlayer().getAirSupply()) / getCameraPlayer().getMaxAirSupply();
        // -=-=-=-
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 0) {
            int i = this.screenHeight - 22;
            int x = 0;
            ItemStack itemStack = getCameraPlayer().getOffhandItem();
            if (!itemStack.isEmpty()) {
                x = 22;
            }
            // health
            assert KelUI.MINECRAFT.player != null;
            int healthColor = KelUI.MINECRAFT.player.hasEffect(MobEffects.POISON) ? 0xFFa3b18a :
                    KelUI.MINECRAFT.player.hasEffect(MobEffects.WITHER) ? 0xff4a4e69 :
                            KelUI.MINECRAFT.player.isFullyFrozen() ? 0x90e0ef : GROUPIE;
            guiGraphics.fill(pos + 182 + x, i, pos + 184 + x, i + 20, getAlphaBarColor(healthColor));
            guiGraphics.fill(pos + 182 + x, i, pos + 184 + x, (int) (i + (20 * health)), healthColor);

            guiGraphics.fill(pos + 186 + x, i, pos + 188 + x, i + 20, getAlphaBarColor(0xff598392));
            guiGraphics.fill(pos + 186 + x, i, pos + 188 + x, (int) (i + (20 * armor)), 0xff598392);

            guiGraphics.fill(pos + 190 + x, i, pos + 192 + x, i + 20, getAlphaBarColor(0xFFff9b54));
            guiGraphics.fill(pos + 190 + x, i, pos + 192 + x, (int) (i + (20 * hunger)), 0xFFff9b54);

            if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
                guiGraphics.fill(pos + 194 + x, i, pos + 196 + x, i + 20, getAlphaBarColor(0xffcae9ff));
                guiGraphics.fill(pos + 194 + x, i, pos + 196 + x, (int) (i + (20 * air)), 0xffcae9ff);
            }
        } else if(KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 1) {
            int i = this.screenHeight - 28;
            assert KelUI.MINECRAFT.player != null;
            int healthColor = KelUI.MINECRAFT.player.hasEffect(MobEffects.POISON) ? 0xFFa3b18a :
                    KelUI.MINECRAFT.player.hasEffect(MobEffects.WITHER) ? 0xff4a4e69 :
                            KelUI.MINECRAFT.player.isFullyFrozen() ? 0x90e0ef : GROUPIE;
            guiGraphics.fill(pos, i - 2, pos + 80, i, getAlphaBarColor(healthColor));
            guiGraphics.fill(pos, i - 2, (int) (pos + (80 * health)), i, healthColor);
            //
            if (armor != 0) {
                guiGraphics.fill(pos, i - 4, pos + 80, i - 6, getAlphaBarColor(0xff598392));
                guiGraphics.fill(pos, i - 4, (int) (pos + (80 * armor)), i - 6, 0xff598392);
            }

            guiGraphics.fill(pos + 100, i - 2, pos + 180, i, getAlphaBarColor(0xFFff9b54));
            guiGraphics.fill(pos + 100, i - 2, (int) (pos + 100 + (80 * hunger)), i, 0xFFff9b54);

            if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
                guiGraphics.fill(pos + 100, i - 4, pos + 180, i - 6, getAlphaBarColor(0xffcae9ff));
                guiGraphics.fill(pos + 100, i - 4, (int) (pos + 100 + (80 * air)), i - 6, 0xffcae9ff);
            }
        }
        ci.cancel();
    }
    @Unique
    int getAlphaBarColor(int color){
        return KelUI.config.getBoolean("HUD.NEW_HOTBAR.COLORED_BAR", false) ? color-0x75000000 : 0x75000000;
    }
    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    void renderExperienceBar(GuiGraphics guiGraphics, int j, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        assert this.minecraft.player != null;
        double exp = this.minecraft.player.experienceProgress;

        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 0) {
            int i = this.screenHeight - 22;
            int x = 0;
            ItemStack itemStack = getCameraPlayer().getOffhandItem();
            if (!itemStack.isEmpty()) {
                x = 22;
            }
            LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
            if (livingEntity != null) {
                x += 4;
            }
            if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
                x += 4;
            }
            guiGraphics.fill(pos + 194 + x, i, pos + 196 + x, i + 20, getAlphaBarColor(SEADRIVE));
            guiGraphics.fill(pos + 194 + x, i, pos + 196 + x, (int) (i + (20 * exp)), SEADRIVE);
        } else {
            int i = this.screenHeight - 24;
            guiGraphics.fill(pos, i, pos + 180, i - 2, getAlphaBarColor(SEADRIVE));
            guiGraphics.fill(pos, i, (int) (pos + (180 * exp)), i - 2, SEADRIVE);
        }
        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    void renderExperienceLevel(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        if (this.isExperienceBarVisible() && this.minecraft.player.experienceLevel > 0) {
            int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
            if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 0) {
                int i = this.screenHeight - 22;
                int x = 0;
                ItemStack itemStack = getCameraPlayer().getOffhandItem();
                if (!itemStack.isEmpty()) {
                    x = 22;
                }
                LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
                if (livingEntity != null) {
                    x += 4;
                }
                if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
                    x += 4;
                }
                guiGraphics.drawString(minecraft.font, "" + this.minecraft.player.experienceLevel, pos + 198 + x, i + (20 / 2) - (minecraft.font.lineHeight / 2), SEADRIVE);
            } else {
                int i = this.screenHeight - 24;
                guiGraphics.drawCenteredString(minecraft.font, "" + this.minecraft.player.experienceLevel, (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 90 : this.screenWidth / 2, i - 2 - minecraft.font.lineHeight, SEADRIVE);
            }
        }
        ci.cancel();
    }

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    void renderVehicleHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.gameMode != null;
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        int i = this.screenHeight - 22;
        int x = 0;
        ItemStack itemStack = getCameraPlayer().getOffhandItem();
        if (!itemStack.isEmpty()) {
            x = 22;
        }
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 0) {
            if (getCameraPlayer().isUnderWater() || getCameraPlayer().getAirSupply() != getCameraPlayer().getMaxAirSupply()) {
                x += 4;
            }
            assert this.minecraft.gameMode != null;
            if (!this.minecraft.gameMode.canHurtPlayer()) {
                x -= 12;
            }
            x += 12;
        }

        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        if (livingEntity != null) {
            double health = livingEntity.getHealth() / livingEntity.getMaxHealth();
            guiGraphics.fill(pos + 182 + x, i, pos + 184 + x, i + 20, CLOWNFISH - 0x75000000);
            guiGraphics.fill(pos + 182 + x, i, pos + 184 + x, (int) (i + (20 * health)), CLOWNFISH);
        }
        ci.cancel();
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    void renderJumpMeter(PlayerRideableJumping playerRideableJumping, GuiGraphics guiGraphics, int j, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        assert this.minecraft.player != null;
        float f = this.minecraft.player.getJumpRidingScale();
        guiGraphics.fill(pos, screenHeight - 26, (int) (pos + (180 * f)), screenHeight - 24, 0x7fffffff);
        int k = 180 / 18;
        guiGraphics.fill(pos, screenHeight - 26, pos + (k * 16), screenHeight - 24, 0x7F598392);
        guiGraphics.fill(pos + (k * 16), screenHeight - 26, pos + 180, screenHeight - 24, CONVICT - 0x7F000000);
        ci.cancel();
    }


    @Inject(method = "renderSelectedItemName", at = @At("HEAD"), cancellable = true)
    void renderSelectedItemName(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            MutableComponent mutableComponent = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color());
            int l = (int) ((float) this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }
            int y = screenHeight - 22 - (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 0 ? 4 : 18) - minecraft.font.lineHeight;
            if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 0).intValue() == 1) {
                assert this.minecraft.gameMode != null;
                if (this.minecraft.gameMode.canHurtPlayer()) y -= 12;
            }
            if (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 1)
                guiGraphics.drawCenteredString(getFont(), mutableComponent, this.screenWidth / 2, y, 16777215 + (l << 24));
            else
                guiGraphics.drawString(getFont(), mutableComponent, KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 0).intValue() == 0 ? 6 : this.screenWidth - 6 - getFont().width(mutableComponent), y, 16777215 + (l << 24));
        }
        ci.cancel();
    }
}
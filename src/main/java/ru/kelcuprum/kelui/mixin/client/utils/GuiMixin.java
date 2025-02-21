package ru.kelcuprum.kelui.mixin.client.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.gui.Util;

import java.util.*;

import static net.minecraft.world.level.GameType.CREATIVE;
import static ru.kelcuprum.alinlib.gui.Colors.*;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Unique
    int screenWidth, screenHeight;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();
    @Shadow
    private int toolHighlightTimer;
    @Shadow
    private ItemStack lastToolHighlight;
    @Shadow
    @Final
    private DebugScreenOverlay debugOverlay;

    // Another shit

    @Shadow protected abstract boolean isExperienceBarVisible();

    @Shadow public abstract Font getFont();

    @Inject(method = "render", at = @At("HEAD"))
    void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        this.screenWidth = guiGraphics.guiWidth();
        this.screenHeight = guiGraphics.guiHeight();
    }

    // KelUI

    @Shadow
    @Final
    private LayeredDraw layers;

    @Shadow @Nullable protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Inject(method = "<init>", at = @At("RETURN"))
    void init(Minecraft minecraft, CallbackInfo ci) {
        LayeredDraw kelUILayer = new LayeredDraw()
                .add(this::renderArmorInfo)
                .add(this::renderItemInfo)
                .add(this::renderPaperDoll);
        layers.add(kelUILayer, () -> !minecraft.options.hideGui);
    }

    // - Item info
    @Unique boolean itemInfoEnable = false;
    @Unique
    public void renderItemInfo(GuiGraphics guiGraphics, DeltaTracker deltaTracker){
        if (!KelUI.config.getBoolean("HUD.ITEM_INFO", true)){
            itemInfoEnable = false;
            return;
        }
        int x = 0;
        if(KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)){
            int conf = KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue();
            if(conf == 0){
                x=184;
                if(!getCameraPlayer().getOffhandItem().isEmpty()) x+=22;
            } else if(conf == 1) {
                x = getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT ? getHotBarX()-22 : getHotBarX()+182;
                if(isAxiomMomentos()) x -= getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT ? 32 : -32;
            }
            else {
                x = getHotBarX()-22;
                if (!getCameraPlayer().getOffhandItem().isEmpty() && getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.LEFT && conf == 2) x -= 22;
            }

        } else {
            x = getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT ? ((screenWidth-180)/2)-29 : ((screenWidth-180)/2)+189;
            if(isAxiomMomentos()) x -= getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT ? 29 : -29;
        }
        if(getCameraPlayer().getMainHandItem().isEmpty()) {
            itemInfoEnable = false;
            return;
        }
        ItemStack is = getCameraPlayer().getMainHandItem();
        int count = 0;
        for(ItemStack itemStack : getCameraPlayer().getInventory().items){
            if(itemStack.getItem() == is.getItem()) count += itemStack.getCount();
        }
        if(!is.isEmpty() && getCameraPlayer().getOffhandItem().getItem() == is.getItem()) count+=getCameraPlayer().getOffhandItem().getCount();
        if(count == is.getCount() || (is.isDamageableItem() && is.getMaxStackSize() == 1)) {
            itemInfoEnable = false;
            return;
        } else {
            itemInfoEnable = true;
        }
        guiGraphics.fill(x, getHotBarY(), x+20, getHotBarY()+20, 0x75000000);
        guiGraphics.renderFakeItem(is, x+2, getHotBarY()+2, 5);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
        String countString = String.valueOf(count);
        int countStringWidth = AlinLib.MINECRAFT.font.width(countString);
        float translate = 1;
        if(countStringWidth > 16) {
            float scale = (float) 16 / countStringWidth;
            translate = (float) countStringWidth / 16;
            guiGraphics.pose().scale(scale, scale, 1);
        }
        guiGraphics.drawString(AlinLib.MINECRAFT.font, countString,  ((int) ((x+18)*translate))-AlinLib.MINECRAFT.font.width(countString), ((int) ((getHotBarY()+20)*translate))-AlinLib.MINECRAFT.font.lineHeight, -1);
        guiGraphics.pose().popPose();
    }

    @Unique
    public boolean isAxiomMomentos(){
        if (!FabricLoader.getInstance().isModLoaded("axiom")) return false;
        assert this.minecraft.gameMode != null;
        return this.minecraft.gameMode.getPlayerMode() == CREATIVE;
    }
    // - Armor Info

    @Unique
    public void renderArmorInfo(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (this.debugOverlay.showDebugScreen()) return;
        if (!KelUI.config.getBoolean("HUD.ARMOR_INFO", true)) return;
        List<ItemStack> items = new ArrayList<>();
        Map<ItemStack, Component> texts = new HashMap<>();
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
                            texts.put(item, itext);
                        }
                    }
                }
            }
            if (KelUI.config.getBoolean("HUD.ARMOR_INFO.SELECTED", false)) {
                ItemStack selected = getCameraPlayer().getInventory().getSelected();
                if (!selected.isEmpty() && selected.isDamageableItem()) {
                    items.add(selected);
                    if (KelUI.config.getBoolean("HUD.ARMOR_INFO.DAMAGE", true)) {
                        Component itext = Component.literal(KelUI.getArmorDamage(selected));
                        if (!itext.getString().isBlank()) {
                            if (KelUI.MINECRAFT.font.width(itext) > maxText)
                                maxText = KelUI.MINECRAFT.font.width(itext);
                            texts.put(selected, itext);
                        }
                    }
                }
            }
            if (KelUI.config.getBoolean("HUD.ARMOR_INFO.OFF_HAND", false)) {
                ItemStack selected = getCameraPlayer().getOffhandItem();
                if (!selected.isEmpty() && selected.isDamageableItem()) {
                    items.add(selected);
                    if (KelUI.config.getBoolean("HUD.ARMOR_INFO.DAMAGE", true)) {
                        Component itext = Component.literal(KelUI.getArmorDamage(selected));
                        if (!itext.getString().isBlank()) {
                            if (KelUI.MINECRAFT.font.width(itext) > maxText)
                                maxText = KelUI.MINECRAFT.font.width(itext);
                            texts.put(selected, itext);
                        }
                    }
                }
            }
        }
        if(items.isEmpty()) return;
        int y = (screenHeight / 2)-((20 * items.size()) / 2);
        int width = 20 + (maxText > 0 ? maxText+6 : 0);
        int x = KelUI.config.getNumber("HUD.ARMOR_INFO.POSITION", 0).intValue() == 0 ? 0 : screenWidth-width;
        int height = 20 * items.size();
        int color = 0x75000000;

        if (KelUI.config.getBoolean("HUD.ARMOR_INFO.WARNING", true)) {
            for (ItemStack item : items) {
                if (item.isDamageableItem() && (((double) (item.getMaxDamage() - item.getDamageValue()) / item.getMaxDamage()) < 0.075)) {
                    if(System.currentTimeMillis() % 500 > 250) color = GROUPIE - 0x75000000;
                    break;
                }
            }
        }
        guiGraphics.fill(x, y, x+width, y+height, color);
        int j = 0;
        for(ItemStack item : items){
            guiGraphics.renderFakeItem(item, x+2, y+(j*20)+2);
            if(texts.containsKey(item)) guiGraphics.drawString(minecraft.font, texts.get(item), x+22, y + (j * 20) + (22 / 2) - (minecraft.font.lineHeight / 2), 0xFFFFFFFF);
            j++;
        }
    }

    // - Alternative PaperDoll (very bad)

    @Unique
    public void renderPaperDoll(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (this.debugOverlay.showDebugScreen()) return;
        if (!KelUI.config.getBoolean("HUD.PAPER_DOLL", false)) return;
        assert this.minecraft.player != null;
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, screenWidth - 130, 0, screenWidth, 150, 45, 0.0625F, (float) screenWidth / 2, 75, this.minecraft.player);
    }

    // Effects

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    void renderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
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
                guiGraphics.blitSprite(RenderType::guiTexturedOverlay, mobEffectTextureManager.get(effect.getEffect()), this.screenWidth - (4 + (24 * i)) - 16, 4 + (24 * j), 16, 16);
                if (!effect.isInfiniteDuration() && KelUI.config.getBoolean("HUD.NEW_EFFECTS.TIME", true)) {
                    Component time = Component.literal(Util.getDurationAsString(effect.getDuration()));
                    guiGraphics.drawString(this.getFont(), time, this.screenWidth - (4 + (24 * i)) - 16, 20 - getFont().lineHeight + (24 * j), -1);
                }
                i++;
            }
        }
        ci.cancel();
    }

    // Hot Bar

    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    void kelUI$renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = getHotBarX();
        if(getCameraPlayer() == null) return;
        for (int slot = 0; slot < 9; slot++) {
            boolean selected = getCameraPlayer().getInventory().selected == slot;
            kelUI$renderSlot(guiGraphics, pos + (slot * 20), getHotBarY(), deltaTracker, getCameraPlayer(), getCameraPlayer().getInventory().getItem(slot), selected, false);
        }
        ItemStack off_item = getCameraPlayer().getOffhandItem();
        if (!off_item.isEmpty()) {
            int off_pos = pos + (getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.LEFT ? -22 : 182);
            kelUI$renderSlot(guiGraphics, off_pos, getHotBarY(), deltaTracker, getCameraPlayer(), off_item, false, true);
        }
        ci.cancel();
    }

    @Unique
    void kelUI$renderSlot(GuiGraphics guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, boolean isSelected, boolean renderItemColor) {
        int color = isSelected ? TETRA : 0xFF000000;
        if (!itemStack.isEmpty() && itemStack.isDamageableItem() && (isSelected || renderItemColor))
            color = (itemStack.getBarColor() | -16777216);
        guiGraphics.fill(x, y, x + 20, y + 20, color - 0x75000000);
        if (!itemStack.isEmpty()) {
            float g = (float) itemStack.getPopTime() - deltaTracker.getGameTimeDeltaTicks();
            if (g > 0.0F) {
                float h = 1.0F + g / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (x + 8), (float) (y + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, itemStack, x + 2, y + 2, 1);
            if (g > 0.0F) guiGraphics.pose().popPose();
            guiGraphics.renderItemDecorations(this.minecraft.font, itemStack, x + 2, y + 2);
        }
        if (isSelected) guiGraphics.fill(x, y + 19, x + 20, y + 21, color == 0xFF000000 ? SEADRIVE : color);
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
            int y = getHotBarY() - 18 - minecraft.font.lineHeight;
            assert this.minecraft.gameMode != null;
            if (!this.minecraft.gameMode.canHurtPlayer()) y += 12;
            if (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 1)
                guiGraphics.drawCenteredString(getFont(), mutableComponent, this.screenWidth / 2, y, 16777215 + (l << 24));
            else
                guiGraphics.drawString(getFont(), mutableComponent, KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 0 ? 6 : this.screenWidth - 6 - getFont().width(mutableComponent), y, 16777215 + (l << 24));
        }
        ci.cancel();
    }

    // Player stats

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    void renderPlayrerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 1).intValue() == 0) {
            renderStats(guiGraphics, getHotBarX()+getStatsX(), KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 2);
            ci.cancel();
            return;
        }
        if(this.minecraft.player == null) return;
        double health = this.minecraft.player.getHealth() / this.minecraft.player.getAttributeValue(Attributes.MAX_HEALTH);
        double armor = (double) this.minecraft.player.getArmorValue() / 20;
        double hunger = (double) this.minecraft.player.getFoodData().getFoodLevel() / 20;
        double air = (double) Math.max(0, this.minecraft.player.getAirSupply()) / this.minecraft.player.getMaxAirSupply();

        int healthColor = this.minecraft.player.hasEffect(MobEffects.POISON) ? 0xFFa3b18a :
                this.minecraft.player.hasEffect(MobEffects.WITHER) ? 0xff4a4e69 :
                        this.minecraft.player.isFullyFrozen() ? 0xFF90e0ef :
                                this.minecraft.player.level().getLevelData().isHardcore() ? ALINA : GROUPIE;

        //
        int size = 85;
        int pos = 95;
        int lvl = this.minecraft.player.experienceLevel;
        if (this.isExperienceBarVisible() && lvl > 0) {
            int fontSize = AlinLib.MINECRAFT.font.width(String.valueOf(lvl));
            if(fontSize > 10){
                int cut = (fontSize - 6) / 2;
                size-= cut;
                pos+= cut;
            }
        }
        //

        renderBar(guiGraphics, getHotBarX(), getHotBarY()-8, healthColor, size, health);
        if (armor != 0) renderBar(guiGraphics, getHotBarX(), getHotBarY()-12, 0xff598392, size, armor);

        //

        renderBar(guiGraphics, getHotBarX()+pos, getHotBarY()-8, 0xFFff9b54, size, hunger);
        if (this.minecraft.player.isUnderWater() || this.minecraft.player.getAirSupply() != this.minecraft.player.getMaxAirSupply())
            renderBar(guiGraphics, getHotBarX()+pos, getHotBarY()-12, 0xffcae9ff, size, air);

        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    void renderExperienceBar(GuiGraphics guiGraphics, int j, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 1).intValue() == 0) {
            renderVerticalExperienceBar(guiGraphics);
            ci.cancel();
            return;
        }
        renderBar(guiGraphics, getHotBarX(), getHotBarY()-4, SEADRIVE, 180, this.minecraft.player.experienceProgress);

        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    void renderExperienceLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.player != null;
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 1).intValue() == 0) {
            ci.cancel();
            return;
        }
        if(!this.isExperienceBarVisible()) return;
        String string = "" + this.minecraft.player.experienceLevel;
        int width = AlinLib.MINECRAFT.font.width(string);
        guiGraphics.drawString(minecraft.font, string, getHotBarX()+90-(width/2), getHotBarY()-5 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, getHotBarX()+90-(width/2), getHotBarY()-3 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, getHotBarX()+89-(width/2), getHotBarY()-4 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, getHotBarX()+91-(width/2), getHotBarY()-4 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, getHotBarX()+90-(width/2), getHotBarY()-4 - minecraft.font.lineHeight, SEADRIVE, false);
        ci.cancel();
    }

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    void renderVehicleHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        assert this.minecraft.gameMode != null;
        assert this.minecraft.player != null;
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
        if (KelUI.config.getNumber("HUD.NEW_HOTBAR.STATE_TYPE", 1).intValue() == 0) {
            renderVerticalVehicleHealth(guiGraphics);
            ci.cancel();
            return;
        }
        int size = 85;
        int lvl = this.minecraft.player.experienceLevel;
        if (this.isExperienceBarVisible() && lvl > 0) size-= lvl>9999 ? 15 : lvl>99 ? 10 : 5;

        int y = getHotBarY()-4;
        assert this.minecraft.gameMode != null;
        if (this.isExperienceBarVisible()) y-=4;
        if (this.minecraft.gameMode.canHurtPlayer()) {
            y-=4;
            if(this.minecraft.player.getArmorValue() != 0) y-=4;
        }

        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        if (livingEntity != null) {

            double health = livingEntity.getHealth() / livingEntity.getMaxHealth();
            renderBar(guiGraphics, getHotBarX(), y, CLOWNFISH, size, Math.min(health, 1));
        }
        ci.cancel();
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    void renderJumpMeter(PlayerRideableJumping playerRideableJumping, GuiGraphics guiGraphics, int j, CallbackInfo ci) {
        if (!KelUI.config.getBoolean("HUD.NEW_HOTBAR", false)) return;
        int pos = (KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 0) ? 0 : (this.screenWidth - 180) / 2;
        assert this.minecraft.player != null;
        float f = this.minecraft.player.getJumpRidingScale();
        guiGraphics.fill(pos, screenHeight - 26, (int) (pos + (180 * f)), screenHeight - 24, 0x7fffffff);
        int k = 180 / 18;
        guiGraphics.fill(pos, screenHeight - 26, pos + (k * 16), screenHeight - 24, 0x7F598392);
        guiGraphics.fill(pos + (k * 16), screenHeight - 26, pos + 180, screenHeight - 24, CONVICT - 0x7F000000);
        ci.cancel();
    }

    // - KelUI Style
    @Unique
    int rs = 0;
    @Unique
    boolean invert = false;
    @Unique
    void renderStats(GuiGraphics guiGraphics, int x, boolean invert){
        rs = x;
        if(getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT && !getCameraPlayer().getOffhandItem().isEmpty()) rs+= invert ? -22 : 22;
        if(itemInfoEnable) {
            if(KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() != 1) rs -= invert ? 22 : -22;
            else if(getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.LEFT) rs+= 22;
        }
        this.invert = invert;
        int pos = invert ? -4 : 4;
        if(this.minecraft.player == null) return;
        double health = this.minecraft.player.getHealth() / this.minecraft.player.getAttributeValue(Attributes.MAX_HEALTH);
        double armor = (double) this.minecraft.player.getArmorValue() / 20;
        double hunger = (double) this.minecraft.player.getFoodData().getFoodLevel() / 20;
        double air = (double) Math.max(0, this.minecraft.player.getAirSupply()) / this.minecraft.player.getMaxAirSupply();
        int healthColor = this.minecraft.player.hasEffect(MobEffects.POISON) ? 0xFFa3b18a :
                this.minecraft.player.hasEffect(MobEffects.WITHER) ? 0xff4a4e69 :
                        this.minecraft.player.isFullyFrozen() ? 0xFF90e0ef : GROUPIE;

        renderVerticalBar(guiGraphics, rs, getHotBarY(), healthColor, 20, health);
        rs+=pos;
        if (armor != 0) {
            renderVerticalBar(guiGraphics, rs, getHotBarY(), 0xff598392, 20, armor);
            rs+=pos;
        }
        //

        renderVerticalBar(guiGraphics, rs, getHotBarY(), 0xFFff9b54, 20, hunger);
        rs+=pos;
        if (this.minecraft.player.isUnderWater() || this.minecraft.player.getAirSupply() != this.minecraft.player.getMaxAirSupply()){
            renderVerticalBar(guiGraphics, rs, getHotBarY(), 0xffcae9ff, 20, air);
            rs+=pos;
        }
    }
    @Unique
    void renderVerticalExperienceBar(GuiGraphics guiGraphics){
        if(getCameraPlayer() == null) return;
        renderVerticalBar(guiGraphics, rs, getHotBarY(), SEADRIVE, 20, getCameraPlayer().experienceProgress);
        rs+=(invert ? -6 : 6);
        String string = "" + getCameraPlayer().experienceLevel;
        int width = AlinLib.MINECRAFT.font.width(string);
        int x = invert ? rs-width : rs;
        int y = getHotBarY() + (20 / 2) + (minecraft.font.lineHeight / 2);
        guiGraphics.drawString(minecraft.font, string, x, y-1 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, x, y+1 - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, x-1, y - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, x+1, y - minecraft.font.lineHeight, 0xFF000000, false);
        guiGraphics.drawString(minecraft.font, string, x, y - minecraft.font.lineHeight, SEADRIVE, false);
    }
    @Unique
    void renderVerticalVehicleHealth(GuiGraphics guiGraphics){
        int pos = invert ? -4 : 4;
        LivingEntity livingEntity = this.getPlayerVehicleWithHealth();
        if (livingEntity != null) {
            double health = livingEntity.getHealth() / livingEntity.getMaxHealth();
            renderBar(guiGraphics, rs, getHotBarY(), CLOWNFISH, 20, health);
            rs+=pos;
        }
    }
    
    @Unique
    int getStatsX(){
        int right = getCameraPlayer() != null && !getCameraPlayer().getOffhandItem().isEmpty() && getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.LEFT ? -26 : -4;
        return KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue() == 2 ? right : 182;
    }

    //

    @Unique
    void renderBar(GuiGraphics guiGraphics, int x, int y, int color, int size, double value){
        guiGraphics.fill(x, y, x+size, y+2, getAlphaBarColor(color));
        guiGraphics.fill(x, y, (int) (x+(size*value)), y+2, color);
    }

    @Unique
    void renderVerticalBar(GuiGraphics guiGraphics, int x, int y, int color, int size, double value){
        guiGraphics.fill(x, y, x+2, y+size, getAlphaBarColor(color));
        guiGraphics.fill(x, y, x+2, (int) (y+(size*value)), color);
    }

    @Unique
    int getAlphaBarColor(int color){
        return KelUI.config.getBoolean("HUD.NEW_HOTBAR.COLORED_BAR", false) ? color-0x75000000 : 0x75000000;
    }

    @Unique
    int getHotBarX() {
        int conf = KelUI.config.getNumber("HUD.NEW_HOTBAR.POSITION", 1).intValue();
        int x = conf == 0 ? 2 : conf == 1 ? (this.screenWidth - 180) / 2 : this.screenWidth-182;
        if(getCameraPlayer() == null) return x;
        ItemStack off_item = getCameraPlayer().getOffhandItem();
        if(!off_item.isEmpty()){
            if(getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.LEFT && conf == 0) x+=22;
            else if(getCameraPlayer().getMainArm().getOpposite() == HumanoidArm.RIGHT && conf == 2) x-=22;
        }
        return x;
    }

    @Unique
    int getHotBarY() {
        return this.screenHeight - 22;
    }
}

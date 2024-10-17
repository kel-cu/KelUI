package ru.kelcuprum.kelui.gui.cicada;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class DummyClientPlayerEntity extends LocalPlayer {
    private static DummyClientPlayerEntity instance;
    private PlayerSkin skinTextures;
    private final Player player;
    private final Options options;
    private final Component name;
    private final Boolean showItem;
    public Function<EquipmentSlot, ItemStack> equippedStackSupplier;

    public static DummyClientPlayerEntity getInstance() {
        if (instance == null) {
            instance = new DummyClientPlayerEntity();
        }

        return instance;
    }

    private DummyClientPlayerEntity() {
        super(Minecraft.getInstance(), DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance(), null, null, false, false);
        this.skinTextures = null;
        this.player = null;
        this.name = null;
        this.showItem = false;
        this.options = null;
        this.equippedStackSupplier = (slot) -> ItemStack.EMPTY;
        this.setUUID(UUID.randomUUID());
        Minecraft.getInstance().getSkinManager().getOrLoad(this.getGameProfile()).thenAccept((textures) -> this.skinTextures = textures);
    }

    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures) {
        this(player, uuid, skinTextures, null);
    }
    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures, Options options) {
        this(player, uuid, skinTextures, options, false);
    }
    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures, Options options, Boolean showItem) {
        this(player, uuid, skinTextures, options, showItem, DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance());
    }

    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures, Options options, Boolean showItem, ClientLevel world, ClientPacketListener networkHandler) {
        super(Minecraft.getInstance(), world, networkHandler, null, null, false, false);
        this.skinTextures = null;
        this.name = null;
        this.options = options;
        this.showItem = showItem;
        this.equippedStackSupplier = (slot) -> ItemStack.EMPTY;
        this.player = player;
        this.setUUID(uuid);
        this.skinTextures = skinTextures;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return this.options == null ? HumanoidArm.RIGHT : this.options.mainHand().get();
    }

    public boolean isModelPartShown(PlayerModelPart modelPart) {
        return this.options == null || this.options.isModelPartEnabled(modelPart);
    }

    public @NotNull PlayerSkin getSkin() {
        return this.skinTextures == null ? DefaultPlayerSkin.get(this.getUUID()) : this.skinTextures;
    }

    protected @Nullable PlayerInfo getPlayerInfo() {
        return null;
    }

    public boolean isSpectator() {
        return false;
    }

    public boolean isCreative() {
        return true;
    }

    public @NotNull ItemStack getItemBySlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND && showItem ? Items.MUSIC_DISC_STRAD.getDefaultInstance() :  this.player != null ? this.player.getItemBySlot(slot) : this.equippedStackSupplier.apply(slot);
    }

    public @NotNull Component getName() {
        return this.name == null ? super.getName() : this.name;
    }
}

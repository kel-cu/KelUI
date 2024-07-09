package ru.kelcuprum.kelui.gui.cicada;

import java.util.UUID;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyClientPlayerEntity extends LocalPlayer {
    private static DummyClientPlayerEntity instance;
    private PlayerSkin skinTextures;
    private final Player player;
    private Component name;
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
        this.equippedStackSupplier = (slot) -> ItemStack.EMPTY;
        this.setUUID(UUID.randomUUID());
        Minecraft.getInstance().getSkinManager().getOrLoad(this.getGameProfile()).thenAccept((textures) -> {
            this.skinTextures = textures;
        });
    }

    public DummyClientPlayerEntity(Component name) {
        this();
        this.name = name;
    }

    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures) {
        this(player, uuid, skinTextures, DummyClientWorld.getInstance(), DummyClientPlayNetworkHandler.getInstance());
    }

    public DummyClientPlayerEntity(@Nullable Player player, UUID uuid, PlayerSkin skinTextures, ClientLevel world, ClientPacketListener networkHandler) {
        super(Minecraft.getInstance(), world, networkHandler, null, null, false, false);
        this.skinTextures = null;
        this.name = null;
        this.equippedStackSupplier = (slot) -> ItemStack.EMPTY;
        this.player = player;
        this.setUUID(uuid);
        this.skinTextures = skinTextures;
    }

    public boolean isModelPartShown(PlayerModelPart modelPart) {
        return true;
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
        return this.player != null ? this.player.getItemBySlot(slot) : this.equippedStackSupplier.apply(slot);
    }

    public @NotNull Component getName() {
        return this.name == null ? super.getName() : this.name;
    }
}

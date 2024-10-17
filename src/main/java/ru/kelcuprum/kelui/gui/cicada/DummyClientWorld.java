package ru.kelcuprum.kelui.gui.cicada;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.NotNull;
import ru.kelcuprum.alinlib.gui.GuiUtils;

public class DummyClientWorld extends ClientLevel {
    private static DummyClientWorld instance;

    public static DummyClientWorld getInstance() {
        if (instance == null) {
            instance = new DummyClientWorld();
        }

        return instance;
    }

    private DummyClientWorld() {
        super(DummyClientPlayNetworkHandler.getInstance(), new ClientLevelData(Difficulty.EASY, false, true), ResourceKey.create(Registries.DIMENSION, GuiUtils.getResourceLocation("kelui", "dummy")), DummyClientPlayNetworkHandler.CURSED_DIMENSION_TYPE_REGISTRY.getHolderOrThrow(ResourceKey.create(Registries.DIMENSION_TYPE, GuiUtils.getResourceLocation("kelui", "dummy"))), 0, 0, () -> Minecraft.getInstance().getProfiler(), Minecraft.getInstance().levelRenderer, false, 0L);
    }

    public @NotNull RegistryAccess registryAccess() {
        return super.registryAccess();
    }
}

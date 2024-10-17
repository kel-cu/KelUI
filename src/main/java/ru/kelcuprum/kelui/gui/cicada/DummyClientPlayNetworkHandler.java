package ru.kelcuprum.kelui.gui.cicada;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData.Type;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ServerLinks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import ru.kelcuprum.alinlib.gui.GuiUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

public class DummyClientPlayNetworkHandler extends ClientPacketListener {
    public static final Registry<DimensionType> CURSED_DIMENSION_TYPE_REGISTRY;
    private static DummyClientPlayNetworkHandler instance;
    private static final Registry<Biome> cursedBiomeRegistry;
    private static final Registry<BannerPattern> cursedBannerRegistry;
    private static final RegistryAccess.Frozen cursedRegistryManager;

    public static DummyClientPlayNetworkHandler getInstance() {
        if (instance == null) {
            instance = new DummyClientPlayNetworkHandler();
        }

        return instance;
    }

    private DummyClientPlayNetworkHandler() {
        super(Minecraft.getInstance(), new Connection(PacketFlow.CLIENTBOUND), new CommonListenerCookie(Minecraft.getInstance().getGameProfile(), Minecraft.getInstance().getTelemetryManager().createWorldSessionManager(true, Duration.ZERO, (String)null), cursedRegistryManager.freeze(), FeatureFlagSet.of(), "", new ServerData("", "", Type.OTHER), (Screen)null, Map.of(), new ChatComponent.State(List.of(), List.of(), List.of()), false, Map.of(), ServerLinks.EMPTY));
    }

    public RegistryAccess.@NotNull Frozen registryAccess() {
        return cursedRegistryManager;
    }

    static {
        CURSED_DIMENSION_TYPE_REGISTRY = new MappedRegistry<>(Registries.DIMENSION_TYPE, Lifecycle.stable());
        Registry.register(CURSED_DIMENSION_TYPE_REGISTRY, GuiUtils.getResourceLocation("kelui", "dummy"), new DimensionType(OptionalLong.of(6000L), true, false, false, true, 1.0, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F, new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)));
        cursedBiomeRegistry = new DefaultedMappedRegistry<Biome>("dummy", Registries.BIOME, Lifecycle.stable(), true) {
            public Holder.Reference<Biome> getHolderOrThrow(ResourceKey<Biome> key) {
                return null;
            }
        };
        cursedBannerRegistry = new DefaultedMappedRegistry<>("dummy", Registries.BANNER_PATTERN, Lifecycle.stable(), true);
        cursedRegistryManager = new RegistryAccess.Frozen() {
            private final CursedRegistry<DamageType> damageTypes;

            {
                this.damageTypes = new CursedRegistry<>(Registries.DAMAGE_TYPE, GuiUtils.getResourceLocation("kelui", "fake_damage"), new DamageType("", DamageScaling.NEVER, 0.0F));
            }

            public @NotNull Optional<Registry> registry(ResourceKey key) {
                Registry x = BuiltInRegistries.REGISTRY.get(key);
                if (x != null) {
                    return Optional.of(x);
                } else if (Registries.DAMAGE_TYPE.equals(key)) {
                    return Optional.of(this.damageTypes);
                } else if (Registries.BIOME.equals(key)) {
                    return Optional.of(DummyClientPlayNetworkHandler.cursedBiomeRegistry);
                } else if (Registries.DIMENSION_TYPE.equals(key)) {
                    return Optional.of(DummyClientPlayNetworkHandler.CURSED_DIMENSION_TYPE_REGISTRY);
                } else {
                    return Registries.BANNER_PATTERN.equals(key) ? Optional.of(DummyClientPlayNetworkHandler.cursedBannerRegistry) : Optional.empty();
                }
            }

            public @NotNull Stream<RegistryEntry<?>> registries() {
                return Stream.empty();
            }
        };
    }
}

package ru.kelcuprum.kelui.mixin.server;

//import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ServerGamePacketListenerImpl.class)
public class ChatMixin {
}

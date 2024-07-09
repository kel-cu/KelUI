package ru.kelcuprum.kelui.mixin.client.utils.skins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.kelui.KelUI;

import java.util.function.Supplier;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {
    @Inject(method = "createSkinLookup", at=@At("RETURN"))
    private static void createSkinLookup(GameProfile gameProfile, CallbackInfoReturnable<Supplier<PlayerSkin>> cir){
        KelUI.playerSkin = AlinLib.MINECRAFT.getSkinManager().getInsecureSkin(gameProfile);
    }
}

package ru.kelcuprum.kelui.mixin.server;

import com.google.gson.JsonArray;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.kelui.KelUIServer;

import static ru.kelcuprum.kelui.KelUIServer.config;

@Mixin(value = MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyArgs(method = "buildServerStatus", at=@At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;<init>(Lnet/minecraft/network/chat/Component;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Z)V"))
    public void buildServerStatus(Args args){
        if(!config.getBoolean("MOTD", true)) return;
        // Инициализация описания
        String descreption = Localization.fixFormatCodes(config.getString("MOTD.LINE_FIRST", "A Minecraft Server"));

        // Подсчет времени. Если мир - null, ставит -1
        ServerLevel level = KelUIServer.MINECRAFT_SERVER.getLevel(Level.OVERWORLD);
        long dayTime = level != null ? level.getDayTime() % 24000L : -1;

        String time = dayTime < 0 ? "" : dayTime < 6000 ? config.getString("MOTD.MORNING", "Morning") : dayTime < 12000 ?
                config.getString("MOTD.DAY", "Day") : dayTime < 16500 ? config.getString("MOTD.EVENING", "Evening") : config.getString("MOTD.NIGHT", "Night");

        // Добавление типа времени, если время > 0
        if (!time.isEmpty()) {
            int countEnable = config.getNumber("MOTD.LINE_SIZE", 58).intValue() - Localization.clearFormatCodes(config.getString("MOTD.LINE_FIRST", "A Minecraft Server")).length() -
                    Localization.clearFormatCodes(time).length();
            descreption = countEnable <= 0 ? Localization.fixFormatCodes(config.getString("MOTD.LINE_FIRST", "A Minecraft Server")) :
                    Localization.fixFormatCodes(config.getString("MOTD.LINE_FIRST", "A Minecraft Server") + " ".repeat(countEnable) + time);
        }

        // В случае, если НЕ рандомная строка на 2 линии, происходит добавление 2 строки. Иначе: выбор рандомной строки.
        JsonArray messages = config.getJsonArray("MOTD.LINE_SECOND.MESSAGES", GsonHelper.parseArray("[\"KelUI MOTD Module worked\", \"Hello, im use AlinLib for better use\"]"));
        double random = Math.floor(Math.random() * messages.size());
        descreption += !config.getBoolean("MOTD.LINE_SECOND.RANDOM_MESSAGES", true) ? "\n" + Localization.fixFormatCodes(config.getString("MOTD.LINE_SECOND", "Hello, world!")) :
                messages.isEmpty() ? "\n" + Localization.fixFormatCodes(config.getString("MOTD.LINE_SECOND", "Hello, world!")) :
                        "\n" + Localization.fixFormatCodes(messages.get((int) random).getAsString());
        args.set(0, Component.nullToEmpty(descreption));
    }
}

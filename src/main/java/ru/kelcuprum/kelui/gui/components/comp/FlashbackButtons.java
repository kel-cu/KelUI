package ru.kelcuprum.kelui.gui.components.comp;

import com.moulberry.flashback.Flashback;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.kelui.gui.components.OneShotTitleButton;

public class FlashbackButtons {
    public static boolean isRecord(){
        return Flashback.RECORDER != null;
    }
    public static boolean isPaused(){
        return Flashback.RECORDER.isPaused();
    }
    public static boolean isShow(){
        return !Flashback.getConfig().hidePauseMenuControls && !Flashback.isInReplay();
    }

    public static ButtonBuilder getStateButton(){
        ButtonBuilder builder = new ButtonBuilder();
        if(isRecord()){
            builder.setOnPress((s) -> {
                        Flashback.finishRecordingReplay();
                        AlinLib.MINECRAFT.setScreen(null);
                    })
                    .setTitle(Component.translatable("kelui.flashback.finish"));
        } else {
            builder.setOnPress((s) -> {
                        Flashback.startRecordingReplay();
                        AlinLib.MINECRAFT.setScreen(null);
                    })
                    .setTitle(Component.translatable("kelui.flashback.start"));
        }
        return builder;
    }
    public static ButtonBuilder getPauseStateButton(){
        ButtonBuilder builder = new ButtonBuilder();
        if(isPaused()){
            builder.setOnPress((s) -> {
                        Flashback.pauseRecordingReplay(false);
                        AlinLib.MINECRAFT.setScreen(null);
                    })
                    .setTitle(Component.translatable("kelui.flashback.unpause"));
        } else {
            builder.setOnPress((s) -> {
                        Flashback.pauseRecordingReplay(true);
                        AlinLib.MINECRAFT.setScreen(null);
                    })
                    .setTitle(Component.translatable("kelui.flashback.pause"));
        }
        return builder;
    }
    public static ButtonBuilder getCancelButton(){
        return new ButtonBuilder(Component.translatable("kelui.flashback.cancel")).setOnPress((s) -> {
            AlinLib.MINECRAFT.setScreen(new ConfirmScreen(value -> {
                if (value) {
                    Flashback.cancelRecordingReplay();
                    AlinLib.MINECRAFT.setScreen(null);
                } else {
                    AlinLib.MINECRAFT.setScreen(new PauseScreen(true));
                }
            }, Component.literal("Confirm Cancel Recording"),
                    Component.literal("Are you sure you want to cancel the recording? You won't be able to recover it")));
        });
    }

    public static OneShotTitleButton getStateButton$oneShot(int x, int y, int width, int height){
        if(isRecord()){
            return new OneShotTitleButton(x, y, width, height, Component.translatable("kelui.flashback.finish"), (s) -> {
                        Flashback.finishRecordingReplay();
                        AlinLib.MINECRAFT.setScreen(null);
                    });
        } else {
            return new OneShotTitleButton(x, y, width, height, Component.translatable("kelui.flashback.start"), (s) -> {
                        Flashback.startRecordingReplay();
                        AlinLib.MINECRAFT.setScreen(null);
                    });
        }
    }
    public static OneShotTitleButton getPauseStateButton$oneShot(int x, int y, int width, int height){
        if(isPaused()){
            return new OneShotTitleButton(x, y, width, height, Component.translatable("kelui.flashback.unpause"), (s) -> {
                        Flashback.pauseRecordingReplay(false);
                        AlinLib.MINECRAFT.setScreen(null);
                    });
        } else {
            return new OneShotTitleButton(x, y, width, height, Component.translatable("kelui.flashback.pause"), (s) -> {
                        Flashback.pauseRecordingReplay(true);
                        AlinLib.MINECRAFT.setScreen(null);
                    });
        }
    }
    public static OneShotTitleButton getCancelButton$oneShot(int x, int y, int width, int height){
        return new OneShotTitleButton(x, y, width, height, Component.translatable("kelui.flashback.cancel"), (s) -> {
            AlinLib.MINECRAFT.setScreen(new ConfirmScreen(value -> {
                if (value) {
                    Flashback.cancelRecordingReplay();
                    AlinLib.MINECRAFT.setScreen(null);
                } else {
                    AlinLib.MINECRAFT.setScreen(new PauseScreen(true));
                }
            }, Component.literal("Confirm Cancel Recording"),
                    Component.literal("Are you sure you want to cancel the recording? You won't be able to recover it")));
        });
    }

}

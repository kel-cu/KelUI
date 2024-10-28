package ru.kelcuprum.kelui.mixin.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.info.Player;
import ru.kelcuprum.kelui.KelUI;
import ru.kelcuprum.kelui.SoundLoader;

import java.util.Objects;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    @Shadow @Final private boolean hardcore;

    @Shadow private Component deathScore;

    @Shadow protected abstract void handleExitToTitleScreen();

    @Shadow @Final private Component causeOfDeath;

    @Shadow @Nullable protected abstract Style getClickedComponentStyleAt(int i);

    @Unique public boolean isInited = false;

    protected DeathScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    void init(CallbackInfo cl) {
        if(isNiko() && KelUI.config.getBoolean("DEATH.NIKO", false)) {
            cl.cancel();
        } else if(KelUI.config.getBoolean("DEATH", true)){
            this.deathScore = Component.translatable("deathScreen.score.value", new Object[]{Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW)});
            Component component = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");
            int x = (width / 2) - 102;
            int y = Math.max(165, (height/2) - 10);
            addRenderableWidget(new ButtonBuilder(component, (s) -> this.minecraft.player.respawn())
                    .setPosition(x, y).setSize(100, 20).build());
            addRenderableWidget(new ButtonBuilder(Component.translatable("deathScreen.titleScreen"),
                    (s) -> this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true))
                    .setPosition(x+104, y).setSize(100, 20).build());
            cl.cancel();
        }
    }
    private long startTime = System.currentTimeMillis();
    private int timeShow = 2000;
    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if(isNiko() && KelUI.config.getBoolean("DEATH.NIKO", false)){
            int back = (int) (255.0F * (Math.clamp((double) (System.currentTimeMillis()-startTime) / timeShow,  0.0, 1.0))) << 24;
            guiGraphics.fillGradient(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), back,back);
            ci.cancel();
        }
        else if(KelUI.config.getBoolean("DEATH", true)){
            renderBlurredBackground();
            int back = (int) (255.0F * (Math.clamp((double) (System.currentTimeMillis()-startTime) / timeShow,  0.0, 1.0)*0.5));
            int bottom = ARGB.color(back, 161, 67, 67);
            int top = (int) (255.0F * (Math.clamp((double) (System.currentTimeMillis()-startTime) / timeShow,  0.0, 1.0)*0.5)) << 24;
            guiGraphics.fillGradient(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), top, bottom);
        }
    }

    @Unique
    public boolean isNiko(){
        return !KelUI.config.getBoolean("DEATH.NIKO.ONLY_HARDCORE", true) || hardcore;
    }
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        super.render(guiGraphics, i, j, f);
        if(isNiko() && KelUI.config.getBoolean("DEATH.NIKO", false)){
            ci.cancel();
            if(!isInited && (System.currentTimeMillis()-startTime) > timeShow) {
                isInited = true;
                TinyFileDialogs.tinyfd_messageBox("", String.format("You killed %s.", Player.getName()), "ok", "error", true);
                switch (KelUI.config.getNumber("DEATH.NIKO.FUNCTION", 0).intValue()){
                    case 1 -> {
                        this.minecraft.player.respawn();
                        this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true);
                    }
                    case 2 -> {
                        minecraft.getSoundManager().stop();
                        SoundLoader.tryPlay();
                        minecraft.stop();
                    }
                    default -> this.minecraft.player.respawn();
                }

            }
        } else if(KelUI.config.getBoolean("DEATH", true)){
            int y = 60;

            guiGraphics.blit(RenderType::guiTextured, ResourceLocation.fromNamespaceAndPath("kelui", "textures/gui/death_screen/icon.png"), (width/2)-25, y, 0,0, 50,50, 50, 50);
            y+=50;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
            guiGraphics.drawCenteredString(this.font, this.title, this.width / 2 / 2, y/2, 16777215);
            guiGraphics.pose().popPose();
            y+=25;
            if (this.causeOfDeath != null) {
                guiGraphics.drawCenteredString(this.font, this.causeOfDeath, this.width / 2, y, 16777215);
            }
            y+=15;

            guiGraphics.drawCenteredString(this.font, this.deathScore, this.width / 2, y, 16777215);
            if (this.causeOfDeath != null && j > (y-15)) {
                Objects.requireNonNull(this.font);
                if (j < (y-15) + 9) {
                    Style style = this.getClickedComponentStyleAt(i);
                    guiGraphics.renderComponentHoverEffect(this.font, style, i, j);
                }
            }
            ci.cancel();
        }

    }
}

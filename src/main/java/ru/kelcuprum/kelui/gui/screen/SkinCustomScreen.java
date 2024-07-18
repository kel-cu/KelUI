package ru.kelcuprum.kelui.gui.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.lwjgl.glfw.GLFW;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.kelui.gui.components.comp.CapesButtons;
import ru.kelcuprum.kelui.gui.components.PlayerButton;

import static ru.kelcuprum.alinlib.gui.Colors.BLACK_ALPHA;

public class SkinCustomScreen extends Screen {
    public Screen parent;
    public Options options;
    public SkinCustomScreen(Screen parent, Options options) {
        super(Component.translatable("options.skinCustomisation.title"));
        this.parent = parent;
        this.options = options;
    }
    PlayerButton pb;
    @Override
    protected void init() {
        pb = addRenderableWidget(new PlayerButton(50, height/2-100, 100, true, true, false, false));
        pb.active = false;
        int pageSize = this.width-200;
        int componentSize =  Math.min(310, pageSize - 10);
        int x = ((pageSize - componentSize) / 2)+200;
        int y = height/2-46;
        if(FabricLoader.getInstance().isModLoaded("capes")) y-=12;
        addRenderableWidget(new TextBox(x, y-24, componentSize, 20, this.title, true));
        PlayerModelPart[] values = PlayerModelPart.values();
        int pos = 0;
        for(PlayerModelPart value : values){
            ButtonBooleanBuilder builder = new ButtonBooleanBuilder(value.getName(), this.options.isModelPartEnabled(value))
                    .setOnPress((s) -> this.options.toggleModelPart(value, s))
                    .setWidth(componentSize/2-2)
                    .setPosition(pos == 0 ? x : x+(componentSize/2+2), y);
            addRenderableWidget(builder.build());
            pos++;
            if(pos>1) {
                pos = 0;
                y+=24;
            }
        }
        String[] arm = {
                Component.translatable("options.mainHand.right").getString(),
                Component.translatable("options.mainHand.left").getString()
        };
        SelectorBuilder builder = new SelectorBuilder(Component.translatable("options.mainHand"))
                .setList(arm)
                .setValue(options.mainHand().get() == HumanoidArm.RIGHT ? 0 : 1)
                .setOnPress((s) -> options.mainHand().set(s.getPosition() == 0 ? HumanoidArm.RIGHT : HumanoidArm.LEFT))
                .setWidth(componentSize/2-2)
                .setPosition(pos == 0 ? x : x+(componentSize/2+2), y);
        addRenderableWidget(builder.build());
        pos++;
        if(pos>1) {
            pos = 0;
            y+=24;
        }

        if(FabricLoader.getInstance().isModLoaded("capes")) addRenderableWidget(CapesButtons.getCapesButton()
                .setWidth(pos == 0 ? componentSize : (componentSize/2-2))
                .setPosition(pos == 0 ? x : x+(componentSize/2+2), y).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_P && (modifiers & GLFW.GLFW_MOD_SHIFT) != 0){
            AlinLib.MINECRAFT.setScreen(new SkinCustomizationScreen(parent, options));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBackground(guiGraphics, i, j, f);
        guiGraphics.fill(0, 0, 200, height, BLACK_ALPHA);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if(pb != null) pb.setRotation((float) (pb.getRotation()-f));

        return super.mouseDragged(d, e, i, f, g);
    }
}

package ru.kelcuprum.kelmenu.gui.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonBoolean;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.kelmenu.KelMenu;

public class ChatConfigScreen extends Screen {
    private final Screen parent;

    private static final Component MainConfigCategory = Localization.getText("kelmenu.config.title.main_menu");
    private Button MainConfigCategoryButton;
    private static final Component PauseConfigCategory = Localization.getText("kelmenu.config.title.pause_menu");
    private Button PauseConfigCategoryButton;
    private static final Component ChatConfigCategory = Localization.getText("kelmenu.config.title.chat");
    private Button ChatConfigCategoryButton;
    private static final Component TITLE = Localization.getText("kelmenu.name");
    private static final Component EXIT = Localization.getText("kelmenu.exit");
    private int scrolled = 0;

    private TextBox titleBox;
    private ButtonBoolean edgelessChatScreen;

    public ChatConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }
    public void tick() {
        this.titleBox.setYPos(15 - this.scrolled);
        this.edgelessChatScreen.setYPos(40 - this.scrolled);
        super.tick();
    }

    @Override
    public void init() {
        initButton();
        initButtonsCategory();
    }
    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f){
        InterfaceUtils.renderBackground(guiGraphics, this.minecraft);
        InterfaceUtils.renderTextureLeftPanel(guiGraphics, 130, this.height);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.drawCenteredString(this.minecraft.font, this.title, 130 / 2, 15, -1);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

    }
    private void initButtonsCategory(){
        int width = this.width - 150;
        int x = 140;
        //
        this.titleBox = new TextBox(x, 15, width, 9, ChatConfigCategory, true);
        addRenderableWidget(titleBox);
        this.edgelessChatScreen = new ButtonBoolean(x, 40, width, 20, KelMenu.config, "CHAT.EDGELESS_SCREEN", true, Localization.getText("kelmenu.config.edgeless_chat_screen"));
        addRenderableWidget(edgelessChatScreen);
    }
    private void initButton(){
        // line 0
        this.MainConfigCategoryButton = this.addRenderableWidget(new Button(10, 40, 110, 20, MainConfigCategory, (OnPress) -> {
            this.minecraft.setScreen(new MenuConfigScreen(this.parent));
        }));
        this.PauseConfigCategoryButton = this.addRenderableWidget(new Button(10, 65, 110, 20, PauseConfigCategory, (OnPress) -> {
            this.minecraft.setScreen(new PauseConfigScreen(this.parent));
        }));
        this.ChatConfigCategoryButton = this.addRenderableWidget(new Button(10, 90, 110, 20, ChatConfigCategory, (OnPress) -> {
            this.minecraft.setScreen(new ChatConfigScreen(this.parent));
        }));

        this.ChatConfigCategoryButton.setActive(false);

        addRenderableWidget(new Button(10, height - 30, 110, 20, 0xB6FF3131, EXIT, (OnPress) -> {
            this.minecraft.setScreen(parent);
        }));
    }


    public boolean mouseScrolled(double d, double e, double f, double g) {
        int scrolled = (int)((double)this.scrolled + g * 10.0 * -1.0);
        int size = 215;
        if (scrolled <= 0 || size <= this.height) {
            this.scrolled = 0;
        } else this.scrolled = Math.min(scrolled, size - this.height);

        return super.mouseScrolled(d, e, f, g);
    }
}

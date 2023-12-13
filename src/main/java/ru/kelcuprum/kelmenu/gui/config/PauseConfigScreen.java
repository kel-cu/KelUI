package ru.kelcuprum.kelmenu.gui.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.buttons.Button;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonBoolean;
import ru.kelcuprum.alinlib.gui.components.editbox.EditBoxString;
import ru.kelcuprum.alinlib.gui.components.selector.SelectorStringWithIntButton;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.kelmenu.KelMenu;

public class PauseConfigScreen extends Screen {
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
    private ButtonBoolean pauseMenu;
    private ButtonBoolean alpha;
    private ButtonBoolean version;
    private ButtonBoolean player;
    //
    private TextBox shortCommandTitle;
    private ButtonBoolean enableShortCommand;
    private EditBoxString shortCommandName;
    private EditBoxString shortCommand;

    public PauseConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }
    public void tick() {
        this.titleBox.setYPos(15 - this.scrolled);
        this.pauseMenu.setYPos(40 - this.scrolled);
        this.alpha.setYPos(65 - this.scrolled);
        this.version.setYPos(90 - this.scrolled);
        this.player.setYPos(115 - this.scrolled);

        this.shortCommandTitle.setYPos(140 - this.scrolled);
        this.enableShortCommand.setYPos(165 - this.scrolled);
        this.shortCommandName.setYPos(190 - this.scrolled);
        this.shortCommand.setYPos(215 - this.scrolled);
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
        this.titleBox = new TextBox(x, 15, width, 9, PauseConfigCategory, true);
        addRenderableWidget(titleBox);
        this.pauseMenu = new ButtonBoolean(x, 40, width, 20, KelMenu.config, "PAUSE_MENU", true, Localization.getText("kelmenu.config.pause_menu"));
        addRenderableWidget(pauseMenu);
        this.alpha = new ButtonBoolean(x, 65, width, 20, KelMenu.config, "PAUSE_MENU.ALPHA", true, Localization.getText("kelmenu.config.pause_menu.alpha"));
        addRenderableWidget(alpha);
        this.version = new ButtonBoolean(x, 90, width, 20, KelMenu.config, "PAUSE_MENU.VERSION", true, Localization.getText("kelmenu.config.pause_menu.version"));
        addRenderableWidget(version);
        this.player = new ButtonBoolean(x, 115, width, 20, KelMenu.config, "PAUSE_MENU.PLAYER", true, Localization.getText("kelmenu.config.pause_menu.player"));
        addRenderableWidget(player);

        this.shortCommandTitle = new TextBox(x, 140, width, 20, Localization.getText("kelmenu.config.pause_menu.short_command"), false);
        addRenderableWidget(shortCommandTitle);
        this.enableShortCommand = new ButtonBoolean(x, 165, width, 20, KelMenu.config, "PAUSE_MENU.ENABLE_SHORT_COMMAND", false, Localization.getText("kelmenu.config.pause_menu.enable_short_command"));
        addRenderableWidget(enableShortCommand);

        this.shortCommandName = new EditBoxString(x, 190, width, 20, Localization.getText("kelmenu.config.pause_menu.short_command.name"));
        this.shortCommandName.setContent(KelMenu.config.getString("PAUSE_MENU.SHORT_COMMAND.NAME", "Lobby"));
        this.shortCommandName.setResponse(s-> KelMenu.config.setString("PAUSE_MENU.SHORT_COMMAND.NAME", s));
        addRenderableWidget(shortCommandName);

        this.shortCommand = new EditBoxString(x, 215, width, 20, Localization.getText("kelmenu.config.pause_menu.short_command.command"));
        this.shortCommand.setContent(KelMenu.config.getString("PAUSE_MENU.SHORT_COMMAND.COMMAND", "/lobby"));
        this.shortCommand.setResponse(s-> KelMenu.config.setString("PAUSE_MENU.SHORT_COMMAND.COMMAND", s));
        addRenderableWidget(shortCommand);
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

        this.PauseConfigCategoryButton.setActive(false);

        addRenderableWidget(new Button(10, height - 30, 110, 20, 0xB6FF3131, EXIT, (OnPress) -> {
            this.minecraft.setScreen(parent);
        }));
    }


    public boolean mouseScrolled(double d, double e, double f, double g) {
        int scrolled = (int)((double)this.scrolled + g * 10.0 * -1.0);
        int size = 240;
        if (scrolled <= 0 || size <= this.height) {
            this.scrolled = 0;
        } else this.scrolled = Math.min(scrolled, size - this.height);

        return super.mouseScrolled(d, e, f, g);
    }
}

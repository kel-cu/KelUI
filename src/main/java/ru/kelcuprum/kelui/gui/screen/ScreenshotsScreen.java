package ru.kelcuprum.kelui.gui.screen;

import net.minecraft.client.gui.screens.Screen;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.kelui.gui.components.ScreenshotButton;

import java.io.File;

import static ru.kelcuprum.alinlib.gui.GuiUtils.DEFAULT_WIDTH;

public class ScreenshotsScreen {
    public static Screen build(Screen parent){
        ConfigScreenBuilder csb = new ConfigScreenBuilder(parent);
        File dir = new File("./screenshots");
        if(dir.exists() && dir.isDirectory()){
            for(File file : dir.listFiles()){
                if(file.isFile()) csb.addWidget(new ScreenshotButton(0, 0, DEFAULT_WIDTH(), file));
            }
        }
        return csb.build();
    }
}

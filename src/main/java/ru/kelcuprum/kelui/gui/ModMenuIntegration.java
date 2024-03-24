package ru.kelcuprum.kelui.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import ru.kelcuprum.kelui.gui.screen.config.HUDConfigScreen;
import ru.kelcuprum.kelui.gui.screen.config.MenuConfigScreen;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() { return FabricLoader.getInstance().isModLoaded("controlify") ? new HUDConfigScreen()::build : new MenuConfigScreen()::build; }
}
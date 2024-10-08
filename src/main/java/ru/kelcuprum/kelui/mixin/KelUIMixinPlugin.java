package ru.kelcuprum.kelui.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class KelUIMixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOG = LogManager.getLogger("KelUI > Mixin");
    public static boolean isSodiumExtraEnable = FabricLoader.getInstance().isModLoaded("sodium-extra");
    public static boolean isSodiumEnable = FabricLoader.getInstance().isModLoaded("sodium");
    public static String sodiumVersion = isSodiumEnable ? FabricLoader.getInstance().getModContainer("sodium").get().getMetadata().getVersion().getFriendlyString() : "";
    public static boolean isSSEnable = FabricLoader.getInstance().isModLoaded("skinshuffle");
    public static boolean isCatalogueEnable = FabricLoader.getInstance().isModLoaded("catalogue");
    public static boolean isDarkLoadingScreen = FabricLoader.getInstance().isModLoaded("dark-loading-screen");
    public static boolean isCustomSplashScreen = FabricLoader.getInstance().isModLoaded("customsplashscreen");
    public static boolean isCarpetEnable = FabricLoader.getInstance().isModLoaded("carpet");
    public static boolean isModMenuEnable = (FabricLoader.getInstance().isModLoaded("modmenu") || FabricLoader.getInstance().isModLoaded("menulogue"));
    public static boolean isBPDInstalled = false;
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.")){
            return false;
        }
        if(FabricLoader.getInstance().isModLoaded("betterpingdisplay") && mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.utils.PlayerListMixin")){
            isBPDInstalled = true;
            LOG.warn(String.format("Mixin %s (Ping Display) for %s not loaded, Better Display Ping not compatibility", mixinClassName, targetClassName));
        }
        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.skinshuffle.")){
            if(isSSEnable) LOG.warn(String.format("Mixin %s for %s loaded, %s", mixinClassName, targetClassName, "SkinShuffle installed"));
            return isSSEnable;
        }

        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.catalogue.")){
            if(isCatalogueEnable) LOG.warn(String.format("Mixin %s for %s loaded, %s", mixinClassName, targetClassName, "Catalogue installed"));
            return isCatalogueEnable;
        }

        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.sodium_extra.")){
            if(isSodiumExtraEnable) LOG.warn(String.format("Mixin %s for %s loaded, %s", mixinClassName, targetClassName, "Sodium Extra installed"));
            return isSodiumExtraEnable;
        }
        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.utils.GuiMixin$TabList")){
            if(isCarpetEnable) LOG.warn(String.format("Mixin %s for %s not loaded, %s", mixinClassName, targetClassName, "Carpet installed"));
            return !isCarpetEnable;
        }

        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.LoadingOverlayMixin")){
            if(isDarkLoadingScreen) LOG.warn(String.format("Mixin %s for %s not loaded, %s", mixinClassName, targetClassName, "Dark Loading Screen installed"));
            else if(isCustomSplashScreen) LOG.warn(String.format("Mixin %s for %s not loaded, %s", mixinClassName, targetClassName, "CustomSplashScreen installed"));
            return !(isDarkLoadingScreen || isCustomSplashScreen);
        }
        if(mixinClassName.startsWith("ru.kelcuprum.kelui.mixin.client.screen.sodium")){
            if(isModMenuEnable && isSodiumEnable){
                LOG.warn(String.format("Mixin %s for %s not loaded, %s", mixinClassName, targetClassName, "Sodium installed, but ModMenu installed"));
                return false;
            } else if(isSodiumEnable || sodiumVersion.startsWith("0.6")) {
                LOG.warn(String.format("Mixin %s for %s loaded, %s", mixinClassName, targetClassName, "Sodium (0.6.x) installed"));
                return true;
            } else return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

package ru.kelcuprum.kelui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class IconStorageHelper {
    private static final String RESOURCES_ROOT = "assets/kelui/icons/";
    private static final Map<String, byte[]> STORAGE = Maps.newLinkedHashMap();
    private static final List<String> PNG_PATHS = Lists.newArrayList();
    private static boolean inited = false;


    public synchronized void init() {
        if (inited) {
            return;
        }

        loadResource("icon_16x16.png");
        loadResource("icon_32x32.png");
        loadResource("icon_48x48.png");
        loadResource("icon_128x128.png");
        loadResource("icon_256x256.png");
        loadResource("minecraft.icns", false);

        inited = true;
    }

    private void loadResource(String path, boolean isPng) {
        String fullPath = RESOURCES_ROOT + path;
        ClassLoader classLoader = this.getClass().getClassLoader();

        try (InputStream stream = classLoader.getResourceAsStream(fullPath)) {
            if (stream == null) {
                throw new IOException("getResourceAsStream failed");
            }
            byte[] data = IOUtils.toByteArray(stream);
            STORAGE.put(path, data);
            if (isPng) {
                PNG_PATHS.add(path);
            }
        } catch (IOException e) {
            KelUI.log(String.format("Failed to load resource %s: %s", fullPath, e));
        }
    }

    private void loadResource(String path) {
        this.loadResource(path, true);
    }
    public InputStream getResource(String path) {
        if(KelUI.config.getBoolean("GLOBAL.ENABLE_CUSTOM_ICON.MOD", true)) {
            byte[] data = STORAGE.get(path);
            if (data == null) {
                throw new RuntimeException("Unexpected resource path " + path);
            }
            return new ByteArrayInputStream(data);
        } else {
            String dir = KelUI.config.getString("GLOBAL.CUSTOM_ICON_PATH", "config/KelUI/icons/");
            File file = new File(dir+path);
            if(file.exists()){
//                InputStream stream = file.toURL().openStream();
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    return new ByteArrayInputStream(fileContent);
                } catch (Exception e){
                    KelUI.log(e.getLocalizedMessage(), Level.ERROR);
                    return null;
                }
            } else return null;
        }
    }
}

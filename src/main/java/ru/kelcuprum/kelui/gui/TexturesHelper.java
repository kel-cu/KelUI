
package ru.kelcuprum.kelui.gui;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Async;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.kelui.KelUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class TexturesHelper {
    public static HashMap<File, ResourceLocation> resourceLocationMap = new HashMap<>();
    public static HashMap<File, Boolean> urls = new HashMap<>();
    public static HashMap<File, DynamicTexture> urlsTextures = new HashMap<>();
    public static HashMap<File, BufferedImage> urlsImages = new HashMap<>();

    public static ResourceLocation getTexture(File file) {
        if (resourceLocationMap.containsKey(file)) return resourceLocationMap.get(file);
        else {
            if (!urls.getOrDefault(file, false)) {
                urls.put(file, true);
                new Thread(() -> registerTexture(file, AlinLib.MINECRAFT.getTextureManager(), GuiUtils.getResourceLocation("kelui", formatUrls(file.getName().toLowerCase())))).start();
            }
            return GuiUtils.getResourceLocation("kelui", "textures/no_icon.png");
        }
    }

    @Async.Execute
    public static void registerTexture(File file, TextureManager textureManager, ResourceLocation textureId) {
        KelUI.log(String.format("REGISTER: %s", file.getName()), Level.DEBUG);
        DynamicTexture texture;
        if(urlsTextures.containsKey(file)) {
            texture = urlsTextures.get(file);
        }
        else {
            NativeImage image;
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                double scale = (double) bufferedImage.getHeight() / 128;
                BufferedImage scaleImage = toBufferedImage(bufferedImage.getScaledInstance((int) (bufferedImage.getWidth()/scale), (int) (bufferedImage.getHeight()/scale), 2));
                urlsImages.put(file, scaleImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(scaleImage, "png", byteArrayOutputStream);
                byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
                image = NativeImage.read(bytesOfImage);
            } catch (Exception e) {
                KelUI.log("Error loading image from URL: " + file.getName() + " - " + e.getMessage());
                resourceLocationMap.put(file, GuiUtils.getResourceLocation("kelui", "fuck_off_file"));
                return;
            }
            texture = new DynamicTexture(image);
        }
        textureManager.register(textureId, texture);
        resourceLocationMap.put(file, textureId);
    }

    public static String formatUrls(String url) {
        return url.replace(" ", "_").replace("/", "_").replace(":", "_");
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }

}

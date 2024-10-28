package ru.kelcuprum.kelui.gui.cicada;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

public class GuiEntityRenderer {
    public static void drawEntity(PoseStack matrices, int x, int y, int size, float rotation, double mouseX, double mouseY, LivingEntity entity) {
        if (entity != null) {
            float yaw = (float)Math.atan(mouseX / 40.0);
            float pitch = (float)Math.atan(mouseY / 40.0);
            Quaternionf entityRotation = (new Quaternionf()).rotateZ(3.1415927F);
            Quaternionf pitchRotation = (new Quaternionf()).rotateX(pitch * 20.0F * 0.017453292F);
            entityRotation.mul(pitchRotation);
            float oldBodyYaw = entity.yBodyRot;
            float oldYaw = entity.getYRot();
            float oldPitch = entity.getXRot();
            float oldPrevHeadYaw = entity.yHeadRotO;
            float oldHeadYaw = entity.yHeadRot;
            entity.yBodyRot = 180.0F + yaw * 20.0F + rotation;
            entity.setYRot(180.0F + yaw * 40.0F + rotation);
            entity.setXRot(-pitch * 20.0F);
            entity.yHeadRot = entity.getYRot();
            entity.yHeadRotO = entity.getYRot();
            Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushMatrix();
            modelViewStack.translate(0.0F, 0.0F, 1000.0F);
            RenderSystem.backupProjectionMatrix();
            matrices.pushPose();
            matrices.translate(x, y, -950.0);
            matrices.mulPose((new Matrix4f()).scaling((float)size, (float)size, (float)(-size)));
            matrices.translate(0.0F, -1.0F, 0.0F);
            matrices.mulPose(entityRotation);
            matrices.translate(0.0F, -1.0F, 0.0F);
            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            if (pitchRotation != null) {
                pitchRotation.conjugate();
                dispatcher.overrideCameraOrientation(pitchRotation);
            }

            dispatcher.setRenderShadow(false);
            if (FabricLoader.getInstance().isModLoaded("entity_texture_features")) {
                ETFCompat.preventRenderLayerIssue();
            }

            MultiBufferSource.BufferSource vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
//            dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrices, vertexConsumers, 15728880);
            dispatcher.render(entity, 0.0, 0.0, 0.0, 1.0F, matrices, vertexConsumers, 15728880);
            vertexConsumers.endBatch();
            dispatcher.setRenderShadow(true);
            matrices.popPose();
            Lighting.setupFor3DItems();
            modelViewStack.popMatrix();
            RenderSystem.getModelViewMatrix();
            entity.yBodyRot = oldBodyYaw;
            entity.setYRot(oldYaw);
            entity.setXRot(oldPitch);
            entity.yHeadRotO = oldPrevHeadYaw;
            entity.yHeadRot = oldHeadYaw;
        }
    }
}

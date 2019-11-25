package com.minelittlepony.client.render.entity;

import com.minelittlepony.client.model.ModelType;
import com.minelittlepony.client.model.entity.ModelEnderStallion;
import com.minelittlepony.client.render.entity.feature.LayerEyeGlow;
import com.minelittlepony.client.render.entity.feature.LayerHeldPonyItem;
import com.minelittlepony.client.render.entity.feature.LayerHeldPonyItemMagical;
import com.minelittlepony.client.render.entity.feature.LayerEyeGlow.IGlowingRenderer;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Random;

public class RenderEnderStallion extends RenderPonyMob<EndermanEntity, ModelEnderStallion> implements IGlowingRenderer {

    public static final Identifier ENDERMAN = new Identifier("minelittlepony", "textures/entity/enderman/enderman_pony.png");
    private static final Identifier EYES = new Identifier("minelittlepony", "textures/entity/enderman/enderman_pony_eyes.png");

    private final Random rnd = new Random();

    public RenderEnderStallion(EntityRenderDispatcher manager) {
        super(manager, ModelType.ENDERMAN);
    }

    @Override
    protected void addLayers() {
        addFeature(createItemHoldingLayer());
        addFeature(new StuckArrowsFeatureRenderer<>(this));
        addFeature(new LayerEyeGlow<>(this));
    }

    @Override
    protected LayerHeldPonyItem<EndermanEntity, ModelEnderStallion> createItemHoldingLayer() {
        return new LayerHeldPonyItemMagical<EndermanEntity, ModelEnderStallion>(this) {
            @Override
            protected ItemStack getRightItem(EndermanEntity entity) {
                BlockState state = entity.getCarriedBlock();
                if (state == null) {
                    return ItemStack.EMPTY;
                }

                return new ItemStack(state.getBlock().asItem());
            }
        };
    }

    @Override
    public Identifier findTexture(EndermanEntity entity) {
        return ENDERMAN;
    }

    @Override
    public void render(EndermanEntity entity, float entityYaw, float tickDelta, MatrixStack stack, VertexConsumerProvider renderContext, int lightUv) {
        ModelEnderStallion modelenderman = getModel();

        modelenderman.isCarrying = entity.getCarriedBlock() != null;
        modelenderman.isAttacking = entity.isAngry();

        if (entity.isAngry()) {
            stack.translate(rnd.nextGaussian() / 50, 0, rnd.nextGaussian() / 50);
        }

        super.render(entity, entityYaw, tickDelta, stack, renderContext, lightUv);
    }

    @Override
    public Identifier getEyeTexture() {
        return EYES;
    }
}

package com.minelittlepony.model.ponies;

import static net.minecraft.client.renderer.GlStateManager.*;

import com.minelittlepony.model.ModelMobPony;
import com.minelittlepony.model.armour.ModelSkeletonPonyArmor;
import com.minelittlepony.model.armour.PonyArmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

public class ModelSkeletonPony extends ModelMobPony {

    public ModelSkeletonPony() {
        super();
    }
    
    @Override
    public PonyArmor createArmour() {
        return new PonyArmor(new ModelSkeletonPonyArmor(), new ModelSkeletonPonyArmor());
    }
    
    public void setLivingAnimations(EntityLivingBase entity, float move, float swing, float ticks) {
        rightArmPose = ModelBiped.ArmPose.EMPTY;
        leftArmPose = ModelBiped.ArmPose.EMPTY;
        ItemStack itemstack = entity.getHeldItem(EnumHand.MAIN_HAND);

        if (itemstack.getItem() == Items.BOW && ((AbstractSkeleton)entity).isSwingingArms())
        {
            if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
                rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            } else {
                leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            }
        }

        super.setLivingAnimations(entity, move, swing, ticks);
    }

    @Override
    protected void rotateLegs(float move, float swing, float tick, Entity entity) {
        super.rotateLegs(move, swing, tick, entity);
        aimBow(leftArmPose, rightArmPose, tick);
    }
    
    @Override
    protected void fixSpecialRotationPoints(float move) {
        if (rightArmPose != ArmPose.EMPTY && !metadata.hasMagic()) {
            bipedRightArm.setRotationPoint(-1.5F, 9.5F, 4);
        }
    }
    
    // TODO: HACK It would be better to just change the size of the legs.
    private void renderScaledArm(ModelRenderer arm, float x, float y, float z) {
        scale(x, y, z);
        x /= 1.5f;
        z /= 1.5f;
        arm.rotateAngleX /= x;
        arm.rotateAngleY /= y;
        arm.rotateAngleZ /= z;
        arm.render(scale);
        arm.rotateAngleX *= x;
        arm.rotateAngleY *= y;
        arm.rotateAngleZ *= z;
    }

    @Override
    protected void renderLegs() {
        pushMatrix();
            translate(0.05F, -0.21F, 0);
            renderScaledArm(bipedLeftArm, 0.5F, 1.15F, 0.5F);
        popMatrix();

        pushMatrix();
            translate(-0.05F, -0.21F, 0);
            renderScaledArm(bipedRightArm, 0.5F, 1.2F, 0.5F);
        popMatrix();

        pushMatrix();
            translate(0.05F, -0.21F, 0.35F);
            renderScaledArm(bipedLeftLeg, 0.5F, 1.2F, 0.5F);
        popMatrix();

        pushMatrix();
            translate(-0.05F, -0.21F, 0.35F);
            renderScaledArm(bipedRightLeg, 0.5F, 1.15F, 0.5F);
        popMatrix();
    }
}

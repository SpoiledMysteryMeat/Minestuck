package com.mraof.minestuck.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.entity.underling.LichEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * Lich - Caldw3ll
 * Created using Tabula 6.0.0
 */
public class LichModel<T extends LichEntity> extends EntityModel<T>
{
	public float[] modelScale = { 0.9F, 1.5F, 0.9F };
	public RendererModel LegRight;
	public RendererModel LegLeft;
	public RendererModel BodyLower;
	public RendererModel BodyMiddle;
	public RendererModel BodyUpper;
	public RendererModel Neck;
	public RendererModel Head;
	public RendererModel ForearmRight;
	public RendererModel ArmRight;
	public RendererModel ArmLeft;
	public RendererModel ForearmLeft;
	public RendererModel HandLeft;
	public RendererModel HandRight;
	public RendererModel Jaw;
	public RendererModel HornRightBase;
	public RendererModel HornLeftBase;
	public RendererModel HornRightSpikeBase;
	public RendererModel HornLeftSpikeBase;
	public RendererModel HornRightSpike;
	public RendererModel HornLeftSpike;

	public LichModel()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.BodyMiddle = new RendererModel(this, 6, 19);
		this.BodyMiddle.setRotationPoint(-3.9F, 7.6F, -5.0F);
		this.BodyMiddle.addBox(0.0F, 0.0F, 0.0F, 6, 9, 10, 0.0F);
		this.ArmLeft = new RendererModel(this, 20, 0);
		this.ArmLeft.setRotationPoint(4.6F, 4.8F, -6.0F);
		this.ArmLeft.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.setRotation(ArmLeft, 0.0F, 0.0F, 0.136659280431156F);
		this.HornLeftBase = new RendererModel(this, 0, 38);
		this.HornLeftBase.setRotationPoint(2.0F, -4.0F, -6.1F);
		this.HornLeftBase.addBox(0.0F, 0.0F, 0.0F, 2, 3, 6, 0.0F);
		this.Head = new RendererModel(this, 28, 19);
		this.Head.setRotationPoint(3.0F, -7.1F, -2.0F);
		this.Head.addBox(0.0F, 0.0F, 0.0F, 9, 4, 4, 0.0F);
		this.setRotation(Head, 0.0F, 0.0F, 1.0016444577195458F);
		this.HandRight = new RendererModel(this, 54, 14);
		this.HandRight.setRotationPoint(5.0F, 3.7F, 4.5F);
		this.HandRight.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		this.setRotation(HandRight, 0.0F, 0.0F, 0.5462880558742251F);
		this.HornRightSpikeBase = new RendererModel(this, 54, 17);
		this.HornRightSpikeBase.setRotationPoint(2.0F, -5.0F, 4.0F);
		this.HornRightSpikeBase.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		this.ArmRight = new RendererModel(this, 16, 0);
		this.ArmRight.setRotationPoint(4.6F, 4.8F, 5.0F);
		this.ArmRight.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1, 0.0F);
		this.setRotation(ArmRight, 0.0F, 0.0F, 0.136659280431156F);
		this.Neck = new RendererModel(this, 38, 0);
		this.Neck.setRotationPoint(1.1F, -3.1F, -2.0F);
		this.Neck.addBox(0.0F, 0.0F, 0.0F, 3, 5, 4, 0.0F);
		this.HandLeft = new RendererModel(this, 48, 13);
		this.HandLeft.setRotationPoint(5.0F, 4.3F, -6.4F);
		this.HandLeft.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		this.setRotation(HandLeft, 0.0F, 0.0F, 0.5462880558742251F);
		this.ForearmLeft = new RendererModel(this, 56, 0);
		this.ForearmLeft.setRotationPoint(0.2F, 2.4F, -6.0F);
		this.ForearmLeft.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1, 0.0F);
		this.setRotation(ForearmLeft, 0.0F, 0.0F, -0.2792526803190927F);
		this.Jaw = new RendererModel(this, 48, 16);
		this.Jaw.setRotationPoint(7.7F, 0.2F, -1.0F);
		this.Jaw.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		this.setRotation(Jaw, 0.0F, 0.0F, 1.0471975511965976F);
		this.HornRightBase = new RendererModel(this, 0, 20);
		this.HornRightBase.setRotationPoint(2.0F, -4.0F, 0.0F);
		this.HornRightBase.addBox(0.0F, 0.0F, 0.0F, 2, 3, 6, 0.0F);
		this.HornRightSpike = new RendererModel(this, 38, 0);
		this.HornRightSpike.setRotationPoint(2.5F, -8.0F, 4.5F);
		this.HornRightSpike.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.BodyLower = new RendererModel(this, 16, 0);
		this.BodyLower.setRotationPoint(-3.9F, 15.6F, -5.0F);
		this.BodyLower.addBox(0.0F, 0.0F, 0.0F, 6, 9, 10, 0.0F);
		this.setRotation(BodyLower, 0.0F, 0.0F, -0.22759093446006054F);
		this.LegLeft = new RendererModel(this, 8, 0);
		this.LegLeft.setRotationPoint(2.0F, 23.0F, -5.0F);
		this.LegLeft.addBox(0.0F, 0.0F, 0.0F, 2, 18, 2, 0.0F);
		this.HornLeftSpikeBase = new RendererModel(this, 50, 20);
		this.HornLeftSpikeBase.setRotationPoint(2.0F, -5.0F, -6.1F);
		this.HornLeftSpikeBase.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
		this.HornLeftSpike = new RendererModel(this, 48, 0);
		this.HornLeftSpike.setRotationPoint(2.5F, -8.0F, -5.6F);
		this.HornLeftSpike.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
		this.LegRight = new RendererModel(this, 0, 0);
		this.LegRight.setRotationPoint(2.0F, 23.0F, 3.0F);
		this.LegRight.addBox(0.0F, 0.0F, 0.0F, 2, 18, 2, 0.0F);
		this.ForearmRight = new RendererModel(this, 52, 0);
		this.ForearmRight.setRotationPoint(0.2F, 2.4F, 5.0F);
		this.ForearmRight.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1, 0.0F);
		this.setRotation(ForearmRight, 0.0F, 0.0F, -0.2792526803190927F);
		this.BodyUpper = new RendererModel(this, 28, 28);
		this.BodyUpper.setRotationPoint(-1.7F, -0.1F, -5.0F);
		this.BodyUpper.addBox(0.0F, 0.0F, 0.0F, 6, 8, 10, 0.0F);
		this.setRotation(BodyUpper, 0.0F, 0.0F, 0.27314402793711257F);
	}

	@Override
	public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{ 
		GlStateManager.pushMatrix();
		GlStateManager.scalef(1F / modelScale[0], 1F / modelScale[1], 1F / modelScale[2]);
		GL11.glTranslatef(0F, -0.5F, 0F);
		GL11.glRotatef(90.0F, 0, 1.0F, 0);
		setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.BodyUpper.render(scale);
		this.HornLeftSpike.render(scale);
		this.ForearmLeft.render(scale);
		this.HandLeft.render(scale);
		this.HornLeftBase.render(scale);
		this.HornRightBase.render(scale);
		this.HornRightSpikeBase.render(scale);
		this.ForearmRight.render(scale);
		this.Jaw.render(scale);
		this.BodyLower.render(scale);
		this.Neck.render(scale);
		this.ArmLeft.render(scale);
		this.LegLeft.render(scale);
		this.HandRight.render(scale);
		this.HornRightSpike.render(scale);
		this.HornLeftSpikeBase.render(scale);
		this.BodyMiddle.render(scale);
		this.Head.render(scale);
		this.ArmRight.render(scale);
		this.LegRight.render(scale);
		GlStateManager.popMatrix();
	}

	public void setRotation(RendererModel model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	@Override
	public void setRotationAngles(T entityIn ,float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		//TO-DO: Add head movement and fix head and jaw rotation point
		this.LegLeft.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.LegRight.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}
}

package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.underling.GiclopsEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class GiclopsModel<T extends GiclopsEntity> extends EntityModel<T>
{

	private RendererModel head;
	private RendererModel leftLeg;
	private RendererModel rightLeg;
	private RendererModel body;
	private RendererModel leftArm;
	private RendererModel rightArm;
	private RendererModel tail;
	float scaling = 2.0F;

	public GiclopsModel()
	{
		float offsetY = -96 + 24 / scaling;
		this.textureWidth = 256;
		this.textureHeight = 256;
		//setTextureOffset("null.Spike", 0, 0);
		head = new RendererModel(this, 0, 0);
		head.addBox(-21F, -26F, -25F, 42, 26, 50);
		head.func_217178_a("Spike", -2F, -34F, -6F, 4, 8, 4, 0.0f, 0, 0);
		head.func_217178_a("Spike", -2, -34, -16, 4, 8, 4, 0.0f, 0, 0);
		head.setRotationPoint(0F, 26F + offsetY, -14F);
		body = new RendererModel(this, 0, 76);
		body.addBox(-32F, 0F, -20F, 64, 64, 40);
		body.setRotationPoint(0F, 16F + offsetY, -8F);
		leftArm = new RendererModel(this, 184, 0);
		leftArm.addBox(-8F, 0F, -4F, 8, 52, 8);
		leftArm.setRotationPoint(-32F, 28F + offsetY, -8F);
		rightArm = new RendererModel(this, 184, 0);
		rightArm.addBox(0F, 0F, -4F, 8, 52, 8);
		rightArm.setRotationPoint(32F, 28F + offsetY, -8F);
		rightArm.mirror = true;
		leftLeg = new RendererModel(this, 120, 180);
		leftLeg.addBox(-12F, 0F, -12F, 24, 16, 24);
		leftLeg.setRotationPoint(-20, 80F + offsetY, -8F);
		rightLeg = new RendererModel(this, 120, 180);
		rightLeg.addBox(-12F, 0F, -12F, 24, 16, 24);
		rightLeg.setRotationPoint(20, 80F + offsetY, -8F);
		rightLeg.mirror = true;
		tail = new RendererModel(this, 0, 180);
		tail.addBox(-20F, -6F, -10F, 40, 44, 20);
		tail.setRotationPoint(0F, 56F + offsetY, 0F);
		tail.rotateAngleX = 30F / (180F / (float)Math.PI);
	}
	
	@Override
	public void render(T entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(entity, par2, par3, par4, par5, par6, par7);
		//this is where I can increase the size
        GL11.glPushMatrix();
//        GL11.glTranslatef(0.0F, par7 * scaling, 0.0F);
		this.body.render(par7 * scaling);
		this.rightArm.render(par7 * scaling);
		this.leftArm.render(par7 * scaling);
		this.rightLeg.render(par7 * scaling);
		this.leftLeg.render(par7 * scaling);
		this.head.render(par7 * scaling);
		this.tail.render(par7 * scaling);
		GL11.glPopMatrix();
	}
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(T par7Entity, float par1, float par2, float par3, float par4, float par5, float par6)
	{
		this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (par5 + 20) / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.4F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.4F * par2;
		this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;

	}		

}

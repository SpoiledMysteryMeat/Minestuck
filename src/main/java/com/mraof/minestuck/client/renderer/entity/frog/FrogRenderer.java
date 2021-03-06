package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FrogRenderer extends MobRenderer<FrogEntity, FrogModel<FrogEntity>>
{

	public FrogRenderer(EntityRendererManager manager)
	{
		super(manager, new FrogModel(), 0.5f);
		this.addLayer(new FrogSkinLayer(this));
		this.addLayer(new FrogEyesLayer(this));
		this.addLayer(new FrogBellyLayer(this));
		
	}

	@Override
	protected void preRenderCallback(FrogEntity frog, float partialTickTime)
	{
		float scale = frog.getFrogSize();
		GlStateManager.scalef(scale,scale,scale);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(FrogEntity entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/frog/base.png");
	}
	
	protected boolean canRenderName(FrogEntity entity)
    {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }
	

}

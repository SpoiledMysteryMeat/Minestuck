package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class MonstersLandAspect extends TitleLandAspect
{
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "monsters");
	private final Variant type;
	
	public MonstersLandAspect(Variant type)
	{
		super(EnumAspect.RAGE, GROUP_NAME);
		this.type = type;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"monster"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.PURPLE_CARPET.getDefaultState());
		if(registry.getCustomBlock("torch") == null)
			registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		if(registry.getCustomBlock("wall_torch") == null)
			registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.skylightBase = Math.min(1/4F, worldProvider.skylightBase);
		worldProvider.mergeFogColor(new Vec3d(0.1, 0, 0), 0.5F);
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		if(this.type == Variant.MONSTERS)
		{
			biome.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.CREEPER, 1, 1, 1));
			biome.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SPIDER, 1, 1, 2));
			biome.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 1, 1, 2));
		}
		else if(this.type == Variant.UNDEAD)
		{
			biome.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 2, 1, 3));
			biome.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SKELETON, 1, 1, 2));
		}
	}
	
	public enum Variant
	{
		MONSTERS,
		UNDEAD
	}
}
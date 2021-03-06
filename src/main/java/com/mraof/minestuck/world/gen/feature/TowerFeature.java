package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

public class TowerFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_TOWER = new ResourceLocation(Minestuck.MOD_ID, "tower");
	private static final ResourceLocation STRUCTURE_TOWER_WITH_CHEST = new ResourceLocation(Minestuck.MOD_ID, "tower_with_chest");
	private static final ResourceLocation STRUCTURE_TOWER_WALL = new ResourceLocation(Minestuck.MOD_ID, "tower_wall");
	private static final ResourceLocation STRUCTURE_ELEVATED_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "elevated_tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_BALCONY = new ResourceLocation(Minestuck.MOD_ID, "tower_balcony");
	
	
	public TowerFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Rotation rotation = Rotation.randomRotation(rand);
		ResourceLocation tower = rand.nextInt(50) == 0 ? STRUCTURE_TOWER_WITH_CHEST : STRUCTURE_TOWER;
		TemplateManager templates = ((ServerWorld) worldIn.getWorld()).getSaveHandler().getStructureTemplateManager();
		Template template = templates.getTemplateDefaulted(tower);
		
		PlacementSettings settings = new PlacementSettings().setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.transformedSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX() - 2) + 1, zOffset = rand.nextInt(16 - size.getZ() - 2) + 1;
		
		int y = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + xOffset + size.getX()/2, pos.getZ() + zOffset + size.getZ()/2) - 2;
		
		BlockPos center = pos.add(xOffset + size.getX()/2, y - pos.getY(), zOffset + size.getZ()/2);
		final int doorSide = 1, doorFront = 4;
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockPos doorPos = center.offset(direction, doorFront);
			
			ResourceLocation doorType;
			if(worldIn.getBlockState(doorPos.up(2)).isSolid())
			{
				if(worldIn.getBlockState(doorPos.up(3)).isSolid())
				{
					doorType = STRUCTURE_TOWER_WALL;
				} else
				{
					doorType = STRUCTURE_ELEVATED_TOWER_DOOR;
				}
			} else
			{
				if(worldIn.getBlockState(doorPos.up()).isSolid())
				{
					doorType = STRUCTURE_TOWER_DOOR;
				} else
				{
					doorType = STRUCTURE_TOWER_BALCONY;
				}
			}
			
			Template door = templates.getTemplateDefaulted(doorType);
			
			Rotation doorRotation;
			switch(direction)
			{
				case EAST:
					doorRotation = Rotation.CLOCKWISE_90;
					break;
				case SOUTH:
					doorRotation = Rotation.CLOCKWISE_180;
					break;
				case WEST:
					doorRotation = Rotation.COUNTERCLOCKWISE_90;
					break;
				case NORTH: default:
					doorRotation = Rotation.NONE;
					break;
			}
			
			settings.setRotation(doorRotation);
			BlockPos structurePos = doorPos.offset(direction.rotateYCCW(), doorSide).offset(direction, door.transformedSize(doorRotation).getZ() - 2);
			door.addBlocksToWorld(worldIn, structurePos, settings);
		}
		
		settings.setRotation(rotation);
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.addBlocksToWorld(worldIn, structurePos, settings);
		
		for(Template.BlockInfo blockInfo : template.func_215381_a(structurePos, settings, Blocks.STRUCTURE_BLOCK))
		{
			if(blockInfo.nbt != null)
			{
				StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
				if (structuremode == StructureMode.DATA)
				{
					String data = blockInfo.nbt.getString("metadata");
					if(data.equals("basic_chest"))
					{
						worldIn.setBlockState(blockInfo.pos, Blocks.AIR.getDefaultState(), 3);
						TileEntity tileentity = worldIn.getTileEntity(blockInfo.pos.down());
						if (tileentity instanceof ChestTileEntity)
						{
							((ChestTileEntity) tileentity).setLootTable(MSLootTables.BASIC_MEDIUM_CHEST, rand.nextLong());
						}
					}
				}
			}
		}
		
		return true;
	}
}
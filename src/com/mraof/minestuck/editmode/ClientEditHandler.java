package com.mraof.minestuck.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.ClientEditPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.world.MinestuckDimensions;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map.Entry;

public class ClientEditHandler {
	
	public final static ClientEditHandler instance = new ClientEditHandler();
	
	static boolean[] givenItems;
	
	static boolean activated;
	
	static int centerX, centerZ;
	
	public static String client;
	
	/**
	 * Used to tell if the client is in edit mode or not.
	 */
	public static boolean isActive() {
		return activated;
	}
	
	public static void onKeyPressed()
	{
		ClientEditPacket packet = ClientEditPacket.exit();
		MinestuckPacketHandler.sendToServer(packet);
	}
	
	public static void onClientPackage(String target, int posX, int posZ, boolean[] items, CompoundNBT deployList)
	{
		Minecraft mc = Minecraft.getInstance();
		ClientPlayerEntity player = mc.player;
		if(target != null) {	//Enable edit mode
			activated = true;
			givenItems = items;
			centerX = posX;
			centerZ = posZ;
			client = target;
		} else if(items != null) {
			givenItems = items;
		}
		else	//Disable edit mode
		{
			player.fallDistance = 0;
			activated = false;
		}
		if(deployList != null)
		{
			DeployList.loadClientDeployList(deployList);
		}
	}
	
	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event)
	{
		if(!isActive())
			return;
		
		GristSet have = PlayerSavedData.getClientGrist();
		
		addToolTip(event.getItemStack(), event.getToolTip(), have, givenItems);
		
	}
	
	static void addToolTip(ItemStack stack, List<ITextComponent> toolTip, GristSet have, boolean[] givenItems)
	{
		
		GristSet cost;
		DeployList.ClientDeployEntry deployEntry = DeployList.getEntryClient(stack);
		if(deployEntry != null)
			cost = givenItems[deployEntry.getIndex()]
					? deployEntry.getSecondaryCost() : deployEntry.getPrimaryCost();
		else if(stack.getItem().equals(MinestuckItems.CAPTCHA_CARD))
			cost = new GristSet();
		else cost = AlchemyCostRegistry.getGristConversion(stack);
		
		if(cost == null)
		{
			toolTip.add(new TranslationTextComponent("gui.notAvailable").setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		
		for(Entry<GristType, Integer> entry : cost.getMap().entrySet())
		{
			GristType grist = entry.getKey();
			TextFormatting color = entry.getValue() <= have.getGrist(grist) ? TextFormatting.GREEN : TextFormatting.RED;
			toolTip.add(new StringTextComponent(entry.getValue()+" ").appendSibling(grist.getDisplayName()).appendText(" ("+have.getGrist(grist) + ")").setStyle(new Style().setColor(color)));
		}
		if(cost.isEmpty())
			toolTip.add(new TranslationTextComponent("gui.free").setStyle(new Style().setColor(TextFormatting.GREEN)));
	}
	
	@SubscribeEvent
	public void tickEnd(TickEvent.PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END || event.player != Minecraft.getInstance().player || !isActive())
			return;
		PlayerEntity player = event.player;
		
		double range = MinestuckDimensions.isLandDimension(player.dimension) ? MinestuckConfig.clientLandEditRange : MinestuckConfig.clientOverworldEditRange;
		
		ServerEditHandler.updatePosition(player, range, centerX, centerZ);
		
	}
	
	@SubscribeEvent
	public void onTossEvent(ItemTossEvent event)
	{
		if(event.getEntity().world.isRemote && event.getPlayer().isUser() && isActive())
		{
			PlayerInventory inventory = event.getPlayer().inventory;
			ItemStack stack = event.getEntityItem().getItem();
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
			{
				if(!ServerEditHandler.isBlockItem(stack.getItem()) && GristHelper.canAfford(PlayerSavedData.getClientGrist(), givenItems[entry.getIndex()]
						? entry.getSecondaryCost() : entry.getPrimaryCost()))
					givenItems[entry.getIndex()] = true;
				else event.setCanceled(true);
				
			}
			if(event.isCanceled())
			{
				if(!inventory.getItemStack().isEmpty())
					inventory.setItemStack(ItemStack.EMPTY);
				else inventory.setInventorySlotContents(inventory.currentItem, ItemStack.EMPTY);
				event.getEntityItem().remove();
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		if(event.getEntity().world.isRemote && isActive() && event.getPlayer().equals(Minecraft.getInstance().player))
			event.setCanceled(true);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRightClickEvent(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && event.getPlayer().isUser() && isActive())
		{
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			ItemStack stack = event.getPlayer().getHeldItemMainhand();
			event.setUseBlock((block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock) ? Event.Result.ALLOW : Event.Result.DENY);
			if(event.getUseBlock() == Event.Result.ALLOW)
				return;
			if(event.getHand().equals(Hand.OFF_HAND) || !ServerEditHandler.isBlockItem(stack.getItem()))
			{
				event.setCanceled(true);
				return;
			}
			
			GristSet cost;
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
				if(givenItems[entry.getIndex()])
					cost = entry.getSecondaryCost();
				else cost = entry.getPrimaryCost();
			else cost = AlchemyCostRegistry.getGristConversion(stack);
			if(!GristHelper.canAfford(PlayerSavedData.getClientGrist(), cost)) {
				StringBuilder str = new StringBuilder();
				if(cost != null)
				{
					for(GristAmount grist : cost.getArray())
					{
						if(cost.getArray().indexOf(grist) != 0)
							str.append(", ");
						str.append(grist.getAmount()+" "+grist.getType().getDisplayName());
					}
					event.getPlayer().sendMessage(new TranslationTextComponent("grist.missing",str.toString()));
				}
				event.setCanceled(true);
			}
			if(event.getUseItem() == Event.Result.DEFAULT)
				event.setUseItem(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onLeftClickEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getWorld().isRemote && event.getPlayer().isUser() && isActive())
		{
			BlockState block = event.getWorld().getBlockState(event.getPos());
			if(block.getBlockHardness(event.getWorld(), event.getPos()) < 0 || block.getMaterial() == Material.PORTAL
					|| PlayerSavedData.getClientGrist().getGrist(GristType.BUILD) <= 0)
				event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onRightClickAir(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getWorld().isRemote && event.getPlayer().isUser() && isActive())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST,receiveCanceled=false)
	public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().isRemote && isActive() && event.getPlayer().equals(Minecraft.getInstance().player)
				&& event.getUseItem() == Event.Result.ALLOW) {
			ItemStack stack = event.getPlayer().getHeldItemMainhand();
			DeployList.ClientDeployEntry entry = DeployList.getEntryClient(stack);
			if(entry != null)
				givenItems[entry.getIndex()] = true;
		}
	}
	
	@SubscribeEvent
	public void onAttackEvent(AttackEntityEvent event)
	{
		if(event.getEntity().world.isRemote && event.getPlayer().isUser() && isActive())
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(event.getWorld().isRemote())
			activated = false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onGuiOpened(GuiOpenEvent event)
	{
		if(isActive() && event.getGui() instanceof DisplayEffectsScreen<?>)
		{
				event.setCanceled(true);
				PlayerStatsScreen.editmodeTab = PlayerStatsScreen.EditmodeGuiType.DEPLOY_LIST;
				PlayerStatsScreen.openGui(true);
		}
	}
	
}

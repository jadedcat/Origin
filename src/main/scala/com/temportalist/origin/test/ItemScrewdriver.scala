package com.temportalist.origin.test

import java.util

import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.utility._
import com.temportalist.origin.wrapper.common.item.ItemWrapper
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, Item, ItemStack}
import net.minecraft.nbt.NBTTagList
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class ItemScrewdriver(n: String) extends ItemWrapper(Origin.MODID, n) {

	this.setHasSubtypes(true)

	@SideOnly(Side.CLIENT)
	override def getSubItems(itemIn: Item, tab: CreativeTabs, subItems: util.List[_]): Unit = {
		for (stack: ItemStack <- Sonic.screwdrivers.toList) Generic.addToList(subItems, stack)
	}

	override def addInformation(stack: ItemStack, player: EntityPlayer, list: util.List[_],
			isAdvanced: Boolean): Unit = {
		Generic.addToList(list, "Mode: " + stack.getTagCompound.getString("modeName"))

		val entityTags: NBTTagList = ScrewdriverMode.getDataCore(stack)
		Generic.addToList(list, "DataCore: " + entityTags.tagCount())
		Scala.foreach(entityTags, (index: Int, any: Any) => {
			Generic.addToList(list, "  " + any)
		})

	}

	// Interaction things

	def getMode(stack: ItemStack): ScrewdriverMode = ScrewdriverMode.getMode(stack)

	override def onItemRightClick(stack: ItemStack, world: World,
			player: EntityPlayer): ItemStack = {
		val retStack: ItemStack = this.getMode(stack).onRightClick(
			stack.copy(), world, player, Cursor.raytraceWorld(world, player)
		)
		if (!Stacks.areStacksMatching(stack, retStack, checkSize = true, checkNBT = true)) {
			player.setCurrentItemOrArmor(0, retStack)
		}
		/* todo: warning: this is a working loading of the entity NBT!
		if (!world.isRemote) {
			val ent: Entity = Sonic.entityStates("Cow").getEntity(world)
			ent.setPositionAndUpdate(player.posX, player.posY, player.posZ)
			world.spawnEntityInWorld(ent)
		}
		stack
		*/
		retStack
	}

	override def getItemUseAction(stack: ItemStack): EnumAction =
		this.getMode(stack).getUseAction(stack)

	override def onItemUseFinish(stack: ItemStack, worldIn: World,
			playerIn: EntityPlayer): ItemStack =
		this.getMode(stack).onUseFinish(stack, worldIn, playerIn,
			Cursor.raytraceWorld(worldIn, playerIn))

}

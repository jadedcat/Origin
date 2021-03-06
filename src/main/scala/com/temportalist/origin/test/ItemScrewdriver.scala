package com.temportalist.origin.test

import java.util

import com.temportalist.origin.api.common.item.ItemBase
import com.temportalist.origin.api.common.utility.{Cursor, Generic, Scala, Stacks}
import com.temportalist.origin.internal.common.Origin
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, Item, ItemStack}
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.IIcon
import net.minecraft.world.World

/**
 *
 *
 * @author TheTemportalist
 */
class ItemScrewdriver(n: String) extends ItemBase(Origin.MODID, n) {

	this.setHasSubtypes(true)

	@SideOnly(Side.CLIENT)
	var icons: Array[IIcon] = _

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

	@SideOnly(Side.CLIENT) override
	def registerIcons(reg: IIconRegister): Unit = {
		this.icons = new Array[IIcon](3)
		for (i <- 0 until this.icons.length)
			this.icons(i) = reg.registerIcon(this.modid + ":screwdriver/" + i)
	}

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(i: Int): IIcon = this.icons(i)

	// Interaction things

	def getMode(stack: ItemStack): ScrewdriverMode = ScrewdriverMode.getMode(stack)

	override def onItemRightClick(stack: ItemStack, world: World,
			player: EntityPlayer): ItemStack = {
		val retStack: ItemStack = this.getMode(stack).onRightClick(
			stack.copy(), world, player, Cursor.raytraceWorld(player)
		)
		if (!Stacks.doStacksMatch(
			stack, retStack, meta = true, size = true, nbt = true, nil = true)) {
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

	override def onPlayerStoppedUsing(stack: ItemStack, worldIn: World,
			playerIn: EntityPlayer, itemInUseCount: Int): Unit = {
		if (this.getMaxItemUseDuration(stack) == itemInUseCount)
			this.getMode(stack).onUseFinish(stack, worldIn, playerIn,
				Cursor.raytraceWorld(playerIn))
	}

}

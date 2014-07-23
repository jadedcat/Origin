package com.countrygamer.cgo.wrapper.common.item

import java.util.List

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World

/**
 * A wrapper for Minecraft's Item
 *
 * @param pluginID
 * The plugin id of the owner plugin
 * @param name
 * The name of the item
 *
 * @author CountryGamer
 */
class ItemWrapper(val pluginID: String, name: String) extends Item {

	// Default Constructor
	this.setUnlocalizedName(name)
	GameRegistry.registerItem(this, name)

	// End Constructor

	// Other Constructors

	// End Constructors

	/**
	 * Register the icons for this item
	 * @param iconRegister
	 */
	@SideOnly(Side.CLIENT) // Make sure this is done only on the client side
	override def registerIcons(iconRegister: IIconRegister): Unit = {
		// Set this item's icon to the icon at the path, gotten from the method getTexturePath
		this.itemIcon = iconRegister.registerIcon(this.getTexturePath)
	}

	/**
	 * Get the texture path of the item's icon
	 * @return
	 * The path of this item's icon
	 */
	protected def getTexturePath: String = {
		// Get the unlocalized name of this item
		val unlocalizedName: String = this.getUnlocalizedName
		// return the result of the unlocalized name, making sure to get rid of the "item." prefix
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1)
	}

	/**
	 * Get the non-local name of this item
	 * @return
	 */
	override def getUnlocalizedName: String = {
		// return a formatted string using the format:
		//   item.{pluginID}:{itemName}
		return String.format("item.%s%s", this.pluginID + ":",
			this.getUnwrappedUnlocalizedName(super.getUnlocalizedName))
	}

	/**
	 * Another way to get the unlocalized name of this item, while being sensitive to metadata
	 * @param itemStack
	 * @return
	 */
	override def getUnlocalizedName(itemStack: ItemStack): String = {
		return this.getUnlocalizedName
	}

	/**
	 * Unwrap the passed string's name (gets rid of the prefix)
	 * @param unlocalizedName
	 * @return
	 */
	def getUnwrappedUnlocalizedName(unlocalizedName: String): String = {
		// Get rid of the "item." prefix and return the result
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1)
	}

	// ~~~~~~~~~~~~~~~ Start supered wrappers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed.
	 *
	 * @param itemStack
	 * @param world
	 * @param player
	 * @return
	 */
	override def onItemRightClick(itemStack: ItemStack, world: World,
			player: EntityPlayer): ItemStack = {
		return super.onItemRightClick(itemStack, world, player)
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking,
	 * it will have one of these. Return true if something happens and false if it doesn't.
	 * This is for ITEMS, not BLOCKS!
	 *
	 * @param itemStack
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @param offsetX
	 * @param offsetY
	 * @param offsetZ
	 * @return
	 */
	override def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, side: Int, offsetX: Float, offsetY: Float, offsetZ: Float): Boolean = {
		return super.onItemUse(itemStack, player, world, x, y, z, side, offsetX, offsetY, offsetZ)
	}

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 *
	 * @param itemStack
	 * @param player
	 * @param entity
	 * @return
	 */
	override def itemInteractionForEntity(itemStack: ItemStack, player: EntityPlayer,
			entity: EntityLivingBase): Boolean = {
		return super.itemInteractionForEntity(itemStack, player, entity)
	}

	/**
	 * Called when the player Left Clicks (attacks) an entity.
	 * Processed before damage is done, if return value is true further processing is canceled
	 * and the entity is not attacked.
	 *
	 * @param itemStack The Item being used
	 * @param player The player that is attacking
	 * @param entity The entity being attacked
	 * @return True to cancel the rest of the interaction.
	 */
	override def onLeftClickEntity(itemStack: ItemStack, player: EntityPlayer,
			entity: Entity): Boolean = {
		return super.onLeftClickEntity(itemStack, player, entity)
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 *
	 * @param itemStack
	 * @param world
	 * @param player
	 */
	override def onCreated(itemStack: ItemStack, world: World, player: EntityPlayer) {
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check
	 * if is on a player hand and update it's contents.
	 *
	 * @param itemStack
	 * @param world
	 * @param entity
	 * @param slot
	 * @param isCurrentItem
	 */
	override def onUpdate(itemStack: ItemStack, world: World, entity: Entity, slot: Int,
			isCurrentItem: Boolean): Unit = {
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param itemStack
	 * @param player
	 * @param list
	 * @param isAdvanced
	 */
	@SideOnly(Side.CLIENT)
	override def addInformation(itemStack: ItemStack, player: EntityPlayer, list: List[_],
			isAdvanced: Boolean): Unit = {
		super.addInformation(itemStack, player, list, isAdvanced);
	}

}

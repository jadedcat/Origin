package com.temportalist.origin.library.common.item

import java.util

import net.minecraft.init.Items
import com.temportalist.origin.library.common.lib.vec.V3O
import com.temportalist.origin.library.common.utility.{Generic, WorldHelper}
import com.temportalist.origin.wrapper.common.item.ItemWrapper
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.{Block, BlockFence}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.{MobSpawnerBaseLogic, TileEntityMobSpawner}
import net.minecraft.util.{IIcon, MathHelper, StatCollector}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

/**
 *
 *
 * @author TheTemportalist 1/26/15
 */
class ItemPlacer(modid: String, name: String) extends ItemWrapper(modid, name) {

	this.setCreativeTab(CreativeTabs.tabMisc)
	this.setHasSubtypes(true)

	@SideOnly(Side.CLIENT)
	private var overlayIcon: IIcon = _

	@SideOnly(Side.CLIENT)
	override def getSubItems(itemIn: Item, tab: CreativeTabs, subItems: util.List[_]): Unit = {
		for (i <- 0 until ItemPlacer.classes.size()) {
			val stack: ItemStack = new ItemStack(itemIn)
			val tag: NBTTagCompound = new NBTTagCompound
			tag.setString("EntityName",
				EntityList.classToStringMapping.get(ItemPlacer.classes.get(i)).toString
			)
			stack.setTagCompound(tag)
			Generic.addToList(subItems, stack)
		}
	}

	@SideOnly(Side.CLIENT)
	override def requiresMultipleRenderPasses: Boolean = true

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	@SideOnly(Side.CLIENT)
	override def getIconFromDamageForRenderPass(damage: Int, pass: Int): IIcon = {
		if (pass > 0) this.overlayIcon else this.itemIcon
	}

	@SideOnly(Side.CLIENT)
	override def registerIcons(reg: IIconRegister): Unit = {
		this.itemIcon = Items.spawn_egg.getIconFromDamageForRenderPass(0, 0)
		this.overlayIcon = Items.spawn_egg.getIconFromDamageForRenderPass(0, 1)
	}

	override def getItemStackDisplayName(stack: ItemStack): String = {
		"Spawn " + (if (stack.hasTagCompound)
			StatCollector.translateToLocal(
				"entity." + stack.getTagCompound.getString("EntityName") + ".name"
			)
		else "Unknown Entity")
	}

	@SideOnly(Side.CLIENT)
	override def getColorFromItemStack(stack: ItemStack, renderPass: Int): Int = {
		if (stack.hasTagCompound) {
			val entity: Class[_ <: Entity] =
				EntityList.stringToClassMapping.get(
					stack.getTagCompound.getString("EntityName")
				).asInstanceOf[Class[_ <: Entity]]
			renderPass match {
				case 0 => return ItemPlacer.primary.get(entity)
				case _ => return ItemPlacer.secondary.get(entity)
			}
		}
		16777215
	}

	override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int,
			z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
		val pos: V3O = new V3O(x, y, z)
		val pos_side: V3O = pos + ForgeDirection.getOrientation(side)
		if (WorldHelper.isClient() ||
				!player.canPlayerEdit(pos_side.x_i(), pos_side.y_i(), pos_side.z_i(), side, stack))
			return false
		if (!stack.hasTagCompound) return false
		val entityName: String = stack.getTagCompound.getString("EntityName")
		val state: Block = pos.getBlock(world)

		if (state == Blocks.mob_spawner) {
			pos.getTile(world) match {
				case spawner: TileEntityMobSpawner =>
					val logic: MobSpawnerBaseLogic = spawner.func_145881_a()
					logic.setEntityName(entityName)
					spawner.markDirty()
					pos.markForUpdate(world)
					if (!player.capabilities.isCreativeMode)
						stack.stackSize -= 1
					return true
				case _ =>
			}
		}

		val placePos: V3O = pos_side + new V3O(
			0.5,
			if (side == 1 && state.isInstanceOf[BlockFence]) 0.5 else 0,
			0.5
		)

		val entity: Entity = this.spawnEntity(world, entityName, placePos)

		if (entity != null) {
			/* todo fix the name tagging
			if (entity.isInstanceOf[EntityLivingBase] && stack.hasDisplayName) {
				entity.setCustomNameTag(stack.getDisplayName)
			}
			*/
			if (!player.capabilities.isCreativeMode) {
				stack.stackSize -= 1
			}
			return true
		}

		false
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
		if (itemStack.hasTagCompound) {
			val entityName: String = itemStack.getTagCompound.getString("EntityName")
			val thatEntityName: String = EntityList.classToStringMapping.get(
				entity.getClass
			).asInstanceOf[String]
			if (thatEntityName.equals(entityName)) {
				entity match {
					case ageable: EntityAgeable =>
						return this.spawnEntity(
							ageable.createChild(null), new V3O(entity)
						)
				}
			}
		}
		false
	}

	def spawnEntity(world: World, name: String, pos: V3O): Entity = {
		val entity: Entity = EntityList.createEntityByName(name, world)
		this.spawnEntity(entity, pos)
		entity
	}

	def spawnEntity(entity: Entity, pos: V3O): Boolean = {
		entity match {
			case living: EntityLivingBase =>
				living.setLocationAndAngles(
					pos.x, pos.y, pos.z,
					MathHelper.wrapAngleTo180_float(entity.worldObj.rand.nextFloat * 360.0F),
					0.0F
				)
				living.rotationYawHead = living.rotationYaw
				living.renderYawOffset = living.rotationYaw
				/*
				living.asInstanceOf[EntityLiving].onInitialSpawn(
					entity.worldObj.getDifficultyForLocation(pos.toBlockPos()),
					null
				)
				*/
				entity.worldObj.spawnEntityInWorld(entity)
				living.asInstanceOf[EntityLiving].playLivingSound()
				true
			case _ =>
				false
		}
	}

}

object ItemPlacer {

	private val classes: util.List[Class[_ <: Entity]] = new
					util.ArrayList[Class[_ <: Entity]]()
	private val primary: util.HashMap[Class[_ <: Entity], Int] =
		new util.HashMap[Class[_ <: Entity], Int]()
	private val secondary: util.HashMap[Class[_ <: Entity], Int] =
		new util.HashMap[Class[_ <: Entity], Int]()

	def register(entity: Class[_ <: Entity], primary: Int, secondary: Int): Unit = {
		this.classes.add(entity)
		this.primary.put(entity, primary)
		this.secondary.put(entity, secondary)
	}

}

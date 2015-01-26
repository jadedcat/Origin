package com.temportalist.origin.wrapper.common.item

import java.util

import com.temportalist.origin.library.common.lib.vec.V3O
import com.temportalist.origin.library.common.utility.WorldHelper
import net.minecraft.block.BlockFence
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity._
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.{MobSpawnerBaseLogic, TileEntityMobSpawner}
import net.minecraft.util.{MathHelper, BlockPos, EnumFacing, StatCollector}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist 1/26/15
 */
class ItemPlacer(modid: String, name: String) extends ItemWrapper(modid, name) {

	this.setCreativeTab(CreativeTabs.tabMisc)
	this.setHasSubtypes(true)

	@SideOnly(Side.CLIENT)
	override def getSubItems(itemIn: Item, tab: CreativeTabs, subItems: util.List[_]): Unit = {
		for (i <- 0 until ItemPlacer.classes.size()) {
			val stack: ItemStack = new ItemStack(itemIn)
			val tag: NBTTagCompound = new NBTTagCompound
			tag.setString("EntityName",
				EntityList.classToStringMapping.get(ItemPlacer.classes.get(i)).toString
			)
			stack.setTagCompound(tag)
			this.addInfo(subItems, stack)
		}
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

	override def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos,
			side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
		if (WorldHelper.isClient() || !playerIn.canPlayerEdit(pos.offset(side), side, stack))
			return false
		if (!stack.hasTagCompound) return false
		val entityName: String = stack.getTagCompound.getString("EntityName")
		val state: IBlockState = worldIn.getBlockState(pos)

		if (state.getBlock == Blocks.mob_spawner) {
			worldIn.getTileEntity(pos) match {
				case spawner: TileEntityMobSpawner =>
					val logic: MobSpawnerBaseLogic = spawner.getSpawnerBaseLogic
					logic.setEntityName(entityName)
					spawner.markDirty()
					worldIn.markBlockForUpdate(pos)
					if (!playerIn.capabilities.isCreativeMode)
						stack.stackSize -= 1
					return true
				case _ =>
			}
		}

		val placePos: V3O = new V3O(pos).add(side)
		if (side == EnumFacing.UP && state.isInstanceOf[BlockFence])
			placePos.add(0, .5, 0)

		placePos.add(.5, 0, .5)
		val entity: Entity = this.spawnEntity(worldIn, entityName, placePos)

		if (entity != null) {
			if (entity.isInstanceOf[EntityLivingBase] && stack.hasDisplayName) {
				entity.setCustomNameTag(stack.getDisplayName)
			}
			if (!playerIn.capabilities.isCreativeMode) {
				stack.stackSize -= 1
			}
			return true
		}

		false
	}

	def spawnEntity(world: World, name: String, pos: V3O): Entity = {
		val entity: Entity = EntityList.createEntityByName(name, world)
		entity match {
			case living: EntityLivingBase =>
				living.setLocationAndAngles(
					pos.x, pos.y, pos.z,
					MathHelper.wrapAngleTo180_float(world.rand.nextFloat * 360.0F), 0.0F
				)
				living.rotationYawHead = living.rotationYaw
				living.renderYawOffset = living.rotationYaw
				living.asInstanceOf[EntityLiving].onSpawnFirstTime(
					world.getDifficultyForLocation(pos.toBlockPos()),
					null
				)
				world.spawnEntityInWorld(entity)
				living.asInstanceOf[EntityLiving].playLivingSound()
			case _ =>
		}
		entity
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

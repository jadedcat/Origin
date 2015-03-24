package com.temportalist.origin.test

import java.util
import java.util.UUID

import scala.collection.mutable

import com.temportalist.origin.api.rendering.ISpriteMapper
import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.handlers.RegisterHelper
import com.temportalist.origin.library.common.lib.IRadialSelection
import com.temportalist.origin.library.common.utility.{WorldHelper, Scala, NBTHelper}
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.{TextureAtlasSprite, TextureMap}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{EntityList, EntityLivingBase}
import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.{MovingObjectPosition, ResourceLocation}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
class ScrewdriverMode(private val name: String, private val textureName: String,
		private val minTier: Int) extends IRadialSelection with ISpriteMapper {

	ScrewdriverMode.modes(name) = this
	RegisterHelper.registerSpritee(this)

	private val texture: ResourceLocation = new ResourceLocation(Origin.MODID,
		"items/screwdriver/addon/" + textureName)

	// Radial Selection things

	@SideOnly(Side.CLIENT)
	override def getResourceLocation(): ResourceLocation = this.texture

	@SideOnly(Side.CLIENT)
	override def draw(mc: Minecraft, x: Double, y: Double, z: Double, w: Double,
			h: Double): Unit = {
		val stack: ItemStack = mc.thePlayer.getCurrentEquippedItem
		if (this.canEnableOn(stack)) {
			val w2: Double = w / 2D
			val h2: Double = h / 2D
			GlStateManager.pushMatrix()

			val sprite: TextureAtlasSprite = Rendering.getSprite(this.texture.toString)

			Rendering.bindResource(TextureMap.locationBlocksTexture)
			mc.currentScreen.drawTexturedModalRect((x - w2).toInt, (y - h2).toInt, sprite, 32, 32)

			GlStateManager.popMatrix()

			GlStateManager.pushMatrix()
			val scale: Float = 0.5F
			val aScale: Float = 1.0F / scale
			GlStateManager.scale(scale, scale, scale)
			mc.fontRendererObj.drawStringWithShadow(
				this.getName(),
				(x * aScale).toInt -
						(mc.fontRendererObj.getStringWidth(this.getName()) / 2.0F).toInt,
				((y + (h / 2)) * aScale).toInt + 2,
				16777215
			)
			GlStateManager.popMatrix()

		}
	}

	override final def triggerSelection(player: EntityPlayer): Unit = {
		val stack: ItemStack = player.getCurrentEquippedItem
		if (this.canEnableOn(stack)) this.onSelection(stack, player)

	}

	def onSelection(stack: ItemStack, player: EntityPlayer): Unit = {
		ScrewdriverMode.setMode(stack, this)
	}

	// Functionality

	def getName(): String = this.name

	def canEnableOn(stack: ItemStack): Boolean = {
		this.minTier <= stack.getItemDamage
	}

	def onRightClick(stack: ItemStack, world: World, player: EntityPlayer,
			mop: MovingObjectPosition): ItemStack = {
		stack
	}

	def getUseAction(stack: ItemStack): EnumAction = EnumAction.NONE

	def onUseFinish(stack: ItemStack, worldIn: World, playerIn: EntityPlayer,
			mop: MovingObjectPosition): ItemStack = stack

}

object ScrewdriverMode {

	/* Tag Structure
	Tag
	-> modeName (String)
	-> modes (NBTTagCompound)
		-> Options (boolean)
		-> ...
	-> dataCore (NBTTagList)
		-> Entity NBTs (EntityList.createEntityFromNBT(tag, world))
	*/

	private val modes: mutable.Map[String, ScrewdriverMode] = mutable.Map[String, ScrewdriverMode]()

	def getMode(stack: ItemStack): ScrewdriverMode =
		this.getMode(stack.getTagCompound.getString("modeName"))

	def getMode(name: String): ScrewdriverMode = this.modes(name)

	def setMode(stack: ItemStack, mode: ScrewdriverMode): Unit = {
		stack.getTagCompound.setString("modeName", mode.getName())
	}

	def initModes(stack: ItemStack): Unit = {
		if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound)
		stack.getTagCompound.setTag("modes", new NBTTagCompound)
		for (mode: ScrewdriverMode <- this.modes.values) {
			this.toggleMode(stack, mode, isEnabled = mode.canEnableOn(stack))
		}
		stack.getTagCompound.setTag("dataCore", new NBTTagList)
		this.setMode(stack, ScrewdriverMode.scanner)
	}

	def getModes(stack: ItemStack, forced: Boolean): util.ArrayList[ScrewdriverMode] = {
		val activeModes: util.ArrayList[ScrewdriverMode] = new util.ArrayList[ScrewdriverMode]()
		val nbtModes: NBTTagCompound = stack.getTagCompound.getCompoundTag("modes")
		for (modeName: String <- this.modes.keys) {
			if (forced || nbtModes.getBoolean(modeName)) {
				activeModes.add(this.modes.get(modeName).get)
			}
		}
		activeModes
	}

	def enableMode(stack: ItemStack, mode: ScrewdriverMode): Unit = {
		this.toggleMode(stack, mode, isEnabled = true)
	}

	def disableMode(stack: ItemStack, mode: ScrewdriverMode): Unit = {
		this.toggleMode(stack, mode, isEnabled = false)
	}

	def toggleMode(stack: ItemStack, mode: ScrewdriverMode, isEnabled: Boolean): Unit = {
		stack.getTagCompound.getCompoundTag("modes").setBoolean(mode.getName(), isEnabled)
	}

	def getDataCore(stack: ItemStack): NBTTagList = {
		NBTHelper.getTagList[String](stack.getTagCompound, "dataCore")
	}

	def addEntityToDataCore(stack: ItemStack, entity: EntityLivingBase, id: UUID): ItemStack = {
		val eID: String = EntityList.getEntityString(entity)
		val entityTags: NBTTagList = this.getDataCore(stack)
		Scala.foreach(entityTags, (index: Int, str: Any) => {
			if (str.asInstanceOf[String].equals(eID)) return stack
		})

		/*
		val tag: NBTTagCompound = new NBTTagCompound
		entity.writeToNBTOptional(tag)
		println(tag.toString)
		*/

		if (!Sonic.entityStates.contains(eID))
			Sonic.researchEntity(eID, entity, id)
		entityTags.appendTag(NBTHelper.asTag(eID))
		stack.getTagCompound.setTag("dataCore", entityTags)

		stack
	}

	def removeEntityFromDataCore(stack: ItemStack, eID: String): ItemStack = {
		val entityTags: NBTTagList = this.getDataCore(stack)
		for (i <- 0 until entityTags.tagCount()) {
			if (entityTags.getStringTagAt(i).equals(eID)) {
				entityTags.removeTag(i)
				return stack
			}
		}
		stack
	}

	val scanner: ScrewdriverMode = new ScrewdriverMode("Scanner", "wireless", 0) {

		override def onRightClick(stack: ItemStack, world: World, player: EntityPlayer,
				mop: MovingObjectPosition): ItemStack = {
			if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY &&
					mop.entityHit.isInstanceOf[EntityLivingBase]) {
				//player.setItemInUse(stack, 32)
				//val tag = new NBTTagCompound
				//mop.entityHit.writeToNBTOptional(tag)
				//println(tag)
				println("Click on " + WorldHelper.isServer())
				return ScrewdriverMode.addEntityToDataCore(stack,
					mop.entityHit.asInstanceOf[EntityLivingBase], player.getGameProfile.getId)
			}
			stack
		}

		/*
		override def getUseAction(stack: ItemStack): EnumAction = EnumAction.BLOCK

		override def onUseFinish(stack: ItemStack, worldIn: World,
				playerIn: EntityPlayer, mop: MovingObjectPosition): ItemStack = {
			LogHelper.info(Origin.MODNAME, "Finished scan")
			if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY &&
					mop.entityHit.isInstanceOf[EntityLivingBase]) {
				return ScrewdriverMode.addEntityToDataCore(stack.copy(),
					mop.entityHit.asInstanceOf[EntityLivingBase])
			}
			stack
		}
		*/

	}

	val dataCore: ScrewdriverMode = new ScrewdriverMode("Data Core", "dataCore", 0) {

		override def onSelection(stack: ItemStack, player: EntityPlayer): Unit = {
			//println("Open the core")
			player.openGui(Origin, 0, player.worldObj,
				player.getPosition.getX, player.getPosition.getY, player.getPosition.getZ)
		}

	}

	val wrench: ScrewdriverMode = new ScrewdriverMode("Wrench", "Wrench", 0)

	val ae2NetworkTool: ScrewdriverMode = new ScrewdriverMode(
		"Network Tool", "basicGear", 5)

	val ae2WirelessTerminal: ScrewdriverMode = new ScrewdriverMode(
		"Wireless Terminal", "basicGear", 5)

}

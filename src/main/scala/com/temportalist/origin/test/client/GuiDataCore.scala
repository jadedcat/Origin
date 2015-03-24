package com.temportalist.origin.test.client

import java.util

import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.utility.Scala
import com.temportalist.origin.test._
import com.temportalist.origin.wrapper.client.gui.GuiScreenWrapper
import net.minecraft.client.renderer.{GlStateManager, OpenGlHelper, RenderHelper}
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList

/**
 *
 *
 * @author TheTemportalist
 */
class GuiDataCore(player: EntityPlayer) extends GuiScreenWrapper {

	val sonicStack: ItemStack = player.getCurrentEquippedItem
	var index: util.List[Pair[String, EntityState]] = null

	override def initGui(): Unit = {
		super.initGui()

		this.index = new util.ArrayList[Pair[String, EntityState]]()
		val entityTags: NBTTagList = ScrewdriverMode.getDataCore(sonicStack)
		Scala.foreach(entityTags, (index: Int, str: Any) => {
			val entityID: String = str.asInstanceOf[String]
			//println("Found ID: " + entityID)
			//println("\tIs ID in loaded data: " + Sonic.entityStates.contains(entityID))
			this.index.add(new Pair[String, EntityState](
				entityID, Sonic.entityStates(entityID)
			))
		}: Unit)

	}

	override protected def drawGuiForegroundLayer(mouseX: Int, mouseY: Int,
			renderPartialTicks: Float): Unit = {
		super.drawGuiForegroundLayer(mouseX, mouseY, renderPartialTicks)

		if (this.index.size() > 0) {
			GlStateManager.pushMatrix()
			GlStateManager.translate(mouseX, mouseY, 0D)
			this.drawEntityState(this.index.get(0).getValue(), 15)
			GlStateManager.popMatrix()
		}

	}

	def drawEntityState(state: EntityState, scale: Float): Unit = {
		val entity: Entity = state.getEntity()
		if (entity == null) return

		GlStateManager.enableColorMaterial()
		GlStateManager.pushMatrix()

		GlStateManager.disableAlpha()

		//GL11.glTranslatef((float)posX, (float)posY, 50.0F);

		//GL11.glScalef((float)(-scale), (float)scale, (float)scale);
		GlStateManager.scale(-scale, scale, scale)
		GlStateManager.rotate(180f, 0f, 0f, 1f)
		/*
		val yawOffset: Float = entity.renderYawOffset
		val yaw: Float = entity.rotationYaw
		val pitch: Float = entity.rotationPitch
		val yawHead: Float = entity.rotationYawHead
		*/

		GlStateManager.rotate(135f, 0f, 1f, 0f)
		RenderHelper.enableStandardItemLighting()
		GlStateManager.rotate(-135f, 0f, 1f, 0f)
		GlStateManager.rotate((-Math.atan(1 / 20f) * 20f).toFloat, 1f, 0f, 0f)
		GlStateManager.rotate(15f, 1f, 0f, 0f)
		GlStateManager.rotate(25f, 0f, 1f, 0f)

		GlStateManager.translate(0f, entity.getYOffset, 0f)

		GlStateManager.color(1f, 1f, 1f, 1f)

		if (entity.isInstanceOf[EntityDragon]) GlStateManager.rotate(180f, 0f, 1f, 0f)

		val viewY: Float = Rendering.mc.getRenderManager.playerViewY
		Rendering.mc.getRenderManager.playerViewY = 180.0F
		Rendering.mc.getRenderManager.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F)

		/*
		if (entity.isInstanceOf[EntityDragon]) GlStateManager.rotate(180f, 0f, -1f, 0f)

		GlStateManager.translate(0f, -0.22f, 0f)
		OpenGlHelper.setLightmapTextureCoords(
			OpenGlHelper.lightmapTexUnit, 255.0F * 0.8F, 255.0F * 0.8F)
		TessRenderer.getRenderer().setBrightness(240)
		*/

		Rendering.mc.getRenderManager.playerViewY = viewY

		GlStateManager.popMatrix()

		RenderHelper.disableStandardItemLighting()

		GlStateManager.enableAlpha()

		GlStateManager.disableRescaleNormal()
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit)
		GlStateManager.disableTexture2D()
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit)

	}

}

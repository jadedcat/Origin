package com.temportalist.origin.test.client

import java.util
import com.temportalist.origin.api.client.utility.Rendering
import com.temportalist.origin.api.common.utility.Scala
import com.temportalist.origin.foundation.client.gui.GuiScreenBase
import com.temportalist.origin.test._
import net.minecraft.client.renderer.{OpenGlHelper, RenderHelper}
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
class GuiDataCore(player: EntityPlayer) extends GuiScreenBase {

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
			GL11.glPushMatrix()
			GL11.glTranslated(mouseX, mouseY, 0D)
			this.drawEntityState(this.index.get(0).getValue(), 15)
			GL11.glPopMatrix()
		}

	}

	def drawEntityState(state: EntityState, scale: Float): Unit = {
		val entity: Entity = state.getEntity()
		if (entity == null) return

		GL11.glEnable(GL11.GL_COLOR_MATERIAL)
		GL11.glPushMatrix()
		GL11.glPushMatrix()

		GL11.glDisable(GL11.GL_ALPHA)

		//GL11.glTranslatef((float)posX, (float)posY, 50.0F);

		//GL11.glScalef((float)(-scale), (float)scale, (float)scale);
		GL11.glScaled(-scale, scale, scale)
		GL11.glRotatef(180f, 0f, 0f, 1f)
		/*
		val yawOffset: Float = entity.renderYawOffset
		val yaw: Float = entity.rotationYaw
		val pitch: Float = entity.rotationPitch
		val yawHead: Float = entity.rotationYawHead
		*/

		GL11.glRotatef(135f, 0f, 1f, 0f)
		RenderHelper.enableStandardItemLighting()
		GL11.glRotatef(-135f, 0f, 1f, 0f)
		GL11.glRotatef((-Math.atan(1 / 20f) * 20f).toFloat, 1f, 0f, 0f)
		GL11.glRotatef(15f, 1f, 0f, 0f)
		GL11.glRotatef(25f, 0f, 1f, 0f)

		GL11.glTranslated(0f, entity.getYOffset, 0f)

		GL11.glColor4f(1f, 1f, 1f, 1f)

		if (entity.isInstanceOf[EntityDragon]) GL11.glRotatef(180f, 0f, 1f, 0f)

		val viewY: Float = Rendering.renderManager.playerViewY
		Rendering.renderManager.playerViewY = 180.0F
		Rendering.renderManager.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F)

		/*
		if (entity.isInstanceOf[EntityDragon]) GlStateManager.rotate(180f, 0f, -1f, 0f)

		GlStateManager.translate(0f, -0.22f, 0f)
		OpenGlHelper.setLightmapTextureCoords(
			OpenGlHelper.lightmapTexUnit, 255.0F * 0.8F, 255.0F * 0.8F)
		TessRenderer.getRenderer().setBrightness(240)
		*/

		Rendering.renderManager.playerViewY = viewY

		GL11.glPopMatrix()

		RenderHelper.disableStandardItemLighting()

		GL11.glEnable(GL11.GL_ALPHA)

		//GlStateManager.disableRescaleNormal()
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit)
		GL11.glDisable(GL11.GL_TEXTURE_2D)
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit)

	}

}

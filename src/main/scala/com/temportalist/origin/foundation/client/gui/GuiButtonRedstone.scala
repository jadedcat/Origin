package com.temportalist.origin.foundation.client.gui

import com.temportalist.origin.api.client.utility.Rendering
import com.temportalist.origin.api.common.tile.RedstoneState
import com.temportalist.origin.internal.common.Origin
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiButtonRedstone(id: Int, x: Int, y: Int, val state: RedstoneState, val useOld: Boolean)
		extends GuiButton(id, x, y, 18, 18, "") {

	val texture: ResourceLocation = new
					ResourceLocation(Origin.MODID, "textures/gui/buttons.png")
	// Default Constructor

	// End Constructor

	// Other Constructors
	def this(id: Int, x: Int, y: Int, state: RedstoneState) {
		this(id, x, y, state, false)
	}

	// End Constructors
	override def drawButton(minecraft: Minecraft, mouseX: Int, mouseY: Int): Unit = {
		if (this.visible) {
			Rendering.bindResource(this.texture)
			GL11.glColor3f(1.0F, 1.0F, 1.0F)
			val isHoveredOn: Boolean = mouseX >= this.xPosition && mouseY >= this.yPosition &&
					mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height
			var u: Int = 100
			var v: Int = 0

			val redstoneStateAsInt: Int = RedstoneState.getIntFromState(this.state)
			u += redstoneStateAsInt * 18

			if (this.useOld) {
				u += 54
			}

			v += this.getHoverState(isHoveredOn) * 18

			if (this.enabled) v += 36

			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, this.width,
				this.height)

		}
	}
}

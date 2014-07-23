package com.countrygamer.cgo.client.gui

import com.countrygamer.cgo.common.Origin
import com.countrygamer.cgo.common.lib.util.UtilRender
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiButtonArrow(id: Int, x: Int, y: Int, buttonType: ArrowButtonType)
		extends GuiButton(id, x, y, 0, 0, "") {

	val longSide: Int = 15
	val shortSide: Int = 10
	val texture: ResourceLocation = new
					ResourceLocation(Origin.pluginID, "textures/gui/buttons.png")
	// Default Constructor
	if (this.buttonType.equals(ArrowButtonType.LEFT) ||
			this.buttonType.equals(ArrowButtonType.RIGHT)) {
		this.width = this.shortSide
		this.height = this.longSide
	}
	else {
		this.width = this.longSide
		this.height = this.shortSide
	}

	// End Constructor

	// Other Constructors

	// End Constructors

	override def drawButton(minecraft: Minecraft, mouseX: Int, mouseY: Int): Unit = {
		if (this.visible) {
			UtilRender.bindResource(this.texture)
			GL11.glColor3f(1.0F, 1.0F, 1.0F)
			val isHoveredOn: Boolean = mouseX >= this.xPosition && mouseY >= this.yPosition &&
					mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height
			val u: Int = 0
			var v: Int = 0

			if (this.buttonType eq ArrowButtonType.UP)
				v += 0
			if (this.buttonType eq ArrowButtonType.DOWN)
				v += 10
			if (this.buttonType eq ArrowButtonType.LEFT)
				v += 20
			if (this.buttonType eq ArrowButtonType.RIGHT)
				v += 35

			this.drawTexturedModalRect(this.xPosition, this.yPosition,
				u + (this.getHoverState(isHoveredOn) * 15), v, this.width, this.height)

		}
	}

}

package com.temportalist.origin.library.client.gui

import com.temportalist.origin.library.client.utility.Rendering
import com.temportalist.origin.library.common.Origin
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
@SideOnly(Side.CLIENT)
class GuiButtonCheck(id: Int, x: Int, y: Int, val isCheckmark: Boolean)
		extends GuiButton(id, x, y, 20, 20, "") {

	val texture: ResourceLocation = new
					ResourceLocation(Origin.MODID, "textures/gui/buttons.png")
	// Default Constructor

	// End Constructor

	// Other Constructors

	// End Constructors
	override def drawButton(minecraft: Minecraft, mouseX: Int, mouseY: Int): Unit = {
		if (this.visible) {
			Rendering.bindResource(this.texture)
			GlStateManager.color(1.0F, 1.0F, 1.0F)
			val isHoveredOn: Boolean = mouseX >= this.xPosition && mouseY >= this.yPosition &&
					mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height
			val k: Int = this.getHoverState(isHoveredOn)

			var u: Int = 0
			var v: Int = 0

			if (this.isCheckmark) {
				u += 20
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, 20 + u, k * 20, this.width,
				this.height)

		}
	}
}

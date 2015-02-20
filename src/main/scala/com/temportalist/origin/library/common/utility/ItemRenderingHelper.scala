package com.temportalist.origin.library.common.utility

import java.util

import com.temportalist.origin.wrapper.common.rendering.IRenderingObject
import net.minecraft.util.IRegistry
import net.minecraftforge.fml.relauncher.{SideOnly, Side}

/**
 *
 *
 * @author TheTemportalist
 */
object ItemRenderingHelper {

	val registered: util.List[IRenderingObject] = new util.ArrayList[IRenderingObject]()

	def register(item: IRenderingObject): Unit = this.registered.add(item)

	@SideOnly(Side.CLIENT)
	def registerItemRenders(): Unit =
		for (i <- 0 until this.registered.size()) {
			this.registered.get(i).registerRendering()
		}

	@SideOnly(Side.CLIENT)
	def bake(reg: IRegistry): Unit =
		for (i <- 0 until this.registered.size()) {
			this.registered.get(i).bakeModel(reg)
		}

}

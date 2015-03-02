package com.temportalist.origin.library.common.utility

import java.util
import com.temportalist.origin.api.rendering.IRenderingObject
import com.temportalist.origin.library.common.lib.LogHelper
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
			LogHelper.info("Origin", "Baking " + this.registered.get(i).getCompoundName())
			this.registered.get(i).bakeModel(reg)
		}

}

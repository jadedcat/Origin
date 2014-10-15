package com.countrygamer.cgo.library.common.utility

import com.countrygamer.cgo.library.common.lib.NameParser
import com.google.gson.JsonArray
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author CountryGamer
 */
object Json {

	def toReadableString(json: String): String = {
		var readable: String = ""
		val chars: Array[Char] = json.toCharArray
		var isIteratingInString: Boolean = false
		var tabs: Int = 0
		for (c <- chars) {
			if (c == '}' || c == ']') {
				tabs -= 1
				readable = this.addLineAndTabs(readable, tabs)
			}

			readable += c

			if (c == '{' || c == '[') {
				tabs += 1
				readable = this.addLineAndTabs(readable, tabs)
			}

			if (c == ':' && !isIteratingInString) {
				readable += " "
			}

			if (c == '"') {
				isIteratingInString = !isIteratingInString
			}

			if (c == ',' && !isIteratingInString) {
				readable += this.addLineAndTabs(readable, tabs)
			}

		}

		readable
	}

	private def addLineAndTabs(currentString: String, tabs: Int): String = {
		var str: String = currentString
		str += '\n'
		for (i <- 1 to tabs) {
			str += "   "
		}
		str
	}

	def registerRecipe(array: JsonArray): Unit = {
		var result: ItemStack = null
		for (i <- 0 until array.size()) {
			val parts: JsonArray = array.get(i).getAsJsonArray
			result = NameParser.getItemStack(parts.get(0).getAsString)
			var isShaped: Boolean = false
			val objs: Array[Object] = new Array[Object](parts.size - 1)
			for (i1 <- 1 until parts.size()) {
				val j: Int = i1 - 1
				val comp: String = parts.get(i1).getAsString
				if (comp.matches("(.*):(.*)")) {
					objs(j) = NameParser.getItemStack(comp)
				}
				else if (comp.length == 1 && j > 1) {
					objs(j) = (comp.charAt(0) + "")
				}
				else {
					objs(j) = comp
					isShaped = true
				}
			}
			if (isShaped) {
				GameRegistry.addShapedRecipe(result, objs)
			}
			else {
				GameRegistry.addShapelessRecipe(result, objs)
			}
		}
	}

}

package com.temportalist.origin.library.common.utility

import java.io.{FileReader, File}
import java.util
import java.util.Map.Entry

import com.google.common.io.Files
import com.google.gson._
import com.temportalist.origin.library.common.lib.NameParser
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Property
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 *
 *
 * @author TheTemportalist
 */
object Json {

	val gson: Gson = new Gson()
	val parser: JsonParser = new JsonParser()

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
				readable = this.addLineAndTabs(readable, tabs)
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

	def writeToFile(jsonElement: JsonElement, file: File, formatted: Boolean): Unit = {
		val jsonStr = this.gson.toJson(jsonElement)
		val fileStr = if (formatted) this.toReadableString(jsonStr) else jsonStr
		Files.write(fileStr.getBytes, file)
	}

	def getJson(file: File): JsonElement = this.parser.parse(new FileReader(file))

	object Config {

		def addTo(json: util.HashMap[String, Any], comments: util.HashMap[String, AnyRef]): Unit = {
			json.put("Bool", false)
			json.put("Integer", 20)
			comments.put("Integer", "This int comment")
			json.put("Float", 1.0F)
			comments.put("Float", Array[String]("Line 1", "Line 2", "Line 3"))
			json.put("Double", 2.0D)
			json.put("String", "strgda")
			json.put("array", Array[Int](0, 5, 2))
			val map: util.HashMap[Int, Int] = new util.HashMap[Int, Int]()
			map.put(0, -1)
			map.put(1, 1)
			map.put(3, 4)
			json.put("list", map)

		}

		def toJson(obj: Any): JsonElement = {
			obj match {
				case bool: Boolean =>
					new JsonPrimitive(bool)
				case int: Int =>
					new JsonPrimitive(int)
				case float: Float =>
					new JsonPrimitive(float)
				case double: Double =>
					new JsonPrimitive(double)
				case string: String =>
					new JsonPrimitive(string)
				case ar: Array[_] =>
					val jsonArray: JsonArray = new JsonArray()
					for (i <- 0 until ar.length) {
						jsonArray.add(this.toJson(ar(i)))
					}
					jsonArray
				case map: util.Map[_, _] =>
					val jsonObj: JsonObject = new JsonObject()
					val iter: util.Iterator[_] = map.keySet().iterator()
					while (iter.hasNext) {
						val key = iter.next()
						jsonObj.add(key.toString, this.toJson(map.get(key)))
					}
					jsonObj
				case prop: Property =>
					prop.getType match {
						case Property.Type.BOOLEAN =>
							if (prop.isList) this.toJson(prop.getBooleanList)
							else this.toJson(prop.getBoolean)
						case Property.Type.DOUBLE =>
							if (prop.isList) this.toJson(prop.getDoubleList)
							else this.toJson(prop.getDouble)
						case Property.Type.INTEGER =>
							if (prop.isList) this.toJson(prop.getIntList)
							else this.toJson(prop.getInt)
						case Property.Type.STRING =>
							if (prop.isList) this.toJson(prop.getStringList)
							else this.toJson(prop.getString)
						case _ =>
							null
					}
				case element: JsonElement => element
				case _ =>
					null
			}
		}

		def toString(element: JsonElement, tab: Int): String = {
			this.toString(element, new util.HashMap[String, String](), tab)
		}

		def toString(element: JsonElement, comments: util.Map[String, _], tab: Int): String = {
			var indents: Int = tab
			element match {
				case prim: JsonPrimitive =>
					if (prim.isBoolean) prim.getAsBoolean + ""
					else if (prim.isNumber) prim.getAsNumber + ""
					else "\"" + prim.getAsString + "\""
				case array: JsonArray =>
					val sb: StringBuilder = new StringBuilder("[\n")
					indents += 1
					for (i <- 0 until array.size()) {
						this.tab(sb, indents)
						sb.append(this.toString(array.get(i), comments, indents))
						if (i != array.size() - 1)
							sb.append(",")
						this.line(sb)
					}
					indents -= 1
					this.tab(sb, indents)
					sb.append("]")
					sb.toString()
				case obj: JsonObject =>
					val sb: StringBuilder = new StringBuilder("{\n")
					indents += 1
					val iter: util.Iterator[Entry[String, JsonElement]] = obj.entrySet().iterator()
					while (iter.hasNext) {
						val entry: Entry[String, JsonElement] = iter.next()
						val name: String = entry.getKey

						if (comments.containsKey(name)) {
							comments.get(name) match {
								case str: String =>
									this.tab(sb, indents)
									sb.append("// " + str)
								case ar: Array[String] =>
									this.tab(sb, indents)
									sb.append("/*")
									this.line(sb)
									indents += 1
									for (i <- 0 until ar.length) {
										this.tab(sb, indents)
										sb.append(ar(i))
										this.line(sb)
									}
									indents -= 1
									this.tab(sb, indents)
									sb.append("*/")
								case _ =>
							}
							this.line(sb)
						}

						val value: JsonElement = entry.getValue
						this.tab(sb, indents)
						sb.append("\"" + name + "\": ")
						sb.append(this.toString(value, comments, indents))
						if (iter.hasNext) sb.append(",")
						this.line(sb)

					}
					indents -= 1
					this.tab(sb, indents)
					sb.append("}")
					sb.toString()
				case _ =>
					""
			}
		}

		def line(builder: StringBuilder): Unit = builder.append("\n")

		def tab(builder: StringBuilder, length: Int): Unit =
			for (i <- 1 to length) builder.append("\t")

	}

}

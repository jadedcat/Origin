package com.temportalist.origin.library.common.utility

import java.util

import com.temportalist.origin.api.INBTSaver
import cpw.mods.fml.common.ObfuscationReflectionHelper
import net.minecraft.nbt.NBTBase.NBTPrimitive
import net.minecraft.nbt._

import scala.reflect.runtime.universe._

/**
 *
 *
 * @author TheTemportalist
 */
object NBTHelper {

	def getNBTType[T: TypeTag]: Int = {
		try {
			typeOf[T] match {
				case t if t =:= typeOf[Byte] => 1
				case t if t =:= typeOf[Short] => 2
				case t if t =:= typeOf[Int] => 3
				case t if t =:= typeOf[Long] => 4
				case t if t =:= typeOf[Float] => 5
				case t if t =:= typeOf[Double] => 6
				case t if t =:= typeOf[Array[Byte]] => 7
				case t if t =:= typeOf[String] => 8
				case t if t =:= typeOf[NBTTagList] => 9
				case t if t =:= typeOf[NBTTagCompound] => 10
				case t if t =:= typeOf[Array[Int]] => 11
				case _ => -1
			}
		}
		catch {
			case e: Exception =>
				e.printStackTrace()
				-1
		}
	}

	def getTagList[T: TypeTag](nbt: NBTTagCompound, key: String): NBTTagList = {
		nbt.getTagList(key, this.getNBTType[T])
	}

	def getTagValueAt(nbtList: NBTTagList, index: Int): Any = {
		val base: NBTBase = ObfuscationReflectionHelper
				.getPrivateValue(classOf[NBTTagList], nbtList, 0).asInstanceOf[util.List[NBTBase]]
				.get(index)
		base.getId match {
			case 1 => base.asInstanceOf[NBTPrimitive].func_150290_f
			case 2 => base.asInstanceOf[NBTPrimitive].func_150289_e
			case 3 => base.asInstanceOf[NBTPrimitive].func_150287_d
			case 4 => base.asInstanceOf[NBTPrimitive].func_150291_c
			case 5 => base.asInstanceOf[NBTPrimitive].func_150288_h
			case 6 => base.asInstanceOf[NBTPrimitive].func_150286_g
			case 7 => base.asInstanceOf[NBTTagByteArray].func_150292_c
			case 8 => base.asInstanceOf[NBTTagString].func_150285_a_
			case 9 => base.asInstanceOf[NBTTagList]
			case 10 => base.asInstanceOf[NBTTagCompound]
			case 11 => base.asInstanceOf[NBTTagIntArray].func_150302_c
			case _ => base
		}
	}

	def asTag(any: Any): NBTBase = {
		any match {
			case b: Byte => new NBTTagByte(b)
			case s: Short => new NBTTagShort(s)
			case i: Int => new NBTTagInt(i)
			case l: Long => new NBTTagLong(l)
			case f: Float => new NBTTagFloat(f)
			case d: Double => new NBTTagDouble(d)
			case ab: Array[Byte] => new NBTTagByteArray(ab)
			case s: String => new NBTTagString(s)
			case ai: Array[Int] => new NBTTagIntArray(ai)
			case saver: INBTSaver =>
				val tag: NBTTagCompound = new NBTTagCompound()
				saver.writeTo(tag)
				tag
			case _ =>
				throw new IllegalArgumentException("Invalid parameter type " +
						any.getClass.getCanonicalName + " with value " + any.toString)
				null
		}
	}

	def getNBT[A](vars: A*): NBTTagList = {
		val tag: NBTTagList = new NBTTagList
		for (num <- vars) tag.appendTag(this.asTag(num))
		tag
	}

}

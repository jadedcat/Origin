package com.temportalist.origin.library.common.utility

import scala.reflect.runtime.universe._

import com.temportalist.origin.api.INBTSaver
import net.minecraft.nbt.NBTBase.NBTPrimitive
import net.minecraft.nbt._

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
		val base: NBTBase = nbtList.get(index)
		base.getId match {
			case 1 => base.asInstanceOf[NBTPrimitive].getByte
			case 2 => base.asInstanceOf[NBTPrimitive].getShort
			case 3 => base.asInstanceOf[NBTPrimitive].getInt
			case 4 => base.asInstanceOf[NBTPrimitive].getLong
			case 5 => base.asInstanceOf[NBTPrimitive].getFloat
			case 6 => base.asInstanceOf[NBTPrimitive].getDouble
			case 7 => base.asInstanceOf[NBTTagByteArray].getByteArray
			case 8 => base.asInstanceOf[NBTTagString].getString
			case 9 => base.asInstanceOf[NBTTagList]
			case 10 => base.asInstanceOf[NBTTagCompound]
			case 11 => base.asInstanceOf[NBTTagIntArray].getIntArray
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

package com.temportalist.origin.api.common.utility

import java.util

import com.temportalist.origin.api.common.general.INBTSaver
import cpw.mods.fml.common.ObfuscationReflectionHelper
import net.minecraft.nbt.NBTBase.NBTPrimitive
import net.minecraft.nbt._

import scala.collection.mutable
import scala.reflect.runtime.universe._

/**
 *
 *
 * @author TheTemportalist
 */
object NBTHelper {

	private val nbtTypes = Map[Type, Int](
		typeOf[Byte] -> 1,
		typeOf[Short] -> 2,
		typeOf[Int] -> 3,
		typeOf[Long] -> 4,
		typeOf[Float] -> 5,
		typeOf[Double] -> 6,
		typeOf[Array[Byte]] -> 7,
		typeOf[String] -> 8,
		typeOf[NBTTagList] -> 9,
		typeOf[NBTTagCompound] -> 10,
		typeOf[Array[Int]] -> 11
	)

	def getNBTType[T: TypeTag]: Int = this.nbtTypes(typeOf[T])

	def getTagList[T: TypeTag](nbt: NBTTagCompound, key: String): NBTTagList = {
		nbt.getTagList(key, this.getNBTType[T])
	}

	def getTagList[T](nbt: NBTTagCompound, key: String, f: T => Unit)
			(implicit t: TypeTag[T]): Unit = {
		val list = nbt.getTagList(key, this.nbtTypes(t.tpe))
		for (i <- 0 until list.tagCount()) f(this.getTagValueAt(list, i).asInstanceOf[T])
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
			case map: mutable.Map[_, _] =>
				val tag = new NBTTagCompound
				map.foreach(f => tag.setTag(f._1.toString, this.asTag(f._2)))
				tag
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

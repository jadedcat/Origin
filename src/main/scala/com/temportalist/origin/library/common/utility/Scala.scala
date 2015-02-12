package com.temportalist.origin.library.common.utility

import java.lang
import java.util

import scala.collection.{mutable, JavaConversions}

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 *
 *
 * @author TheTemportalist 2/1/15
 */
object Scala {

	def foreach[T](collection: util.Collection[T], callback:(T) => Unit): Unit = {
		collection match {
			case list: util.List[T] =>
				for (item <- JavaConversions.asScalaBuffer(list)) callback(item)
			case set: util.Set[T] =>
				for (item <- JavaConversions.asScalaSet(set)) callback(item)
			case _ =>
				val iter: util.Iterator[T] = collection.iterator()
				while (iter.hasNext) callback(iter.next())
		}
	}

	def foreach(inv: IInventory, callback: (Int, ItemStack) => Unit): Unit = {
		for (i <- 0 until inv.getSizeInventory) callback(i, inv.getStackInSlot(i))
	}

	def foreach[T](data: Array[T], callback: (Int, T) => Unit): Unit = {
		for (i <- 0 until data.length) {
			callback(i, data(i))
		}
	}

	def foreach[T](iter: lang.Iterable[T], callback: (Int, T) => Unit): Unit = {
		var i: Int = 0
		JavaConversions.asScalaIterator(iter.iterator()).foreach(
			(t: T) => {
				i += 1
				callback(i, t)
			}
		)
	}

	def fill[B](size: Int, obj: B): Map[Int, B] = {
		val map: mutable.Map[Int, B] = mutable.Map[Int, B]()
		for(i <- 0 until size)
			map(i) = obj
		map.toMap
	}

}

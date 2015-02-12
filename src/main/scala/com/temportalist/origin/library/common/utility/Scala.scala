package com.temportalist.origin.library.common.utility

import java.util

import scala.collection.JavaConversions

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

}

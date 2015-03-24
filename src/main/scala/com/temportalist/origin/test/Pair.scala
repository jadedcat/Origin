package com.temportalist.origin.test

/**
 *
 *
 * @author TheTemportalist
 */
class Pair[A, B](private val key: A, private val value: B) {

	def getKey(): A = this.key

	def getValue(): B = this.value

	override def equals(other: Any): Boolean = other match {
		case that: Pair[A, B] => key == that.key && value == that.value
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(key, value)
		state.map( _.hashCode() ).foldLeft(0)(
			(a, b) => {
				31 * a + b
			}
		)
	}

}

package com.temportalist.origin.library.common.lib.vec

import codechicken.lib.vec._
import com.google.common.io.ByteArrayDataInput
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{ChunkCoordinates, MovingObjectPosition, Vec3}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.util.ForgeDirection

/**
 *
 *
 * @author TheTemportalist
 */
class Vector3O(_x: Double, _y: Double, _z: Double) extends Vector3(_x, _y, _z) {

	def this(array: Array[Double]) {
		this(array(0), array(1), array(2))
	}

	def this(vec: Vec3) {
		this(vec.xCoord, vec.yCoord, vec.zCoord)
	}

	def this(vec: Vector3) {
		this(vec.x, vec.y, vec.z)
	}

	def this(c: BlockCoord) {
		this(c.x, c.y, c.z)
	}

	def this(amount: Double) {
		this(amount, amount, amount)
	}

	def this(ent: Entity) {
		this(ent.posX, ent.posY, ent.posZ)
	}

	def this(tile: TileEntity) {
		this(tile.xCoord, tile.zCoord, tile.yCoord)
	}

	def this(mop: MovingObjectPosition) {
		this(mop.blockX, mop.blockY, mop.blockZ)
	}

	def this(chunk: ChunkCoordinates) {
		this(chunk.posX, chunk.posY, chunk.posZ)
	}

	def this(dir: ForgeDirection) {
		this(dir.offsetX, dir.offsetY, dir.offsetZ)
	}

	def this(nbt: NBTTagCompound) {
		this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
	}

	def this(data: ByteArrayDataInput) {
		this(data.readDouble(), data.readDouble(), data.readDouble());
	}

	def x_i(): Int = this.x.asInstanceOf[Int]

	def y_i(): Int = this.y.asInstanceOf[Int]

	def z_i(): Int = this.z.asInstanceOf[Int]

	def x_f(): Float = this.x.asInstanceOf[Float]

	def y_f(): Float = this.y.asInstanceOf[Float]

	def z_f(): Float = this.z.asInstanceOf[Float]

	def getBlock(world: IBlockAccess): Block =
		world.getBlock(this.x_i(), this.y_i(), this.z_i())

	def getMetadata(world: IBlockAccess): Int =
		world.getBlockMetadata(this.x_i(), this.y_i(), this.z_i())

	def getTile(world: IBlockAccess): TileEntity =
		world.getTileEntity(this.x_i(), this.y_i(), this.z_i())

	def setBlock(world: World, block: Block, meta: Int, notify: Int): Unit = {
		world.setBlock(this.x_i(), this.y_i(), this.z_i(), block, meta, notify)
	}

	def setBlock(world: World, block: Block, meta: Int): Unit = {
		this.setBlock(world, block, meta, 3)
	}

	def setBlock(world: World, block: Block): Unit = {
		this.setBlock(world, block, 0)
	}

	def setBlockToAir(world: World): Unit = {
		this.setBlock(world, Blocks.air)
	}

	def scale(x: Double, y: Double, z: Double): Vector3O = {
		this.x *= x
		this.y *= y
		this.z *= z
		this
	}

	def scale(amount: Double): Vector3O = this.scale(amount, amount, amount)

	def scale(vec: Vector3): Vector3O = this.scale(vec.x, vec.y, vec.z)

	def invert(): Vector3O = this.scale(-1)

	override def copy(): Vector3O = super.copy().asInstanceOf[Vector3O]

	def toNBT(nbt: NBTTagCompound) {
		nbt.setDouble("x", this.x)
		nbt.setDouble("y", this.y)
		nbt.setDouble("z", this.z)
	}

	def distance(vec: Vector3): Double = {
		this.copy().subtract(vec).mag()
	}

	def distance(x: Double, y: Double, z: Double): Double = {
		this.distance(new Vector3O(x, y, z))
	}

	@SideOnly(Side.CLIENT)
	def addVecUV(u: Double, v: Double): Unit = {
		Tessellator.instance.addVertexWithUV(this.x, this.y, this.z, u, v)
	}

	def add(dir: ForgeDirection): Vector3O = this.add(new Vector3O(dir))

	/*  Overrides  */

	override def set(d: Double, d1: Double, d2: Double): Vector3O =
		new Vector3O(super.set(d, d1, d2))

	override def set(vec: Vector3): Vector3O = new Vector3O(super.set(vec))

	override def setSide(s: Int, v: Double): Vector3O = new Vector3O(super.setSide(s, v))

	override def add(d: Double, d1: Double, d2: Double): Vector3O =
		new Vector3O(super.add(d, d1, d2))

	override def add(vec: Vector3): Vector3O = new Vector3O(super.add(vec))

	override def add(d: Double): Vector3O = new Vector3O(super.add(d))

	override def sub(vec: Vector3): Vector3O = new Vector3O(super.sub(vec))

	override def subtract(vec: Vector3): Vector3O = new Vector3O(super.subtract(vec))

	override def negate(vec: Vector3): Vector3O = new Vector3O(super.negate(vec))

	override def multiply(d: Double): Vector3O = new Vector3O(super.multiply(d))

	override def multiply(f: Vector3): Vector3O = new Vector3O(super.multiply(f))

	override def multiply(fx: Double, fy: Double, fz: Double): Vector3O =
		new Vector3O(super.multiply(fx, fy, fz))

	override def normalize(): Vector3O = new Vector3O(super.normalize())

	override def perpendicular(): Vector3O = new Vector3O(super.perpendicular())

	override def xCrossProduct(): Vector3O = new Vector3O(super.xCrossProduct())

	override def zCrossProduct(): Vector3O = new Vector3O(super.zCrossProduct())

	override def yCrossProduct(): Vector3O = new Vector3O(super.yCrossProduct())

	override def rotate(angle: Double, axis: Vector3): Vector3O =
		new Vector3O(super.rotate(angle, axis))

	override def rotate(rotator: Quat): Vector3O = new Vector3O(super.rotate(rotator))

	override def YZintercept(end: Vector3, px: Double): Vector3O =
		new Vector3O(super.YZintercept(end, px))

	override def XZintercept(end: Vector3, py: Double): Vector3O =
		new Vector3O(super.XZintercept(end, py))

	override def XYintercept(end: Vector3, pz: Double): Vector3O =
		new Vector3O(super.XYintercept(end, pz))

	override def negate(): Vector3O = new Vector3O(super.negate())

	override def project(b: Vector3): Vector3O = new Vector3O(super.project(b))

	override def apply(t: Transformation): Vector3O = new Vector3O(super.apply(t))

	override def unary_$tilde(): Vector3O = new Vector3O(super.unary_$tilde())

	override def $tilde(): Vector3O = new Vector3O(super.$tilde())

	override def $minus(v: Vector3): Vector3O = new Vector3O(super.$minus(v))

	override def $plus(v: Vector3): Vector3O = new Vector3O(super.$plus(v))

	override def $times(d: Double): Vector3O = new Vector3O(super.$times(d))

	override def $div(d: Double): Vector3O = new Vector3O(super.$div(d))

	override def $times(v: Vector3): Vector3O = new Vector3O(super.$times(v))
}

object Vector3O {

	def from(x: Double, y: Double, z: Double, dir: ForgeDirection): Vector3O = {
		new Vector3O(x, y, z).add(new Vector3O(dir)).asInstanceOf[Vector3O]
	}

	def UP: Vector3O = new Vector3O(ForgeDirection.UP)

	def DOWN: Vector3O = new Vector3O(ForgeDirection.DOWN)

	def NORTH: Vector3O = new Vector3O(ForgeDirection.NORTH)

	def SOUTH: Vector3O = new Vector3O(ForgeDirection.SOUTH)

	def EAST: Vector3O = new Vector3O(ForgeDirection.EAST)

	def WEST: Vector3O = new Vector3O(ForgeDirection.WEST)

	def ZERO: Vector3O = new Vector3O(0, 0, 0)

	def CENTER: Vector3O = new Vector3O(0.5, 0.5, 0.5)

}

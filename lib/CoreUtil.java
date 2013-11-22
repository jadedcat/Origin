package CountryGamer_Core.lib;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class CoreUtil {
	
	public static Class<?> findClass(String className) {
		
		try{
			Class<?> desiredClass = Class.forName(className);
			
			return desiredClass;
		}catch(ClassNotFoundException e){
			System.err.println("Class with name " + className + " not found.");
			return null;
		}
	}
	
	public static boolean teleportTo(EntityLivingBase ent,
			double x, double y, double z, boolean onlySolids) {
		
		EnderTeleportEvent event = new EnderTeleportEvent(
				ent, x, y, z, 0);
		if(MinecraftForge.EVENT_BUS.post(event)) {
			System.out.println("Event Error");
			return false;
		}
		double prevX = ent.posX;
		double prevY = ent.posY;
		double prevZ = ent.posZ;
		double newX = event.targetX;
		double newY = event.targetY;
		double newZ = event.targetZ;
		int blockX = MathHelper.floor_double(ent.posX);
		int blockY = MathHelper.floor_double(ent.posY);
		int blockZ = MathHelper.floor_double(ent.posZ);
		boolean isSolidBlock = false, noLiquids = false;
		
		if(ent.worldObj.blockExists(blockX, blockY, blockZ)) {
			isSolidBlock = false;
			int blockID = 0;
			// Check for valid solid blocks
			while(!isSolidBlock && blockY > 0) {
				blockID = ent.worldObj.getBlockId(blockX, blockY-1, blockZ);
				if(blockID != 0 &&
						Block.blocksList[blockID]
								.blockMaterial.blocksMovement()) {
					isSolidBlock = true;
				}else{
					--newY;
					--blockY;
				}
			}
			
			// Check for liquids around the entity
			noLiquids = false;
			if(ent.worldObj.getCollidingBoundingBoxes(
						ent, ent.boundingBox).isEmpty() &&
					!ent.worldObj.isAnyLiquid(ent.boundingBox))
				noLiquids = true;
			
			/*
			 * if not only solids OR
			 * if only solids && solids
			 */
			if(!onlySolids || (onlySolids && isSolidBlock && noLiquids)) {
				ent.setPosition(newX, newY, newZ);
				
				
				// particles
				short short1 = 128;
				Random rand = new Random();
				for (int l = 0; l < short1; ++l) {
					double d6 = (double)l / ((double)short1 - 1.0D);
					float f = (rand.nextFloat() - 0.5F) * 0.2F;
					float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
					float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
					double d7 = prevX + (ent.posX - prevX) * d6 +
							(rand.nextDouble() - 0.5D) *
							(double)ent.width * 2.0D;
					double d8 = prevY + (ent.posY - prevY) * d6 +
							rand.nextDouble() * (double)ent.height;
					double d9 = prevZ + (ent.posZ - prevZ) * d6 +
							(rand.nextDouble() - 0.5D) *
							(double)ent.width * 2.0D;
					ent.worldObj.spawnParticle("portal", d7, d8, d9,
							(double)f, (double)f1, (double)f2);
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean teleportTo(EntityLivingBase ent,
			double par1, double par3, double par5)
    {
        EnderTeleportEvent event = new EnderTeleportEvent(ent, par1, par3, par5, 0);
        if (MinecraftForge.EVENT_BUS.post(event)){
            System.out.println("Event Error");
        	return false;
        }

        double d3 = ent.posX;
        double d4 = ent.posY;
        double d5 = ent.posZ;
        double newX = event.targetX;
        double newY = event.targetY;
        double newZ = event.targetZ;
        boolean flag = false;
        int i = MathHelper.floor_double(newX);
        int j = MathHelper.floor_double(newY);
        int k = MathHelper.floor_double(newZ);
        int l;

        if (ent.worldObj.blockExists(i, j, k))
        {
            boolean flag1 = false;

            while (!flag1 && j > 0)
            {
                l = ent.worldObj.getBlockId(i, j - 1, k);

                if (l != 0 && Block.blocksList[l].blockMaterial.blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --newY;
                    --j;
                }
            }

            if (flag1)
            {
                ent.setPosition(newX, newY, newZ);
                System.out.println("Teleporting entity to xyz");
                if (ent.worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox).isEmpty() && !ent.worldObj.isAnyLiquid(ent.boundingBox))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            ent.setPosition(d3, d4, d5);
            System.out.println("Put player back");
            return false;
        }
        else
        {
            short short1 = 128;
            Random rand = new Random();
            for (l = 0; l < short1; ++l)
            {
                double d6 = (double)l / ((double)short1 - 1.0D);
                float f = (rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (newX - d3) * d6 + (rand.nextDouble() - 0.5D) * (double)ent.width * 2.0D;
                double d8 = d4 + (newY - d4) * d6 + rand.nextDouble() * (double)ent.height;
                double d9 = d5 + (newZ - d5) * d6 + (rand.nextDouble() - 0.5D) * (double)ent.width * 2.0D;
                ent.worldObj.spawnParticle("portal", d7, d8, d9, (double)f, (double)f1, (double)f2);
            }

            //ent.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            //ent.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }
}

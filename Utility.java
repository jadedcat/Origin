package CountryGamer_Core;

import net.minecraft.entity.EntityList;

public class Utility {
	
	public static int getUniqueEntityId() {
		int entityid = 0;
		do {
			entityid += 1;
		} while (EntityList.getStringFromID(entityid) != null);
		return entityid;
	}
	
}

package CountryGamer_Core.lib;

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
	
}

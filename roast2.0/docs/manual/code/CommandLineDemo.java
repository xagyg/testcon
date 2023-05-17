import roast.*;

public class CommandLineDemo {

public static void main(String[] args) {
	CommandLine c = null;

	try {
		// custom flags
		FlagSpec f1 = new FlagSpec("-maxsize",false,true,
			new IntegerArgType(10));
		FlagSpec f2 = new FlagSpec("-debug",false,false,null);

		c = Roast.parseArgs(new FlagSpec[] {f1,f2}, args);
	
		String errorMsg = c.getErrorMessage();
		if (errorMsg == null) {
			// default flags
			if (c.isArgPresent("-level"))
				System.out.println("level: " + 
					c.getFlagArg("-level"));
			if (c.isArgPresent("-logfile"))
				System.out.println("logfile: " + 
					c.getFlagArg("-logfile"));
			System.out.println("serialize: " + 
				c.isArgPresent("-serialize"));

			// custom flags
			if (c.isArgPresent("-maxsize"))
				System.out.println("maxsize: " + 
					c.getFlagArg("-maxsize"));
			System.out.println("debug: " + 
				c.isArgPresent("-debug"));
		} else {
			System.out.println(errorMsg);
		}
	} catch (ParameterException pe) {
		System.out.println(pe);
	}
}

}

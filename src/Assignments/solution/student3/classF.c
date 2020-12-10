package files.solution.student3;

/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

interface animals {
	void speak();
	// void run();
}

class horse implements animals {
	public void speak() {
		System.out.println("Neigh");
	}
	public void run() {
		System.out.println("Run");
	}
}

/* Name of the class has to be "Main" only if the class is public. */
class data
{
	public static void main (String[] args) throws java.lang.Exception
	{
		animals a = new horse();
		a.speak();
		a.run(); // if run is not defined in interface then it is compile error
		// since reference "a" is of type animal
	}
}
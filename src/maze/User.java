package maze;

import java.io.*;
//import java.net.*;

public class User {

	ObjectOutputStream out;
	ObjectInputStream in;
	
	public User(ObjectOutputStream out, ObjectInputStream in)
	{
		this.out = out;
		this.in = in;
	}

}
package info.daviot.util.misc;

import java.io.*;

public class TextFileReader extends FileReader {
	BufferedReader reader ;
	
	public TextFileReader(File inputFile) throws FileNotFoundException{
		super(inputFile);
		reader = new BufferedReader(this);
	}
	
	public TextFileReader(String inputFileName) throws FileNotFoundException{
		super(inputFileName);
		reader = new BufferedReader(this);
	}
	
	public String readFile() throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		while ((line = readLine()) != null) 
			stringBuffer.append(line + System.getProperty("line.separator"));
		
		return stringBuffer.toString();
	}

	public String readLine() throws IOException {
		return reader.readLine();
	}
}


/*
* This is a simple program to illustrate some features of
* the SmallXMLParser class.
*/

import com.room4me.xml.*;
import java.util.*;
import java.io.*;

public class XMLEcho
{

  /**
  * Main routine of this program will "echo" the contents of
  * the XML file called "movies2.xml", or whatever is on commandline.
  */
  public static void main(String[] args) throws Exception
  {

    String sFilePath = "movies2.xml"; //The XML file to process.
    String sXML;	//We will load the content into this variable.
    SmallXMLParser oParse; //Declare the parser variable.

    //Override default filepath with commandline if given.
    if(args.length > 0) 
    {
      sFilePath = args[0]; //User has specified a file.
    }
    sXML = getTextFromFile(sFilePath); //Assume our test file.

    //We must check for errors when parsing a document.
    try
    {
      //Parse the XML document as we create the object.
      oParse = new SmallXMLParser(sXML);
    }
    catch(Exception e)
    {
      //We would do something with the error here.
      throw e;
    }

    //Print the parsed document with nice indenting.
    System.out.println("Here is \"" + sFilePath + "\" formatted by SmallXMLParser...");
    System.out.println(oParse.getXMLAsText());
  }

  /**
  * Return the content of a text file as a String object.
  */
  private static String getTextFromFile(String sFilePath) throws IOException
  {
    System.out.println("Loading File:" + '"' + sFilePath + '"');
    String sLine; //We will read each line into this temporary variable.
    StringBuffer sBuffer = new StringBuffer("");

    BufferedReader oIn = new BufferedReader(new FileReader(sFilePath));
    while((sLine = oIn.readLine()) != null)
    {
      sBuffer.append(sLine);
    }

    //Return the stuff as a regular string object.
    return sBuffer.toString();
  }
}

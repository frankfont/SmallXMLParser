/*
* This is a simple program to illustrate some features of
* the SmallXMLParser class.
* @author Frank Font
*/

import com.room4me.xml.*;
import java.util.*;
import java.io.*;

public class XMLStats
{

  /**
  * Main routine of this program will report some stats for
  * the XML file called "movies2.xml", or whatever you specify
  * as a commandline parameter.
  */
  public static void main(String[] args) throws Exception
  {

    SmallXMLParser oParse; //Declare the parser variable.
    String sFilePath = "movies2.xml";    //The XML file to process.
    String sXML;           //We will load text into this variable.

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
    System.out.println("Here are some statistics for \"" + sFilePath 
                        + "\" by SmallXMLParser...");
    System.out.println(getXMLStats(oParse));
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


  /**
  * Returns some statistics about the parsed XML document.
  */
  public static String getXMLStats(SmallXMLParser oParse)
  {

    StringBuffer sOut = new StringBuffer("");
    ArrayList oNodeList;
    Node oNode;
    StatInfo oPrologStats = new StatInfo();
    StatInfo oContentStats = new StatInfo();

    oNodeList = oParse.getPrologNodes();
    sOut.append("\nProlog Portion\n");     
    sOut.append("--------------\n");     
    for(Iterator i=oNodeList.iterator();i.hasNext();)
    {
      oPrologStats.updateStats((Node) i.next());
    }
    sOut.append(oPrologStats.getStatsText());

    sOut.append("\nContent Portion\n");     
    sOut.append("---------------\n");     
    walkTree(oParse.getRootNode(),oContentStats);
    sOut.append(oContentStats.getStatsText());

    return sOut.toString();
  }

  /**
  * Recursively walk the document object.
  */
  private static void walkTree(Node oNode, StatInfo oStatInfo)
  {

    //First get stats for this node.
    oStatInfo.updateStats(oNode);

    //Next process all the children.
    ArrayList oChildren = oNode.getChildNodes();
    for(int i=0;i<oChildren.size();i++)
    {
      walkTree((Node) oChildren.get(i), oStatInfo);
    }
  }
}


/*
* This class is used to track some statistic info.
*/
class StatInfo
{
  public int nTagNode;            //Also known as XML "element" nodes.
  public int nCommentNode;        //Things like <!-- This is a comment -->
  public int nNakedTextNode;      //Narative text containing tags.
  public int nDocumentTypeNode;         //Things like <!DOCTYPE ...
  public int nCDATANode;                //Things like <![CDATA[ ...
  public int nProcessingInstructionNode; //Things like <?...
  public int nDeepestLevel;
  
  public String getStatsText()
  {
    return
        "TagNode Count ..................... " + nTagNode
    + "\nCommentNode Count ................. " + nCommentNode
    + "\nNakedTextNode Count ............... " + nNakedTextNode
    + "\nDocumentTypeNode Count ............ " + nDocumentTypeNode
    + "\nCDATANode Count ................... " + nCDATANode
    + "\nProcessingInstructionNode Count ... " + nProcessingInstructionNode
    + "\nDeepestLevel ...................... " + nDeepestLevel + "\n";
  }

  public void updateStats(Node oNode)
  {
    if (oNode instanceof NakedTextNode)
    {
      nNakedTextNode++;
    } else if (oNode instanceof ProcessingInstructionNode) {
      nProcessingInstructionNode++;
    } else if (oNode instanceof CDATANode) {
      nCDATANode++;
    } else if (oNode instanceof CommentNode) {
      nCommentNode++;
    } else if (oNode instanceof DocumentTypeNode) {
      nDocumentTypeNode++;
    } else if (oNode instanceof TagNode) {
      nTagNode++;
    }
    if(oNode.getLevel() > nDeepestLevel)
    {
      nDeepestLevel = oNode.getLevel();
    }
  }
}


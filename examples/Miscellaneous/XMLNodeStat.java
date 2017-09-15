/*
* This is a simple program to illustrate some features of
* the SmallXMLParser class.
*
* Outputs some summary statistics about an XML file and
* a detailed list of unique node paths with a listing
* for each unique attribute sequence.
*
* Tip: Send the output to a file to see the whole thing.
*
* @author Frank Font
* @version 1.0
*/

import com.room4me.xml.*;
import java.util.*;
import java.io.*;

public class XMLNodeStat
{

  /**
  * Main routine of this program will report some stats for
  * the node path of the specified XML file.  The XML file
  * should be specified as a commandline parameter.
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
    } else {
      System.out.println("Program Usage: java XMLNodeStat <xmlfile>");
      System.out.println("Where...");
      System.out.println("   <xmlfile>::= Name of XML file to process.");
      return;
    }

    //Try to open the file.
    try{
      sXML = getTextFromFile(sFilePath); //Assume our test file.
    }
    catch(FileNotFoundException e)
    {
      System.out.println("Cannot find file called " + sFilePath);
      return;
    }

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
* This class is used to track some documemnt level statistic info.
*/
class StatInfo
{
  public int nTagNode;            //Also known as XML "element" nodes.
  public int nCommentNode;        //Things like <!-- This is a comment -->
  public int nNakedTextNode;      //Narrative text containing tags.
  public int nDocumentTypeNode;          //Things like <!DOCTYPE ...
  public int nCDATANode;                 //Things like <![CDATA[ ...
  public int nProcessingInstructionNode; //Things like <?...
  public int nDeepestLevel;

  //Keep a sorted collection of unique node paths.
  private TreeSet kNode = new TreeSet(new CompareNodeStat());


  /*
  * This class keeps the full string path of a node and
  * a collection of the attribute names for that node.
  */
  private class NodeStat
  {
    public String    sPath;    //Path string of the node.
    public ArrayList kAttribs; //Attribute names for the node.

    /**
    * Construct an instance of this class by setting the
    * member fields from the content of the specified Node.
    */
    private NodeStat(TagNode oNode)
    {
      //First build the path string.
      StringBuffer sPath1 = new StringBuffer("");
      Node oN1 = oNode;
      while(oN1.getLevel() > 0)
      {
        //We build the path as a string.
        sPath1.insert(0,oN1.getName() + "/");

        //Get the next node.
        oN1 = oN1.getParent();
      }
      sPath1.insert(0,oN1.getName() + "/");
      sPath = sPath1.toString();

      //Now get the list of attribute names as a collection.
      kAttribs = getAttribNames(oNode.getAttributes());
    }
  }


  /*
  * This class knows how to compare instances of NodeStat class.
  * First we compare the node path.  If that is the same, we
  * count the number of attributes in each node.  If that is the
  * same, we compare the names of each attribute.
  */
  class CompareNodeStat implements Comparator
  {
    public int compare(Object o1, Object o2)
    {
      int nCompareResult;
      NodeStat oN1 = (NodeStat) o1;
      NodeStat oN2 = (NodeStat) o2;
  
      nCompareResult = oN1.sPath.compareTo(oN2.sPath);
      if (nCompareResult == 0)
      {
        //Looks the same at high level, look lower.
        int nSize1 = oN1.kAttribs.size();
        int nSize2 = oN2.kAttribs.size();
        if( nSize1 < nSize2 )
        {
          //o1 is smaller.
          nCompareResult = -1;

        } else if( nSize1 > nSize2 )
        {
          //o2 is smaller.
          nCompareResult = 1;

        } else {

          //Same number of attributes, compare each name.
          String sAttrib1;
          String sAttrib2;
          int nCount;
          for(nCount = 0; nCount < nSize1; nCount++)
          {
            sAttrib1 = (String) oN1.kAttribs.get(nCount);
            sAttrib2 = (String) oN2.kAttribs.get(nCount);
            nCompareResult = sAttrib1.compareTo(sAttrib2);
            if(nCompareResult != 0)
            {
              //They are not the same and we know how now!
              break;
            }
          }
        }
      }

      //We are here once we know value of nCompareResult.
      return nCompareResult;
    }
  }


  /**
  * Pass in a collection of Attribute objects from the SmallXMLParser
  * and get a collection of attribute names.
  */
  private ArrayList getAttribNames(ArrayList kAttribs)
  {
    Attribute oAttrib;  //This type is declared in SmallXMLParser.
    ArrayList kAttribNames = new ArrayList();
    for(Iterator i=kAttribs.iterator();i.hasNext();)
    {
      oAttrib = (Attribute) i.next();
      kAttribNames.add(oAttrib.getName());
    }
    return kAttribNames;  //This is just collection of the names.
  }


  /**
  * Return formatted statistics output.
  */
  public String getStatsText()
  {
    NodeStat oNS;
    String sAttribName;
    StringBuffer sNodeInfo = new StringBuffer();
    String sPath = "";  //Start with a blank path.

    //Iterate through all the NodeStat instances.
    if(kNode.size() > 0)
    {
      sNodeInfo.append("\n\nUnique Node Path/Attribute instances"
                       + "\n------------------------------------");
    }
    for(Iterator i=kNode.iterator(); i.hasNext();)
    {
      //Get the next NodeStat instance in the collection
      oNS = (NodeStat) i.next();

      //Append path name if not already appended.
      if(0 != sPath.compareTo(oNS.sPath))
      {
        //Path not same as before.
        sPath = oNS.sPath;
        sNodeInfo.append("\n" + sPath); 
      }

      //Append the attributes to the string buffer.
      if(oNS.kAttribs.size() > 0)
      {
        sNodeInfo.append("\n...");
        for(Iterator a=oNS.kAttribs.iterator(); a.hasNext();)
        {
          sAttribName = (String) a.next();
          sNodeInfo.append(" " + sAttribName);
        }
      }
    }

    return
        "TagNode Count ..................... " + nTagNode
    + "\nCommentNode Count ................. " + nCommentNode
    + "\nNakedTextNode Count ............... " + nNakedTextNode
    + "\nDocumentTypeNode Count ............ " + nDocumentTypeNode
    + "\nCDATANode Count ................... " + nCDATANode
    + "\nProcessingInstructionNode Count ... " + nProcessingInstructionNode
    + "\nDeepestLevel ...................... " + nDeepestLevel
    + sNodeInfo.toString() + "\n";

  }


  /**
  * Update our statistics for the specified node.
  */
  public void updateStats(Node oNode)
  {
    //Lets figure out what kind of node we have.
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
      NodeStat oNodeStat = new NodeStat((TagNode) oNode);
      kNode.add(oNodeStat);
    }
    if(oNode.getLevel() > nDeepestLevel)
    {
      nDeepestLevel = oNode.getLevel();
    }
  }
}


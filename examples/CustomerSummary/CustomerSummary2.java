/*
* This program summarizes content of a customer xml document.
* Created January 2002
*
* Uses SmallXMLParser 1.01.
*
* @author Frank Font
* @version 1.0
*/

import com.room4me.xml.*;
import java.util.*;
import java.io.*;

public class CustomerSummary2 
{
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
  * Main entry point for our program.
  */
  public static void main (String args[]) throws Exception
  {
    SmallXMLParser oParse; //Declare the parser variable.
    String sFilePath = "customers.xml";    //The XML file to process.
    String sXML;           //We will load text into this variable.

    //Override default filepath with commandline if given.
    if(args.length > 0) 
    {
      sFilePath = args[0]; //User has specified a file.
    } else {
      System.out.println("Program Usage: java CustomerSummary1 <xmlfile>");
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
    
    //Do the custom work here.
    outputReport(analyzeData(oParse.getRootNode()));
  }
 
  /**
   *  Create a sorted collection of processed data.
   */
  static Collection analyzeData(Node oRootNode)
  {
    //Sort report rows as we add them to the list.
    TreeSet kSortedReportRows = new TreeSet(new CompareReportRows());
    
    //Iterate through all the level 0 nodes.
    ArrayList kLevel0 = oRootNode.getChildNodes(NodeFilter.nTagNode);
    for(Iterator iLevel0 = kLevel0.iterator(); iLevel0.hasNext();)
    {
      Node oNode = (Node) iLevel0.next();
      TagNode oTagNode = (TagNode) oNode;
      ArrayList kAttribs = oTagNode.getAttributes();
      Attribute oState = oTagNode.findAttribute("state");
      Attribute oFirstName = oTagNode.findAttribute("firstname");
      Attribute oLastName = oTagNode.findAttribute("lastname");
      ReportRow oReportRow = new ReportRow();
      oReportRow.sName = oFirstName.getValue() + " " + oLastName.getValue();
      oReportRow.sState = oState.getValue();
        
      //Now get all of the detail for this customer.
      ArrayList kLevel1 = oNode.getChildNodes(NodeFilter.nTagNode);
      for(Iterator iLevel1 = kLevel1.iterator(); iLevel1.hasNext();)
      {
        oNode = (Node) iLevel1.next();

        //Get the info for this detail row.
        oTagNode = (TagNode) oNode;
        kAttribs = oTagNode.getAttributes();
        Attribute oDate = oTagNode.findAttribute("date"); 
        Attribute oPrice = oTagNode.findAttribute("price"); 
        String sDate = oDate.getValue();
        oReportRow.purchases += 100 * Double.parseDouble(oPrice.getValue());
        if(sDate.compareTo(oReportRow.sMostRecentDate) < 0)
        {
          oReportRow.sMostRecentDate = oDate.getValue();
        }
      }
        
      //Now add the processed row to the collection.
      kSortedReportRows.add(oReportRow);
    }
    
    //Return the sorted data.
    return kSortedReportRows;
  }
  
  /**
   * Output the report in a nice format.
   **/
  static void outputReport(Collection kSortedReportRows)
  {
    System.out.println(   Pad("State",5,false)
                        + Pad("Purchases",10,true)
                        + " "
                        + Pad("Customer",20,false)
                        + "Most Recent Date");
    for(Iterator i = kSortedReportRows.iterator();i.hasNext();)
    {
      ReportRow oRow = (ReportRow) i.next();
      System.out.println(   Pad(oRow.sState,5,false) 
                          + Pad("" + (((double) oRow.purchases))/100,10,true)
                          + " "
                          + Pad(oRow.sName,20,false)
                          + oRow.sMostRecentDate);
    }
  }
  
  /**
   * Pad the text so it lines up nicely on fixed font output.
   */
  static String Pad(String sText, int width, boolean bRightJustify)
  {
    String sPad;
    int padLen = width - sText.length();
    if(padLen>0)
    {
      sPad = "                      ".substring(0,padLen);
    } else {
      sPad = "";
    }
    if(bRightJustify)
    {
      return sPad + sText;
    } else {
      return sText + sPad;
    }
  }
}

/**
 * Convenient object for our report data.
 */
class ReportRow implements Comparable
{
  String sState           = "";
  long purchases          = 0;          //Keep running total here.
  String sName            = "";
  String sMostRecentDate  = "99999999"; //Biggest value to start.

  //There can only be one row for each customer.
  public int compareTo(Object o)
  {
    ReportRow rr = (ReportRow) o;
    return sName.compareTo(rr.sName);
  }
}

/**
 * Used to sort our report rows.
 */
class CompareReportRows implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    int nResult;
    ReportRow t1 = (ReportRow) o1;
    ReportRow t2 = (ReportRow) o2;

    //First compare the most significant stuff.
    nResult = t1.sState.compareTo(t2.sState);
    if(nResult == 0)
    {
      //The most significant was the same, check the next level.
      if(t1.purchases < t2.purchases)
      {
        return -1;
      } else if (t1.purchases > t2.purchases) {
        return 1;
      } else {
        return t1.sName.compareTo(t2.sName);
      }  
    } else {
      //First level already decided sort order.
      return nResult;  
    }
  }
}

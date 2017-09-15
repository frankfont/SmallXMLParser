/*
* Used by the SmallXMLParser class.
* Created by Frank Font June 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a simple XML tag (also known as an "element") node.  
* It has several constructors, one of which does not parse the attribute list
* until an attribute is requested.  The "lazy" evaluation of attributes can give
* faster performance.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.SmallXMLParser
*/
public class TagNode extends Node
{

  //We do not want anyone to edit these directly.
  private String sRawAttribs;     //This is set if we used a lazy constructor.
  private String sNodeAttribs;    //String of node attributes.
  private ArrayList oNodeAttribs; //ArrayList of Attribute objects.

  /**
  * Creates a node with the specified name and attribute text.
  * @param sName Name of this node. e.g., For XML tag &lt;a b='x' c='y'&gt; the name is "a".
  * @param sAttribs The text of the attribute list.  For above example, that would be "b='x' c='y'". 
  * @throws MalformedXMLException when attribute syntax is bad.
  */
  public TagNode(String sName, String sAttribs) throws MalformedXMLException
  {
    super(sName);
    try{
      oNodeAttribs = parseAttributes(sAttribs);
    }
    catch(MalformedXMLException e){
      throw e;
    }
  }

  /**
  * Creates a node with the specified name and attribute text but does not parse the
  * attribute list until needed <i>if</i> it is used in "lazy" mode.
  * @param sName Name of this node. e.g., For XML tag &lt;a b='x' c='y'&gt; the name is "a".
  * @param sAttribs The text of the attribute list.  For above example, that would be "b='x' c='y'". 
  * @param bLazyAttribParse If true, then attribute list is not parsed until needed.
  * @throws MalformedXMLException if attribute syntax is bad <i>and</i> bLazyAttribParse=false.
  */
  public TagNode(String sName, String sAttribs, boolean bLazyAttribParse) throws MalformedXMLException
  {
    super(sName);
    if(bLazyAttribParse)
    {
      //Parse it later if it is needed.
      sRawAttribs = sAttribs;
    } else {
      //Not lazy.
      try{
        oNodeAttribs = parseAttributes(sAttribs);
      }
      catch(MalformedXMLException e){
        throw e;
      }
    }
  }

  /**
  * Returns attributes as ArrayList of Attribute objects.
  * @return The attributes as list of Attribute objects.
  * @see com.room4me.xml.Attribute
  */
  public ArrayList getAttributes()
  {
    if(oNodeAttribs == null)
    {
      //This will happen if lazy constructor was used.
      try{
        oNodeAttribs = parseAttributes(sRawAttribs);
      }
      catch(MalformedXMLException e){
        //They should not use lazy eval anyways if they suspect the input.
        throw new RuntimeException(e.getMessage());
      }
    }
    return oNodeAttribs;
  }

  /**
  * Get the attribute by name if it exists.
  * @param sName Name of attribute to find.
  * @return If found this is instance of matching attribute, else null.
  */
  public Attribute findAttribute(String sName)
  {
    ArrayList kAttribs;
    try{
      //Do it this way in case we have lazy evaluation.
      kAttribs = getAttributes();
    }
    catch(Exception e)
    {
      //Just don't return an attribute.
      return null;
    }
    for(Iterator i=kAttribs.iterator();i.hasNext();)
    {
      Attribute attrib = (Attribute) i.next();
      if(attrib.getName().equals(sName))
      {
        return attrib;
      }
    }
    return null;
  }

  /**
  * Returns attribute list as as text in a String object.
  * @return The attribute list as text.
  */
  public String getAttributesAsText()
  {
    if(sNodeAttribs == null)
    {
      //We don't do this unless we have to do it, then we don't do it again.
      StringBuffer sB = new StringBuffer("");
      Attribute oAttrib;
      ArrayList oAL = getAttributes();  //Important to call method because of lazy parsing!
      for(int i=0;i<oAL.size();i++)
      {
        oAttrib = (Attribute) oAL.get(i);
        sB.append(" " + oAttrib.getName() + "=" + '"' + oAttrib.getValue() + '"');
      }
      if(sB.length() > 0)
      {
        sNodeAttribs = sB.toString().substring(1);
      } else {
        sNodeAttribs = "";
      }
    }
    return sNodeAttribs;
  }

  /*
  * Returns attributes as ArrayList of Attribute objects.
  */
  private ArrayList parseAttributes(String sAttribs) throws MalformedXMLException
  {
    ArrayList oAL = new ArrayList();
    StringTokenizer oST;//Tokenize the raw attribute text.
    String sName;       //Attribute name.
    String sValue;      //Attribute value.
    String s;           //Temporary string variable.
    StringBuffer sB;    //Used to re-construct the value from the tokens.
    String sQ;          //Used to track the quote delimiter (' or ")
    Attribute oAttrib;  //Added to the arraylist.

    //Tokenize the raw attribute text.
    oST = new StringTokenizer(sAttribs,"='" + '"',true);

    //Loop through all the tokens.
    while(oST.hasMoreElements())
    {
      //This is an attribute name.
      sName = oST.nextToken().trim();
      s = oST.nextToken();  //We will do a syntax check with this.
      if(!s.equals("="))
      {
        throw new MalformedXMLException("Bad attribute content for node " + sName);
      }

      //Find the start of the value.
      s = oST.nextToken();  //We will do a syntax check with this.
      while(!s.equals("'") && !s.equals("" + '"'))
      {
        s = oST.nextToken();  
      }

      //We should now be at the value.
      sQ = s; // This is the quote character used.
      sB = new StringBuffer("");  
      while(oST.hasMoreTokens())
      {
        s = oST.nextToken();
        if(s.equals(sQ))
        {
          //Done with the value.
          break;
        }
        sB.append(s);  
      }
      sValue = sB.toString();

      oAttrib = new Attribute(sName,sValue);
      oAL.add(oAttrib);

    }

    return oAL;
  }
}


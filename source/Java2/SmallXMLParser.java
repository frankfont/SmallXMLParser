/*
* This source code can be used for any purpose by anyone but is provided
* without warrantee or guarantee of any kind.
*
* Send comments/questions/suggestions to mrfont@room4me.com.
*/

package com.room4me.xml;

import java.util.*;

/**
* <p>
* An XML document is parsed into an object model at construction time through the constructor of SmallXMLParser.
* Pass the XML document into the constructor as a String object.
* </p>
* <p>This is a small XML parser created with the following design goals:</p>
* <p>1. Keep the distribution files small.</p> 
* <p>2. Parse XML documents <b>quickly!</b></p> 
* <p>3. Parse ANY legitimate data-centric XML file (sans references).</p> 
* <p>4. Keep the feature list minimal.</p> 
* <p>5. Keep the features simple.</p>
* <p>6. Leverage standard java packages wherever possible.</p> 
* <p>
* SmallXMLParser is not targeted to compete with feature-rich parsers like
* Xerces on feature count.  However, it is meant to compete with other parsers
* on the basis of distribution size, execution speed, and with less licensing hassle.
* </p>
* <p>If you feel this falls short of the goals, offer a suggestion or a fix!</p>
* <p>
* This source code can be used by anyone but is provided
* without warrantee or guarantee of any kind.  Do not use this software to run
* nuclear power plants, rocketships, medical devices, or weapons of mass destruction.
* There is no charge for using this code or package but if your product uses portions of this code or the program itself,
* your product documentation should make appropriate attribution either
* to the "com.room4me.xml" package or to "SmallXMLParser by Frank Font".
* </p>
* @author Frank Font (mrfont@room4me.com)
* @version 1.01
*/
public class SmallXMLParser
{
  private ArrayList oProlog;            //Collection of prolog nodes.
  private Node oRootNode;               //Root node of the document.
  private String sXMLTextLineBreak="\n";//Affects the output.
  private String sXMLTextIndent=" ";    //Affects the output.

  /**
  * When producing XML Text, format at linebreak positions
  * with this String.
  * @param sLineBreak The String to use for as a linebreak.
  */
  public void setXMLTextLineBreak(String sLineBreak)
  {
    sXMLTextLineBreak=sLineBreak;    
  }

  /**
  * When producing XML Text, format at linebreak positions
  * with '\n' character.
  */
  public void setXMLTextLineBreak()
  {
    setXMLTextLineBreak("\n");
  }

  /**
  * When producing XML Text, format with this String for
  * each indent position.
  * @param sIndent The String to use for each indent space.
  */
  public void setXMLTextIndent(String sIndent)
  {
    sXMLTextIndent=sIndent;
  }

  /**
  * When producing XML Text, format with a blank space
  * for each indent position.
  */
  public void setXMLTextIndent()
  {
    setXMLTextIndent(" ");
  }

  /**
  * Get the list of prolog entries in the xml document as Node objects.
  * @return The ArrayList of prolog nodes.
  * @see com.room4me.xml.Node
  */
  public ArrayList getPrologNodes()
  {
    return oProlog;
  }

  /**
  * Get the root node of the parsed XML object model
  * through this method.
  * @return The root Node of the parsed XML document.
  * @see com.room4me.xml.Node
  */
  public Node getRootNode()
  {
    return oRootNode;
  }

  /**
  * You can replace the root node of the parsed XML document through this method.
  * It is up to you to make sure the settings of the Node object are correct for 
  * a root node.
  * @param oNode The replacement root node.
  * @see com.room4me.xml.Node
  */
  public void setRootNode(Node oNode)
  {
    oRootNode = oNode;
  }

  /**
  * Create the XML object model instance by sending an XML document
  * as a string into this constructor.
  * @param sXML The XML document as a text string.
  * @throws MalformedXMLException Use getMessage() method of this exception object for details.
  */
  public SmallXMLParser(String sXML) throws MalformedXMLException
  {
    Stack oStack = new Stack(); //We use this to parse the structure.
  
    Node oN1;             //Temporary node for processing.
    Node oNode = null;    //Current node we are processing.
    String sTagStuff;     //All the stuff between < and > symbols.
    String sNodeName;     //Name of a node.
    String sAttribs;      //Attributes of a node.
    String sLeafContent;  //Text content of a leaf node.
    int ps;               //Start position in text stream. '<'
    int pa;               //Start position of attribute section. ' '
    int pe;               //End position in text stream.   '>'
    int pl;               //Last end position in the text stream.
    int realpe;           //The "real" end position in some parse logic.
    boolean bUphill;      //True as long as we are collecting start tags.
    boolean bEmpty;       //True if a tag is empty, e.g., <a/>
    boolean bMore = (sXML.length() > 0);
    int nLevel = -1;      //First level is 0, next is 1, etc.
    char ch;              //Used for simple tests.

    oProlog = new ArrayList();  //Initialize it to an empty list.

    pe = -1;
    while(bMore)
    {

      //Find the start of the next tag.
      ps = sXML.indexOf('<',pe+1);
      if(ps < 0)
      {
        //No more tags to process.
        break;
      }
      pl = pe;  //Remember the last end position.

      //Check for CDATA section
      ch  = sXML.charAt(ps+1);  //Used further down.
      if(ps+9 < sXML.length() && sXML.substring(ps,ps+9).equals("<![CDATA["))
      {

        //Adjust everything past the CDATA section.
        pe = sXML.indexOf("]]>",ps+9);
        realpe = pe + 2;

        //Create a node instance for the CDATA section.
        try{
          oNode = new CDATANode(sXML.substring(ps+9,pe));
        }
        catch(MalformedXMLException e){
          throw e;
        }
        oNode.nParsePosition = ps;
        if(!oStack.empty())
        {
          oNode.oParent = (Node) oStack.peek();
          oNode.oParent.addChildNode(oNode);
        }

        ps = sXML.indexOf('<',realpe);
        pl = -1;  //So we do not store this as naked text.
        pe = realpe;

        continue;

      } else if(ch == '?') {

        //Adjust everything past the processing instruction section.
        pe = sXML.indexOf("?>",ps+2);
        realpe = pe + 1;

        //Create a node instance for the processing instruction.
        try{
          oNode = new ProcessingInstructionNode(sXML.substring(ps+2,pe));
        }
        catch(MalformedXMLException e){
          throw e;
        }
        oNode.nParsePosition = ps;
        if(!oStack.empty())
        {
          oNode.oParent = (Node) oStack.peek();
          oNode.oParent.addChildNode(oNode);
        } else {
          //This is part of the prolog.
          oProlog.add(oNode);
        }

        ps = sXML.indexOf('<',realpe);
        pl = -1;  //So we do not store this as naked text.
        pe = realpe;

        continue;

      } else if(ch == '!') {

        if(ps+9 < sXML.length() && sXML.substring(ps,ps+9).equals("<!DOCTYPE"))
        {
          //Adjust everything past the DOCTYPE section.
          int nPos = ps+9;
          while(nPos < sXML.length())
          {
            if(sXML.charAt(nPos++) != ' ')
            {
              break;
            }
          }
          while(nPos < sXML.length())
          {
            if(sXML.charAt(nPos++) == ' ')
            {
              break;
            }
          }
          while(nPos < sXML.length())
          {
            if(sXML.charAt(nPos) != ' ')
            {
              break;
            }
            nPos++;
          }
          if(sXML.charAt(nPos) == '[')
          {
            //Contains an embedded DTD.
            pe = sXML.indexOf("]>",nPos);
            realpe = pe + 1;
          } else {
            //Just has a URI.
            pe = sXML.indexOf(">",nPos);
            realpe = pe;
          }
  
          //Create a node instance for the DOCTYPE section.
          try{
            oNode = new DocumentTypeNode(sXML.substring(ps+9,pe).trim());
          }
          catch(MalformedXMLException e){
            throw e;
          }
          oNode.nParsePosition = ps;
          if(!oStack.empty())
          {
            oNode.oParent = (Node) oStack.peek();
            oNode.oParent.addChildNode(oNode);
          } else {
            //This is part of the prolog.
            oProlog.add(oNode);
          }
  
          ps = sXML.indexOf('<',realpe);
          pl = -1;  //So we do not store this as naked text.
          pe = realpe;
  
          continue;

        } else {

          //Adjust everything past the comment section.
          pe = sXML.indexOf("-->",ps+2);
          realpe = pe + 2;
  
          //Create a node instance for the processing instruction.
          try{
            oNode = new CommentNode(sXML.substring(ps+4,pe).trim());
          }
          catch(MalformedXMLException e){
            throw e;
          }
          oNode.nParsePosition = ps;
          if(!oStack.empty())
          {
            oNode.oParent = (Node) oStack.peek();
            oNode.oParent.addChildNode(oNode);
          } else {
            //This is part of the prolog.
            oProlog.add(oNode);
          }
  
          ps = sXML.indexOf('<',realpe);
          pl = -1;  //So we do not store this as naked text.
          pe = realpe;

          continue;
        }
      }

      //Find the end of the tag marker.
      pe = sXML.indexOf('>',ps+1);
      if(pe < 0)
      {
        throw new MalformedXMLException("Missing '>' char in [..." + sXML.substring(ps) + "]");
      }

      //Is this an empty node? e.g., <a/>
      bEmpty  = sXML.substring(ps+1,pe).endsWith("/");

      //Look for start of attribute list.
      pa = sXML.substring(ps+1,pe).indexOf(' ',0);
      if(pa > 0)
      {

        //We have attributes.
        if(bEmpty)
        {
          sAttribs = sXML.substring(ps+1+pa+1,pe-1).trim();
        } else {
          sAttribs = sXML.substring(ps+1+pa+1,pe).trim();
        }
        sNodeName = sXML.substring(ps+1,ps+1+pa);

      } else {

        //We have no attributes.
        if(bEmpty)
        {
          sNodeName = sXML.substring(ps+1,pe-1);  //Do not include slash.
        } else {
          sNodeName = sXML.substring(ps+1,pe);
        }
        sAttribs = "";

      }

      bUphill = (sXML.charAt(ps+1) != '/'); //This isn't an end-node?

      if(bEmpty)
      {

        //Empty node such as "<my_node/>".
        try{
          oNode = new TagNode(sNodeName,sAttribs);
        }
        catch(MalformedXMLException e){
          throw e;
        }
        oNode.nParsePosition = ps;
        if(!oStack.empty())
        {
          oNode.oParent = (Node) oStack.peek();
          if(oNode.oParent.oChild == null)
          {
            //This is the only child so far.
            oNode.oParent.oChild = oNode;
          } else {
            //Add this child but preserve the ordering.
            oN1 = oNode.oParent.oChild;
            while(oN1.oSibling != null)
            {
              oN1 = oN1.oSibling;
            }
            oN1.oSibling = oNode;
          }
        }
        oNode.nLevel = nLevel+1;

      } else if(bUphill) {

        if(pl>-1)
        {
          //Maybe we have naked text?
          String sNakedText = new String(sXML.substring(pl+1,ps).trim());
          if(sNakedText.length() > 0)
          {
            //Store the naked text too.
            try{
                oNode = new NakedTextNode(sNakedText);
            }
            catch(MalformedXMLException e){
              throw e;
            }
            oNode.nParsePosition = pl;
            if(!oStack.empty())
            {
              oNode.oParent = (Node) oStack.peek();
              oNode.oParent.addChildNode(oNode);
            }
          }
        }

        //Going uphill.
        nLevel++;
        try{
          oNode = new TagNode(sNodeName,sAttribs);
        }
        catch(MalformedXMLException e){
          throw e;
        }
        oNode.nParsePosition = ps;
        if(!oStack.empty())
        {
          oNode.oParent = (Node) oStack.peek();
          oNode.oParent.addChildNode(oNode);
        }
        oStack.push(oNode);

      } else {

        //Downhill.
        nLevel--;
        oNode = (Node) oStack.pop();
        if(!sNodeName.equals("/" + oNode.getName()))
        {
          //Report an error!
          String sPre;  //Prefix for xml sample.
          String sPost; //Postfix for xml sample.
          int nMin = oNode.nParsePosition-10;
          int nMax = pe+5;
          if(nMin < 0)
          {
            nMin = 0;
          }
          if(nMin == 0)
          {
            sPre = "";
          } else {
            sPre = "...";
          }
          if(nMax > sXML.length())
          {
            nMax = sXML.length();
          }
          if(nMax == sXML.length())
          {
            sPost = "";
          } else {
            sPost = "...";
          }

          throw new MalformedXMLException(
                  "Found '<" + sNodeName + ">' but expected '</"
                + oNode.getName() + ">' node around position "
                + oNode.nParsePosition 
                + " (" + sPre + sXML.substring(nMin,nMax) + sPost + ")");
        }
  
        sLeafContent = sXML.substring(pl+1,ps);
        oNode.setText(sLeafContent);

      }
    }

    //All done.
    oRootNode = oNode;
  }

  /**
  * Returns the entire parsed XML document starting with the Root node 
  * as formatted text in a String object.
  */
  public String getXMLAsText()
  {
    StringBuffer sProlog = new StringBuffer("");

    for(Iterator i=oProlog.iterator();i.hasNext();)
    {
      sProlog.append(getNodeXML(((Node) i.next()),""));
    }

    return sProlog.toString() + walkTree(oRootNode);
  }

  /**
  * Recursively walk the document object.
  */
  private String walkTree(Node oNode)
  {

    StringBuffer sOut = new StringBuffer("");

    //Create indent for the level.
    StringBuffer sPad = new StringBuffer(oNode.nLevel);
    for(int i=0;i<oNode.nLevel;i++)
    {
      sPad.append(sXMLTextIndent);
    }

    //Output all the children.
    if(oNode.oChild == null)
    {

      //This node has no children.
      sOut.append(getNodeXML(oNode,sPad.toString()));

    } else {

      //We have children here. (This also implies we have a TagNode!)
      String sAttribs = getPaddedAttribs(oNode);
      sOut.append(sXMLTextLineBreak + sPad + "<" + oNode.getName() + sAttribs + ">");
      ArrayList oChildren = oNode.getChildNodes();
      for(int i=0;i<oChildren.size();i++)
      {
        sOut.append(walkTree((Node) oChildren.get(i)));
      }
      sOut.append(sXMLTextLineBreak + sPad + "</" + oNode.getName() + ">");
    }

    return sOut.toString();
  }

  /**
  * Formats the node information for printing.
  */
  private String getNodeXML(Node oNode, String sPad)
  {
    StringBuffer sOut = new StringBuffer("");

    if(oNode.getText().length() == 0)
    {
      //Figure out what kind of node we have, then print it appropriately.
      if(oNode instanceof TagNode)
      {
        String sAttribs = getPaddedAttribs(oNode);
        sOut.append(sXMLTextLineBreak + sPad + "<" + oNode.getName() + sAttribs + "/>");
      } else {
        //Mystery node.
        sOut.append(sXMLTextLineBreak + sPad + "<" + oNode.getName() + "/>");
      }

    } else {

      //Figure out what kind of node we have, then print it appropriately.
      if (oNode instanceof NakedTextNode)
      {
        //Just spit out naked text without the wrapper.
        sOut.append(sXMLTextLineBreak + sPad + oNode.getText());
      } else if (oNode instanceof ProcessingInstructionNode) {
        //Wrap the Processing Instruction content.
        sOut.append(sXMLTextLineBreak + sPad 
                     + ((ProcessingInstructionNode) oNode).getDecoratedText());
      } else if (oNode instanceof CDATANode) {
        //Wrap the CDATA content.
        sOut.append(sXMLTextLineBreak + sPad + ((CDATANode) oNode).getDecoratedText());
      } else if (oNode instanceof CommentNode) {
        //Wrap the Comment content.
        sOut.append(sXMLTextLineBreak + sPad + ((CommentNode) oNode).getDecoratedText());
      } else if (oNode instanceof DocumentTypeNode) {
        //Wrap the Document Type content.
        sOut.append(sXMLTextLineBreak + sPad 
                     + ((DocumentTypeNode) oNode).getDecoratedText(sXMLTextLineBreak));
      } else if (oNode instanceof TagNode) {
        //Spit out this node and the text it contains.
        String sAttribs = getPaddedAttribs(oNode);
        sOut.append(sXMLTextLineBreak + sPad + "<" + oNode.getName() + sAttribs + ">");
        sOut.append(oNode.getText());
        sOut.append("</" + oNode.getName() + ">");
      } else {
        //Spit out this mystery node and the text it contains.
        sOut.append(sXMLTextLineBreak + sPad + "<" + oNode.getName() + ">");
        sOut.append(oNode.getText());
        sOut.append("</" + oNode.getName() + ">");
      }
    }

    return sOut.toString();
  }

  /**
  * Pad with a starting space if the node has attributes.
  * This is handy for constructing printable output.
  */
  private String getPaddedAttribs(Node oNode)
  {
    String sAttrib = ((TagNode) oNode).getAttributesAsText();
    if(sAttrib.length()!=0)
    {
      return " " + sAttrib;  
    } else {
      return "";
    }
  }

}


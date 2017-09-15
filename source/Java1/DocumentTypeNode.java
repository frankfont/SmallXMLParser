/*
* Used by the SmallXMLParser class.
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a document type declaration in an XML document.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.Node
* @see com.room4me.xml.SmallXMLParser
*/
public class DocumentTypeNode extends Node
{

  private String sTerminator;

  /**
  * Creates an instance of a ProcessingInstructionNode.  Pass in the
  * raw text of the processing instruction.
  * @param sRawText Pass in the raw text between the &lt;? and &gt; markers.
  */
  public DocumentTypeNode(String sRawText) throws MalformedXMLException
  {
    super("DocumentType");
    setText(sRawText);
    if(sRawText.indexOf(" [") > 0)
    {
      sTerminator = "]>";
    } else {
      sTerminator = ">";
    }
  }

  /**
  * Returns the doctype text within the &lt;!DOCTYPE and &gt; markers without line breaks.
  * @return the doctype text within the &lt;!DOCTYPE and &gt; markers.
  */
  public String getDecoratedText()
  {
    return "<!DOCTYPE " + getText() + sTerminator;
  }

  /**
  * Returns the doctype text within the &lt;!DOCTYPE and &gt; markers using line breaks.
  * @param sLineBreak Override the "\n" settings through this parameter.
  * @return the doctype text within the &lt;!DOCTYPE and &gt; markers.
  */
  public String getDecoratedText(String sLineBreak)
  {
    if(sLineBreak.equals(""))
    {
      sLineBreak = "\n";
    }
    return "<!DOCTYPE " + sLineBreak + getText() + sLineBreak + sTerminator;
  }
}


/*
* Used by the SmallXMLParser class.
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a processing instruction in an XML document.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.Node
* @see com.room4me.xml.SmallXMLParser
*/
public class ProcessingInstructionNode extends Node
{
  /**
  * Creates an instance of a ProcessingInstructionNode.  Pass in the
  * raw text of the processing instruction.
  * @param sRawText Pass in the raw text between the &lt;? and &gt; markers.
  */
  public ProcessingInstructionNode(String sRawText) throws MalformedXMLException
  {
    super("ProcessingInstruction");
    setText(sRawText.trim());
  }

  /**
  * Returns the processing instruction text within the &lt;? and ?&gt; markers.
  * @return the processing instruction text within the &lt;? and ?&gt; markers.
  */
  public String getDecoratedText()
  {
    return "<?" + getText() + "?>";
  }
}


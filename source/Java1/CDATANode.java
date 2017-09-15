/*
* Used by the SmallXMLParser class.
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a CDATA section.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.Node
* @see com.room4me.xml.SmallXMLParser
*/
public class CDATANode extends Node
{
  /**
  * Creates an instance of a CDATANode object containing the
  * text specified.
  * @param sCDATAText The naked text.
  */
  public CDATANode(String sCDATAText) throws MalformedXMLException
  {
    super("CDATA");
    setText(sCDATAText);
  }

  /**
  * Returns the text with wrapping decorations.  For example, if
  * the getText() method returns <i>apple</i> then this method will
  * return <i>&lt;![CDATA[apple]]&gt;</i>.
  * @returns The text wrapped in &lt;![CDATA[...]]&gt; decorations.
  */
  public String getDecoratedText()
  {
    return "<![CDATA[" + getText() + "]]>";
  }
}


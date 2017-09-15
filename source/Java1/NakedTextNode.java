/*
* Used by the SmallXMLParser class.
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents text that is not exclusively contained by a single tag.
* Such text is referred to as "naked text" in this package.
* This object will occur in narrative-centric XML documents.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.Node
* @see com.room4me.xml.SmallXMLParser
*/
public class NakedTextNode extends Node
{
  /**
  * Creates an instance of a NakedTextNode object containing the
  * text specified.
  * @param sNakedText The naked text.
  */
  public NakedTextNode(String sNakedText) throws MalformedXMLException
  {
    super("NakedText");
    setText(sNakedText);
  }
}


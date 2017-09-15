/*
* Used by the SmallXMLParser class.
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents an XML comment entry.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.Node
* @see com.room4me.xml.SmallXMLParser
*/
public class CommentNode extends Node
{
  /**
  * Creates an instance of a CommentNode object containing the
  * text specified.
  * @param sCommentText The naked text.
  */
  public CommentNode(String sCommentText) throws MalformedXMLException
  {
    super("Comment");
    setText(sCommentText);
  }

  /**
  * Returns the text with wrapping decorations.  For example, if
  * the getText() method returns <i>starting</i> then this method will
  * return <i>&lt;!--[starting--&gt;</i>.
  * @returns The text wrapped in &lt;!--...--&gt; decorations.
  */
  public String getDecoratedText()
  {
    return "<!-- " + getText() + " -->";
  }
}


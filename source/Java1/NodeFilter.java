/*
* Used by the SmallXMLParser class.
* Created by Frank Font June 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

/**
* This interface is an enumeration for each of the Node types.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.SmallXMLParser
*/
public interface NodeFilter
{
  public static final long nTagNode = 1;
  public static final long nProcessingInstructionNode = 2;
  public static final long nNakedTextNode = 4;
  public static final long nDocumentTypeNode = 8;
  public static final long nCommentNode = 16;
  public static final long nCDATANode = 32;
}


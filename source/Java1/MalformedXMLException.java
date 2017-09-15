/*
* Use this program and the source code for
* whatever you like as long as you include
* proper attribution.
*
* Created by Frank Font July 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

/**
* This exception is thrown when a problem is detected in the XML document.
*@see com.room4me.xml.SmallXMLParser
*@author Frank Font (mrfont@room4me.com)
*/
public class MalformedXMLException extends Exception
{

  MalformedXMLException()
  {}

  MalformedXMLException(String sMsg)
  {
    super(sMsg);
  }

}

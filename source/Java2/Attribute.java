/*
* Created by Frank Font June 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a simple XML node attribute and allows the value to be changed.
* @see com.room4me.xml.Node
*/
public class Attribute implements Comparable
{
  //Leave these friendly for the package.
  String sAttribName;
  String sAttribValue;

  /**
  * Compares names of Attribute instances.
  * @param o An Attribute instance.
  * @return negative (<), zero (=), positive (>)
  */
  public int compareTo(Object o)
  {
    Attribute oAttribute = (Attribute) o;
    return sAttribName.compareTo(oAttribute.sAttribName);
  }

  /**
  * Creates an Attribute object with a blank value.
  * @param sName Name of this attribute.
  */
  public Attribute(String sName)
  {
    sAttribName = sName;
  }

  /**
  * Creates an Attribute object with a custom value.
  * @param sName Name of this attribute.
  * @param sValue Value to assign this attribute.
  */
  public Attribute(String sName,String sValue)
  {
    sAttribName = sName;
    sAttribValue = sValue;
  }

  /**
  * Returns the attribute name.
  * @return The attribute name as a String.
  */
  public String getName()
  {
    return sAttribName;
  }

  /**
  * Returns the attribute value.
  * @return The attribute value as a String.
  */
  public String getValue()
  {
    return sAttribValue;
  }

  /**
  * Changes the attribute value.
  */
  public void setValue(String sValue)
  {
    sAttribValue = sValue;
  }
}


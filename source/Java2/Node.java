/*
* Used by the SmallXMLParser class.
* Created by Frank Font June 2001
* mrfont@room4me.com
*/

package com.room4me.xml;

import java.util.*;

/**
* This class represents a simple XML node.  A Node can have child nodes, have a parent, 
* have siblings, and contain text data.  This is the super class of all other nodes.
* @author Frank Font (mrfont@room4me.com)
* @see com.room4me.xml.SmallXMLParser
*/
public class Node implements Comparable
{

  //These things are friendly so only package members can use.
  int nParsePosition; //Here for programming convenience within the package.
  int nLevel;         //Here for programming convenience, use getLevel() outside this package.)
  Node oParent;       //Reference to parent node.
  Node oSibling;      //Reference to next sibling node.
  Node oChild;        //Reference to first child node.

  //We do not want anyone to edit these directly.
  private String sNodeName;       //Name of the node. 
  private String sText = "";      //Text content of the node.

  /**
  * Compares nodes by name.
  * @param o Instance of a Node.
  * @return negative (<), zero (=) , or positive (>)
  */
  public int compareTo(Object o)
  {
    Node oNode = (Node) o;
    return sNodeName.compareTo(oNode.sNodeName);
  }

  /**
  * Returns the parent node.
  * @return Node Returns the parent node.  If no parent, then null.
  */
  public Node getParent()
  {
    return oParent;
  }

  /**
  * Returns the node's text content.
  * @return The text content of the node.
  */
  public String getText()
  {
    return sText;
  }

  /**
  * Sets the node's text content.
  * @param sSetText The text you want contained by this node.
  */
  public void setText(String sSetText)
  {
    sText = sSetText;
  }

  /**
  * Creates a node with the specified name.
  * @param sName Name of this node. e.g., For XML tag &lt;a b='x' c='y'&gt; the name is "a".
  * @throws MalformedXMLException when attribute syntax is bad.
  */
  public Node(String sName) throws MalformedXMLException
  {
    sNodeName = sName;
  }

  /**
  * Return the name of the node.
  * @return Returns the name of the node as a String object.
  */
  public String getName()
  {
    return sNodeName;
  }

  /**
  * Returns all the child nodes as an ArrayList object.  Order of XML document is preserved.
  * @return Returns the ordered collection of all children for this node.  Each child is a Node object.
  */
  public ArrayList getChildNodes()
  {
    return getChildNodes(0);
  }

  /**
  * Returns all the child nodes as an ArrayList object.  Order of XML document is preserved.
  * @param nFilter Summation of NodeFilter members to restrict the returned content.  For no filtering, pass value of 0.  (For example, to only get TagNodes children, pass in NodeFilter.TagNode as parameter.)
  * @return Returns the ordered collection of all children matching the filter criteria for this node.  Each child is a Node object.
  * @see com.room4me.xml.NodeFilter
  */
  public ArrayList getChildNodes(long nFilter)
  {
    ArrayList oList = new ArrayList();
    Node oNode = this.oChild;
    if(oNode != null)
    {
      if(passFilter(oNode, nFilter))
      {
        //We will output this one.
        oList.add(oNode);
      }
      while(oNode.oSibling != null)
      {
        if(passFilter(oNode, nFilter))
        {
          //We will output this one.
          oList.add(oNode.oSibling);
        }
        oNode = oNode.oSibling;
      }
    }
    oList.trimToSize();
    return oList;
  }

  /**
  * Returns true if node is included in filter, else false.
  * @param oNode The node we want to compare to the filter.
  * @param nFilter Summation of NodeFilter members.
  * @see com.room4me.xml.NodeFilter
  */
  private boolean passFilter(Node oNode, long nFilter)
  {
    if(nFilter == 0)
    {
      //No filter thus everything passes.
      return true;
    } else  {
      //Lets figure out what kind of node we have.
      if (oNode instanceof NakedTextNode)
      {
        return 1 == (NodeFilter.nNakedTextNode & nFilter);
      } else if (oNode instanceof ProcessingInstructionNode) {
        return 1 == (NodeFilter.nProcessingInstructionNode & nFilter);
      } else if (oNode instanceof CDATANode) {
        return 1 == (NodeFilter.nCDATANode & nFilter);
      } else if (oNode instanceof CommentNode) {
        return 1 == (NodeFilter.nCommentNode & nFilter);
      } else if (oNode instanceof DocumentTypeNode) {
        return 1 == (NodeFilter.nDocumentTypeNode & nFilter);
      } else if (oNode instanceof TagNode) {
        return 1 == (NodeFilter.nTagNode & nFilter);
      } else {
        //We do not know what this is so chuck it.
        return false;
      }
    }
  }

  /**
  * Appropriately adds a child node to the collection and sets
  * the parent of the node.
  * @param oNode Adds this node to the collection of children.
  */
  public void addChildNode(Node oNode)
  {
    oNode.oParent = this;
    if(this.oChild == null)
    {
    
      //This is the only child so far.
      this.oChild = oNode;
    
    } else {
    
      //Add this child but preserve the ordering.
      Node oN1 = this.oChild;
      while(oN1.oSibling != null)
      {
        oN1 = oN1.oSibling;
      }
      oN1.oSibling = oNode;
    }
    oNode.nLevel = this.nLevel + 1;
  }
  
  /**
  * Returns the nesting level of this node.
  * @return The nesting level of this node in the document object model.  Root level is value 0, next is 1 and so on.
  */
  public int getLevel()
  {
    //Return the value the safe way --- calculate it now.
    int n=0;
    Node oNode = this;
    while(oNode.oParent != null)
    {
      n++;
      oNode = oNode.oParent;      
    }
      
    return n;
  }

}


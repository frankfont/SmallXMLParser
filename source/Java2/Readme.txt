package: com.room4me.xml
author:  Frank Font (mrfont@room4me.com)
date:    July 2001

WHAT IS THIS?
-------------
This package contains the SmallXMLParser class and its helper
classes that parse well-formed XML 1.0 documents FAST and without
a bunch of code-bloat.  And you can have the source code.

GIVE ME THE GIST OF THE APPROACH.
---------------------------------
This parser creates an object model of an XML file that can be
navigated using simple methods of the java.util.ArrayList class.
Children of nodes are returned as ArrayList objects through a
"getChildNodes" method.  Each type of node is a sub-class of Node.

IS IT SIMPLE?
-------------
That is part of the idea.  Some small sample programs are available
at the www.room4me.com website along with the source code for the
parser itself.

HOW MUCH DOES THIS COST?
------------------------
Version 1.0 of this package costs nothing.  Neither does the
source code.  Modify it as you see fit or use it as-is.

ANYTHING ELSE?
--------------
This source code can be used by anyone but is provided
without warrantee or guarantee of any kind.  Do not use it
to run nuclear power plants, rocket ships, medical devices,
or weapons of mass destruction.  Don't risk your life or
anyone else's life on this program -- there has not been
enough QA Testing for that.

There is no charge for using this code or package but your
product documentation should make appropriate attribution
either to the "com.room4me.xml" package or
to "SmallXMLParser by Frank Font".

If you have any questions, comments, or suggestions
contact me at "mrfont@room4me.com".


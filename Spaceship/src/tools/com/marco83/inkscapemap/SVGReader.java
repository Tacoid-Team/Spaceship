/*
 * SVGReader.java
 * This file is released under LGPL http://www.gnu.org/licenses/lgpl-3.0.txt
 * Author: Marco Conti http://www.marco83.com
 * Created on 18 ottobre 2007, 10.22
 *
 */

package tools.com.marco83.inkscapemap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SVGReader  extends DefaultHandler{
    
    private List<SVGShape> shapes;
    private float width;
    private float height;
    private SVGVersion version;
    private String parseLog; // debug output
    
    /**
     * Known SVG versions
     */
    private enum SVGVersion
    {
        V_UNKNOWN,
        V_1_0,
        V_1_1,
    }
    
    /**
     * Logs a string
     * @param s 
     */
    protected void log(String s)
    {
        if(!s.endsWith("\n"))
            s += "\n";
        parseLog += s;
    }
    
    /**
     * Logs a string and dies
     * @param log
     * @throws SAXParseException 
     */
    protected void die(String log) throws SAXParseException
    {
        log(log);
        throw new SAXParseException(log, null);
    }
    
    /** Creates a new instance of SVGReader */
    public SVGReader()  {
	shapes = new ArrayList<SVGShape>();
        width = 0;
        height = 0;
        version = SVGVersion.V_UNKNOWN;
        parseLog = "";
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }
    
    /**
     * Parse a SVG file
     * @param f the input file
     * @return the list of shapes found
     * @throws Exception 
     */
    public List<SVGShape> readSVGFromFile(File f) throws Exception {
	DefaultHandler handler = this;
	SAXParserFactory factory = SAXParserFactory.newInstance();
        parseLog = "";
        log("Parsing file " + f.getName());
        
        // Parse the input
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse( f, handler );
	return shapes;
    }
    
    @Override
    public void startDocument() {
        log("Document start");
    }
    
    @Override
    public void endDocument() {
        log("Document end");
    }
    
    @Override
    public void startElement(String namespaceURI, 
                             String sName, // simple name (localName)
                             String qName, // qualified name
                             Attributes attrs)  throws SAXParseException {
        String eName = sName; // element name
        if ("".equals(eName)) 
            eName = qName; // namespaceAware = false
        if(eName.equals("svg"))
        {
            parseSvg(attrs);
            if(version == SVGVersion.V_UNKNOWN)
            {
                log("SVG version not specified, assuming 1.0");
                version = SVGVersion.V_1_0;
            }
            log("SVG version " + version.name());
        }
        if(eName.equals("path")) {
	    parsePath(attrs);
	}
    }
    
    /**
     * Parses svg attributes
     * @param attrs
     * @throws SAXParseException 
     */
    private void parseSvg(Attributes attrs) throws SAXParseException
    {
        if (attrs == null)
	    throw new SAXParseException("No attributes in SVG node", null);
        // attributes
	for (int i = 0; i < attrs.getLength(); i++) {
            String aName = attrs.getLocalName(i); // Attr name
            if ("".equals(aName))
                aName = attrs.getQName(i);

            if(aName.equals("width"))
                width = new Float(attrs.getValue(i));

            if(aName.equals("height"))
                height = new Float(attrs.getValue(i));

            if(aName.equals("inkscape:version"))
            {
                log("Inkscape version "+attrs.getValue(i));
            }
            if(aName.equals("version"))
            {
                if(attrs.getValue(i).equals("1.0"))
                {
                    version = SVGVersion.V_1_0;
                }
                else if(attrs.getValue(i).equals("1.1"))
                {
                    version = SVGVersion.V_1_1;
                }
                else
                {
                    log("SVG version not recognised, defaulting to 1.0");
                    version = SVGVersion.V_1_0;
                }
            }
        }
    }

    @Override
    public void endElement(String namespaceURI,
                           String sName, // simple name
                           String qName  // qualified name
                          ) {
    }

    @Override
    public void characters(char buf[], int offset, int len)
    {
    }
    
    /**
     * Parses a path into a shape
     * @param attrs path attributes
     * @throws SAXParseException 
     */
    private void parsePath(Attributes attrs) throws SAXParseException {
	log("Parsing path");
        if (attrs == null)
	    return;
	SVGShape s = new SVGShape();
	s.setShapeType(SVGShape.SHAPE_PATH);
	
	// attributes
	for (int i = 0; i < attrs.getLength(); i++) {
	    String aName = attrs.getLocalName(i); // Attr name
            if ("".equals(aName)) aName = attrs.getQName(i);
	    
	    // label
	    if(aName.equals("inkscape:label"))
            {
		s.setLabel(attrs.getValue(i));
                log("Label is "+attrs.getValue(i));
            }
	    // ID
	    if(aName.equals("id"))
            {
		s.setID(attrs.getValue(i));
                log("ID is "+attrs.getValue(i));
            }
	    // points list
	    if(aName.equals("d")) {
                SVGPathParser pparser = new SVGPathParser(this);
		pparser.parsePointList(s, attrs.getValue(i));
	    }
		
	}
	shapes.add(s);
    }
    
    /**
     * Gets the last parse log
     * @return 
     */
    public String GetLastLog()
    {
        return parseLog;
    }
}

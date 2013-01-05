/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.com.marco83.inkscapemap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Marco
 */
public class SVGPathParser {
    
    SVGReader parent;
    
    // PARSING INTERNALS
    Command lastComm = Command.NONE; // last command
    boolean isLastRelative = false; // whether last command was relative
    boolean onPath = false; // are we on a path (after M, before Z?)
    boolean isAbsoluteFound = false; // did I find an absolute yet?
    SVGShape sub = null;
    PointF lastPoint = new PointF(0,0);
    int tokenPointer = 0; // next token to parse
    List<String> tokenArray;
    float prevVertical;
    
    SVGPathParser(SVGReader father)
    {
        parent = father;
    }
    
    /**
     * Splits a SVG path string into tokens ready to be parsed
     */
    private void SplitStringIntoTokens(String str) {
        String[] arr = str.split("[\\s,]");
        tokenArray = new ArrayList<String>();
        for(String s : arr)
        {
            if(s.matches("[lLzZmM]"))
            {
                tokenArray.add(s.substring(0,1));
                if(s.length() > 1) // other stuff after a command, should be a number
                {
                    s = s.substring(1); // cut and treat as a number
                }
                else // move to next token
                {
                    continue;
                }
            }
            
            // here, it should be a number
            String[] numbers = s.split(",");
            tokenArray.addAll(Arrays.asList(numbers));
        }
    }
	
    /**
     * Last command found
     */
    private enum Command
    {
        NONE,
        L,
        M,
        Z,
        H,
        V
    }
    
    /**
     * Direction for next point, when only one coordinate is specified (H or V)
     */
    public enum Direction
    {
        HORIZONTAL,
        VERTICAL
    }
    
    /**
     * Gets the coordinate of the point from next token
     * @throws SAXParseException 
     */
    private void PointFromNextToken(Direction d) throws SAXParseException
    {
        if(tokenPointer+1 >= tokenArray.size())
            parent.die("Expecting coordinate, not found (size "+tokenArray.size()+" and expecting >"+(tokenPointer+2));
        
        if(isLastRelative && !isAbsoluteFound)
            parent.die("Found a relative command but no absolute value earlier");

        isAbsoluteFound = isAbsoluteFound || !isLastRelative; 
        
        // only one coord
        Float f = new Float(tokenArray.get(++tokenPointer));
        PointF p;
        // I'm gonna have one point 0, then later set it to the right value
        if(d == Direction.HORIZONTAL)
            p = new PointF(f, 0);
        else
            p = new PointF(0, f);
        
        // if relative, add last point, but then we have to reset the one set to 0
        if(isLastRelative)
            p.add(lastPoint);
        
        if(d == Direction.HORIZONTAL)
            p = new PointF(p.getX(), lastPoint.getY());
        else
            p = new PointF(lastPoint.getX(), p.getY());
        
        
        lastPoint = p;
        sub.addPoint(p);
    }
    
    /**
     * Gets the coordinate of the point from next two tokens
     * @throws SAXParseException 
     */
    private void PointFromNextTwoTokens() throws SAXParseException
    {
        if(tokenPointer+2 >= tokenArray.size())
            parent.die("Expecting 2 coordinates, not found (size "+tokenArray.size()+" and expecting >"+(tokenPointer+2));
        
        if(isLastRelative && !isAbsoluteFound)
            parent.die("Found a relative command but no absolute value earlier");

        isAbsoluteFound = isAbsoluteFound || !isLastRelative; 
        
        PointF p = new PointF(
                new Float(tokenArray.get(++tokenPointer)),
                new Float(tokenArray.get(++tokenPointer)));
        if(isLastRelative)
        {
            p.add(lastPoint);
        }
        lastPoint = p;
        
        sub.addPoint(p);
    }

            
    /**
     * Splits a point list into single points and add them to a shape
     */
    protected void parsePointList(SVGShape s, String val) throws SAXParseException {
	
        SplitStringIntoTokens(val);
	// parse point list
	for(tokenPointer = 0; tokenPointer < tokenArray.size(); ++tokenPointer) 
        {
	    String token = tokenArray.get(tokenPointer);
	    
	    // move to
	    if(token.equalsIgnoreCase("m")) 
                foundM(token, s);
            
            // line to
            else if(token.equalsIgnoreCase("l"))
                foundL(token, s);
            
	    // closepath
	    else if(token.equalsIgnoreCase("z"))
                foundZ(token, s);
            
            // horizontal line
            else if(token.equalsIgnoreCase("h"))
                foundH(token, s);
            
            // vertical line
            else if(token.equalsIgnoreCase("v"))
                foundV(token, s);

            // no curve or arc implementation yet!
            else if(token.matches("[cCsSqQtTaA]$"))
            {
                parent.log("Found "+token);
                parent.die(token + " not implemented yet");
            }
            
            // what is this?
            else {
                // is it a number?
             
                try
                {
                    // if it works, it's a number!
                    Float.parseFloat(token);
                    
                    if(lastComm != Command.L && lastComm != Command.M && lastComm != Command.H && lastComm != Command.V)
                        parent.die("Found coordinates with no previous command");
                    
                    parent.log("Found a number, repeating command, relative is "+isLastRelative);
                    
                    // roll back and parse
                    --tokenPointer;
                    if(lastComm == Command.H)
                        PointFromNextToken(Direction.HORIZONTAL);
                    else if(lastComm == Command.V)
                        PointFromNextToken(Direction.VERTICAL);
                    else
                        PointFromNextTwoTokens();
                    
                }
                catch(NumberFormatException e)
                {
                    // not a number, what's this?
                    parent.die("Unrecognized token: "+token);
                }
            }   
	}
    }
    
    /**
     * Found an M while parsing
     */
    protected void foundM(String token, SVGShape s) throws SAXParseException
    {
        parent.log("Found move to: "+token);
        onPath = true;
        sub = new SVGShape();
        sub.setID(s.getID());
        sub.setLabel(s.getLabel());
        sub.setHREF(s.getHREF());
        sub.setText(s.getText());
        sub.setShapeType(s.getShapeType());
        lastComm = Command.M;
        isLastRelative = false;
        PointFromNextTwoTokens();
        isLastRelative = token.equals("m");            

    }
    
    /**
     * Found an L while parsing
     */
    protected void foundL(String token, SVGShape s) throws SAXParseException
    {
        parent.log("Found line to: "+token);
        lastComm = Command.L;
        isLastRelative = token.equals("l");
        PointFromNextTwoTokens();
    }
    
    /**
     * Found an V while parsing
     */
    protected void foundV(String token, SVGShape s) throws SAXParseException
    {
        parent.log("Found vertical line to: "+token);
        lastComm = Command.V;
        isLastRelative = token.equals("v");
        PointFromNextToken(Direction.VERTICAL);
    }
    
    /**
     * Found an H while parsing
     */
    protected void foundH(String token, SVGShape s) throws SAXParseException
    {
        parent.log("Found horizontal line to: "+token);
        lastComm = Command.H;
        isLastRelative = token.equals("h");
        PointFromNextToken(Direction.HORIZONTAL);
    }
    
    /**
     * Found an Z while parsing
     */
    protected void foundZ(String token, SVGShape s) throws SAXParseException
    {
        parent.log("Found close: "+token);
        if(!onPath || sub == null)
            parent.die("z not after a M");
        lastComm = Command.Z;

        List<PointF> points = sub.getPoints();
        if(!points.isEmpty())
        {
            // should I close manually?
            if(!points.get(0).equals(points.get(points.size()-1)))
            {
                points.add(points.get(0));
            }

            s.addShape(sub);
        }
        else
        {
            parent.log("Shape had no points, ignoring it");
        }
        sub = null;
    }
}

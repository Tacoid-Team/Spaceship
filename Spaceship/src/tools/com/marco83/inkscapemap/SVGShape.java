/*
 * Shape.java
 * This file is released under LGPL http://www.gnu.org/licenses/lgpl-3.0.txt
 * Author: Marco Conti http://www.marco83.com
 * Created on 18 ottobre 2007, 10.24
 *
 */

package tools.com.marco83.inkscapemap;

import java.util.ArrayList;
import java.util.List;
public class SVGShape {
    
    public static final int SHAPE_PATH = 1;
    public static final int SHAPE_RECT = 2;
    
    private int shapetype;
    private List<PointF> points;
    private List<SVGShape> subshapes;
    private String label;
    private String ID;
    private boolean output = true;
    private String HREF;
    private String text;
    
    /** Creates a new instance of Shape */
    public SVGShape() {
	points = new ArrayList<PointF>();
	subshapes = new ArrayList<SVGShape>();
    }
    
    public SVGShape(int shapetype, String label, String ID) {
	this.shapetype = shapetype;
	this.label = label;
	this.ID = ID;
	points = new ArrayList<PointF>();
	subshapes = new ArrayList<SVGShape>();
    }

    public int getShapeType() {
	    return shapetype;
    }

    public void setShapeType(int shape) {
	    this.shapetype = shape;
    }

    public List<PointF> getPoints() {
	    return points;
    }
    
    public List<SVGShape> getSubshapes() {
	    return subshapes;
    }

    public String getLabel() {
	    return label;
    }

    public void setLabel(String label) {
	    this.label = label;
    }

    public String getID() {
	    return ID;
    }

    public void setID(String ID) {
	    this.ID = ID;
    }
    
    public void addPoint(PointF p) {
	    this.points.add(p);
    }
    
    public void clearPoints() {
	this.points.clear();
    }
    
    public void addShape(SVGShape s) {
	this.subshapes.add(s);
    }
    
    public void clearSubshapes() {
	this.subshapes.clear();
    }

    public boolean isOutput() {
	    return output;
    }

    public void setOutput(boolean output) {
	    this.output = output;
    }

    public String getHREF() {
	    return HREF;
    }

    public void setHREF(String HREF) {
	    this.HREF = HREF;
    }

    public String getText() {
	    return text;
    }

    public void setText(String text) {
	    this.text = text;
    }
}

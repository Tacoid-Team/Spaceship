/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tools.com.marco83.inkscapemap;

/**
 *
 * @author guest
 */
public class PointF {
    private float x;
    private float y;

    public static final Float DELTA = 0.0001f;
    
    public PointF(float p1, float p2)
    {
        x = p1;
        y = p2;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    void add(PointF point) {
        x += point.x;
        y += point.y;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof PointF)
        {
            PointF other = (PointF) o;
            return Math.abs(other.x - x) < x*DELTA 
                    && Math.abs(other.y - y) < y*DELTA;
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Float.floatToIntBits(this.x);
        hash = 59 * hash + Float.floatToIntBits(this.y);
        return hash;
    }
}

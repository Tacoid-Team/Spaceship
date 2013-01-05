package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import tools.com.marco83.inkscapemap.PointF;
import tools.com.marco83.inkscapemap.SVGReader;
import tools.com.marco83.inkscapemap.SVGShape;

public class Convert {

	public static void save(File f, List<SVGShape> shapelist) throws Exception {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
		for (SVGShape shape : shapelist) {
			if(!shape.isOutput())
				continue;

			for (SVGShape shape2 : shape.getSubshapes()) {
				for(int k = 0; k < shape2.getPoints().size(); k++) { // points
					PointF p = shape2.getPoints().get(k);
					out.println(new Float(p.getX()).intValue()+","+new Float(1024-p.getY()).intValue());
					if (k == shape2.getPoints().size() - 1) {
						out.println("-");
					}
				}
			}
		}
		out.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SVGReader parser = new SVGReader();
		try {
			List<SVGShape> shapelist = parser.readSVGFromFile(new File(args[0]));
			save(new File(args[1]), shapelist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

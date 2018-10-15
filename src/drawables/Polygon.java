package drawables;

import utils.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Drawable{

    List<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(Point p){
        points.add(p);
    }

    @Override
    public void draw(Renderer renderer) {
        //TODO - for cyklus, propojit body
        //TODO - i první bod s posledním bodem
    }
}

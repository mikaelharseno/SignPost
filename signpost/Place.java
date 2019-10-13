package signpost;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/** An (X, Y) position on a Signpost puzzle board.  We require that
 *  X, Y >= 0.  Each Place object is unique; no other has the same x and y
 *  values.  As a result, "==" may be used for comparisons.
 *  @author Kezia Devina Liman
 */
class Place {

    /** Convenience list-of-Place class.  (Defining this allows one to create
     *  arrays of lists without compiler warnings.) */
    //OK
    static class PlaceList extends ArrayList<Place> {
        /** Initialize empty PlaceList. */
        PlaceList() {
        }

        /** Initialze PlaceList from a copy of INIT. */
        PlaceList(List<Place> init) {
            super(init);
        }
    }

    /** The position (X0, Y0), where X0, Y0 >= 0. */
    //OK
    private Place(int x0, int y0) {
        x = x0; y = y0;
    }

    /** Return the position (X, Y).  This is a factory method that
     *  creates a new Place only if needed by caching those that are
     *  created. */
    //OK
    static Place pl(int x, int y) {
        assert x >= 0 && y >= 0;
        int s = max(x, y);
        if (s >= _places.length) {
            Place[][] newPlaces = new Place[s + 1][s + 1];
            for (int i = 0; i < _places.length; i += 1) {
                System.arraycopy(_places[i], 0, newPlaces[i], 0,
                                 _places.length);
            }
            _places = newPlaces;
        }
        if (_places[x][y] == null) {
            _places[x][y] = new Place(x, y);
        }
        return _places[x][y];
    }

    /** Returns the direction from (X0, Y0) to (X1, Y1), if we are a queen
     *  move apart.  If not, returns 0. The direction returned (if not 0)
     *  will be an integer 1 <= dir <= 8 corresponding to the definitions
     *  in Model.java */
    //OK Complicated, fuck this
    static int dirOf(int x0, int y0, int x1, int y1) {
        int dx = x1 < x0 ? -1 : x0 == x1 ? 0 : 1;
        int dy = y1 < y0 ? -1 : y0 == y1 ? 0 : 1;
        if (dx == 0 && dy == 0) {
            return 0;
        }
        if (dx != 0 && dy != 0 && Math.abs(x0 - x1) != Math.abs(y0 - y1)) {
            return 0;
        }

        return dx > 0 ? 2 - dy : dx == 0 ? 6 + 2 * dy : 6 + dy;
    }

    /** Returns the direction from me to PLACE, if we are a queen
     *  move apart.  If not, returns 0. */
    //OK
    int dirOf(Place place) {
        return dirOf(x, y, place.x, place.y);
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  x1 - x. */
    //OK
    static int dx(int dir) {
        return DX[dir];
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  y1 - y. */
    //OK
    static int dy(int dir) {
        return DY[dir];
    }

    /** Return an array, M, such that M[x][y][dir] is a list of Places that are
     *  one queen move away from square (x, y) in direction dir on a
     *  WIDTH x HEIGHT board.  Additionally, M[x][y][0] is a list of all Places
     *  that are a queen move away from (x, y) in any direction (the union of
     *  the lists of queen moves in directions 1-8). */
    static PlaceList[][][] successorCells(int width, int height) {
        PlaceList[][][] M = new PlaceList[width][height][9];
        //PlaceList M = new PlaceList();
        for (int ix = 0; ix < width; ix++) {
            for (int iy = 0; iy < height; iy++) {
                for (int i = x+1, j = y+2; i < width && j < height; i++, j++) {
                    M[ix][iy][1].add(Place.pl(i,j));
                }
                for (int i = x+1; i < width; i++) {
                    M[ix][iy][2].add(Place.pl(i, y));
                }
                for (int i = x+1, j = y-1; i < width && j >= 0; i++, j--) {
                    M[ix][iy][3].add(Place.pl(i,j));
                }
                for (int j = y-1; j >= 0; j--) {
                    M[ix][iy][4].add(Place.pl(x, j));
                }
                for (int i = x-1, j = y-1; i >= 0 && j >= 0; i--, j--) {
                    M[ix][iy][5].add(Place.pl(i,j));
                }
                for (int i = x-1; i >= 0; i--) {
                    M[ix][iy][6].add(Place.pl(i, y));
                }
                for (int i = x-1, j = y+1; i >= 0 && j < height; i--, j++) {
                    M[ix][iy][7].add(Place.pl(i, j));
                }
                for (int j = y+1; j < height; j++) {
                    M[ix][iy][8].add(Place.pl(x, j));
                }
                for (int i = 1; i < 9; i++) {
                    M[ix][iy][0].addAll(M[ix][iy][i]);
                }
            }
        }

        return M;
    }



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Place)) {
            return false;
        }
        Place other = (Place) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return (x << 16) + y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    /** X displacement of adjacent squares, indexed by direction. */
    static final int[] DX = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };

    /** Y displacement of adjacent squares, indexed by direction. */
    static final int[] DY = { 0, 1, 0, -1, -1, -1, 0, 1, 1 };

    /** Coordinates of this Place. */
    protected final int x, y;

    /** Places already generated. */
    private static Place[][] _places = new Place[10][10];


}

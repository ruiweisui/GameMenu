import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.security.Key;

public class Sokoban extends Game{
    public Board board = new Board();
    public static Point LEFT = new Point(-1, 0),
                        RIGHT = new Point(1, 0),
                        UP = new Point(0, -1),
                        DOWN = new Point(0, 1);

    public Sokoban() {
        super("Sokoban", 1000, 800);
        board.loadStringArray(levelOne);
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, 5000, 5000);
        board.show(g);
        if (board.done()){
            g.setColor(Color.BLACK);
            g.drawString("Nice Job!", 20, 20);
        }
    }

    @Override
    public void endGame() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();  //vk stands for virtual key
        if (vk == KeyEvent.VK_LEFT){board.go(LEFT);}
        if (vk == KeyEvent.VK_RIGHT){board.go(RIGHT);}
        if (vk == KeyEvent.VK_UP){board.go(UP);}
        if (vk == KeyEvent.VK_DOWN){board.go(DOWN);}
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public static class Board{
        public static final int N = 25;
        public char[][] b = new char[N][N];
        public Point person = new Point(0, 0);
        public static String boardState = " WPCGgE";
        public static Color[] colors = {
                Color.WHITE,
                Color.DARK_GRAY,
                Color.GREEN,
                Color.ORANGE,
                Color.CYAN,
                Color.BLUE,
                Color.RED
        };
        public static final int xM = 50, yM = 50, W = 40;
        public static boolean onGoal = false; // tracks if the player is on goal square
        public static Point dest = new Point(0, 0);

        public Board() {clear();}
        public char ch(Point p) {return b[p.x][p.y];}
        public void set(Point p, char c) {b[p.x][p.y] = c;}
        public void movePerson() {  // simple move to an empty square
            boolean res = ch(dest) == 'G';
            set(person, onGoal ? 'G':' ');
            set(dest, 'P');
            person.setLocation(dest);
            onGoal = res;
        }
        public void go(Point p){
            dest.setLocation(person.x + p.x, person.y + p.y);
            if(ch(dest) == 'W' || ch(dest) == 'E') {return;}  // do not walk into wall
            if(ch(dest) == ' ' || ch(dest) == 'G') {movePerson(); return;}  //move container
            if(ch(dest) == 'C' || ch(dest) == 'g') {
                dest.setLocation(dest.x + p.x, dest.y + p.y);  //changing dest to box dest
                if(ch(dest) != ' ' && ch(dest) != 'G'){return;}
                set(dest, ch(dest) == 'G' ? 'g' : 'C');  // put boxes in spot
                dest.setLocation(dest.x - p.x, dest.y - p.y);  //back uo to the person position
                set(dest, ch(dest) == 'g' ? 'G':' ');
                movePerson();
            }
        }

        public boolean done(){
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    if(b[i][j] == 'C') {return false;}
                }
            }
            return true;
        }

        public void clear(){
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    b[i][j] = ' ';
                }
            }
        }

        public void show(Graphics g){
            for (int r = 0; r < N; r++){
                for (int c = 0; c < N; c++){
                    int ndx = boardState.indexOf(b[c][r]);
                    g.setColor(colors[ndx]);
                    g.fillRect(xM + c * W, yM + r * W, W, W);
                }
            }
        }

        public void loadStringArray(String[] a){
            person.setLocation(0,0);
            for (int r = 0; r < a.length; r++){
                String s = a[r];
                for (int c = 0; c < s.length(); c++){
                    char ch = s.charAt(c);
                    b[c][r] = (boardState.indexOf(ch) > -1) ? ch : 'E';  // detect illegal characters
                    if (ch == 'P' && person.x == 0){person.x = c; person.y = r;}
                }
            }
        }

    }

    public static String[] levelOne = {
            "  WWWWW",
            "WWW   W",
            "WGPC  W",
            "WWW CGW",
            "WGWWC W",
            "W W G WW",
            "WC gCCGW",
            "W   G  W",
            "WWWWWWWW",

    };

}

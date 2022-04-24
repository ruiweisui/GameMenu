import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.Timer;

public class Destructo extends Game implements ActionListener {

    public static final int nR = 15, nC = 13;
    public int brickRemaining;
    public int[][] grid = new int[nC][nR];

    public static Color[] color = {
            Color.lightGray,
            Color.cyan,
            Color.green,
            Color.yellow,
            Color.red,
            Color.pink
    };

    public int w = 50, h = 30, xM = 100, yM = 100;
    public static Random RANDOM = new Random();
    public static int rnd(int k) {return RANDOM.nextInt(k);}
    public static Timer timer;

    public Destructo() {
        super("Destructo", 1000, 1000);
        rndColors(3);
        timer = new Timer(100, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        g.setColor(color[0]); g.fillRect(0, 0, 5000, 5000);
        showGrid(g);
        bubbleSort();
        if(slideCol()){xM += w/2;}
        g.setColor(Color.black);
        g.drawString("Remaining: " + brickRemaining, 50, 25);
    }

    @Override
    public void endGame() {
        timer.stop();
        timer = null;
    }

    public void rndColors(int k){
        brickRemaining = nR * nC;
        for (int c = 0; c < nC; c++){
            for (int r = 0; r < nR; r++){
                grid[c][r] = 1 + rnd(k); // skip index 0ßßß
            }
        }
    }

    public void showGrid(Graphics g){
        for (int c = 0; c < nC; c++){
            for (int r = 0; r < nR; r++){
                g.setColor(color[grid[c][r]]);
                g.fillRect(x(c), y(r), w, h);
            }
        }
    }

    public int x(int c){return xM + c * w;}
    public int y(int r){return yM + r * h;}
    public int c(int x){return (x - xM)/w;}
    public int r(int y){return (y - yM)/h;}

    public void mouseClicked(MouseEvent me){
        int x = me.getX(), y = me.getY();
        if (x < xM || y < yM){return;}
        int r = r(y), c = c(x);
        if (r < nR && c < nC){
            rcAction(r, c);
        }
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

    public void rcAction(int r, int c){
        if (infectable(c, r)){
            infect(c, r, grid[c][r]);
            repaint();
        }
    }

    public void infect(int c, int r, int v){
        if (grid[c][r] != v){return;}
        grid[c][r] = 0; // kill the cell before infecting neighbors
        brickRemaining--;
        if (r > 0){infect(c, r - 1, v);}
        if (c > 0){infect(c - 1, r, v);}
        if (r < nR - 1){infect(c, r + 1, v);}
        if (c < nC - 1){infect(c + 1, r, v);}
    }

    public boolean infectable(int c, int r){
        int v = grid[c][r];
        if (v == 0){return false;}
        if (r > 0){if (grid[c][r-1] == v){return true;}}
        if (c > 0){if (grid[c-1][r] == v){return true;}}
        if (r < nR - 1){if (grid[c][r+1] == v){return true;}}
        if (c < nC - 1){if (grid[c+1][r] == v){return true;}}
        return false;
    }

    public boolean bubble(int c){
        boolean res = false;
        for (int r = nR - 1; r > 0; r--){
            if(grid[c][r] == 0 && grid[c][r-1] != 0){
                res = true;
                grid[c][r] = grid[c][r-1];
                grid[c][r-1] = 0;
            }
        }
        return res;
    }

    public void bubbleSort(){
        for (int c = 0; c < nC; c++){
            if (bubble(c)){break;}
        }
    }

    public boolean colIsEmpty(int c){
        for (int r = 0; r < nR; r++){
            if(grid[c][r] != 0){return false;}
        }
        return true;
    }

    public void swapCol(int c){  //c is not empty, c - 1 is empty
        for (int r = 0; r < nR; r++){
            grid[c-1][r] = grid[c][r];
            grid[c][r] = 0;
        }
    }

    public boolean slideCol(){
        boolean res = false;
        for (int c = 1; c < nC; c++){
            if(colIsEmpty(c-1) && !colIsEmpty(c)){
                swapCol(c);
                res = true;
            }
        }
        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e) {repaint();}


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Breakout extends Game implements ActionListener {
    public static final int H = 16, W = 50, PW = 100, nBrick = 13, PV = 16;
    public static final int LEFT = 100, RIGHT = LEFT+nBrick*W, TOP = 50, BOT = 700;
    public static Paddle paddle = new Paddle();
    public static Ball ball = new Ball();
    public static final int MAX_LIFE = 3;
    public static int lives = MAX_LIFE, score = 0;
    public Timer timer;
    public static final int GAP = 3 * H;
    public static int rowCount = 1;

    public Breakout() {
        super("Breakout", 1000, 800);
        timer = new Timer(30, this);
        timer.start();
        startGame();
    }

    public void paintComponent(Graphics g){
        G.whiteBackground(g);
        g.setColor(Color.BLACK);
        g.fillRect(LEFT, TOP, RIGHT - LEFT, BOT- TOP);
        g.drawString("Lives: " + lives, LEFT + 20, 30);
        g.drawString("Score: " + score, RIGHT -80, 30);
        paddle.show(g);
        ball.show(g);
        Brick.List.ALL.show(g);
    }

    public static void startGame(){
        lives = MAX_LIFE;
        score = 0;
        rowCount = 0;
        startNewRows();
    }

    @Override
    public void endGame() {
        timer.stop();
        timer = null;
    }

    public static void startNewRows(){
        rowCount++;
        Brick.List.ALL.clear();
        Brick.newBrickRows(rowCount);
        ball.init();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int vK = ke.getKeyCode();
        if (vK == KeyEvent.VK_LEFT){paddle.left();}
        if (vK == KeyEvent.VK_RIGHT){paddle.right();}
        if(ke.getKeyChar() == ' '){paddle.dxStuck = -1;}
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.move();
        repaint();
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


    //----------------------------------------Paddle-----------------------------------------//

    public static class Paddle extends G.VS {

        public Color color = Color.YELLOW;
        public int dxStuck = 10;

        public Paddle() {
            super(LEFT, BOT - H, PW, H);
        }

        public void show(Graphics g) {
            fill(g, color);
        }

        public void left() {
            loc.x += -2*PV;
            limitX();
        }

        public void right() {
            loc.x += 2*PV;
            limitX();
        }

        public void limitX() {
            if (loc.x < LEFT) {
                loc.x = LEFT;
            }
            if (loc.x + PW > RIGHT) {
                loc.x = RIGHT - PW;
            }
            if (dxStuck >= 0) {
                ball.loc.set(loc.x + dxStuck, BOT - 2 * H);
            }
        }

        public void hitBall(){
            if (ball.loc.x < loc.x || ball.loc.x > loc.x + PW){
                // ball did not hit paddle, one life is lost
                lives--;
                if (lives == 0){
                    startGame();
                } else {
                    ball.init();
                }
            } else {
                // ball hit paddle, ball needs to bounce
                ball.dy = - ball.dy;
                ball.dx += dxAdjust();
            }
        }

        public int dxAdjust(){
            int cp = paddle.loc.x + PW / 2;
            return (ball.loc.x + H/2 -cp)/10;
        }
    }

    //----------------------------------------Ball-----------------------------------------//
    public static class Ball extends G.VS {
        public Color color = Color.WHITE;
        public int dx = 11, dy = DY_START;
        public static final int DY_START = -11;

        public Ball() {
            super(LEFT, BOT-2*H, H, H);
        }

        public void show(Graphics g) {fill(g, color);}

        public void move(){
            if (paddle.dxStuck < 0){
                loc.x += dx; loc.y += dy;
                wallBounce();
                Brick.List.ballHitBrick();
            }
        }

        public void init(){
            paddle.dxStuck = PW/2-H/2;
            loc.set(paddle.loc.x + paddle.dxStuck, BOT-2*H);
            dx = 0;
            dy = DY_START;
        }

        public void wallBounce(){
            if (loc.x < LEFT){
                loc.x = LEFT;
                dx = -dx;
            }
            if (loc.x > RIGHT - H){
                loc.x = RIGHT - H;
                dx = -dx;
            }
            if (loc.y < TOP){
                loc.y = TOP;
                dy = -dy;
            }
            if (loc.y > BOT - H){
                paddle.hitBall();
            }
        }
    }
    //----------------------------------------Brick-----------------------------------------//
    public static class Brick extends G.VS {
        public static Color[] colors = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN};
        public Color color;

        public Brick(int x, int y) {
            super(x, y, W, H);
            color = colors[G.rnd(colors.length)];
            Brick.List.ALL.add(this);
        }

        public void show(Graphics g){
            fill(g, color);
            draw(g, Color.BLACK);
        }

        public boolean hit(int x, int y){
            return (x < loc.x + W && x + H > loc.x && y > loc.y && y < loc.y + H);
        }

        public void destroy(){
            ball.dy = -ball.dy;
            Brick.List.ALL.remove(this);
            score += 17;
            if (Brick.List.ALL.isEmpty()){
                startNewRows();
            }
        }

        public static void newBrickRows(int n){
            for (int i = 0; i < n; i++){
                for (int j = 0; j < nBrick; j++){
                    new Brick(LEFT + j * W, TOP + GAP + i * H);
                }
            }
        }
        //  -------------------------------------List---------------------------------------//
        public static class List extends ArrayList<Brick>{
            public static List ALL = new List();
            public void show(Graphics g) {for (Brick b : this){b.show(g);}}
            public static void ballHitBrick(){
                int x = ball.loc.x, y = ball.loc.y;
                for (Brick b : ALL){
                    if (b.hit(x,y)){b.destroy(); return;} //should not change a list that is being iterated
                }
            }
        }
    }

}

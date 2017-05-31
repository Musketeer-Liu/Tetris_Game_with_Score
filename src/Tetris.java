/**
 * Created by Yutong on 3/11/2017.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.Timer;

public class Tetris extends JFrame {
    public Tetris() {
        Tetrisblok a = new Tetrisblok();
        addKeyListener(a);
        add(a);
    }

    public static void main(String[] args) {
        Tetris frame = new Tetris();
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu game = new JMenu("Game");
        JMenuItem newgame = game.add("New Game");
        JMenuItem pause = game.add("Pause");
        JMenuItem goon = game.add("Resume");
        JMenuItem exit = game.add("Exit");
        JMenu help = new JMenu("Help");
        JMenuItem about = help.add("About");
        menu.add(game);
        menu.add(help);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(220, 275);
        frame.setTitle("Tetris Lite");
        frame.setVisible(true);
        frame.setResizable(false);
    }
}

class Tetrisblok extends JPanel implements KeyListener {
    private int blockType;
    private int score = 0;
    private int turnState;
    private int x;
    private int y;
    private int i = 0;

    int j = 0;
    int flag = 0;
    int[][] map = new int[13][23];

    // 1: Tetris Shape S, Z, L, J, I, O, T:
    private final int shapes[][][] = new int[][][]{
            // I
            {{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
             { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 }},
            // S
            {{ 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }},
            // Z
            {{ 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }},
            // J
            {{ 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
             { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }},
            // O
            {{ 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }},
            // L
            {{ 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
             { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }},
            // T
            {{ 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
             { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
             { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 }}
    };

    public void newblock() {
        blockType = (int) (Math.random() * 1000) % 7;
        turnState = (int) (Math.random() * 1000) % 4;
        x = 4;
        y = 0;
        if (gameover(x, y) == 1) {
            newmap();
            drawwall();
            score = 0;
            JOptionPane.showMessageDialog(null, "GAME OVER");
        }
    }

    public void drawwall() {
        for (i = 0; i < 12; i++) {
            map[i][21] = 2;
        }
        for (j = 0; j <22; j++) {
            map[11][j] = 2;
            map[0][j] = 2;
        }
    }

    public void newmap() {
        for (i = 0; i < 12; i++) {
            for (j = 0; j < 22; j++) {
                map[i][j] = 0;
            }
        }
    }

    // Initialize Construction
    Tetrisblok() {
        newblock();
        newmap();
        drawwall();
        Timer timer = new Timer(1000, new TimerListener());
        timer.start();
    }

    public void turn() {
        int tempturnState = turnState;
        turnState = (turnState + 1) % 4;
        if (blow(x, y, blockType, turnState) == 1) {

        }
        if (blow(x, y, blockType, turnState) == 0) {
            turnState = tempturnState;
        }
        repaint();
    }

    public void left() {
        if (blow(x - 1, y, blockType, turnState) == 1) {
            x = x - 1;
        };
        repaint();
    }

    public void right() {
        if (blow(x + 1, y, blockType, turnState) == 1) {
            x = x + 1;
        };
        repaint();
    }

    public void down() {
        if (blow(x, y + 1, blockType, turnState) == 1) {
            y = y + 1;
            delline();
        };
        if (blow(x, y + 1, blockType, turnState) == 0) {
            add(x, y, blockType, turnState);
            newblock();
            delline();
        };
        repaint();
    }

    public int blow(int x, int y, int blockType, int turnState) {
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x + b + 1][y + a] == 1)) ||
                        ((shapes[blockType][turnState][a * 4 + b] == 1) && (map[x + b + 1][y + a] == 2))) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public void delline() {
        int c = 0;
        for (int b = 0; b < 22; b++) {
            for (int a = 0; a < 12; a++) {
                if (map[a][b] == 1) {
                    c = c + 1;
                    if (c == 10) {
                        score += 10;
                        for (int d = b; d > 0; d--) {
                            for (int e = 0; e < 11; e++) {
                                map[e][d] = map[e][d - 1];
                            }
                        }
                    }
                }
            }
            c = 0;
        }
    }

    public int gameover(int x, int y) {
        if (blow(x, y, blockType, turnState) == 0) {
            return 1;
        }
        return 0;
    }

    public void add(int x, int y, int blockType, int turnState) {
        int j = 0;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (map[x + b + 1][y + a] == 0) {
                    map[x + b + 1][y + a] = shapes[blockType][turnState][j];
                };
                j++;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(j = 0; j < 16; j++) {
            if (shapes[blockType][turnState][j] == 1) {
                g.fillRect((j % 4 + x + 1) * 10, (j / 4 + y) * 10, 10, 10);
            }
        }
        for (j = 0; j < 22; j++) {
            for (i = 0; i < 12; i++) {
                if (map[i][j] == 1) {
                    g.fillRect(i * 10, j * 10, 10, 10);
                }
            }
        }
        for (j = 0; j < 22; j++) {
            for (i = 0; i < 12; i++) {
                if (map[i][j] == 1) {
                    g.fillRect(i * 10, j * 10, 10, 0);
                }
                if (map[i][j] == 2) {
                    g.drawRect(i * 10, j * 10, 10, 10);
                }
            }
        }
        g.drawString("score = " + score, 125, 10);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                down();
                break;
            case KeyEvent.VK_UP:
                turn();
                break;
            case KeyEvent.VK_RIGHT:
                right();
                break;
            case KeyEvent.VK_LEFT:
                left();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    class TimerListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            repaint();
            if (blow(x, y + 1, blockType, turnState) == 1) {
                y = y + 1;
                delline();
            };
            if (blow(x, y + 1, blockType, turnState) == 0) {
                if (flag == 1) {
                    add(x, y, blockType, turnState);
                    delline();
                    newblock();
                    flag = 0;
                }
                flag = 1;
            };
        }
    }

}







































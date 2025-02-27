import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class flappyBread extends JPanel implements ActionListener {
    private int birdY = 250, birdVelocity = 0;
    private final int GRAVITY = 1, JUMP_STRENGTH = -10;
    private ArrayList<Rectangle> pipes;
    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;

    Image background = Toolkit.getDefaultToolkit().createImage("background.jpg");
    Image scaleBack = background.getScaledInstance(400, 500,Image.SCALE_DEFAULT);
    Image bread = Toolkit.getDefaultToolkit().createImage("bread.png");
    Image scaleBread = bread.getScaledInstance(50, 50,Image.SCALE_DEFAULT);

    public flappyBread() {
        setPreferredSize(new Dimension(400, 500));
        pipes = new ArrayList<>();
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    birdVelocity = JUMP_STRENGTH;
                }
            }
        });
        setFocusable(true);
        addPipes();
    }

    public void menu() {
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "File menu");
        menuBar.add(menu);

        //JMenuItems show the menu items
        menuItem = new JMenuItem("New",
                                new ImageIcon("images/new.gif"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menu.add(menuItem);

        // add a separator
        menu.addSeparator();

        menuItem = new JMenuItem("Pause", new ImageIcon("images/pause.gif"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit", new ImageIcon("images/exit.gif"));
        menuItem.setMnemonic(KeyEvent.VK_E);
        menu.add(menuItem);
    }

    private void addPipes() {
        Random rand = new Random();
        int gap = 120;
        int height = rand.nextInt(200) + 50;
        pipes.add(new Rectangle(400, 0, 50, height));
        pipes.add(new Rectangle(400, height + gap, 50, 500 - height - gap));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        birdVelocity += GRAVITY;
        birdY += birdVelocity;

        for (Rectangle pipe : pipes) {
            pipe.x -= 5;
        }
        if (pipes.get(0).x + 50 < 0) {
            pipes.remove(0);
            pipes.remove(0);
            addPipes();
        }

        checkCollision();
        repaint();
    }

    private void checkCollision() {
        if (birdY >= 500 || birdY <= 0) {
            score = 0;
            gameOver = true;
            timer.stop();
        }
        for (Rectangle pipe : pipes) {
            if (new Rectangle(100, birdY, 30, 30).intersects(pipe)) {
                score = 0;
                gameOver = true;
                timer.stop();
            }
            else if (!new Rectangle(100, birdY, 30, 30).intersects(pipe) && pipe.getX() == 100) {
                score = score + 1;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(scaleBack, 0, 0, null);
        g.setColor(Color.lightGray);
        g.drawImage(scaleBread, 90, birdY-10, getFocusCycleRootAncestor());
        g.setColor(Color.pink);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }
        g.setColor(Color.MAGENTA);
        g.drawString("score: " + score, 20, 20);
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over", 170, 250);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("flappy bread");
        flappyBread game = new flappyBread();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

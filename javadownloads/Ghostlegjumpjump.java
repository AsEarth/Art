import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Ghostlegjumpjump extends JFrame {
    private int lines = 4;              // 垂直線數
    private int levels = 10;            // 橫線層數
    private boolean[][] steps;          // 橫線布置
    private String[] gifts = {
        "$10優惠劵", "鉛筆", "毛公仔", "哈哈笑貼紙",
        "糖果", "小筆記本", "橡皮擦", "小吊飾"
    };

    private JButton[] startButtons;
    private JLabel messageLabel;
    private LadderPanel ladderPanel;

    public Ghostlegjumpjump() {
        super("🎮 Ghost Leg Jump Jump Jump");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        steps = new boolean[levels][lines - 1];
        randomizeSteps();

        // 按鍵列
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("選擇起點："));

        startButtons = new JButton[lines];
        for (int i = 0; i < lines; i++) {
            int index = i;
            startButtons[i] = new JButton("起點 " + (i + 1));
            startButtons[i].addActionListener(e -> play(index));
            topPanel.add(startButtons[i]);
        }

        add(topPanel, BorderLayout.NORTH);

        // 畫圖區
        ladderPanel = new LadderPanel();
        add(ladderPanel, BorderLayout.CENTER);

        // 底部訊息
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageLabel = new JLabel("點擊上方起點開始抽籤！", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomPanel.add(messageLabel, BorderLayout.CENTER);

        JButton resetBtn = new JButton("重新生成");
        resetBtn.addActionListener(e -> {
            randomizeSteps();
            ladderPanel.repaint();
            messageLabel.setText("已重新生成籤架！");
        });
        bottomPanel.add(resetBtn, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void randomizeSteps() {
        Random rand = new Random();
        for (int i = 0; i < levels; i++) {
            for (int j = 0; j < lines - 1; j++) {
                if (j > 0 && steps[i][j - 1]) continue;
                steps[i][j] = rand.nextDouble() < 0.3;
            }
        }
    }

    private void play(int start) {
        int pos = start;
        for (int i = 0; i < levels; i++) {
            if (pos > 0 && steps[i][pos - 1]) pos--;
            else if (pos < lines - 1 && steps[i][pos]) pos++;
        }
        String gift = gifts[pos % gifts.length];
        messageLabel.setText("🎁 從起點 " + (start + 1) + " 開始，抽到了「" + gift + "」！");
    }

    // 自訂面板繪製鬼腳
    private class LadderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();
            int xSpacing = w / (lines + 1);
            int ySpacing = h / (levels + 2);

            // 畫垂直線
            g.setColor(Color.BLACK);
            for (int i = 1; i <= lines; i++) {
                g.drawLine(xSpacing * i, 30, xSpacing * i, h - 50);
            }

            // 畫橫線
            g.setColor(new Color(37, 99, 235));
            for (int i = 0; i < levels; i++) {
                for (int j = 0; j < lines - 1; j++) {
                    if (steps[i][j]) {
                        int y = 40 + ySpacing * i;
                        int x1 = xSpacing * (j + 1);
                        g.drawLine(x1, y, x1 + xSpacing, y);
                    }
                }
            }

            // 畫底部禮物
            g.setColor(new Color(30, 58, 138));
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            for (int i = 1; i <= lines; i++) {
                g.drawString(gifts[(i - 1) % gifts.length], xSpacing * i - 30, h - 20);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ghostlegjumpjump frame = new Ghostlegjumpjump();
            frame.setVisible(true);
        });
    }
}

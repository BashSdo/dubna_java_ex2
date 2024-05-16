import java.awt.*;
import javax.swing.*;

enum State {
    X,
    O,
    EMPTY
}

public class t3game extends JFrame {
    JPanel panel = new JPanel();
    Cell[][] field = new Cell[3][3];
    State currentPlayer = State.X;

    class Cell extends JButton {
        State state;
        int x, y;

        public Cell(int x, int y) {
            super();

            this.x = x;
            this.y = y;

            setBounds(x * 100, y * 100, 100, 100);
            setState(State.EMPTY);
            addActionListener(this::cellActionPerformed);
        }

        public void cellActionPerformed(java.awt.event.ActionEvent evt) {
            if (state == State.EMPTY) {
                setState(currentPlayer);
                currentPlayer = (currentPlayer == State.X ? State.O : State.X);
                checkGameState();
            }
        }

        public void setState(State state) {
            this.state = state;
            setText(state == State.X ? "X" : (state == State.O ? "O" : ""));
        }

        public State getState() {
            return state;
        }
    }

    public t3game() {
        setTitle("Tic-Tac-Toe");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        panel.setLayout(new GridLayout(3, 3));
        panel.setPreferredSize(new Dimension(300, 300));
        add(panel);

        createField();

        pack();
        setLocationRelativeTo(null);
    }

    private void createField() {
        Cell[][] field = new Cell[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[j][i] = new Cell(j, i);
                panel.add(field[j][i]);
            }
        }

        this.field = field;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].getState() == State.EMPTY) {
                    return false;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Ничья!");

        return true;
    }

    private void checkGameState() {
        if (checkWin() || checkDraw()) {
            dispose();
            Main.authorizedMenu = new Main.AuthorizedMenu();
            Main.authorizedMenu.setVisible(true);
        }
    }

    private boolean checkWin() {
        // Demo.
        return false;
    }
}

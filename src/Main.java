import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    static WelcomeFrame welcomeFrame;
    static RegisterFrame registerFrame;
    static AuthorizedMenu authorizedMenu;
    static t3game gameWindow = new t3game();
    static Player player = new Player();

    static class EditPlayerFrame extends JFrame {
        Player player;

        JLabel nameLabel = new JLabel("Имя:");
        JLabel passwordLabel = new JLabel("Пароль:");
        JTextField nameField = new JTextField();
        JTextField passwordField = new JTextField();
        JLayeredPane buttons = new JLayeredPane();
        JButton btnEdit = new JButton("Сохранить");
        JButton btnCancel = new JButton("Отмена");

        public EditPlayerFrame(Player player) {
            super(player.name + " - Редактирование");
            setPreferredSize(new Dimension(320, 300));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(null);
            setResizable(false);
            setAlwaysOnTop(true);

            nameLabel.setBounds(50, 20, 200, 24);
            nameField.setBounds(50, 50, 200, 24);

            passwordLabel.setBounds(50, 80, 200, 24);
            passwordField.setBounds(50, 110, 200, 24);


            btnEdit.setBounds(0, 0, 150, 40);
            btnEdit.addActionListener(this::btnEditActionPerformed);

            btnCancel.setBounds(10, 50, 130, 24);
            btnCancel.addActionListener(this::btnCancelActionPerformed);

            buttons.setBounds(75, 150, 150, 80);
            buttons.add(btnEdit);
            buttons.add(btnCancel);

            add(nameLabel);
            add(passwordLabel);
            add(nameField);
            add(passwordField);
            add(buttons);

            this.player = player;

            pack();
            setLocationRelativeTo(null);
        }

        private void btnCancelActionPerformed(ActionEvent actionEvent) {
            dispose();
            authorizedMenu = new AuthorizedMenu();
            authorizedMenu.setVisible(true);
        }

        private void btnEditActionPerformed(ActionEvent actionEvent) {
            var newPlayer = new Player();
            newPlayer.name = nameField.getText();
            newPlayer.password = passwordField.getText();

            PlayersStorage.editPlayer(player, newPlayer);
            PlayersStorage.save();
            dispose();
            authorizedMenu = new AuthorizedMenu();
            authorizedMenu.setVisible(true);
        }
    }

    static class AuthorizedMenu extends JFrame {
        JLayeredPane buttons = new JLayeredPane();

        JLabel welcomeLabel = new JLabel("Добро пожаловать!");
        JList<String> playerList;
        JButton playButton = new JButton("Играть");
        JButton deleteButton = new JButton("Удалить");
        JButton editButton = new JButton("Редактировать");

        Player selectedPlayer;

        public AuthorizedMenu() {
            super("Меню");
            setPreferredSize(new Dimension(600, 400));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(null);
            setResizable(false);

            buttons.setBounds(0, getPreferredSize().height - 80, getPreferredSize().width, 30);

            welcomeLabel.setBounds(10, 0, getPreferredSize().width, 30);
            welcomeLabel.setText("Добро пожаловать, " + player.name + "!");

            playerList = new JList<String>();
            playerList.setBounds(10, 30, getPreferredSize().width - 35, getPreferredSize().height - 120);
            playerList.setListData(
                    PlayersStorage.getPlayers().stream().map(e->e.name).toList().toArray(new String[0])
            );
            playerList.addListSelectionListener(this::playerListListSelectionChanged);

            playButton.setBounds(10, 0, 120, 30);
            playButton.addActionListener(this::playButtonActionPerformed);
            playButton.setEnabled(false);

            deleteButton.setBounds(300, 0, 120, 30);
            deleteButton.addActionListener(this::deleteButtonActionPerformed);
            deleteButton.setEnabled(false);

            editButton.setBounds(425, 0, 150, 30);
            editButton.addActionListener(this::editButtonActionPerformed);
            editButton.setEnabled(false);

            buttons.add(playButton);
            buttons.add(editButton);
            buttons.add(deleteButton);


            add(welcomeLabel);
            add(playerList);
            add(buttons);
            pack();
            setLocationRelativeTo(null);
        }

        private void playerListListSelectionChanged(ListSelectionEvent listSelectionEvent) {
            if (playerList.getSelectedIndex() != -1) {
                selectedPlayer = PlayersStorage.getByName(playerList.getSelectedValue());

                assert selectedPlayer != null;
                if (!Objects.equals(selectedPlayer.name, player.name)) {
                    playButton.setEnabled(true);
                }
                deleteButton.setEnabled(true);
                editButton.setEnabled(true);
            }
        }


        private void editButtonActionPerformed(ActionEvent evt) {
            EditPlayerFrame editPlayerFrame = new EditPlayerFrame(selectedPlayer);
            editPlayerFrame.setVisible(true);
            dispose();
        }

        public void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
            gameWindow = new t3game();
            gameWindow.setVisible(true);
            dispose();
        }

        public void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
            int action = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите удалить " + selectedPlayer.name + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);

            if (action == JOptionPane.YES_OPTION) {
                PlayersStorage.removePlayer(selectedPlayer);
                PlayersStorage.save();
                if (selectedPlayer == player) {
                    dispose();
                    welcomeFrame = new WelcomeFrame();
                    welcomeFrame.setVisible(true);

                    return;
                }
                playerList.setListData(PlayersStorage.getPlayers().stream().map(e->e.name).collect(Collectors.toList()).toArray(new String[0]));
                selectedPlayer = null;
                playButton.setEnabled(false);
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        }
    }

    static class RegisterFrame extends JFrame {
        JLayeredPane layeredPane = new JLayeredPane();
        JLabel lblName = new JLabel("Имя");
        JTextField txtName = new JTextField();
        JLabel lblPassword = new JLabel("Пароль");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnRegister = new JButton("Зарегистрироваться");
        JButton btnBack = new JButton("Назад");

        public RegisterFrame() {
            super("Регистрация");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(320, 300));

            lblName.setBounds(50, 20, 200, 24);
            txtName.setBounds(50, 50, 200, 24);

            lblPassword.setBounds(50, 80, 200, 24);
            txtPassword.setBounds(50, 110, 200, 24);

            btnRegister.setBounds(50, 150, 200, 40);
            btnRegister.addActionListener(evt -> registerFrame.btnRegisterActionPerformed(evt));

            btnBack.setBounds(50, 200, 200, 40);
            btnBack.addActionListener(evt -> registerFrame.btnBackActionPerformed(evt));

            layeredPane.add(lblName);
            layeredPane.add(txtName);
            layeredPane.add(lblPassword);
            layeredPane.add(txtPassword);
            layeredPane.add(btnRegister);
            layeredPane.add(btnBack);
            add(layeredPane);

            pack();
            setLocationRelativeTo(null);
        }

        public void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
            var name = txtName.getText();
            var password = txtPassword.getText();

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все поля");
                return;
            }

            PlayersStorage.addPlayer(PlayersStorage.newPlayer(name, password));
            PlayersStorage.save();

            setVisible(false);
            dispose();
            welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        }

        public void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
            dispose();
            welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
            setVisible(false);
        }
    }

    static class WelcomeFrame extends JFrame {
        JButton btnCreate = new JButton("Регистрация");
        JLayeredPane layeredPane = new JLayeredPane();
        JLabel lblName = new JLabel("Имя");
        JTextField txtName = new JTextField();
        JLabel lblPassword = new JLabel("Пароль");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Вход");

        public WelcomeFrame() {
            super("Вход");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);
            setPreferredSize(new Dimension(320, 300));

            lblName.setBounds(50, 20, 200, 24);
            txtName.setBounds(50, 50, 200, 24);
            lblPassword.setBounds(50, 75, 200, 24);
            txtPassword.setBounds(50, 105, 200, 24);

            btnLogin.setBounds(50, 140, 200, 36);
            btnLogin.addActionListener(evt -> welcomeFrame.btnLoginActionPerformed(evt));

            btnCreate.setBounds(getPreferredSize().width / 2 - 75, 200, 130, 24);
            btnCreate.addActionListener(evt -> welcomeFrame.btnCreateActionPerformed(evt));
            //layeredPane.setBounds(0, getPreferredSize().height - 100, 320, 24);
            layeredPane.add(lblName);
            layeredPane.add(txtName);
            layeredPane.add(lblPassword);
            layeredPane.add(txtPassword);
            layeredPane.add(btnLogin);
            layeredPane.add(btnCreate);


            setVisible(true);
            add(layeredPane);
            pack();
            setLocationRelativeTo(null);
        }

        public void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
            var name = txtName.getText();
            var password = txtPassword.getText();

            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все поля");
                return;
            }

            var player = PlayersStorage.getByName(name);
            if (player == null || !player.password.equals(password)) {
                System.out.println(player == null ? "null" : player.password);
                JOptionPane.showMessageDialog(this, "Неправильное имя или пароль");
                return;
            }

            Main.player = player;

            dispose();

            if (PlayersStorage.getPlayers().size() == 1) {
                JOptionPane.showMessageDialog(this, "Необходимо создать второго игрока для начала игры");
                registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);

                return;
            }
            authorizedMenu = new AuthorizedMenu();
            authorizedMenu.setVisible(true);
        }

        public void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {
            dispose();
            registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        PlayersStorage.load();
        System.out.println("Loaded players: " + PlayersStorage.getPlayers().size());

        welcomeFrame = new WelcomeFrame();
    }
}


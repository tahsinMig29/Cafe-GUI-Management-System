import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.io.*;

public class Cafe extends JFrame implements ActionListener, MouseListener {
    JPanel panel, headerPanel;
    JLabel logoLabel;
    JTextField searchField;
    JComboBox<String> menuComboBox;
    JButton loginButton, cartButton, historyButton;
    JLabel bannerLabel, welcomeLabel, usernameDisplayLabel, popularLabel, coffeeLabel;

    private static final String CART_FILE = "cart_data.txt";
    
    String currentUsername = "";
    String currentAddress = "";
    String currentMobile = "";

    class OrderHistory {
        String itemName;
        int quantity;
        double totalPrice;

        OrderHistory(String itemName, int quantity, double totalPrice) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
    }

    java.util.List<OrderHistory> orderHistoryList = new java.util.ArrayList<>();

    class CartItem {
        String name;
        double price;
        int quantity;

        CartItem(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

    java.util.List<CartItem> cartList = new ArrayList<>();

    public Cafe() {
        super("Café Elixir");
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
		
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 240, 230));
        panel.setPreferredSize(new Dimension(1280, 1100));
		

        headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 1280, 60);
        headerPanel.setBackground(new Color(225, 215, 205));
        headerPanel.setBorder(new LineBorder(Color.WHITE, 7));

        ImageIcon logoIcon = new ImageIcon("D:/Coding/Java/Final/final_Project_draft/img/logo.jpg");
        logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(105, 5, 50, 50);
        headerPanel.add(logoLabel);

        searchField = new JTextField(15);
        searchField.setBounds(190, 15, 300, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(searchField);

        cartButton = new JButton("Cart");
        cartButton.setBounds(990, 15, 80, 30);
        cartButton.setBackground(new Color(183, 131, 123));
        cartButton.addActionListener(this);
        cartButton.addMouseListener(this);
        headerPanel.add(cartButton);
        
        historyButton = new JButton("History");
        historyButton.setBounds(1080, 15, 80, 30);
        historyButton.setBackground(new Color(183, 131, 123));
        historyButton.addActionListener(this);
        historyButton.addMouseListener(this);
        headerPanel.add(historyButton);
        
        loginButton = new JButton("Login");
        loginButton.setBounds(1170, 15, 80, 30);
        loginButton.setBackground(new Color(183, 131, 123));
        loginButton.addActionListener(this);
        loginButton.addMouseListener(this);
        headerPanel.add(loginButton);
        
        String[] menuItems = {"Menu", "Cheesecake", "Cappuccino", "Espresso", "Beaf Burger"};
        menuComboBox = new JComboBox<>(menuItems);
        menuComboBox.setBounds(900, 15, 80, 30);
        headerPanel.add(menuComboBox);
        menuComboBox.addActionListener(e -> {
            String selectedItem = (String) menuComboBox.getSelectedItem();
            if (!"Menu".equals(selectedItem)) {
                JOptionPane.showMessageDialog(null, "You selected: " + selectedItem);
            }
        });

        panel.add(headerPanel);

        ImageIcon banner = new ImageIcon("D:/Coding/Java/Final/final_Project_draft/img/Banner.png");
        bannerLabel = new JLabel(banner);
        bannerLabel.setBounds(0, 90, 1280, 280);
        panel.add(bannerLabel);

        welcomeLabel = new JLabel("WELCOME TO OUR CAFÉ ");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 30));
        welcomeLabel.setBounds(460, 390, 400, 40);
        welcomeLabel.setForeground(new Color(90, 50, 30));
        panel.add(welcomeLabel);

        usernameDisplayLabel = new JLabel("");
        usernameDisplayLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usernameDisplayLabel.setBounds(440, 430, 400, 40);
        usernameDisplayLabel.setForeground(new Color(60, 30, 10));
        usernameDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(usernameDisplayLabel);

        popularLabel = new JLabel("Popular Items");
        popularLabel.setFont(new Font("Serif", Font.BOLD, 24));
        popularLabel.setBounds(140, 490, 200, 30);
        popularLabel.setForeground(new Color(70, 40, 20));
        panel.add(popularLabel);

        coffeeLabel = new JLabel("Coffee");
        coffeeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        coffeeLabel.setBounds(140, 700, 200, 30);
        coffeeLabel.setForeground(new Color(70, 40, 20));
        panel.add(coffeeLabel);

        addItem("Cheesecake", 199, "img1.jpg", 105, 530);
        addItem("Mocha", 359, "img2.jpg", 455, 530);
        addItem("Double Beef Burger", 299, "img3.jpg", 805, 530);
        addItem("Cappuccino", 389, "img4.jpg", 105, 740);
        addItem("Espresso", 419, "img5.jpg", 455, 740);
        addItem("Americano", 359, "img6.jpg", 805, 740);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane);

        loadCartFromFile();

        this.setVisible(true);
    }

    private void addItem(String name, double price, String imgPath, int x, int y) {
        ImageIcon img = new ImageIcon("D:/Coding/Java/Final/final_Project_draft/img/" + imgPath);
        JLabel imgLabel = new JLabel(img);
        imgLabel.setBounds(x, y, 200, 130);
        panel.add(imgLabel);

        JLabel itemLabel = new JLabel(name);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 16));
        itemLabel.setBounds(x, y + 135, 200, 20);
        panel.add(itemLabel);

        JLabel priceLabel = new JLabel("Price: TK " + price);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        priceLabel.setBounds(x, y + 155, 200, 20);
        panel.add(priceLabel);

        JLabel qtyLabel = new JLabel("Qty:");
        qtyLabel.setBounds(x + 210, y, 40, 25);
        panel.add(qtyLabel);

        JTextField qtyField = new JTextField("1");
        qtyField.setBounds(x + 250, y, 40, 25);
        panel.add(qtyField);

        JButton cartBtn = new JButton("Add to cart");
        cartBtn.setBounds(x + 210, y + 40, 120, 30);
        cartBtn.setBackground(new Color(220, 210, 200));
        cartBtn.setActionCommand("cart:" + name);
        cartBtn.addActionListener(this);
        cartBtn.addMouseListener(this);
        panel.add(cartBtn);

        JButton orderBtn = new JButton("Place Order");
        orderBtn.setBounds(x + 210, y + 80, 120, 30);
        orderBtn.setBackground(new Color(220, 210, 200));
        orderBtn.setActionCommand("order:" + name);
        orderBtn.addActionListener(this);
        orderBtn.addMouseListener(this);
        panel.add(orderBtn);

        cartBtn.putClientProperty("qtyField", qtyField);
        orderBtn.putClientProperty("qtyField", qtyField);
        cartBtn.putClientProperty("price", price);
        orderBtn.putClientProperty("price", price);
    }

    private void showCart() {
        showCartDetailsFrame();
    }

    private void showCartDetailsFrame() {
        JFrame cartFrame = new JFrame("Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setLocationRelativeTo(this);
        cartFrame.setLayout(new BorderLayout());

        JPanel cartPanel = new JPanel(new GridLayout(0, 1));

        double[] totalPrice = {0};

        for (CartItem item : cartList) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel nameLabel = new JLabel(item.name);
            JLabel priceLabel = new JLabel("Price: Tk " + item.price);
            JLabel qtyLabel = new JLabel("Qty: " + item.quantity);
            JLabel totalLabel = new JLabel("Total: Tk " + (item.price * item.quantity));

            JButton incBtn = new JButton("+");
            JButton decBtn = new JButton("-");
            JButton removeBtn = new JButton("Remove");

            incBtn.addActionListener(e -> {
                item.quantity++;
                saveCartToFile();
                cartFrame.dispose();
                showCartDetailsFrame();
            });

            decBtn.addActionListener(e -> {
                if (item.quantity > 1) {
                    item.quantity--;
                    saveCartToFile();
                    cartFrame.dispose();
                    showCartDetailsFrame();
                }
            });

            removeBtn.addActionListener(e -> {
                cartList.remove(item);
                saveCartToFile();
                cartFrame.dispose();
                showCartDetailsFrame();
            });

            itemPanel.add(nameLabel);
            itemPanel.add(priceLabel);
            itemPanel.add(qtyLabel);
            itemPanel.add(totalLabel);
            itemPanel.add(incBtn);
            itemPanel.add(decBtn);
            itemPanel.add(removeBtn);

            cartPanel.add(itemPanel);

            totalPrice[0] += item.price * item.quantity;
        }

        JScrollPane scrollPane = new JScrollPane(cartPanel);
        cartFrame.add(scrollPane, BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total Price: Tk " + totalPrice[0], SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cartFrame.add(totalLabel, BorderLayout.NORTH);

        JButton confirmBtn = new JButton("Confirm Order");
        confirmBtn.addActionListener(e -> {
            if (currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(cartFrame, "Please login to confirm your order.");
            } else if (cartList.isEmpty()) {
                JOptionPane.showMessageDialog(cartFrame, "Your cart is empty.");
            } else {
                StringBuilder orderDetails = new StringBuilder(); 
                double total = 0;
                for (CartItem item : cartList) {
                    orderDetails.append(item.name)
                              .append(" x").append(item.quantity)
                              .append(" = Tk ").append(item.price * item.quantity)
                              .append("\n");
                    total += item.price * item.quantity;
                    orderHistoryList.add(new OrderHistory(item.name, item.quantity, item.price * item.quantity));
                }
                orderDetails.append("Total: Tk ").append(total).append("\n");
                
                saveOrderToHistoryFile(orderDetails.toString());
                cartList.clear();
                saveCartToFile();
                cartFrame.dispose();
                JOptionPane.showMessageDialog(this, "Order confirmed! Thank you, " + currentUsername + ".");
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(confirmBtn);
        cartFrame.add(bottomPanel, BorderLayout.SOUTH);

        cartFrame.setVisible(true);
    }

    private void saveOrderToHistoryFile(String orderDetails) {
        try (FileWriter writer = new FileWriter("history_data.txt", true)) {
            writer.write("Order by " + currentUsername + ":\n");
            writer.write(orderDetails);
            writer.write("\n---------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistoryFromFile(JTextArea historyArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader("history_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCartItem(String name, int newQuantity) {
        for (CartItem item : cartList) {
            if (item.name.equals(name)) {
                item.quantity = newQuantity;
                saveCartToFile();
                JOptionPane.showMessageDialog(this, name + " updated.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, name + " not found in cart.");
    }

    private void deleteCartItem(String name) {
        Iterator<CartItem> iterator = cartList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().name.equals(name)) {
                iterator.remove();
                saveCartToFile();
                JOptionPane.showMessageDialog(this, name + " removed.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, name + " not found in cart.");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        String command = ae.getActionCommand();

        if (source == loginButton) {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JTextField addressField = new JTextField();
            JTextField mobileField = new JTextField();

            Object[] loginFields = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Address:", addressField,
                "Mobile Number:", mobileField
            };

            int option = JOptionPane.showConfirmDialog(
                this,
                loginFields,
                "Login",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText().trim();
                String mobile = mobileField.getText().trim();

                if (!username.isEmpty() && !password.isEmpty() && !address.isEmpty() && !mobile.isEmpty()) {
                    currentUsername = username;
                    currentAddress = address;
                    currentMobile = mobile;
                    usernameDisplayLabel.setText("Customer: " + currentUsername );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please enter all fields: username, password, address, and mobile number."
                    );
                }
            }
        } else if (source == historyButton) {
            if (currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please login to view history.");
                return;
            }

            JFrame historyFrame = new JFrame("Order History");
            historyFrame.setSize(500, 400);
            historyFrame.setLocationRelativeTo(this);
            historyFrame.setLayout(new BorderLayout());

            JTextArea historyArea = new JTextArea();
            historyArea.setEditable(false);

            // Load history from file
            loadHistoryFromFile(historyArea);

            historyFrame.add(new JScrollPane(historyArea), BorderLayout.CENTER);

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(e -> historyFrame.dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.add(closeBtn);
            historyFrame.add(btnPanel, BorderLayout.SOUTH);

            historyFrame.setVisible(true);
        } else if (source == cartButton) {
            showCart();
        } else if (command.startsWith("cart:")) {
            JButton btn = (JButton) source;
            String itemName = command.substring(5);
            JTextField qtyField = (JTextField) btn.getClientProperty("qtyField");
            double price = (double) btn.getClientProperty("price");

            try {
                int quantity = Integer.parseInt(qtyField.getText().trim());
                if (quantity <= 0) throw new NumberFormatException();
                cartList.add(new CartItem(itemName, price, quantity));
                saveCartToFile();
                JOptionPane.showMessageDialog(this, itemName + " added to cart.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid quantity for " + itemName);
            }
        } else if (command.startsWith("order:")) {
            JButton btn = (JButton) source;
            String itemName = command.substring(6);
            JTextField qtyField = (JTextField) btn.getClientProperty("qtyField");

            try {
                int quantity = Integer.parseInt(qtyField.getText().trim());
                if (quantity <= 0) throw new NumberFormatException();
                double price = (double) btn.getClientProperty("price");
                orderHistoryList.add(new OrderHistory(itemName, quantity, price * quantity));
                JOptionPane.showMessageDialog(this, "Order placed: " + itemName + " x" + quantity);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid quantity for " + itemName);
            }
        }
    }

    private void saveCartToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE))) {
            oos.writeObject(cartList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCartFromFile() {
        File file = new File(CART_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CART_FILE))) {
                cartList = (java.util.List<CartItem>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void mouseEntered(MouseEvent me) {
        if (me.getSource() instanceof JButton) {
            JButton btn = (JButton) me.getSource();
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(183, 131, 123));
        }
    }

    @Override public void mouseExited(MouseEvent me) {
        if (me.getSource() instanceof JButton) {
            JButton btn = (JButton) me.getSource();
            if (btn == loginButton || btn == cartButton || btn == historyButton) {
                btn.setBackground(new Color(183, 131, 123));
            } else {
                btn.setBackground(new Color(220, 210, 200));
            }
            btn.setForeground(Color.BLACK);
        }
    }

    @Override public void mouseClicked(MouseEvent me) {}
    @Override public void mousePressed(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent me) {}
   
}

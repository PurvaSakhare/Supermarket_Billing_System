import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

public class SupermarketBillingSystem extends JFrame {
    // Panels
    private JPanel availableProductsPanel, cartPanel, billPanel;

    // Buttons
    private JButton btnNewCustomer, btnCustomerDetails,btnAddToCart, btnCheckout;
    private JButton btnPrintBill;

    // Tables for Products and Cart
    private JTable availableProductsTable, cartTable;
    private DefaultTableModel availableProductsModel, cartModel;

    // Customer and Bill Data
    private JTextField txtCustomerName, txtCustomerEmail, txtCustomerPhone;

    // Sample data
    private String[] availableProductsColumns = {"Product ID", "Product Name", "Price"};
    private String[][] availableProductsData = {
        {"101", "Apple", "1.50"},
        {"102", "Banana", "0.50"},
        {"103", "Milk", "2.00"},
        {"104", "Bread", "1.20"}
    };

    public SupermarketBillingSystem() {
        setTitle("Supermarket Billing System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Upper Buttons (New Customer, Customer Details)
        JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNewCustomer = new JButton("New Customer");
        btnCustomerDetails = new JButton("Customer Details");
        btnPrintBill = new JButton("Print Bill");


        upperPanel.add(btnNewCustomer);
        upperPanel.add(btnCustomerDetails);
        add(upperPanel, BorderLayout.NORTH);

        // Three Panels: Available Products, Cart, Bill Generation
        availableProductsPanel = new JPanel(new BorderLayout());
        cartPanel = new JPanel(new BorderLayout());
        billPanel = new JPanel(new BorderLayout());

        // Available Products Panel
        availableProductsModel = new DefaultTableModel(availableProductsData, availableProductsColumns);
        availableProductsTable = new JTable(availableProductsModel);
        availableProductsPanel.add(new JScrollPane(availableProductsTable), BorderLayout.CENTER);
        availableProductsPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));

        // Cart Panel
        String[] cartColumns = {"Product ID", "Product Name", "Price", "Quantity", "Total"};
        cartModel = new DefaultTableModel(null, cartColumns);
        cartTable = new JTable(cartModel);

        btnAddToCart = new JButton("Add to Cart");
        btnCheckout = new JButton("Checkout");

        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JPanel cartButtonsPanel = new JPanel();
        cartButtonsPanel.add(btnAddToCart);
        cartButtonsPanel.add(btnCheckout);
        cartPanel.add(cartButtonsPanel, BorderLayout.SOUTH);
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

        // Bill Panel
        billPanel.setBorder(BorderFactory.createTitledBorder("Bill Details"));
        JTextArea txtBill = new JTextArea();
        txtBill.setEditable(false);
        billPanel.add(new JScrollPane(txtBill), BorderLayout.CENTER);

        

        // Add Print Button at the bottom of the bill panel
        JPanel billButtonPanel = new JPanel();
        billButtonPanel.add(btnPrintBill);
        billPanel.add(billButtonPanel, BorderLayout.SOUTH);

        

        // Main layout with three panels
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, availableProductsPanel, cartPanel);
        splitPane1.setDividerLocation(300);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, billPanel);
        splitPane2.setDividerLocation(600);
        add(splitPane2, BorderLayout.CENTER);

        // Button Actions
        btnAddToCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = availableProductsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String productId = availableProductsModel.getValueAt(selectedRow, 0).toString();
                    String productName = availableProductsModel.getValueAt(selectedRow, 1).toString();
                    String price = availableProductsModel.getValueAt(selectedRow, 2).toString();
                    cartModel.addRow(new Object[]{productId, productName, price, 1, price});
                }
            }
        });

        btnCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double total = 0;
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    total += Double.parseDouble(cartModel.getValueAt(i, 4).toString());
                }
                double gst = total * 0.18; // 18% GST
                double finalTotal = total + gst;

                // Generate Bill
                StringBuilder bill = new StringBuilder();
                bill.append("Customer Details:\n")
                    .append("Name: ").append(txtCustomerName.getText()).append("\n")
                    .append("Email: ").append(txtCustomerEmail.getText()).append("\n")
                    .append("Phone: ").append(txtCustomerPhone.getText()).append("\n\n")
                    .append("Products Purchased:\n");

                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    bill.append(cartModel.getValueAt(i, 1)).append(" - $")
                        .append(cartModel.getValueAt(i, 4)).append("\n");
                }
                bill.append("\nTotal: $").append(total)
                    .append("\nGST (18%): $").append(gst)
                    .append("\nFinal Total: $").append(finalTotal);

                txtBill.setText(bill.toString());
            }
        });

        // New Customer Button Action (For example purposes, hardcoded values)
        btnNewCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCustomerName = new JTextField("John Doe");
                txtCustomerEmail = new JTextField("johndoe@example.com");
                txtCustomerPhone = new JTextField("1234567890");
                JOptionPane.showMessageDialog(null, new Object[]{
                    "Customer Name:", txtCustomerName,
                    "Customer Email:", txtCustomerEmail,
                    "Customer Phone:", txtCustomerPhone
                }, "New Customer", JOptionPane.PLAIN_MESSAGE);
            }
        });
        btnCustomerDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtCustomerName != null && txtCustomerEmail != null && txtCustomerPhone != null) {
                    JOptionPane.showMessageDialog(null, new Object[]{
                        "Customer Name: " + txtCustomerName.getText(),
                        "Customer Email: " + txtCustomerEmail.getText(),
                        "Customer Phone: " + txtCustomerPhone.getText()
                    }, "Customer Details", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No customer information available.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
    
          // Print Bill Button Action
          btnPrintBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtBill.getText().isEmpty()) {
                    printBill(txtBill.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "No bill to print.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
    public void printBill(String billContent) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE; // Only one page, no additional pages
                }
                
                // Set up the graphics context
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // Print the bill content
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
                g2d.drawString(billContent, 100, 100);

                return PAGE_EXISTS;
            }
        });

        // Show print dialog to the user and print if accepted
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                JOptionPane.showMessageDialog(null, "Bill printed successfully!");
            } catch (PrinterException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to print bill.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SupermarketBillingSystem().setVisible(true);
            }
        });
    }
}

   
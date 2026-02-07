import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SupermarketBillingSystem extends JFrame {

    private JTable productTable, cartTable;
    private DefaultTableModel productModel, cartModel;
    private JTextArea billArea;

    private JButton btnAdd, btnRemove, btnClear, btnCheckout, btnNewCustomer, btnPrint;

    private JTextField txtName, txtEmail, txtPhone;

    String[] columns = {"ID", "Product", "Price"};

    String[][] data = {
        {"101","Apple","50"},
        {"102","Banana","20"},
        {"103","Orange","40"},
        {"104","Grapes","60"},
        {"105","Mango","80"},
        {"201","Potato","30"},
        {"202","Onion","25"},
        {"203","Tomato","35"},
        {"204","Carrot","40"},
        {"205","Cabbage","45"},
        {"301","Milk","60"},
        {"302","Butter","120"},
        {"303","Cheese","180"},
        {"304","Curd","50"},
        {"305","Paneer","220"},
        {"401","Bread","40"},
        {"402","Biscuit","30"},
        {"403","Chips","20"},
        {"404","Chocolate","50"},
        {"405","Cake","150"},
        {"501","Rice","70"},
        {"502","Wheat Flour","55"},
        {"503","Sugar","45"},
        {"504","Salt","20"},
        {"505","Cooking Oil","160"},
        {"601","Cold Drink","40"},
        {"602","Juice","60"},
        {"603","Mineral Water","20"},
        {"604","Tea","120"},
        {"605","Coffee","180"},
        {"701","Soap","35"},
        {"702","Shampoo","180"},
        {"703","Toothpaste","95"},
        {"704","Detergent","130"},
        {"705","Dish Wash","90"}
    };

    public SupermarketBillingSystem() {

        setTitle("Supermarket Billing System");
        setSize(1100,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel top = new JPanel();
        btnNewCustomer = new JButton("New Customer");
        btnPrint = new JButton("Print Bill");
        top.add(btnNewCustomer);
        top.add(btnPrint);
        add(top, BorderLayout.NORTH);

        // Product Table
        productModel = new DefaultTableModel(data, columns);
        productTable = new JTable(productModel);
        JScrollPane sp1 = new JScrollPane(productTable);

        // Cart Table
        cartModel = new DefaultTableModel(new String[]{"ID","Product","Price","Qty","Total"},0);
        cartTable = new JTable(cartModel);
        JScrollPane sp2 = new JScrollPane(cartTable);

        // Bill Area
        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced",Font.PLAIN,12));
        JScrollPane sp3 = new JScrollPane(billArea);

        // Buttons
        btnAdd = new JButton("Add To Cart");
        btnRemove = new JButton("Remove");
        btnClear = new JButton("Clear Cart");
        btnCheckout = new JButton("Checkout");

        JPanel cartButtons = new JPanel();
        cartButtons.add(btnAdd);
        cartButtons.add(btnRemove);
        cartButtons.add(btnClear);
        cartButtons.add(btnCheckout);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(sp2,BorderLayout.CENTER);
        cartPanel.add(cartButtons,BorderLayout.SOUTH);

        JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, cartPanel);
        JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split1, sp3);
        split1.setDividerLocation(400);
        split2.setDividerLocation(800);
        add(split2,BorderLayout.CENTER);

        // Add to Cart Logic
        btnAdd.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(this,"Select product!");
                return;
            }

            String qtyStr = JOptionPane.showInputDialog("Enter Quantity:");
            if(qtyStr==null || qtyStr.isEmpty()) return;

            int qty = Integer.parseInt(qtyStr);
            double price = Double.parseDouble(productModel.getValueAt(row,2).toString());
            double total = price * qty;

            cartModel.addRow(new Object[]{
                productModel.getValueAt(row,0),
                productModel.getValueAt(row,1),
                price,
                qty,
                total
            });
        });

        btnRemove.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if(row!=-1) cartModel.removeRow(row);
        });

        btnClear.addActionListener(e -> cartModel.setRowCount(0));

        btnCheckout.addActionListener(e -> generateBill());

        btnNewCustomer.addActionListener(e -> customerDialog());

        btnPrint.addActionListener(e -> printBill());

    }

    void customerDialog(){
        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();

        Object[] msg = {
            "Name:",txtName,
            "Email:",txtEmail,
            "Phone:",txtPhone
        };

        JOptionPane.showMessageDialog(this,msg,"Customer Info",JOptionPane.PLAIN_MESSAGE);
    }

    void generateBill(){

        double sum = 0;
        for(int i=0;i<cartModel.getRowCount();i++){
            sum += Double.parseDouble(cartModel.getValueAt(i,4).toString());
        }

        double gst = sum * 0.18;
        double finalAmt = sum + gst;

        String time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

        StringBuilder bill = new StringBuilder();
        bill.append("=========== SUPERMARKET BILL ===========\n");
        bill.append("Date: ").append(time).append("\n\n");

        if(txtName!=null){
            bill.append("Customer: ").append(txtName.getText()).append("\n");
            bill.append("Phone: ").append(txtPhone.getText()).append("\n\n");
        }

        bill.append("----------------------------------------\n");
        bill.append("Item\tQty\tPrice\tTotal\n");
        bill.append("----------------------------------------\n");

        for(int i=0;i<cartModel.getRowCount();i++){
            bill.append(cartModel.getValueAt(i,1)).append("\t")
                .append(cartModel.getValueAt(i,3)).append("\t")
                .append(cartModel.getValueAt(i,2)).append("\t")
                .append(cartModel.getValueAt(i,4)).append("\n");
        }

        bill.append("----------------------------------------\n");
        bill.append("Total : ").append(sum).append("\n");
        bill.append("GST 18% : ").append(gst).append("\n");
        bill.append("Final Amount : ").append(finalAmt).append("\n");
        bill.append("========================================\n");
        bill.append("Thank You! Visit Again 😊");

        billArea.setText(bill.toString());
    }

    void printBill(){
        try{
            billArea.print();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Print Failed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupermarketBillingSystem().setVisible(true));
    }
}

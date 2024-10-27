import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Vector;

public class ClientForm extends JFrame {
    private JPanel MainPanel; // Corrected field name
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfAdress;
    private JTextField tfPhone;
    private JButton btnAdd;
    private JButton btnClear;
    private JButton btnDelete;
    private JTable clientsTable;

    public ClientForm() {
        setTitle("Client Form");
        setContentPane(MainPanel); // Corrected to match the new field name
        setMinimumSize(new Dimension(700, 419));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel = new JPanel(); // Initialize mainPanel
        MainPanel.setLayout(new BorderLayout()); // Set layout manager

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        tfName = new JTextField();
        tfEmail = new JTextField();
        tfAdress = new JTextField();
        tfPhone = new JTextField();
        btnAdd = new JButton("Add");
        btnClear = new JButton("Clear");
        btnDelete = new JButton("Delete");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(tfEmail);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(tfAdress);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(tfPhone);
        inputPanel.add(btnAdd);
        inputPanel.add(btnClear);

        // Add input panel to main panel
        MainPanel.add(inputPanel, BorderLayout.NORTH);

        // Create table
        clientsTable = new JTable();
        createTable();
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        MainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add delete button at the bottom
            MainPanel.add(btnDelete, BorderLayout.SOUTH);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                String email = tfEmail.getText();
                String address = tfAdress.getText();
                String phone = tfPhone.getText();

                if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    DefaultTableModel model = (DefaultTableModel) clientsTable.getModel();
                    model.addRow(new Object[]{name, email, address, phone});
                    tfName.setText("");
                    tfEmail.setText("");
                    tfAdress.setText("");
                    tfPhone.setText("");
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfName.setText("");
                tfEmail.setText("");
                tfAdress.setText("");
                tfPhone.setText("");
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = clientsTable.getSelectedRow();

                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "No Row Selected to be deleted", "Select a Row", JOptionPane.ERROR_MESSAGE);
                } else {
                    DefaultTableModel model = (DefaultTableModel) clientsTable.getModel();
                    model.removeRow(row);
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    formWindowOpened(e);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                formWindowClosing(e);
            }
        });

        setVisible(true);
    }

    private void createTable() {
        clientsTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Name", "Email", "Address", "Phone"}));
    }

    private void formWindowOpened(WindowEvent evt) throws IOException, ClassNotFoundException {
        File file = new File("Clients.bin");
        if (!file.exists()) {
            return;
        }

        FileInputStream inputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Vector<Vector> tableData = (Vector<Vector>) objectInputStream.readObject();
        objectInputStream.close();

        DefaultTableModel model = (DefaultTableModel) clientsTable.getModel();
        for (Vector row : tableData) {
            model.addRow(new Object[]{row.get(0), row.get(1), row.get(2), row.get(3)});
        }
    }

    private void formWindowClosing(WindowEvent evt) {
        DefaultTableModel model = (DefaultTableModel) clientsTable.getModel();
        Vector<Vector> tableData = model.getDataVector();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Clients.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tableData);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientForm();
            }
        });
    }
}

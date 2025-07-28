package main;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InventarioFrame extends JFrame {

    private JTextField txtCodigo, txtNombre, txtCategoria, txtProveedor, txtPrecio, txtStock, txtBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ProductoDAO dao = new ProductoDAO();

    public InventarioFrame() throws ClassNotFoundException {
        setTitle("Sistema de Gestión de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icono.png").getImage());
        // Usamos BorderLayout con un margen alrededor
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(250, 250, 250));
        setContentPane(contentPane);

        // PANEL FORMULARIO con GridBagLayout para mejor control del espacio
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(new Color(245, 245, 245));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Datos del Producto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Labels y campos
        String[] labels = {"Código:", "Nombre:", "Categoría:", "Proveedor:", "Precio:", "Stock:"};
        JTextField[] textFields = new JTextField[6];

        txtCodigo = new JTextField(12);
        txtNombre = new JTextField(12);
        txtCategoria = new JTextField(12);
        txtProveedor = new JTextField(12);
        txtPrecio = new JTextField(12);
        txtStock = new JTextField(12);

        textFields[0] = txtCodigo;
        textFields[1] = txtNombre;
        textFields[2] = txtCategoria;
        textFields[3] = txtProveedor;
        textFields[4] = txtPrecio;
        textFields[5] = txtStock;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            panelFormulario.add(lbl, gbc);

            gbc.gridx = 1;
            textFields[i].setFont(txtFont);
            textFields[i].setBackground(Color.WHITE);
            textFields[i].setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
            panelFormulario.add(textFields[i], gbc);
        }

        contentPane.add(panelFormulario, BorderLayout.NORTH);

        // PANEL BOTONES Y BUSCAR
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBotones.setBackground(new Color(250, 250, 250));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        txtBuscar = new JTextField(20);

        // Botones con colores suaves y fuente legible
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        Color btnColor = new Color(33, 150, 243);
        Color btnTextColor = Color.WHITE;

        JButton[] buttons = {btnAgregar, btnEditar, btnEliminar, btnLimpiar};
        for (JButton b : buttons) {
            b.setFont(btnFont);
            b.setBackground(btnColor);
            b.setForeground(btnTextColor);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panelBotones.add(b);
        }

        // Buscar label
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(labelFont);
        panelBotones.add(lblBuscar);

        // Campo buscar estilo limpio
        txtBuscar.setFont(txtFont);
        txtBuscar.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        panelBotones.add(txtBuscar);

        contentPane.add(panelBotones, BorderLayout.CENTER);

        // TABLA ESTILIZADA
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Código", "Nombre", "Categoría", "Proveedor", "Precio", "Stock"
        }, 0);
        tabla = new JTable(modeloTabla) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.setFont(txtFont);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(33, 150, 243));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(new Color(135, 206, 250));
        tabla.setSelectionForeground(Color.BLACK);

        // Filas alternas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Color fila alterna
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }

                // Resaltar stock bajo
                int stock = Integer.parseInt(table.getValueAt(row, 6).toString());
                if (stock < 5) {
                    c.setBackground(isSelected ? new Color(255, 182, 193) : new Color(255, 240, 245)); // rosa suave
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        // EVENTOS

        btnAgregar.addActionListener(e -> {
            try {
                Producto p = leerFormulario();
                try {
					if (dao.insertar(p)) {
					    JOptionPane.showMessageDialog(this, "Producto agregado");
					    limpiar();
					    cargarTabla();
					}
				} catch (HeadlessException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                try {
                    int id = (int) modeloTabla.getValueAt(fila, 0);
                    Producto p = leerFormulario();
                    p.setId(id);
                    try {
						if (dao.actualizar(p)) {
						    JOptionPane.showMessageDialog(this, "Producto actualizado");
						    limpiar();
						    cargarTabla();
						}
					} catch (HeadlessException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para editar");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
						if (dao.eliminar(id)) {
						    JOptionPane.showMessageDialog(this, "Producto eliminado");
						    limpiar();
						    cargarTabla();
						}
					} catch (HeadlessException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar");
            }
        });

        btnLimpiar.addActionListener(e -> limpiar());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtCodigo.setText(tabla.getValueAt(fila, 1).toString());
                    txtNombre.setText(tabla.getValueAt(fila, 2).toString());
                    txtCategoria.setText(tabla.getValueAt(fila, 3).toString());
                    txtProveedor.setText(tabla.getValueAt(fila, 4).toString());
                    txtPrecio.setText(tabla.getValueAt(fila, 5).toString());
                    txtStock.setText(tabla.getValueAt(fila, 6).toString());
                }
            }
        });

        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
					cargarTabla(txtBuscar.getText());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        cargarTabla();
    }

    private Producto leerFormulario() {
        Producto p = new Producto();
        p.setCodigo(txtCodigo.getText().trim());
        p.setNombre(txtNombre.getText().trim());
        p.setCategoria(txtCategoria.getText().trim());
        p.setProveedor(txtProveedor.getText().trim());
        p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
        p.setStock(Integer.parseInt(txtStock.getText().trim()));
        return p;
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtProveedor.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtBuscar.setText("");
        tabla.clearSelection();
    }

    private void cargarTabla() throws ClassNotFoundException {
        cargarTabla("");
    }

    private void cargarTabla(String filtro) throws ClassNotFoundException {
        modeloTabla.setRowCount(0);
        List<Producto> lista = dao.listar();
        for (Producto p : lista) {
            if (filtro.isEmpty() ||
                p.getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                p.getCategoria().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(),
                    p.getCategoria(), p.getProveedor(),
                    p.getPrecio(), p.getStock()
                });
            }
        }
    }
}

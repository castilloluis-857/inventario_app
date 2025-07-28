package main;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean insertar(Producto producto) throws ClassNotFoundException {
        String sql = "INSERT INTO productos(codigo, nombre, categoria, proveedor, precio, stock) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getCategoria());
            stmt.setString(4, producto.getProveedor());
            stmt.setDouble(5, producto.getPrecio());
            stmt.setInt(6, producto.getStock());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listar() throws ClassNotFoundException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = Conexion.abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setCategoria(rs.getString("categoria"));
                p.setProveedor(rs.getString("proveedor"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    // Puedes agregar luego métodos: actualizar(), eliminar(), buscarPorNombre(), etc.

    public boolean actualizar(Producto producto) throws ClassNotFoundException {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, proveedor=?, precio=?, stock=? WHERE id=?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getCategoria());
            stmt.setString(4, producto.getProveedor());
            stmt.setDouble(5, producto.getPrecio());
            stmt.setInt(6, producto.getStock());
            stmt.setInt(7, producto.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) throws ClassNotFoundException {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection conn = Conexion.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    
}

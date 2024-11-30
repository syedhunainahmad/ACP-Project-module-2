import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Store {
    private String name;
    private String id;
    private String price;
    private String status;
    private List<Product> inventory;

    // Database credentials
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Update with your DB URL
    private static final String USER = "system"; // Update with your DB username
    private static final String PASSWORD = "hr"; // Update with your DB password

    public Store(String name, String id, String price, String status) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.status = status;
        this.inventory = new ArrayList<>();
    }

    Store() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public List<Product> getInventory() {
        return inventory;
    }

    public void addProduct(Product product) {
        inventory.add(product);
    }

    public void removeProduct(Product product) {
        inventory.remove(product);
    }

    // Method to save the inventory to the database
    public void saveInventoryToDatabase() {
        String insertSQL = "INSERT INTO products (name, id, price, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(insertSQL)) {

            for (Product product : inventory) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getId());
                ps.setString(3, product.getPrice());
                ps.setString(4, product.getStatus());
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Inventory saved successfully to the database.");
        } catch (SQLException e) {
            System.err.println("Error saving inventory to database: " + e.getMessage());
        }
    }

    // Method to load the inventory from the database
    public void loadInventoryFromDatabase() {
        String selectSQL = "SELECT name, id, price, status FROM products";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String id = rs.getString("id");
                String price = rs.getString("price");
                String status = rs.getString("status");
                Product product = new Product(name, id, price, status);
                addProduct(product);
            }
            System.out.println("Inventory loaded successfully from the database.");
        } catch (SQLException e) {
            System.err.println("Error loading inventory from database: " + e.getMessage());
        }
    }

    public void displaySelectedRowData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

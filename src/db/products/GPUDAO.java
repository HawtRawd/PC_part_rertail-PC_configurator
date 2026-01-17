package db.products;
import components.CPU;
import db.ResultSetMapper;
import components.GPU;
import db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GPUDAO {
    private static Connection getConnection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public GPU getGpuById(int id) throws SQLException {
        List<GPU> gpus = getAllGpus();
        return gpus.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public static List<GPU> getAllGpus() throws SQLException {
        List<GPU> gpus = new ArrayList<>();

        String sql = "SELECT p.*, s.*, m.name AS manufacturer_name, c.category AS category_name " +
                "FROM products p " +
                "JOIN gpu_specs s ON p.id = s.product_id " + // <--- Changed table
                "JOIN manufacturers m ON p.manufacturer = m.id " +
                "JOIN product_categories c ON p.category = c.id";
        ResultSetMapper<GPU> mapper = new ResultSetMapper<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return mapper.mapResultSet(rs, GPU.class);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
package db;

import components.*;
import db.products.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuildDAO {
    private void setIntOrNull(PreparedStatement stmt, int index, Product product) throws SQLException {
        if (product == null) {
            stmt.setNull(index, Types.INTEGER);
        } else {
            stmt.setInt(index, product.getId());
        }
    }

    public List<Build> getAllBuildsForUser(int userId) {
        List<Build> builds = new ArrayList<>();
        String sql = "SELECT id FROM pc_build WHERE user_id = ? ORDER_BY id DESC";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int buildId = rs.getInt("id");
                Build b = getBuildById(buildId);
                if(b != null){
                    builds.add(b);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return builds;
    }

    public Build getBuildById(int buildId) {
        Build build = null;
        String sql = "SELECT * FROM pc_build WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            build = new Build();
            build.setId(rs.getInt("id"));
            build.setUser_id(rs.getInt("user_id"));
            build.setBuildName(rs.getString("build_name"));
            if (rs.getInt("cpu_id") != 0)
                build.setCpu(new CPUDAO().getCpuById(rs.getInt("cpu_id")));
            if (rs.getInt("motherboard_id") != 0)
                build.setMobo(new MoboDAO().getMoboById(rs.getInt("motherboard_id")));
            if (rs.getInt("psu_id") != 0)
                build.setPsu(new PSUDAO().getPsuById(rs.getInt("psu_id")));
            if (rs.getInt("case_id") != 0)
                build.setPcCase(new CaseDAO().getCaseById(rs.getInt("case_id")));
            build.setGpus(getGpusForBuild(buildId));
            build.setRams(getRamForBuild(buildId));
            build.setStorages(getStorageForBuild(buildId));
            build.setAccessories(getAccsForBuild(buildId));
            build.calculateTotal();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return build;
    }

    public boolean saveBuild(Build build) {
        double total = build.calculateTotal();

        String sqlBuild = "INSERT INTO pc_build (user_id, build_name, total_price, " +
                "cpu_id, motherboard_id, psu_id, case_id, cooler_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                int buildId = -1;
                try (PreparedStatement stmt = conn.prepareStatement(sqlBuild)) {
                    stmt.setInt(1, build.getUser_id());
                    stmt.setString(2, build.getBuildName());
                    stmt.setDouble(3, total);

                    setIntOrNull(stmt, 4, build.getCpu());
                    setIntOrNull(stmt, 5, build.getMobo());
                    setIntOrNull(stmt, 6, build.getPsu());
                    setIntOrNull(stmt, 7, build.getPcCase());
                    setIntOrNull(stmt, 8, build.getCooler());

                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) buildId = rs.getInt("id");
                }

                if (build.getGpus() != null && !build.getGpus().isEmpty()) {
                    String sqlGpu = "INSERT INTO pc_build_gpu (build_id, gpu_id) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlGpu)) {
                        for (GPU gpu : build.getGpus()) {
                            stmt.setInt(1, buildId);
                            stmt.setInt(2, gpu.getId());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                if (build.getRams() != null && !build.getRams().isEmpty()) {
                    String sqlRam = "INSERT INTO pc_build_ram (build_id, ram_id) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlRam)) {
                        for (RAM ram : build.getRams()) {
                            stmt.setInt(1, buildId);
                            stmt.setInt(2, ram.getId());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                if (build.getStorages() != null && !build.getStorages().isEmpty()) {
                    String sqlStorage = "INSERT INTO pc_build_storage (build_id, storage_id) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlStorage)) {
                        for (Storage drive : build.getStorages()) {
                            stmt.setInt(1, buildId);
                            stmt.setInt(2, drive.getId());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                if(build.getAccessories() != null && !build.getAccessories().isEmpty()){
                    String sqlAccesories = "INSERT INTO pc_build_accessories (build_id, accessories_id) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlAccesories)) {
                        for(Accessory accessory : build.getAccessories()) {
                            stmt.setInt(1, buildId);
                            stmt.setInt(2, accessory.getId());
                            stmt.addBatch();
                        }
                        stmt.executeBatch();
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private List<GPU> getGpusForBuild(int buildId) {
        List<GPU> list = new ArrayList<>();
        String sql = "SELECT gpu_id FROM pc_build_gpu WHERE build_id = ?";
        GPUDAO dao = new GPUDAO();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GPU part = dao.getGpuById(rs.getInt("gpu_id"));
                if (part != null) list.add(part);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private List<RAM> getRamForBuild(int buildId) {
        List<RAM> list = new ArrayList<>();
        String sql = "SELECT ram_id FROM pc_build_ram WHERE build_id = ?";
        RAMDAO dao = new RAMDAO();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RAM part = dao.getRamById(rs.getInt("ram_id"));
                if (part != null) list.add(part);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private List<Storage> getStorageForBuild(int buildId) {
        List<Storage> list = new ArrayList<>();
        String sql = "SELECT storage_id FROM pc_build_storage WHERE build_id = ?";
        StorageDAO dao = new StorageDAO();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Storage part = dao.getStorageById(rs.getInt("storage_id"));
                if (part != null) list.add(part);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    private List<Accessory> getAccsForBuild(int buildId) {
        List<Accessory> list = new ArrayList<>();
        String sql = "SELECT accessory_id FROM pc_build_ram WHERE build_id = ?";
        AccessoryDAO dao = new AccessoryDAO();

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Accessory part = dao.getAccById(rs.getInt("ram_id"));
                if (part != null) list.add(part);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}

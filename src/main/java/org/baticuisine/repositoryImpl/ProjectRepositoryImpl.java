package org.baticuisine.repositoryImpl;

import org.baticuisine.Status;
import org.baticuisine.database.DatabaseConnection;
import org.baticuisine.entities.*;
import org.baticuisine.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectRepositoryImpl implements ProjectRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryImpl.class);
    private Connection conn;

    public ProjectRepositoryImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO project (project_name, profit_margin, total_cost, client_id) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setDouble(2, project.getProfitMargin());
            pstmt.setDouble(3, project.getTotalCost());
            pstmt.setInt(4, project.getClient().getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getInt(1));
                        logger.info("Project added with ID: {}", project.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding project: {}", e.getMessage());
        }
    }

    @Override
    public List<Project> getAllProjects() {
        String sql = "SELECT \n" +
                "    p.id AS project_id, \n" +
                "    p.project_name, \n" +
                "    p.profit_margin, \n" +
                "    p.total_cost, \n" +
                "    p.status, \n" +
                "    c.id AS client_id, \n" +
                "    c.name AS client_name, \n" +
                "    c.address AS client_address, \n" +
                "    c.phone_number AS client_phone_number, \n" +
                "    c.is_professional AS client_isProfessional, \n" +
                "    c.discount AS client_discount, \n" +
                "    'Material' AS component_type, \n" +
                "    m.id AS component_id, \n" +
                "    m.name AS component_name, \n" +
                "    m.tax_rate, \n" +
                "    m.unit_cost AS cost, \n" +
                "    m.quantity AS quantity, \n" +
                "    m.transport_cost, \n" +
                "    m.quality_coefficient, \n" +
                "    NULL AS worker_productivity\n" +
                "FROM \n" +
                "    project p\n" +
                "JOIN \n" +
                "    client c ON p.client_id = c.id\n" +
                "LEFT JOIN \n" +
                "    material m ON p.id = m.project_id\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT \n" +
                "    p.id AS project_id, \n" +
                "    p.project_name, \n" +
                "    p.profit_margin, \n" +
                "    p.total_cost, \n" +
                "    p.status, \n" +
                "    c.id AS client_id, \n" +
                "    c.name AS client_name, \n" +
                "    c.address AS client_address, \n" +
                "    c.phone_number AS client_phone_number, \n" +
                "    c.is_professional AS client_isProfessional, \n" +
                "    c.discount AS client_discount, \n" +
                "    'Labor' AS component_type, \n" +
                "    l.id AS component_id, \n" +
                "    l.name AS component_name, \n" +
                "    l.tax_rate, \n" +
                "    l.hourly_rate AS cost, \n" +
                "    l.work_hours AS quantity, \n" +
                "    NULL AS transport_cost, \n" +
                "    NULL AS quality_coefficient, \n" +
                "    l.worker_productivity\n" +
                "FROM \n" +
                "    project p\n" +
                "JOIN \n" +
                "    client c ON p.client_id = c.id\n" +
                "LEFT JOIN \n" +
                "    labor l ON p.id = l.project_id;\n";

        Map<Integer, Project> projectMap = new HashMap<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("project_id");
                Project project = projectMap.get(projectId);
                if (project == null) {
                    project = new Project();
                    project.setId(projectId);
                    project.setProjectName(rs.getString("project_name"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setStatus(Status.valueOf(rs.getString("status")));

                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setName(rs.getString("client_name"));
                    client.setAddress(rs.getString("client_address"));
                    client.setPhoneNumber(rs.getString("client_phone_number"));
                    client.setProfessional(rs.getBoolean("client_isProfessional"));
                    client.setDiscount(rs.getDouble("client_discount"));
                    project.setClient(client);

                    projectMap.put(projectId, project);
                }

                int componentId = rs.getInt("component_id");
                if (!rs.wasNull()) {
                    Component component;
                    String componentType = rs.getString("component_type");

                    if ("Material".equals(componentType)) {
                        component = new Material();
                        ((Material) component).setUnitCost(rs.getDouble("cost"));
                        ((Material) component).setQuantity(rs.getDouble("quantity"));
                        ((Material) component).setTransportCost(rs.getDouble("transport_cost"));
                        ((Material) component).setQualityCoefficient(rs.getDouble("quality_coefficient"));
                    } else if ("Labor".equals(componentType)) {
                        component = new Labor();
                        ((Labor) component).setHourlyRate(rs.getDouble("cost"));
                        ((Labor) component).setWorkHours(rs.getDouble("quantity"));
                        ((Labor) component).setWorkerProductivity(rs.getDouble("worker_productivity"));
                    } else {
                        continue;
                    }

                    component.setId(componentId);
                    component.setName(rs.getString("component_name"));
                    component.setTaxRate(rs.getDouble("tax_rate"));

                    if (project.getComponents() == null) {
                        project.setComponents(new ArrayList<>());
                    }
                    project.getComponents().add(component);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching projects: {}", e.getMessage());
        }

        return new ArrayList<>(projectMap.values());
    }

    @Override
    public Project getProjectById(String projectName) {
        String sql = "SELECT \n" +
                "    p.id AS project_id, \n" +
                "    p.project_name, \n" +
                "    p.profit_margin, \n" +
                "    p.total_cost, \n" +
                "    p.status, \n" +
                "    c.id AS client_id, \n" +
                "    c.name AS client_name, \n" +
                "    c.address AS client_address, \n" +
                "    c.phone_number AS client_phone_number, \n" +
                "    c.is_professional AS client_isProfessional, \n" +
                "    c.discount AS client_discount, \n" +
                "    'Material' AS component_type, \n" +
                "    m.id AS component_id, \n" +
                "    m.name AS component_name, \n" +
                "    m.tax_rate, \n" +
                "    m.unit_cost AS cost, \n" +
                "    m.quantity AS quantity, \n" +
                "    m.transport_cost, \n" +
                "    m.quality_coefficient, \n" +
                "    NULL AS worker_productivity\n" +
                "FROM \n" +
                "    project p\n" +
                "JOIN \n" +
                "    client c ON p.client_id = c.id\n" +
                "LEFT JOIN \n" +
                "    material m ON p.id = m.project_id\n" +
                "WHERE p.project_name = ?\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT \n" +
                "    p.id AS project_id, \n" +
                "    p.project_name, \n" +
                "    p.profit_margin, \n" +
                "    p.total_cost, \n" +
                "    p.status, \n" +
                "    c.id AS client_id, \n" +
                "    c.name AS client_name, \n" +
                "    c.address AS client_address, \n" +
                "    c.phone_number AS client_phone_number, \n" +
                "    c.is_professional AS client_isProfessional, \n" +
                "    c.discount AS client_discount, \n" +
                "    'Labor' AS component_type, \n" +
                "    l.id AS component_id, \n" +
                "    l.name AS component_name, \n" +
                "    l.tax_rate, \n" +
                "    l.hourly_rate AS cost, \n" +
                "    l.work_hours AS quantity, \n" +
                "    NULL AS transport_cost, \n" +
                "    NULL AS quality_coefficient, \n" +
                "    l.worker_productivity\n" +
                "FROM \n" +
                "    project p\n" +
                "JOIN \n" +
                "    client c ON p.client_id = c.id\n" +
                "LEFT JOIN \n" +
                "    labor l ON p.id = l.project_id\n" +
                "WHERE p.project_name = ?;\n";

        Project project = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projectName);
            pstmt.setString(2, projectName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (project == null) {
                    project = new Project();
                    project.setId(rs.getInt("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setProfitMargin(rs.getDouble("profit_margin"));
                    project.setTotalCost(rs.getDouble("total_cost"));
                    project.setStatus(Status.valueOf(rs.getString("status")));

                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));
                    client.setName(rs.getString("client_name"));
                    client.setAddress(rs.getString("client_address"));
                    client.setPhoneNumber(rs.getString("client_phone_number"));
                    client.setProfessional(rs.getBoolean("client_isProfessional"));
                    client.setDiscount(rs.getDouble("client_discount"));
                    project.setClient(client);
                }

                int componentId = rs.getInt("component_id");
                if (!rs.wasNull()) {
                    Component component;
                    String componentType = rs.getString("component_type");

                    if ("Material".equals(componentType)) {
                        component = new Material();
                        ((Material) component).setUnitCost(rs.getDouble("cost"));
                        ((Material) component).setQuantity(rs.getDouble("quantity"));
                        ((Material) component).setTransportCost(rs.getDouble("transport_cost"));
                        ((Material) component).setQualityCoefficient(rs.getDouble("quality_coefficient"));
                    } else if ("Labor".equals(componentType)) {
                        component = new Labor();
                        ((Labor) component).setHourlyRate(rs.getDouble("cost"));
                        ((Labor) component).setWorkHours(rs.getDouble("quantity"));
                        ((Labor) component).setWorkerProductivity(rs.getDouble("worker_productivity"));
                    } else {
                        continue;
                    }

                    component.setId(componentId);
                    component.setName(rs.getString("component_name"));
                    component.setTaxRate(rs.getDouble("tax_rate"));

                    if (project.getComponents() == null) {
                        project.setComponents(new ArrayList<>());
                    }
                    project.getComponents().add(component);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching project by name: {}", e.getMessage());
        }

        return project;
    }

    @Override
    public void updateProjectProfitMarginAndTotalCost(int projectId, double profitMargin, double totalCost) {
        String sql = "UPDATE project SET profit_margin = ?, total_cost = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, profitMargin);
            stmt.setDouble(2, totalCost);
            stmt.setInt(3, projectId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Project with ID {} updated successfully.", projectId);
            } else {
                logger.warn("No project found with ID {}.", projectId);
            }
        } catch (SQLException e) {
            logger.error("Error updating project: {}", e.getMessage());
        }
    }
}

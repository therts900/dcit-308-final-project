package com.ug.smartcampus.ui;

import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.SampleDataLoader;
import com.ug.smartcampus.database.dao.BuildingDao;
import com.ug.smartcampus.database.dao.RequestDao;
import com.ug.smartcampus.database.dao.ResourceDao;
import com.ug.smartcampus.datastructures.graph.Graph;
import com.ug.smartcampus.model.Request;
import com.ug.smartcampus.model.Resource;
import com.ug.smartcampus.service.PersistenceService;
import com.ug.smartcampus.service.ResourceAllocationService;
import com.ug.smartcampus.service.RoutingService;
import com.ug.smartcampus.service.SchedulingService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/** A guided desktop dashboard for demonstrating the project's complete workflow. */
public final class SmartCampusDashboard {
    private static final Color NAVY = new Color(18, 44, 71);
    private static final Color TEAL = new Color(22, 142, 130);
    private static final Color BACKGROUND = new Color(244, 247, 250);
    private static final Font TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 28);
    private static final Font SUBTITLE = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    private SmartCampusDashboard() { }

    public static void showDashboard() {
        SwingUtilities.invokeLater(() -> new SmartCampusDashboard().createAndShow());
    }

    private void createAndShow() {
        JFrame frame = new JFrame("Smart Campus | Service Operations Optimizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1080, 700));
        frame.setLayout(new BorderLayout());
        frame.add(header(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        tabs.addTab("Overview", overview(tabs));
        tabs.addTab("1. Data setup", dataSetup());
        tabs.addTab("2. Schedule", schedule());
        tabs.addTab("3. Allocate", allocation());
        tabs.addTab("4. Route", routing());
        tabs.addTab("Reports & experiments", reports());
        frame.add(tabs, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel header() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(NAVY);
        panel.setBorder(new EmptyBorder(20, 28, 18, 28));
        JLabel title = new JLabel("SMART CAMPUS");
        title.setForeground(Color.WHITE);
        title.setFont(TITLE);
        JLabel context = new JLabel("Service Operations Optimizer  •  DCIT 308");
        context.setForeground(new Color(195, 220, 227));
        context.setFont(SUBTITLE);
        panel.add(title, BorderLayout.NORTH);
        panel.add(context, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel overview(JTabbedPane tabs) {
        JPanel panel = page();
        JLabel heading = heading("A guided operational workflow", "Move from trusted campus data to defensible scheduling decisions.");
        panel.add(heading, BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(2, 2, 16, 16));
        cards.setOpaque(false);
        cards.add(workflowCard("1", "Load campus data", "Import the supplied CSV records into SQLite. Buildings are imported first to preserve relationships.", () -> tabs.setSelectedIndex(1)));
        cards.add(workflowCard("2", "Prioritize requests", "Use the custom priority queue to plan urgent requests before lower-severity work.", () -> tabs.setSelectedIndex(2)));
        cards.add(workflowCard("3", "Allocate teams", "Match each request to an available resource at the required campus building.", () -> tabs.setSelectedIndex(3)));
        cards.add(workflowCard("4", "Optimize routes", "Inspect a Dijkstra shortest path across a demonstration campus network.", () -> tabs.setSelectedIndex(4)));
        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    private JPanel dataSetup() {
        JPanel panel = page();
        panel.add(heading("Step 1 — Prepare persistent data", "Load the project CSV files into the local SQLite database. This action is safe to repeat."), BorderLayout.NORTH);
        JTextArea information = text("The loader imports:\n\n• buildings.csv — campus locations\n• resources.csv — teams and vehicles\n• maintenance_requests.csv — requests with status, priority, time, and department\n\nAfter import, the DAO layer retrieves these records for the scheduling and allocation screens.");
        JButton load = primaryButton("Load / refresh sample data");
        JLabel status = new JLabel("No database action has been run in this session.");
        status.setFont(SUBTITLE);
        load.addActionListener(event -> {
            try (DatabaseManager database = new DatabaseManager()) {
                database.initializeSchema(Path.of("database/schema.sql"));
                SampleDataLoader.load(database, Path.of("database/data"));
                status.setText("Ready: " + new BuildingDao(database).findAll().size() + " buildings, "
                        + new ResourceDao(database).findAll().size() + " resources, " + new RequestDao(database).findAll().size() + " requests.");
            } catch (Exception exception) { showError(exception); }
        });
        JPanel body = new JPanel(new BorderLayout(20, 20));
        body.setOpaque(false);
        body.add(card(information), BorderLayout.CENTER);
        JPanel action = new JPanel(new GridLayout(0, 1, 0, 12));
        action.setOpaque(false);
        action.add(load);
        action.add(status);
        body.add(action, BorderLayout.SOUTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel schedule() {
        JPanel panel = page();
        panel.add(heading("Step 2 — Prioritize service work", "The schedule uses severity first, then earliest request time as a fair tie-breaker."), BorderLayout.NORTH);
        DefaultTableModel model = tableModel("Order", "Request", "Priority", "Building", "Status", "Requested");
        JButton run = primaryButton("Generate priority schedule from database");
        run.addActionListener(event -> {
            try {
                List<Request> requests = persistence().loadRequests();
                SchedulingService service = new SchedulingService();
                requests.forEach(service::add);
                model.setRowCount(0);
                int order = 1;
                for (Request request : service.plan()) model.addRow(new Object[] {order++, request.getDescription(), request.getPriority(), request.getBuildingId(), request.getStatus(), request.getRequestedTime()});
                if (requests.isEmpty()) showInfo("No requests found. Open Data setup and load the sample dataset first.");
            } catch (Exception exception) { showError(exception); }
        });
        panel.add(actionTable(run, model), BorderLayout.CENTER);
        return panel;
    }

    private JPanel allocation() {
        JPanel panel = page();
        panel.add(heading("Step 3 — Match available campus resources", "Greedy allocation handles high-priority requests first and assigns a resource located at the request building."), BorderLayout.NORTH);
        DefaultTableModel model = tableModel("Request", "Priority", "Building", "Assigned resource", "Capability");
        JButton run = primaryButton("Run resource allocation");
        run.addActionListener(event -> {
            try {
                PersistenceService persistence = persistence();
                Map<Request, Resource> assignments = new ResourceAllocationService().allocate(persistence.loadRequests(), persistence.loadResources());
                model.setRowCount(0);
                assignments.forEach((request, resource) -> model.addRow(new Object[] {request.getDescription(), request.getPriority(), request.getBuildingId(), resource.getName(), resource.getCapabilities()}));
                if (assignments.isEmpty()) showInfo("No matching assignments. Load sample data first, then confirm building IDs match.");
            } catch (Exception exception) { showError(exception); }
        });
        panel.add(actionTable(run, model), BorderLayout.CENTER);
        return panel;
    }

    private JPanel routing() {
        JPanel panel = page();
        panel.add(heading("Step 4 — Explore shortest routes", "A compact campus graph demonstrates Dijkstra's weighted shortest-path algorithm."), BorderLayout.NORTH);
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        JComboBox<String> from = new JComboBox<>(new String[] {"Library", "Computing", "Great Hall", "Engineering", "Commonwealth Hall"});
        JComboBox<String> to = new JComboBox<>(new String[] {"Library", "Computing", "Great Hall", "Engineering", "Commonwealth Hall"});
        JButton route = primaryButton("Find optimized route");
        controls.add(new JLabel("From:")); controls.add(from); controls.add(new JLabel("To:")); controls.add(to); controls.add(route);
        JTextArea result = text("Choose a start and destination, then calculate the shortest route.");
        route.addActionListener(event -> {
            RoutingService<String> service = new RoutingService<>(demoGraph());
            String start = (String) from.getSelectedItem(); String destination = (String) to.getSelectedItem();
            List<String> path = service.route(start, destination);
            result.setText("Recommended route\n\n" + String.join("  →  ", path) + "\n\nTotal weighted distance: " + String.format("%.1f", service.distance(start, destination)) + " units\n\nDijkstra processes the lowest currently known distance first, making it appropriate for non-negative campus road distances.");
        });
        JPanel body = new JPanel(new BorderLayout(16, 16)); body.setOpaque(false); body.add(controls, BorderLayout.NORTH); body.add(card(result), BorderLayout.CENTER); panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel reports() {
        JPanel panel = page();
        panel.add(heading("Reports, quality, and experiments", "Use these final checks to support a credible demonstration and final submission."), BorderLayout.NORTH);
        JTextArea report = text("Quality gate\n• mvn verify compiles, tests, checks style, packages the JAR, and generates JaCoCo coverage.\n• GitHub Actions repeats this quality gate and retains reports as artifacts.\n\nPerformance experiments\n• SortingBenchmark prints median timing for increasing input sizes (target: O(n log n)).\n• GraphBenchmark measures Dijkstra on sparse connected graphs (target: O(E + V log V)).\n\nSuggested demonstration\n1. Load sample data.\n2. Generate the priority schedule.\n3. Run resource allocation.\n4. Find a route.\n5. Run mvn verify and present the coverage report in target/site/jacoco/.");
        panel.add(card(report), BorderLayout.CENTER);
        return panel;
    }

    private PersistenceService persistence() { DatabaseManager database = new DatabaseManager(); return new PersistenceService(new RequestDao(database), new ResourceDao(database)); }
    private Graph<String> demoGraph() { Graph<String> graph = new Graph<>(); graph.addEdge("Library", "Computing", 2); graph.addEdge("Library", "Great Hall", 4); graph.addEdge("Computing", "Engineering", 3); graph.addEdge("Great Hall", "Commonwealth Hall", 2); graph.addEdge("Engineering", "Commonwealth Hall", 4); return graph; }
    private JPanel page() { JPanel panel = new JPanel(new BorderLayout(20, 20)); panel.setBackground(BACKGROUND); panel.setBorder(new EmptyBorder(26, 30, 30, 30)); return panel; }
    private JLabel heading(String title, String description) { JLabel label = new JLabel("<html><div style='font-size:22px;font-weight:bold;color:#122C47'>" + title + "</div><div style='padding-top:7px;color:#52616B'>" + description + "</div></html>"); label.setVerticalAlignment(SwingConstants.TOP); return label; }
    private JPanel workflowCard(String number, String title, String description, Runnable action) { JPanel panel = card(new JLabel("<html><div style='color:#168E82;font-size:28px;font-weight:bold'>" + number + "</div><br><div style='font-size:17px;font-weight:bold'>" + title + "</div><br><div style='color:#52616B'>" + description + "</div></html>")); panel.addMouseListener(new java.awt.event.MouseAdapter() { @Override public void mouseClicked(java.awt.event.MouseEvent event) { action.run(); } }); return panel; }
    private JPanel card(Component component) { JPanel panel = new JPanel(new BorderLayout()); panel.setBackground(Color.WHITE); panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 228, 234)), new EmptyBorder(20, 20, 20, 20))); panel.add(component); return panel; }
    private JTextArea text(String value) { JTextArea area = new JTextArea(value); area.setEditable(false); area.setOpaque(false); area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15)); area.setLineWrap(true); area.setWrapStyleWord(true); return area; }
    private JButton primaryButton(String text) { JButton button = new JButton(text); button.setBackground(TEAL); button.setForeground(Color.WHITE); button.setFocusPainted(false); button.setBorder(new EmptyBorder(11, 16, 11, 16)); button.setMargin(new Insets(10, 16, 10, 16)); return button; }
    private DefaultTableModel tableModel(String... columns) { return new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } }; }
    private JPanel actionTable(JButton action, DefaultTableModel model) { JPanel content = new JPanel(new BorderLayout(12, 12)); content.setOpaque(false); content.add(action, BorderLayout.NORTH); JTable table = new JTable(model); table.setRowHeight(28); table.setFillsViewportHeight(true); content.add(new JScrollPane(table), BorderLayout.CENTER); return content; }
    private void showError(Exception exception) { JOptionPane.showMessageDialog(null, exception.getMessage(), "Action could not be completed", JOptionPane.ERROR_MESSAGE); }
    private void showInfo(String message) { JOptionPane.showMessageDialog(null, message, "Smart Campus", JOptionPane.INFORMATION_MESSAGE); }
}

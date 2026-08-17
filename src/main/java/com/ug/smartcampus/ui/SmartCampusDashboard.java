package com.ug.smartcampus.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.SampleDataLoader;
import com.ug.smartcampus.database.dao.CampusResourceDao;
import com.ug.smartcampus.database.dao.LocationDao;
import com.ug.smartcampus.database.dao.RoadDao;
import com.ug.smartcampus.database.dao.ServiceRequestDao;
import com.ug.smartcampus.datastructures.graph.Graph;
import com.ug.smartcampus.model.CampusResource;
import com.ug.smartcampus.model.Location;
import com.ug.smartcampus.model.Road;
import com.ug.smartcampus.model.ServiceRequest;
import com.ug.smartcampus.service.RoutingService;
import com.ug.smartcampus.service.CampusOperationsService;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 * Sidebar-and-cards dashboard, backed by the real campus dataset via the DAO
 * layer.
 */
public final class SmartCampusDashboard {
    private static final Color TEAL_ACCENT = new Color(32, 139, 143);
    private static final Color TEXT_DARK = new Color(45, 74, 77);
    private static final Color TEXT_MUTED = new Color(130, 150, 150);

    private SmartCampusDashboard() {
    }

    /** Entry point used by SmartCampusApplication — keep this signature stable. */
    public static void showDashboard() {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 999);
            UIManager.put("Component.arc", 20);
            UIManager.put("Component.focusWidth", 0);
            build().setVisible(true);
        });
    }

    private static JFrame build() {
        JFrame frame = new JFrame("Smart Campus | Service Operations Optimizer");
        frame.setSize(1280, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel rootGradientPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(225, 240, 238);
                Color color2 = new Color(235, 230, 245);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        frame.setContentPane(rootGradientPanel);
        CardLayout pageLayout = new CardLayout();
        JPanel pages = createPages(frame, pageLayout);
        rootGradientPanel.add(createSidebar(pages, pageLayout), BorderLayout.WEST);
        rootGradientPanel.add(pages, BorderLayout.CENTER);
        return frame;
    }

    private static JPanel createSidebar(JPanel pages, CardLayout pageLayout) {
        JPanel sidebar = new JPanel(
                new MigLayout("wrap 1, fillx, insets 30 20 20 20", "[fill]", "[]40[]10[]10[]10[]10[]"));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(240, 800));
        JLabel brandLabel = new JLabel("SmartCampus");
        brandLabel.setForeground(TEXT_DARK);
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        sidebar.add(brandLabel);
        Map<String, JButton> navigation = new LinkedHashMap<>();
        for (String page : List.of("Dashboard", "Data Setup", "Schedule", "Allocate", "Route", "Reports")) {
            JButton button = createNavButton(page, "Dashboard".equals(page));
            navigation.put(page, button);
            sidebar.add(button);
        }
        navigation.forEach((page, button) -> button.addActionListener(event -> {
            pageLayout.show(pages, page);
            navigation.forEach((name, navButton) -> applyNavStyle(navButton, name.equals(page)));
        }));
        return sidebar;
    }

    private static JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        applyNavStyle(btn, isActive);
        return btn;
    }

    private static void applyNavStyle(JButton button, boolean isActive) {
        if (isActive) {
            button.setBackground(TEAL_ACCENT);
            button.setForeground(Color.WHITE);
            button.setContentAreaFilled(true);
            button.setBorderPainted(true);
            button.putClientProperty("JButton.buttonType", "roundRect");
        } else {
            button.setContentAreaFilled(false);
            button.setForeground(TEXT_MUTED);
            button.setBorderPainted(false);
            button.putClientProperty("JButton.buttonType", null);
        }
        button.repaint();
    }

    private static JPanel createPages(JFrame frame, CardLayout pageLayout) {
        JPanel pages = new JPanel(pageLayout);
        pages.setOpaque(false);
        pages.add(createMainContent(frame), "Dashboard");
        pages.add(createDataSetupPage(), "Data Setup");
        pages.add(createSchedulePage(), "Schedule");
        pages.add(createAllocationPage(), "Allocate");
        pages.add(createRoutePage(), "Route");
        pages.add(createReportsPage(), "Reports");
        return pages;
    }

    private static JPanel createPage(String title, String subtitle) {
        JPanel page = new JPanel(new MigLayout("wrap 1, insets 40 40 40 40, fillx", "[grow, fill]", "[]25[]"));
        page.setOpaque(false);
        JLabel pageTitle = new JLabel(title);
        pageTitle.setForeground(TEXT_DARK);
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        page.add(pageTitle);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        page.add(subtitleLabel, "gapy -20 10");
        return page;
    }

    private static JPanel createDataSetupPage() {
        JPanel page = createPage("Data Setup", "Load the supplied CSV data into the local campus database.");
        JLabel status = new JLabel("Data has not been loaded in this session.");
        JButton load = primaryButton("Load / refresh sample data");
        load.addActionListener(event -> {
            try (DatabaseManager database = new DatabaseManager()) {
                database.initializeSchema(Path.of("database/schema.sql"));
                SampleDataLoader.load(database, Path.of("database/data"));
                status.setText(new LocationDao(database).findAll().size() + " locations · "
                        + new RoadDao(database).findAll().size() + " roads · "
                        + new CampusResourceDao(database).findAll().size() + " resources · "
                        + new ServiceRequestDao(database).findAll().size() + " requests loaded.");
            } catch (Exception exception) { showError(exception); }
        });
        JPanel card = createWhiteCard("Campus data loader", "Import the repository CSV fixtures safely; existing rows are preserved.", "Ready to load");
        card.add(load, "gapy 18");
        card.add(status);
        page.add(card, "growx, h 220!");
        return page;
    }

    private static JPanel createSchedulePage() {
        JPanel page = createPage("Schedule", "Rank active requests by urgency, then by submitted time.");
        DefaultTableModel model = tableModel("Order", "Request", "Category", "Urgency", "From", "To", "Status");
        JButton run = primaryButton("Generate priority schedule");
        run.addActionListener(event -> {
            try (DatabaseManager database = new DatabaseManager()) {
                List<ServiceRequest> requests = new CampusOperationsService(database).prioritySchedule();
                model.setRowCount(0); int order = 1;
                for (ServiceRequest r : requests) model.addRow(new Object[] { order++, r.getId(), r.getCategory(), r.getUrgency(), r.getSourceLocationId(), r.getDestinationLocationId(), r.getStatus() });
            } catch (Exception exception) { showError(exception); }
        });
        page.add(createActionTable(run, model), "grow, push");
        return page;
    }

    private static JPanel createAllocationPage() {
        JPanel page = createPage("Allocate", "Assign available resources located at each request’s source location.");
        DefaultTableModel model = tableModel("Request", "Urgency", "Source", "Assigned resource", "Type", "Capacity");
        JButton run = primaryButton("Run resource allocation");
        run.addActionListener(event -> {
            try (DatabaseManager database = new DatabaseManager()) {
                model.setRowCount(0);
                for (CampusOperationsService.Allocation allocation : new CampusOperationsService(database).allocate()) {
                    ServiceRequest request = allocation.request();
                    CampusResource resource = allocation.resource();
                    if (resource == null) model.addRow(new Object[] { request.getId(), request.getUrgency(), request.getSourceLocationId(), "Unassigned", "—", "—" });
                    else model.addRow(new Object[] { request.getId(), request.getUrgency(), request.getSourceLocationId(), resource.getId(), resource.getResourceType(), resource.getCapacity() });
                }
            } catch (Exception exception) { showError(exception); }
        });
        page.add(createActionTable(run, model), "grow, push");
        return page;
    }

    private static JPanel createRoutePage() {
        JPanel page = createPage("Route", "Use Dijkstra’s algorithm to find the lowest travel-time path across campus roads.");
        JComboBox<LocationOption> from = new JComboBox<>(); JComboBox<LocationOption> to = new JComboBox<>();
        JTextArea result = new JTextArea("Load locations, choose an origin and destination, then find a route.");
        result.setEditable(false); result.setLineWrap(true); result.setWrapStyleWord(true); result.setBackground(Color.WHITE);
        JButton loadLocations = primaryButton("Load locations");
        loadLocations.addActionListener(event -> { try (DatabaseManager database = new DatabaseManager()) { List<Location> locations = new CampusOperationsService(database).locations(); from.removeAllItems(); to.removeAllItems(); for (Location location : locations) { LocationOption option = new LocationOption(location.getId(), location.getName()); from.addItem(option); to.addItem(option); } if (to.getItemCount() > 1) to.setSelectedIndex(1); result.setText("Network ready: " + locations.size() + " locations loaded. Choose endpoints and calculate a Dijkstra route."); } catch (Exception exception) { showError(exception); } });
        JButton route = primaryButton("Find optimized route");
        route.addActionListener(event -> { if (from.getSelectedItem() == null || to.getSelectedItem() == null) { showInfo("Load locations and select both route endpoints first."); return; } try (DatabaseManager database = new DatabaseManager()) { String start = ((LocationOption) from.getSelectedItem()).id(); String end = ((LocationOption) to.getSelectedItem()).id(); CampusOperationsService.Route calculated = new CampusOperationsService(database).route(start, end); result.setText(calculated.path().isEmpty() || Double.isInfinite(calculated.weightedTravelMinutes()) ? "No connected route was found." : "Recommended route\n\n" + String.join("  →  ", calculated.path()) + "\n\nWeighted travel time: " + String.format("%.1f", calculated.weightedTravelMinutes()) + " minutes\n\nComputed with Dijkstra over the collected road network."); } catch (Exception exception) { showError(exception); } });
        JPanel controls = new JPanel(new MigLayout("insets 0", "[][]20[][]20[]", "[]")); controls.setOpaque(false); controls.add(loadLocations); controls.add(new JLabel("From:")); controls.add(from, "w 200!"); controls.add(new JLabel("To:")); controls.add(to, "w 200!"); controls.add(route);
        page.add(controls); page.add(new JScrollPane(result), "grow, push"); return page;
    }

    private static JPanel createReportsPage() {
        JPanel page = createPage("Reports", "Review the current operational dataset and availability.");
        JTextArea report = new JTextArea(); report.setEditable(false); report.setBackground(Color.WHITE);
        JButton refresh = primaryButton("Refresh operational report");
        refresh.addActionListener(event -> { try (DatabaseManager database = new DatabaseManager()) { CampusOperationsService operations = new CampusOperationsService(database); List<ServiceRequest> requests = operations.requests(); List<CampusResource> resources = operations.resources(); long active = operations.prioritySchedule().size(); long available = resources.stream().filter(r -> "AVAILABLE".equalsIgnoreCase(r.getAvailabilityStatus())).count(); long assigned = operations.allocate().stream().filter(a -> a.resource() != null).count(); report.setText("OPERATIONAL SUMMARY\n\nLocations: " + operations.locations().size() + "\nRoads: " + operations.roads().size() + "\nService requests: " + requests.size() + "\nActive requests: " + active + "\nResources: " + resources.size() + "\nAvailable resources: " + available + "\nGreedy allocations possible: " + assigned + "\n\nAlgorithms used\n• Binary-heap priority queue for request scheduling\n• Greedy matching for resource allocation\n• Dijkstra for weighted shortest paths"); } catch (Exception exception) { showError(exception); } });
        page.add(refresh); page.add(new JScrollPane(report), "grow, push"); return page;
    }

    private static DefaultTableModel tableModel(String... columns) { return new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } }; }
    private static JPanel createActionTable(JButton action, DefaultTableModel model) { JPanel panel = new JPanel(new BorderLayout(12, 12)); panel.setOpaque(false); panel.add(action, BorderLayout.NORTH); JTable table = new JTable(model); table.setRowHeight(28); table.setFillsViewportHeight(true); panel.add(new JScrollPane(table), BorderLayout.CENTER); return panel; }
    private static JButton primaryButton(String text) { JButton button = new JButton(text); button.setBackground(TEAL_ACCENT); button.setForeground(Color.WHITE); button.setFocusPainted(false); button.setBorder(new EmptyBorder(11, 16, 11, 16)); button.putClientProperty("JButton.buttonType", "roundRect"); return button; }
    private static void showError(Exception exception) { JOptionPane.showMessageDialog(null, exception.getMessage(), "Action could not be completed", JOptionPane.ERROR_MESSAGE); }
    private static void showInfo(String message) { JOptionPane.showMessageDialog(null, message, "Smart Campus", JOptionPane.INFORMATION_MESSAGE); }
    private record LocationOption(String id, String name) { @Override public String toString() { return name + " (" + id + ")"; } }

    private static JPanel createMainContent(JFrame frame) {
        DashboardStats stats = loadStats(frame);

        JPanel mainPanel = new JPanel(new MigLayout("wrap 3, insets 40 20 20 40, gap 20",
                "[grow 2, fill][grow 1, fill][grow 1, fill]",
                "[]20[150::, fill][grow, fill]"));
        mainPanel.setOpaque(false);

        JPanel headerPanel = new JPanel(new MigLayout("insets 0", "[grow][]", "[]"));
        headerPanel.setOpaque(false);
        JLabel greeting = new JLabel(
                "<html><span style='color:#829696; font-size:12px;'>Welcome back, Godwin\uD83D\uDC4B</span><br>"
                        + "<span style='color:#2D4A4D; font-size:28px; font-weight:bold;'>Dashboard</span></html>");
        headerPanel.add(greeting, "left");
        mainPanel.add(headerPanel, "span 3, wrap");

        mainPanel
                .add(createWhiteCard("Next Priority Request", stats.nextRequestHeadline(), stats.nextRequestSubtext()));
        mainPanel.add(createWhiteCard("Total Resources", String.valueOf(stats.totalResources()),
                stats.availableResources() + " available"));
        mainPanel.add(createWhiteCard("Campus Roads", String.valueOf(stats.roadCount()), "Mapped routes"));

        mainPanel.add(createTableWidget("Active Service Requests", stats.activeRequests()), "span 1 2, growy");
        mainPanel.add(createWhiteCard("Locations", String.valueOf(stats.locationCount()), "Nodes active"), "wrap");
        mainPanel.add(createCallToActionCard("Optimize Routes", "Run Dijkstra for next shift"), "span 2");
        return mainPanel;
    }

    private static JPanel createWhiteCard(String title, String mainValue, String subtext) {
        JPanel card = new JPanel(new MigLayout("wrap 1, insets 20", "[left]", "[][grow][]"));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc: 30");
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT_MUTED);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel lblValue = new JLabel(mainValue);
        lblValue.setForeground(TEXT_DARK);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        JLabel lblSub = new JLabel(subtext);
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        card.add(lblTitle);
        card.add(lblValue, "gapy 10 10");
        card.add(lblSub);
        return card;
    }

    private static JPanel createCallToActionCard(String title, String subtext) {
        JPanel card = new JPanel(new MigLayout("wrap 1, insets 20", "[left]", "[]10[]"));
        card.setBackground(TEAL_ACCENT);
        card.putClientProperty("FlatLaf.style", "arc: 30");
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblSub = new JLabel(subtext);
        lblSub.setForeground(new Color(255, 255, 255, 200));
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(lblTitle);
        card.add(lblSub);
        return card;
    }

    private static JPanel createTableWidget(String title, List<ServiceRequest> activeRequests) {
        JPanel widget = new JPanel(new BorderLayout());
        widget.setBackground(Color.WHITE);
        widget.putClientProperty("FlatLaf.style", "arc: 30");
        widget.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        widget.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Request", "Category", "Urgency", "From", "To", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (ServiceRequest request : activeRequests) {
            model.addRow(new Object[] { request.getId(), request.getCategory(), request.getUrgency(),
                    request.getSourceLocationId(), request.getDestinationLocationId(), request.getStatus() });
        }
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        if (activeRequests.isEmpty()) {
            JTextArea placeholder = new JTextArea(
                    "\n\n\n   No active service requests. Load the sample dataset first.");
            placeholder.setBackground(new Color(248, 250, 250));
            placeholder.setForeground(TEXT_MUTED);
            placeholder.setEditable(false);
            widget.add(placeholder, BorderLayout.CENTER);
        } else {
            widget.add(scroll, BorderLayout.CENTER);
        }
        return widget;
    }

    /**
     * Opens the database, loads the sample dataset if needed, and reads the numbers
     * the cards show.
     */
    private static DashboardStats loadStats(JFrame frame) {
        try (DatabaseManager database = new DatabaseManager()) {
            database.initializeSchema(Path.of("database/schema.sql"));
            SampleDataLoader.load(database, Path.of("database/data"));

            int locationCount = new LocationDao(database).findAll().size();
            int roadCount = new RoadDao(database).findAll().size();
            List<CampusResource> resources = new CampusResourceDao(database).findAll();
            long availableResources = resources.stream()
                    .filter(r -> "AVAILABLE".equalsIgnoreCase(r.getAvailabilityStatus())).count();

            List<ServiceRequest> allRequests = new ServiceRequestDao(database).findAll();
            List<ServiceRequest> active = allRequests.stream()
                    .filter(r -> !"DONE".equalsIgnoreCase(r.getStatus())
                            && !"CANCELLED".equalsIgnoreCase(r.getStatus()))
                    .sorted(Comparator.comparingInt(ServiceRequest::getUrgency).reversed()
                            .thenComparing(ServiceRequest::getTimeSubmitted))
                    .toList();

            Optional<ServiceRequest> top = active.stream().findFirst();
            String headline = top.map(r -> r.getCategory() + " at " + r.getSourceLocationId())
                    .orElse("No pending requests");
            String subtext;
            if (top.isPresent()) {
                Optional<CampusResource> match = resources.stream()
                        .filter(r -> "AVAILABLE".equalsIgnoreCase(r.getAvailabilityStatus())
                                && r.getHomeLocationId().equals(top.get().getSourceLocationId()))
                        .findFirst();
                subtext = match.map(r -> "Assigning to: " + r.getId() + " (" + r.getResourceType() + ")")
                        .orElse("No available resource at source location");
            } else {
                subtext = "All caught up";
            }

            List<ServiceRequest> topActive = active.size() > 8 ? active.subList(0, 8) : active;
            return new DashboardStats(locationCount, roadCount, resources.size(), (int) availableResources, headline,
                    subtext, topActive);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, exception.getMessage(), "Could not load campus data",
                    JOptionPane.ERROR_MESSAGE);
            return new DashboardStats(0, 0, 0, 0, "Data not loaded", "Check database/data CSVs and schema.sql",
                    new ArrayList<>());
        }
    }

    private record DashboardStats(int locationCount, int roadCount, int totalResources, int availableResources,
            String nextRequestHeadline, String nextRequestSubtext, List<ServiceRequest> activeRequests) {
    }
}

package com.ug.smartcampus.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.ug.smartcampus.database.DatabaseManager;
import com.ug.smartcampus.database.SampleDataLoader;
import com.ug.smartcampus.database.dao.CampusResourceDao;
import com.ug.smartcampus.database.dao.LocationDao;
import com.ug.smartcampus.database.dao.RoadDao;
import com.ug.smartcampus.database.dao.ServiceRequestDao;
import com.ug.smartcampus.model.CampusResource;
import com.ug.smartcampus.model.ServiceRequest;
import java.awt.BorderLayout;
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
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        rootGradientPanel.add(createSidebar(), BorderLayout.WEST);
        rootGradientPanel.add(createMainContent(frame), BorderLayout.CENTER);
        return frame;
    }

    private static JPanel createSidebar() {
        JPanel sidebar = new JPanel(
                new MigLayout("wrap 1, fillx, insets 30 20 20 20", "[fill]", "[]40[]10[]10[]10[]10[]"));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(240, 800));
        JLabel brandLabel = new JLabel("SmartCampus");
        brandLabel.setForeground(TEXT_DARK);
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        sidebar.add(brandLabel);
        // NOTE: nav buttons are visual-only for now — Data Setup / Schedule / Allocate
        // /
        // Route pages in this sidebar style aren't built yet. The working logic for
        // each
        // (real DAOs, priority scheduling, greedy allocation, Dijkstra routing) exists
        // in
        // git history from the tabbed layout this replaced — port it into new page
        // panels
        // wired to these nav buttons as a follow-up, rather than re-deriving it.
        sidebar.add(createNavButton("Dashboard", true));
        sidebar.add(createNavButton("Data Setup", false));
        sidebar.add(createNavButton("Schedule", false));
        sidebar.add(createNavButton("Allocate", false));
        sidebar.add(createNavButton("Route", false));
        sidebar.add(createNavButton("Reports", false));
        return sidebar;
    }

    private static JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        if (isActive) {
            btn.setBackground(TEAL_ACCENT);
            btn.setForeground(Color.WHITE);
            btn.putClientProperty("JButton.buttonType", "roundRect");
        } else {
            btn.setContentAreaFilled(false);
            btn.setForeground(TEXT_MUTED);
            btn.setBorderPainted(false);
        }
        return btn;
    }

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

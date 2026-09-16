/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.gui.main;

import static com.cburch.logisim.gui.Strings.S;

import com.cburch.logisim.circuit.CircuitChecker;
import com.cburch.logisim.circuit.CircuitChecker.Issue;
import com.cburch.logisim.util.LocaleListener;
import com.cburch.logisim.util.LocaleManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/** Bottom editor panel containing the results of the circuit checker. */
final class CircuitCheckPanel extends JPanel implements LocaleListener {
  private static final long serialVersionUID = 1L;

  private final Frame frame;
  private final IssueTableModel model = new IssueTableModel();
  private final JTable table = new JTable(model);
  private final JLabel title = new JLabel();
  private final JLabel summary = new JLabel();
  private final JButton close = new JButton();

  CircuitCheckPanel(Frame frame) {
    super(new BorderLayout(6, 4));
    this.frame = frame;

    setPreferredSize(new Dimension(100, 175));
    setBorder(BorderFactory.createEmptyBorder(4, 6, 6, 6));
    title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD));

    table.setFillsViewportHeight(true);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);
    table.getColumnModel().getColumn(0).setPreferredWidth(90);
    table.getColumnModel().getColumn(0).setMaxWidth(130);
    table.getColumnModel().getColumn(2).setPreferredWidth(100);
    table.getColumnModel().getColumn(2).setMaxWidth(150);
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
    table.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2 && table.getSelectedRow() >= 0) {
              final var modelRow = table.convertRowIndexToModel(table.getSelectedRow());
              frame.showCircuitCheckIssue(model.getIssue(modelRow));
            }
          }
        });

    final var footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    footer.add(summary);
    close.addActionListener(event -> setVisible(false));
    footer.add(close);

    add(title, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(footer, BorderLayout.SOUTH);
    LocaleManager.addLocaleListener(this);
    localeChanged();
    setVisible(false);
  }

  void showIssues(List<Issue> issues) {
    model.setIssues(issues);
    updateSummary();
    setVisible(true);
    revalidate();
    repaint();
    if (!issues.isEmpty()) {
      table.setRowSelectionInterval(0, 0);
    }
  }

  @Override
  public void localeChanged() {
    title.setText(S.get("circuitCheckTitle"));
    close.setText(S.get("circuitCheckClose"));
    model.fireTableStructureChanged();
    table.getColumnModel().getColumn(0).setPreferredWidth(90);
    table.getColumnModel().getColumn(0).setMaxWidth(130);
    table.getColumnModel().getColumn(2).setPreferredWidth(100);
    table.getColumnModel().getColumn(2).setMaxWidth(150);
    updateSummary();
  }

  private void updateSummary() {
    final var count = model.getRowCount();
    summary.setText(
        count == 0 ? S.get("circuitCheckNoIssues") : S.get("circuitCheckSummary", count));
  }

  private static String describe(Issue issue) {
    final var location = issue.location();
    return switch (issue.kind()) {
      case WIDTH_MISMATCH ->
          S.get(
              "circuitCheckWidthMismatch",
              issue.widths().stream().map(String::valueOf).collect(Collectors.joining(", ")));
      case UNCONNECTED_INPUT ->
          S.get("circuitCheckUnconnectedInput", issue.component().getFactory().getDisplayName());
      case DANGLING_WIRE -> S.get("circuitCheckDanglingWire");
    };
  }

  private static final class IssueTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Issue> issues = List.of();

    void setIssues(List<Issue> value) {
      issues = List.copyOf(value);
      fireTableDataChanged();
    }

    Issue getIssue(int row) {
      return issues.get(row);
    }

    @Override
    public int getColumnCount() {
      return 3;
    }

    @Override
    public int getRowCount() {
      return issues.size();
    }

    @Override
    public String getColumnName(int column) {
      return switch (column) {
        case 0 -> S.get("circuitCheckSeverityColumn");
        case 1 -> S.get("circuitCheckProblemColumn");
        case 2 -> S.get("circuitCheckLocationColumn");
        default -> "";
      };
    }

    @Override
    public Object getValueAt(int row, int column) {
      final var issue = issues.get(row);
      return switch (column) {
        case 0 ->
            S.get(
                issue.severity() == CircuitChecker.Severity.ERROR
                    ? "circuitCheckSeverityError"
                    : "circuitCheckSeverityWarning");
        case 1 -> describe(issue);
        case 2 -> String.format("(%d, %d)", issue.location().getX(), issue.location().getY());
        default -> "";
      };
    }
  }
}

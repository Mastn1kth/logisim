/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.circuit;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Location;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

/** Performs non-destructive, editor-oriented checks on a circuit. */
public final class CircuitChecker {
  public enum IssueKind {
    WIDTH_MISMATCH,
    UNCONNECTED_INPUT,
    DANGLING_WIRE
  }

  public enum Severity {
    ERROR,
    WARNING
  }

  public record Issue(
      Severity severity,
      IssueKind kind,
      Location location,
      Component component,
      List<Integer> widths) {
    public Issue {
      widths = List.copyOf(widths);
    }
  }

  private CircuitChecker() {}

  public static List<Issue> check(Circuit circuit) {
    if (circuit == null) return List.of();

    final var issues = new ArrayList<Issue>();
    addWidthMismatches(circuit, issues);
    addUnconnectedInputs(circuit, issues);
    addDanglingWires(circuit, issues);
    issues.sort(
        Comparator.comparing(Issue::severity)
            .thenComparing(Issue::kind)
            .thenComparingInt(issue -> issue.location().getY())
            .thenComparingInt(issue -> issue.location().getX()));
    return List.copyOf(issues);
  }

  private static void addWidthMismatches(Circuit circuit, List<Issue> issues) {
    final var mismatchData = circuit.getWidthIncompatibilityData();
    if (mismatchData == null) return;

    for (final var mismatch : mismatchData) {
      if (mismatch.size() == 0) continue;

      final var widths = new LinkedHashSet<Integer>();
      for (var i = 0; i < mismatch.size(); i++) {
        widths.add(mismatch.getBitWidth(i).getWidth());
      }
      final var sortedWidths = new ArrayList<>(widths);
      sortedWidths.sort(Integer::compareTo);
      issues.add(
          new Issue(
              Severity.ERROR,
              IssueKind.WIDTH_MISMATCH,
              mismatch.getPoint(0),
              null,
              sortedWidths));
    }
  }

  private static void addUnconnectedInputs(Circuit circuit, List<Issue> issues) {
    for (final var component : circuit.getNonWires()) {
      for (final var end : component.getEnds()) {
        if (end == null || !end.isInput()) continue;

        final var location = end.getLocation();
        final var connectedByWire = !circuit.getWires(location).isEmpty();
        final var connectedDirectly =
            circuit.getNonWires(location).stream().anyMatch(other -> other != component);
        if (!connectedByWire && !connectedDirectly) {
          issues.add(
              new Issue(
                  Severity.WARNING,
                  IssueKind.UNCONNECTED_INPUT,
                  location,
                  component,
                  List.of()));
        }
      }
    }
  }

  private static void addDanglingWires(Circuit circuit, List<Issue> issues) {
    for (final var wire : circuit.getWires()) {
      addDanglingWireEnd(circuit, wire, wire.getEnd0(), issues);
      addDanglingWireEnd(circuit, wire, wire.getEnd1(), issues);
    }
  }

  private static void addDanglingWireEnd(
      Circuit circuit, Wire wire, Location location, List<Issue> issues) {
    if (circuit.getWires(location).size() == 1 && circuit.getNonWires(location).isEmpty()) {
      issues.add(
          new Issue(
              Severity.WARNING,
              IssueKind.DANGLING_WIRE,
              location,
              wire,
              List.of()));
    }
  }
}

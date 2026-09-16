/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.circuit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.std.wiring.Pin;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CircuitCheckerTest {
  @Test
  void reportsUnconnectedInputAsWarning() {
    final var circuit = new Circuit("main", null, null);
    final var outputPin = createPin(Location.create(100, 100, true), Pin.OUTPUT, BitWidth.ONE);
    add(circuit, outputPin);

    final var issues = CircuitChecker.check(circuit);

    assertEquals(1, issues.size());
    assertEquals(CircuitChecker.IssueKind.UNCONNECTED_INPUT, issues.get(0).kind());
    assertEquals(CircuitChecker.Severity.WARNING, issues.get(0).severity());
    assertEquals(outputPin, issues.get(0).component());
  }

  @Test
  void reportsBothEndsOfIsolatedWire() {
    final var circuit = new Circuit("main", null, null);
    add(
        circuit,
        Wire.create(
            Location.create(100, 100, true), Location.create(140, 100, true)));

    final var issues = CircuitChecker.check(circuit);

    assertEquals(2, issues.size());
    assertTrue(
        issues.stream()
            .allMatch(issue -> issue.kind() == CircuitChecker.IssueKind.DANGLING_WIRE));
  }

  @Test
  void reportsWidthMismatchOnConnectedPins() {
    final var circuit = new Circuit("main", null, null);
    final var inputPin = createPin(Location.create(100, 100, true), Pin.INPUT, BitWidth.ONE);
    final var outputPin =
        createPin(Location.create(140, 100, true), Pin.OUTPUT, BitWidth.create(8));
    add(
        circuit,
        inputPin,
        outputPin,
        Wire.create(inputPin.getLocation(), outputPin.getLocation()));

    final var issues = CircuitChecker.check(circuit);

    final var mismatch =
        issues.stream()
            .filter(issue -> issue.kind() == CircuitChecker.IssueKind.WIDTH_MISMATCH)
            .findFirst()
            .orElseThrow();
    assertEquals(CircuitChecker.Severity.ERROR, mismatch.severity());
    assertEquals(Arrays.asList(1, 8), mismatch.widths());
  }

  @Test
  void acceptsFullyConnectedSameWidthCircuit() {
    final var circuit = new Circuit("main", null, null);
    final var inputPin = createPin(Location.create(100, 100, true), Pin.INPUT, BitWidth.ONE);
    final var outputPin = createPin(Location.create(140, 100, true), Pin.OUTPUT, BitWidth.ONE);
    add(
        circuit,
        inputPin,
        outputPin,
        Wire.create(inputPin.getLocation(), outputPin.getLocation()));

    assertTrue(CircuitChecker.check(circuit).isEmpty());
  }

  @Test
  void directComponentConnectionCountsAsConnected() {
    final var circuit = new Circuit("main", null, null);
    final var location = Location.create(100, 100, true);
    final var inputPin = createPin(location, Pin.INPUT, BitWidth.ONE);
    final var outputPin = createPin(location, Pin.OUTPUT, BitWidth.ONE);
    add(circuit, inputPin, outputPin);

    assertTrue(CircuitChecker.check(circuit).isEmpty());
  }

  private static Component createPin(
      Location location, com.cburch.logisim.data.AttributeOption type, BitWidth width) {
    final var attributes = Pin.FACTORY.createAttributeSet();
    attributes.setValue(Pin.ATTR_TYPE, type);
    attributes.setValue(StdAttr.WIDTH, width);
    return Pin.FACTORY.createComponent(location, attributes);
  }

  private static void add(Circuit circuit, Component... components) {
    final var mutation = new CircuitMutation(circuit);
    for (final var component : components) {
      mutation.add(component);
    }
    mutation.execute();
  }
}

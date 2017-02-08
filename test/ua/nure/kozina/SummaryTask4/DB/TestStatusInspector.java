package ua.nure.kozina.SummaryTask4.DB;

import org.junit.Test;


public class TestStatusInspector {

    private final StatusInspector inspector = new StatusInspector();

    @Test
    public void testInspectorStart() {
        inspector.checkOrderStatuses();
    }

    @Test
    public void testInspectorStop() {
        inspector.interrupt();
    }
}

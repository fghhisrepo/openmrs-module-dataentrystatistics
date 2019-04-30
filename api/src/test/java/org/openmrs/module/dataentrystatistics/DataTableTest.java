package org.openmrs.module.dataentrystatistics;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataTableTest {

    @Test
    public void generateSpreadsheetShouldWork() {
        DataTable dataTable = new DataTable();
        TableRow tableRow = new TableRow("form", "form1");
        tableRow.put("user1", Integer.valueOf(100));
        tableRow.put("user2", Integer.valueOf(120));

        dataTable.addRow(tableRow);

        tableRow = new TableRow("form", "form12");
        tableRow.put("user1", Integer.valueOf(22));
        tableRow.put("user2", Integer.valueOf(9));
        dataTable.addRow(tableRow);

        assertTrue(dataTable.getRowCount() > 0);
        assertNotNull(dataTable.generateSpreadsheet());
    }
}

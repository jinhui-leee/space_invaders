package org.newdawn.spaceinvaders;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class MyTableCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setFont(cell.getFont().deriveFont(14.0f)); // 글자 크기 조정
//        ((JLabel) cell).setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        return cell;
    }
}

package src.views;

import src.Database.Controllers.BankStockController;
import src.Database.Controllers.UserStockController;
import src.Database.Models.BankStock;
import src.Database.Models.UserStock;
import src.user.LoggedInUser;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class provides the user interface for users to create new stock and make trade. The UserStockController and BankStockController are responsible for manipulating the data in respective side.
 */
public class StockMarket extends PageBack {
    private static final String[] column = {"ID", "Symbol", "Name", "Price", "Change", "Country", "Availability", "Sector", "Industry"};
    private static final int ID_COLUMN = 0;

    private static final int SYMBOL_COLUMN = 1;
    private static final int PRICE_COLUMN = 3;
    private static final int AVAILABILITY_COLUMN = 6;
    private static final int OWN_COLUMN = column.length; // Last column
    private JTable stockTable;
    private JButton backButton;
    private JPanel rootPanel;
    private JButton addANewStockButton;
    private JLabel errorLabel;
    private long userId;
    private long accountNumber;
    private HashMap<String, BankStock> stocks;
    private final StockTrade stockTrade = new StockTrade();
    private final StockCreation stockCreation = new StockCreation();
    private static final UserStockController userStockController = new UserStockController();
    private static final BankStockController stockController = new BankStockController();

    public StockMarket(boolean isManager) {
        $$$setupUI$$$();
        initStockTable();

        userId = LoggedInUser.getUser().getUserId();

        addANewStockButton.setVisible(isManager);
        addANewStockButton.setEnabled(isManager);
        stockTable.setCellSelectionEnabled(isManager);
        stockTable.setDragEnabled(!isManager);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                headBack();
            }
        });

        if (!isManager) {
            addOwnedStock();
            stockTable.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    int selectedRow = stockTable.getSelectedRow();
                    if (selectedRow < 0 || selectedRow >= stocks.size()) {
                        return;
                    }
                    String symbol = stockTable.getValueAt(selectedRow, SYMBOL_COLUMN).toString();
                    int ownAmount = (int) stockTable.getValueAt(selectedRow, OWN_COLUMN);
                    stockTrade.setUser(userId, accountNumber);
                    stockTrade.setStock(stockController, stocks.get(symbol));
                    stockTrade.setOwnAmount(ownAmount);
                    stockTrade.setPreviousPanel(rootPanel);
                    MainView.setContentPanel(stockTrade.getRootPanel());
                }
            });
        } else {
            addANewStockButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stockCreation.setPreviousPanel(rootPanel);
                    MainView.setContentPanel(stockCreation.getRootPanel());
                }
            });
        }

        rootPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                initStockTable();
                updateStockOwned();
                super.componentShown(e);
            }
        });
    }

    private void addOwnedStock() {
        Object[] owned = new Object[stockTable.getRowCount()];
        Arrays.fill(owned, 0);

        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.addColumn("Owned", owned);
        updateStockOwned();
    }

    private void updateStockOwned() {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        userStockController.getAllStocksOfUser(userId, accountNumber).forEach(stock -> {
            // Find row index and insert the value
            for (int i = 0; i < model.getRowCount(); i++) {
                if (Long.parseLong(stockTable.getValueAt(i, ID_COLUMN).toString()) == stock.getStockId()) {
                    System.out.println(stock.getOwnedAmount());
                    stockTable.setValueAt(stock.getOwnedAmount(), i, OWN_COLUMN);
                    break;
                }
            }
        });
    }

    private void initStockTable() {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.setColumnCount(0);
        for (String col: column) {
            model.addColumn(col);
        }
        model.setRowCount(0);
        stocks = stockController.getAllAvailableStocks();
        for (BankStock stock : stocks.values()) {
            model.addRow(new Object[]{
                    String.format("%04d", stock.getId()),
                    stock.getStockSymbol(),
                    stock.getStockName(),
                    stock.getStockPrice(),
                    stock.getStockChange(),
                    stock.getStockCountry(),
                    stock.getStockAvailable(),
                    stock.getStockSector(),
                    stock.getStockIndustry(),
            });
        }
        // TODO: set green and red color.
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setUser(long userId, long accountNumber) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        updateStockOwned();
    }

    private void createUIComponents() {
        stockTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == PRICE_COLUMN || column == AVAILABILITY_COLUMN);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                NumberFormatter floatFormatter = new NumberFormatter(new DecimalFormat("#.##"));
                floatFormatter.setAllowsInvalid(false);
                floatFormatter.setMinimum(0.0);
                final TableCellEditor priceEditor = new DefaultCellEditor(new JFormattedTextField(floatFormatter));
                priceEditor.addCellEditorListener(new CellEditorListener() {
                    @Override
                    public void editingStopped(ChangeEvent e) {
                        int i = stockTable.getSelectedRow();
                        String symbol = stockTable.getValueAt(i, SYMBOL_COLUMN).toString();
                        String price = stockTable.getValueAt(i, PRICE_COLUMN).toString();
                        stockController.updateStock(symbol, Double.parseDouble(price));
                        System.out.println("[Updated] " + symbol + "'s price: " + price);
                    }

                    @Override
                    public void editingCanceled(ChangeEvent e) {
                        ; // nothing
                    }
                });

                NumberFormatter intFormatter = new NumberFormatter(new DecimalFormat("#"));
                intFormatter.setAllowsInvalid(false);
                intFormatter.setMinimum(0);
                final TableCellEditor availabilityEditor = new DefaultCellEditor(new JFormattedTextField(intFormatter));
                availabilityEditor.addCellEditorListener(new CellEditorListener() {
                    @Override
                    public void editingStopped(ChangeEvent e) {
                        int i = stockTable.getSelectedRow();
                        String symbol = stockTable.getValueAt(i, SYMBOL_COLUMN).toString();
                        String left = stockTable.getValueAt(i, AVAILABILITY_COLUMN).toString();
                        stockController.updateStockAvailable(symbol, Integer.parseInt(left));
                        System.out.println("[Updated] " + symbol + "'s availability: " + left);
                    }

                    @Override
                    public void editingCanceled(ChangeEvent e) {
                        ; // nothing
                    }
                });

                int modelColumn = convertColumnIndexToModel(column);
                switch (modelColumn) {
                    case PRICE_COLUMN:
                        return priceEditor;
                    case AVAILABILITY_COLUMN:
                        return availabilityEditor;
                    default:
                        return super.getCellEditor(row, column);
                }
            }
        };
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        rootPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        rootPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Stock Market", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        stockTable.setAutoCreateRowSorter(true);
        stockTable.setEnabled(true);
        stockTable.setPreferredScrollableViewportSize(new Dimension(700, 500));
        scrollPane1.setViewportView(stockTable);
        backButton = new JButton();
        backButton.setText("Back");
        rootPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addANewStockButton = new JButton();
        addANewStockButton.setText("Add a new stock");
        rootPanel.add(addANewStockButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-65536));
        errorLabel.setText("");
        rootPanel.add(errorLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}

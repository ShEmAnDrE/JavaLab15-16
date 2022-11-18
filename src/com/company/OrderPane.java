package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class OrderPane extends JTabbedPane {
    private int tableNum;
    public OrderPane(int tNum){
        this.tableNum = tNum;
        this.setTabPlacement(LEFT);
        addOrderTab();
        this.addChangeListener(e-> {
            if(this.getSelectedIndex() == this.getTabCount()-1) {
                addOrderTab();
            }
        });
    }

    public static class MenuItemPanel extends JPanel {
        private JComboBox<String> comboBox;
        private JComboBox<DrinkTypeEnum> drinkType;
        private JTextField textField, name, desc, cost;
        private JPanel menuPanel;

        public String[] getData() {
            String[] str = new String[menuPanel.getComponentCount()==12?5:3];
            str[0] = name.getText();
            str[1] = desc.getText();
            str[2] = cost.getText();
            if(str.length == 5){
                str[3] = ((JTextField) menuPanel.getComponent(menuPanel.getComponentCount()-1)).getText();
                str[4] = Objects.requireNonNull(((JComboBox) menuPanel.getComponent(menuPanel.getComponentCount() - 3)).getSelectedItem()).toString();
            }
            return str;
        }

        MenuItemPanel(int tNum) {
            menuPanel = new JPanel();
            comboBox = new JComboBox<>(new String[]{"Блюдо", "Напиток"});
            comboBox.addItemListener(e->{
                if(comboBox.getSelectedIndex() == 1 && drinkType == null){
                    drinkType = new JComboBox<>(DrinkTypeEnum.values());
                    JLabel label = new JLabel("Тип напитка: ");
                    label.setLabelFor(drinkType);
                    textField = new JTextField();
                    var labelAlc = new JLabel("Алкоголь: ");
                    labelAlc.setLabelFor(textField);

                    menuPanel.add(label);
                    menuPanel.add(drinkType);
                    menuPanel.add(labelAlc);
                    menuPanel.add(textField);
                } else if(comboBox.getSelectedIndex() != 1 && drinkType != null) {
                    for(int i = 0; i < 4; i++) {
                        menuPanel.remove(menuPanel.getComponentCount() - 1);
                    }
                    drinkType = null;
                    textField = null;
                }
                this.updateUI();
            });
            name = new JTextField();
            name.setPreferredSize(new Dimension(300, 20));
            var l = new JLabel("Название: ");
            l.setLabelFor(name);
            desc = new JTextField();
            desc.setPreferredSize(new Dimension(60, 20));
            var ld = new JLabel("Описание: ");
            ld.setLabelFor(name);
            cost = new JTextField();
            cost.setPreferredSize(new Dimension(60, 20));
            var l2 = new JLabel("Цена: ");
            l2.setLabelFor(name);

            menuPanel.setLayout(new GridLayout(0, 2));
            menuPanel.add(comboBox);
            menuPanel.add(new JPanel());
            menuPanel.add(l);
            menuPanel.add(name);
            menuPanel.add(ld);
            menuPanel.add(desc);
            menuPanel.add(l2);
            menuPanel.add(cost);
            this.add(menuPanel, BorderLayout.CENTER);
            menuPanel.setVisible(true);
            if(tNum >= 0){
                var add = new JButton("Добавить в заказ");
                add.addActionListener(e-> Controller.getInstance().addToOrder(getData(), tNum));
                this.add(add, BorderLayout.SOUTH);
            }
        }
    }

    private void addOrderTab(){
        if(this.getTabCount() > 0) {
            this.setSelectedIndex(0);
            removeTabAt(getTabCount() - 1);
        }
        this.addTab("Элемент " + (this.getTabCount() + 1), new MenuItemPanel(tableNum));
        this.addTab("+", new JPanel());

    }
}
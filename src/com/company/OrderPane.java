package com.company;

import javax.swing.*;
import java.awt.*;

public class OrderPane extends JTabbedPane {
    private int tNum;
    public OrderPane(int tNum){
        this.tNum = tNum;
        this.setTabPlacement(LEFT);
        addOrderTab();
        this.addChangeListener(e->{
            if(this.getSelectedIndex() == this.getTabCount()-1){
                addOrderTab();
            }
        });

    }

    public static class MenuItemPanel extends JPanel{
        private JComboBox<String> comboBox;
        private JComboBox<DrinkTypeEnum> drinkType;
        private JTextField textField, name, desc, cost;
        private JPanel menuPanel;

        public String [] getData(){
            var s = new String[menuPanel.getComponentCount()==12?5:3];
            s[0] = name.getText();
            s[1] = desc.getText();
            s[2] = cost.getText();
            if(s.length == 5){
                s[3] = ((JTextField) menuPanel.getComponent(menuPanel.getComponentCount()-1)).getText();
                s[4] = ((JComboBox) menuPanel.getComponent(menuPanel.getComponentCount()-3)).getSelectedItem().toString();
            }
            return s;
        }

        MenuItemPanel(int tNum){

            menuPanel = new JPanel();

            comboBox = new JComboBox<>(new String[]{
                    "Блюдо", "Напиток"
            });
            comboBox.addItemListener(e->{
                if(comboBox.getSelectedIndex() == 1 && drinkType == null){
                    drinkType = new JComboBox<>(DrinkTypeEnum.values());
                    JLabel l = new JLabel("Тип напитка: ");
                    l.setLabelFor(drinkType);
                    textField = new JTextField();
                    var l2 = new JLabel("Алкоголь: ");
                    l2.setLabelFor(textField);

                    menuPanel.add(l);
                    menuPanel.add(drinkType);
                    menuPanel.add(l2);
                    menuPanel.add(textField);
                }else if(comboBox.getSelectedIndex() != 1 && drinkType != null){
                    for(int i = 0; i < 4; i++)
                        menuPanel.remove(menuPanel.getComponentCount()-1);
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
        if(this.getTabCount()>0) {
            this.setSelectedIndex(0);
            removeTabAt(getTabCount()-1);
        }
        this.addTab("Элемент " + (this.getTabCount()+1), new MenuItemPanel(tNum));

        this.addTab("+", new JPanel());

    }
}
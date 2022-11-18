package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CafeWindow extends JFrame {

    private JLabel summaryLabel;
    private JPanel tablesOrders;

    public CafeWindow(int tablesCount){
        super("Все заказы кафе");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(480, 480);
        summaryLabel = new JLabel("Заказов в ресторане: 0, заказов через интернет: 0");
        tablesOrders = new JPanel();
        tablesOrders.setLayout(new BoxLayout(tablesOrders, BoxLayout.Y_AXIS));
        for(int i = 0; i < tablesCount; i++){

            class OrderWindow extends JFrame{
                OrderWindow(OrderUI ui, int i){
                    getContentPane().add(new JScrollPane(ui), BorderLayout.CENTER);
                    setSize(160, 480);
                    JButton b = new JButton("Выполнен");
                    b.addActionListener(e-> {
                        Controller.getInstance().removeOrder(i);
                        this.dispose();
                    });
                    getContentPane().add(b, BorderLayout.SOUTH);
                }
            }
            JButton b = new JButton("Столик № " + (i + 1));
            b.addActionListener(e->{
                try{
                    var orderWindow = new OrderWindow(new OrderUI(Controller.getInstance().getOrder(Integer.parseInt(b.getText().substring(9)) - 1)),
                            Integer.parseInt(b.getText().substring(9)) -1);
                    orderWindow.setVisible(true);
                }catch (NoOrderForTableException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Нет заказа!", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            tablesOrders.add(b);
        }

        var scrollPane = new JScrollPane(tablesOrders);
        getContentPane().add(summaryLabel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.WEST);
        JPanel panel = new JPanel();
        JButton button = new JButton("Выполнить");
        button.addActionListener(e->Controller.getInstance().removeOrder());
        getContentPane().add(panel);
        setIntOrder(null);
        panel.add(button);
        setJMenuBar(new GeneralMenu(this, new JMenu[0]));
    }

    public void updateDataLabel(int table, int internet){
        summaryLabel.setText("Заказов в ресторане: " + table + ", заказов через интернет: " + internet);
    }

    public void setIntOrder(Order order){
        if(((JPanel)getContentPane().getComponent(2)).getComponentCount() > 0)
            ((JPanel)getContentPane().getComponent(2)).remove(0);
        if(order == null) {
            ((JPanel)getContentPane().getComponent(2)).add(new JLabel("Интернет заказов пока нет!"), 0);
        }else{
            ((JPanel)getContentPane().getComponent(2)).add(new OrderUI(order), 0);
        }
    }

    public void onRemoveOrder(int tNum) {
        ((JButton)tablesOrders.getComponent(tNum)).setIcon(null);
    }
}
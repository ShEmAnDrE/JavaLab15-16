package com.company;

import javax.swing.*;
import java.awt.*;

public class WaitersWindow extends JFrame {

    private JTabbedPane wTables;

    public WaitersWindow(){
        super("Окно для гостя в кафе");
        setSize(860, 320);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        wTables = new JTabbedPane();
        wTables.setTabPlacement(SwingConstants.RIGHT);
        var newGuest = new JButton("Обслужить гостя");
        newGuest.addActionListener(e->{
            int[] tableNums;
            try{
                tableNums = Controller.getInstance().getFreeNums();
            }catch (NoFreeTablesException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }
            Integer[] tableFreeNums = new Integer[tableNums.length];
            for (int i = 0; i < tableNums.length; i++) {
                tableFreeNums[i] = tableNums[i] + 1;
            }
            Integer table = (Integer) JOptionPane.showInputDialog(this, "Укажите столик: ", "Начать обработку заказа", JOptionPane.INFORMATION_MESSAGE, null, tableFreeNums,  tableFreeNums[0]);
            try{
                Controller.getInstance().addOrder(table - 1, JOptionPane.showConfirmDialog(this, "Гость - совершеннолетний?", "Начало работы с клиентом", JOptionPane.YES_NO_OPTION) == 0);
            }catch (NullPointerException ignored){}

            wTables.addTab("Столик № " + table, new OrderPane(table - 1));

        });

        getContentPane().add(newGuest, BorderLayout.NORTH);
        getContentPane().add(wTables, BorderLayout.CENTER);

        setJMenuBar(new GeneralMenu(this, new JMenu[0]));
    }

    public void onRemovedOrder(int tNum){
        for(int i = 0; i < wTables.getTabCount(); i++){
            if(Integer.parseInt(wTables.getTitleAt(i).substring(9)) - 1 == tNum){
                wTables.removeTabAt(i);
                break;
            }
        } }
}
package com.company;

import javax.swing.*;
import java.awt.*;

public class GeneralMenu extends JMenuBar {

    public GeneralMenu(Component parent, JMenu[] others){
        JMenu fileMenu = new JMenu("Файл");

        var exit = new JMenuItem("Выход");
        exit.addActionListener(e->System.exit(0));
        fileMenu.add(exit);

        this.add(fileMenu);

        for(var m: others){
            this.add(m);
        }
    }
}
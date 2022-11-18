package com.company;

import javax.swing.*;

public class OrderUI extends JPanel {
    public OrderUI(Order order){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        var items = order.getItems();
        for (var item : items) {
            JLabel label = new JLabel(item.getName() + "\t: " + order.itemQuantity(item));
            StringBuilder stringBuilder = new StringBuilder();
            if (item instanceof Drink) {
                stringBuilder.append("Тип: ").append(((Drink) item).getType()).append("\nАлкоголя: ").append(((Drink) item).getAlcoholVol()).append("\n");
            }
            stringBuilder.append(item.getDescription());
            label.setToolTipText(stringBuilder.toString());
            add(label);
        }
    }
}

package com.company;

import javax.swing.*;

public class Controller implements Runnable{
    private KitchenWindow kitchenWindow;
    private InternetUserWindow internetUserWindow;
    private WaitersWindow waitersWindow;

    private InternetOrderManager internetOrderManager;
    private TableOrderManager tableOrderManager;

    private static Controller instance;
    private Controller(){
        tableOrderManager = new TableOrderManager();
        internetOrderManager = new InternetOrderManager();

        kitchenWindow = new KitchenWindow(32);
        kitchenWindow.setVisible(true);
        internetUserWindow = new InternetUserWindow();
        internetUserWindow.setVisible(true);
        waitersWindow = new WaitersWindow();
        waitersWindow.setVisible(true);
        new Thread(this).start();
    }

    public int[] getFreeNums() throws NoFreeTablesException {
        return tableOrderManager.freeTableNumbers();
    }

    public void addOrder(int tNum, boolean mature){
        var tableOrder = new TableOrder(mature ? Customer.MATURE_UNKNOWN_CUSTOMER : Customer.NOT_MATURE_UNKNOWN_CUSTOMER);
        try{
            tableOrderManager.add(tableOrder, tNum);
        }catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(waitersWindow, e.getMessage(), "Ошибка номера столика!", JOptionPane.WARNING_MESSAGE);
        }
        kitchenWindow.notifyTableOrderAdded(tNum);
    }

    public void addToOrder(String[] arr, int tNum){
        MenuItem mi;
        try{
            if(arr.length == 3)
                mi = new Dish(arr[0], arr[1], Double.parseDouble(arr[2]));
            else mi = new Drink(arr[0], arr[1], Double.parseDouble(arr[2]), Double.parseDouble(arr[3]), DrinkTypeEnum.valueOf(arr[4]));
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(waitersWindow, "Ошибка ввода числа:\n"+e.getMessage(), "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            tableOrderManager.addItem(mi, tNum);
        }catch (AlcoholForNotMatureCustomerException e){
            JOptionPane.showMessageDialog(waitersWindow, e.getMessage(), "Алкоголь нельзя!", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void addOrder(String[][] ord){
        MenuItem[] items = new MenuItem[ord.length];
        for(int i = 0; i< ord.length; i++){
            try{
                if(ord[i].length == 3)
                    items[i] = new Dish(ord[i][0], ord[i][1], Double.parseDouble(ord[i][2]));
                else items[i] = new Drink(ord[i][0], ord[i][1], Double.parseDouble(ord[i][2]), Double.parseDouble(ord[i][3]), DrinkTypeEnum.valueOf(ord[i][4]));
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(internetUserWindow, "В элементе заказа номер "+ (i+1)+ " введено некорректное число.");
                return;
            }
        }
        String[] c = internetUserWindow.askCustomer();
        Customer cast;
        try{
            cast = new Customer(
                    c[0], c[1], Integer.parseInt(c[2]),
                    new Address(c[3], Integer.parseInt(c[4]), c[5], Integer.parseInt(c[6]), c[7].charAt(0), Integer.parseInt(c[8]))
            );
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(internetUserWindow, "Вы ввели некорректное число. Повторите попытку позже.");
            return;
        }
        InternetOrder io;
        try{
            io = new InternetOrder(items, cast);
        }catch (AlcoholForNotMatureCustomerException e){
            JOptionPane.showMessageDialog(internetUserWindow, e.getMessage());
            return;
        }
        internetOrderManager.add(io);
        if(internetOrderManager.ordersQuantity() == 1) kitchenWindow.setIntOrder(internetOrderManager.order());
        JOptionPane.showMessageDialog(internetUserWindow, "Заказ поставлен в очередь обработки!");
    }

    public Order getOrder(int tableNum){
        return tableOrderManager.getOrder(tableNum);
    }
    public void removeOrder(int tNum){
        tableOrderManager.remove(tNum);
        waitersWindow.onRemovedOrder(tNum);
        kitchenWindow.onRemoveOrder(tNum);
    }
    public void removeOrder(){
        try {
            internetOrderManager.remove();
        }catch (EmptyOrderListException e){
            JOptionPane.showMessageDialog(kitchenWindow, "В очереди нет заказов!");
        }
        try{
            kitchenWindow.setIntOrder(internetOrderManager.order());
        }catch (EmptyOrderListException e){
            kitchenWindow.setIntOrder(null);
        }
    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            if (!kitchenWindow.isVisible() && !internetUserWindow.isVisible() && !waitersWindow.isVisible())
                System.exit(0);
            kitchenWindow.updateDataLabel(tableOrderManager.ordersQuantity(), internetOrderManager.ordersQuantity());
        }
    }

    public static void main(String[] args) {
        Controller.getInstance();
    }
}

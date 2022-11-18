package com.company;

public class TableOrder implements Order {

    private MenuItem[] items;
    private int size;
    private Customer customer;

    public TableOrder(Customer customer) {
        this.customer = customer;
        this.items = new MenuItem[0];
        size = 0;
    }

    private void increase(){
        MenuItem[] tmp = new MenuItem[items.length+1];
        System.arraycopy(items, 0, tmp, 0, items.length);
        items = tmp;
        size++;
    }
    private void decrease(int removedIndex){
        MenuItem[] tmp = new MenuItem[items.length-1];
        System.arraycopy(items, 0, tmp, 0, removedIndex);
        System.arraycopy(items, removedIndex+1, tmp, removedIndex, items.length-removedIndex-1);
        items = tmp;
        size--;
    }

    @Override
    public boolean add(MenuItem item) throws AlcoholForNotMatureCustomerException {
        if(item instanceof Alcoholable && ! customer.isMature() && ((Alcoholable) item).isAlcoholDrink())
            throw new AddingAlcoholForNotMatureCustomerException(customer, (Drink) item);
        increase();
        items[size-1] = item;
        return true;
    }

    @Override
    public String[] itemsNames() {
        String [] res = new String[size];
        MenuItem[] uniqueItems = getItems();
        for(int i = 0; i < uniqueItems.length; i++){
            res[i] = uniqueItems[i].getName();
        }
        return res;
    }

    @Override
    public int itemsQuantity(){
        return size;
    }

    @Override
    public int itemQuantity(String itemName) {
        int count = 0;
        for (MenuItem item : items) {
            if(item.getName().equals(itemName)) count++;
        }
        return count;
    }

    @Override
    public int itemQuantity(MenuItem item) {
        int count = 0;
        for (MenuItem mItem : items) {
            if(mItem.equals(item)) count++;
        }
        return count;
    }

    @Override
    public MenuItem[] getItems() {
        // А здесь лучше бы через множество сделать, но нет - использовать Collections нельзя...
        MenuItem[] uniqueItems = new MenuItem[size];
        int count = 0;
        for(int i = 0; i < size; i++){
            boolean found = false;
            for(int j = 0; j < count; j++){
                if(uniqueItems[j].equals(items[i])) {
                    found = true; break;
                }
            }
            if(!found) uniqueItems[count++] = items[i];
        }
        MenuItem[]res = new MenuItem[count];
        System.arraycopy(uniqueItems, 0, res, 0, count);
        return res;
    }

    @Override
    public boolean remove(String itemName) {
        for(int i = 0; i < size; i++){
            if(items[i].getName().equals(itemName)){
                decrease(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(MenuItem item) {
        for(int i = 0; i < size; i++){
            if(items[i].equals(item)){
                decrease(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int removeAll(String itemName) {
        int count = 0;
        while(remove(itemName)) count++;
        return count;
    }

    @Override
    public int removeAll(MenuItem item) {
        int count = 0;
        while(remove(item)) count++;
        return count;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public double costTotal() {
        double cost = 0;
        for (MenuItem item : items) {
            cost += item.getCost();
        }
        return cost;
    }

    @Override
    public void setCustomer(Customer newCustomer) throws AlcoholForNotMatureCustomerException{
        this.customer = newCustomer;
        if(!this.customer.isMature()){
            Drink[] removedItems = new Drink[0];
            for (MenuItem item : getItems()) {
                if(item instanceof Alcoholable && ((Alcoholable) item).isAlcoholDrink()){
                    Drink[] tmp = new Drink[removedItems.length+1];
                    System.arraycopy(removedItems, 0, tmp, 0, removedItems.length);
                    tmp[tmp.length-1] = (Drink) item;
                    removedItems = tmp;
                    removeAll(item);
                }
            }
            if(removedItems.length > 0) throw new CustomerChangedToNotMatureException(customer, removedItems);
        }
    }

    @Override
    public MenuItem[] sortedItemsByCostDesc() {
        MenuItem[] items = getItems();
        mergeSort(items);
        return items;
    }

    private void mergeSort(MenuItem[] arr){
        if(arr.length < 2) return;
        int middle = arr.length/2;
        MenuItem[]l = new MenuItem[middle];
        MenuItem[]r = new MenuItem[arr.length-middle];
        System.arraycopy(arr, 0, l, 0, middle);
        System.arraycopy(arr, middle, r, 0, arr.length-middle);

        mergeSort(l);
        mergeSort(r);

        merge(arr, l, r);
    }
    private void merge(MenuItem[] arr, MenuItem[] left, MenuItem[] right){
        int iArr = 0, iLeft = 0, iRight = 0;
        while (iLeft < left.length && iRight < right.length){
            if(left[iLeft].compareTo(right[iRight]) <= 0) arr[iArr++] = left[iLeft++];
            else arr[iArr++] = right[iRight++];
        }
        while(iLeft < left.length)
            arr[iArr++] = left[iLeft++];
        while (iRight < right.length)
            arr[iArr++] = right[iRight++];
    }
}
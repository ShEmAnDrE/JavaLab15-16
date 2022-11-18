package com.company;

public class InternetOrder implements Order {
    private static class ListNode{
        private ListNode next;
        private MenuItem value;

        ListNode(MenuItem value){
            this.value = value;
        }
        ListNode(){this(null);}

    }

    private ListNode head, tail;
    private int size;
    private Customer customer;

    public InternetOrder(Customer customer){
        this.customer = customer;
        head = tail = new ListNode();
        size = 0;
    }
    public InternetOrder(MenuItem[] arr, Customer customer) throws AlcoholForNotMatureCustomerException{
        this.customer = customer;
        head = tail = new ListNode();
        size = 0;
        if(arr == null || arr.length == 0) return;
        for(int i = 0; i < arr.length; i++, size++){
            add(arr[i]);
        }
    }

    @Override
    public boolean add(MenuItem item) throws AlcoholForNotMatureCustomerException {
        if(item instanceof Alcoholable && ((Alcoholable) item).isAlcoholDrink() && ! customer.isMature())
            throw new AddingAlcoholForNotMatureCustomerException(customer, (Drink) item);
        if(head.value == null){
            head.value = item;
            return true;
        }
        tail.next = new ListNode(item);
        tail = tail.next;
        size++;
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
        ListNode node = head;
        while(node != null){
            if(node.value.getName().equals(itemName)) count++;
            node = node.next;
        }
        return count;
    }

    @Override
    public int itemQuantity(MenuItem item) {
        int count = 0;
        ListNode node = head;
        while(node != null){
            if(node.value.equals(item)) count++;
            node = node.next;
        }
        return count;
    }

    @Override
    public MenuItem[] getItems() {
        MenuItem[] items = new MenuItem[0];
        ListNode node = head;
        while(node != null){
            boolean found = false;
            for (MenuItem item : items) {
                if (item.equals(node.value)) {
                    found = true;
                    break;
                }
            }
            if(!found){
                MenuItem[] tmp = new MenuItem[items.length+1];
                System.arraycopy(items, 0, tmp, 0, items.length);
                tmp[tmp.length-1] = node.value;
                items = tmp;
            }
            node = node.next;
        }
        return items;
    }

    @Override
    public boolean remove(String itemName) {
        ListNode node = head, prevNode = null;

        while(node != null){
            if(node.value.getName().equals(itemName)){
                if(prevNode != null) prevNode.next = node.next;
                if (node == tail) tail = prevNode;
                if(node == head) head = head.next;
                size--;
                return true;
            }
            prevNode = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(MenuItem item) {
        ListNode node = head, prevNode = null;
        while(node != null){
            if(node.value.equals(item)){
                if(prevNode != null) prevNode.next = node.next;
                if (node == tail) tail = prevNode;
                if(node == head) head = head.next;
                size--;
                return true;
            }
            prevNode = node;
            node = node.next;
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

    @Override
    public double costTotal() {
        double cost = 0;
        ListNode node = head;
        while (node != null){
            cost+=node.value.getCost();
            node = node.next;
        }
        return cost;
    }
}

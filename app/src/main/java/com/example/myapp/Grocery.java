package com.example.myapp;
public class Grocery {
    private String grocery_Item;
    private String grocery_desc;
    private int present_Quantity;
    private int buy_Quantity;
    private String itemIgnoreCase;

    public Grocery() {

    }
    public Grocery(String grocery_Item, String grocery_desc, int present_Quantity, int buy_Quantity) {
        this.grocery_Item = grocery_Item;
        this.grocery_desc = grocery_desc;
        this.present_Quantity = present_Quantity;
        this.buy_Quantity = buy_Quantity;
        this.itemIgnoreCase=grocery_Item.toLowerCase();
    }
    public String getGrocery_Item() {
        return grocery_Item;
    }

    public String getGrocery_desc() {
        return grocery_desc;
    }

    public int getPresent_Quantity() {
        return present_Quantity;
    }

    public int getBuy_Quantity() {
        return buy_Quantity;
    }

    public String getItemIgnoreCase() {
        return itemIgnoreCase;
    }
}

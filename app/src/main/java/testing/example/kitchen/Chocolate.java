package testing.example.kitchen;

public class Chocolate {
    final private String name;
    final private int alert;
    final private String popular;
    final private int quantity;
    final private int priority;

    public Chocolate(String name, int alert, int quantity, String popular,int priority) {
        this.name = name;
        this.alert = alert;
        this.popular = popular;
        this.quantity = quantity;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getAlert() {
        return alert;
    }

    public int getPriority(){
        return priority;
    }

    public String getPopular() {
        return popular;
    }

    public int getQuantity() {
        return quantity;
    }

    /*
        Algorithm Explanation
        alert level = value at which the chocolate may need to be made
        Quantity = amount of the Chocolate at the time
        Popular = 1 means popular and 0 means not popular
        6 levels of priority for the chocolates
        Level 1: Chocolates are fine. Quantity Exceeds Alert number
        Level 2: Not important Chocolates fall below Alert number
        Level 3: Important Chocolates fall below Alert number but quantity is still greater than 2
        Level 4: Not important Chocolates are out of stock (0 quantity)
        Level 5: Important Chocolates Fall below Alert number and quantity is either 1 or 2
        Level 6: Important Chocolates are out of stock (0 quantity)
     */

    public static int getPriority(int alert, int quantity,int popular){
        int value = 0;
        if(quantity == 0 && popular == 1)
            value = 6;
        if(quantity <= 2 && popular == 1 && quantity != 0)
            value = 5;
        if(quantity == 0 && popular == 0)
            value = 4;
        if(alert > quantity && quantity > 2 && popular == 1)
            value = 3;
        if(alert > quantity && popular == 0 && quantity != 0)
            value = 2;
        if(alert < quantity)
            value = 1;
        return value;
    }
}
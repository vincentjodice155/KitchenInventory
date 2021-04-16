package testing.example.kitchen;

import java.util.Comparator;

public class MyComparator implements Comparator<Chocolate> {
    public int compare(Chocolate s1, Chocolate s2) {
        if (s1.getPriority() < s2.getPriority())
            return 1;
        else if (s1.getPriority() > s2.getPriority())
            return -1;
        else {
            if (s1.getName().compareTo(s2.getName()) > 1) {
                return -1;
            } else if (s1.getName().compareTo(s2.getName()) < 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

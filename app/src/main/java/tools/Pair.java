package tools;

public class Pair<F, S> {

    public F categoryName;
    public S categorySize;
    public S numberOfAllDishesBefore;

    public Pair(F first, S second) {
        this.categoryName = first;
        this.categorySize = second;
    }

}

package tuananh.com.budgetmanager;

/**
 * Created by Tuan Anh 2 on 11/19/2016.
 */
public class define {
    private static define ourInstance = new define();

    public static define getInstance() {
        return ourInstance;
    }

    private define() {
    }
}

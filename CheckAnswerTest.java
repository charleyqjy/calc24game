package game24;

import java.util.Vector;
import static game24.CheckAnswer.CheckAnswer;

// Check answer function test driver.
public class CheckAnswerTest {
    public static void main(String[] args) {
        int[] arr = new int[4];
        arr[0] = 5;
        arr[1] = 7;
        arr[2] = 4;
        arr[3] = 8;
        int returnVal = CheckAnswer("(5+7)*(8/4)", arr);
        System.out.println("Return value:" + returnVal);
    }
}

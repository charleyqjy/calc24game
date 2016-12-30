package game24;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

public class CheckAnswer {
    // This function takes an string as player input and an array of four ints
    // as the value of four assigned cards. Check whether the player input forms
    // a valid formula that calculates to 24.
    // Return 1 if the answer is accepted, 0 if wrong answer, -1 if player input
    // contains syntax error.
    static HashMap<Integer, Integer> numberSet;
    // static boolean syntaxError;

    static int CheckAnswer(String input, int[] cards) {
        if (input.equals(""))
            return -1;
        char[] inputArr = input.toCharArray();
        numberSet = new HashMap<Integer, Integer>();
        // syntaxError=false;
        for (int i = 0; i < 4; i++) {
            if (numberSet.containsKey(cards[i])) {
                numberSet.put(cards[i], numberSet.get(cards[i]) + 1);
            } else {
                numberSet.put(cards[i], 1);
            }
        }
        Stack<Character> normalStack = new Stack<Character>();
        Stack<Character> priorityStack = new Stack<Character>();
        for (int i = 0; i < inputArr.length; i++) {
            if (inputArr[i] != ')') {
                normalStack.push(inputArr[i]);
            } else {
                while (normalStack.peek() != '(') {
                    priorityStack.push(normalStack.peek());
                    normalStack.pop();
                }
                normalStack.pop();
                char[] resultArr = new char[priorityStack.size()];
                int j = 0;
                while (!priorityStack.isEmpty()) {
                    resultArr[j] = priorityStack.peek();
                    priorityStack.pop();
                    j++;
                }
                String result = getResult(String.copyValueOf(resultArr));
                resultArr = result.toCharArray();
                for (int k = 0; k < resultArr.length; k++) {
                    normalStack.push(resultArr[k]);
                }
            }
        }
        while (!normalStack.empty()) {
            priorityStack.push(normalStack.peek());
            normalStack.pop();
        }
        char[] resultArr = new char[priorityStack.size()];
        int len = priorityStack.size();
        for (int i = 0; i < len; i++) {
            resultArr[i] = priorityStack.peek();
            priorityStack.pop();
        }

        int result = Integer
                .valueOf(getResult(String.copyValueOf(resultArr)));
        for (Map.Entry<Integer, Integer> entry: numberSet.entrySet()) {
            if (entry.getValue() != 0) {
                if (entry.getKey() != result)
                    return -1;
            }
        }
        /*for (Integer value: numberSet.values()){
            if (value.intValue()!=0 && value.intValue()!=result) 
                return -1;
        }*/
        if (result == 24)
            return 1;
        else if (result == -1)
            return -1;
        else
            return 0;
    }

    static String getResult(String str) {
        System.out.println(str);
        if (str == "-1" || (!str.contains("*") && !str.contains("/")
                && !str.contains("+") && !str.contains("-")))
            return str;
        boolean priorty = (str.contains("*") || str.contains("/"))
                && (str.contains("+") || str.contains("-"));
        char[] strArr = str.toCharArray();
        int result = 0;
        if (!priorty) {
            System.out.println("Not priority");
            Vector<Integer> operands = new Vector<Integer>();
            Vector<Character> operations = new Vector<Character>();
            for (int i = 0; i < strArr.length; i++) {
                //System.out.println(strArr[i]);
                if (Character.isDigit(strArr[i])) {
                    int number = 0;
                    if (i + 1 < strArr.length
                            && Character.isDigit(strArr[i + 1])) {
                        number = Integer.parseInt(
                                Character.toString(strArr[i]) + Character
                                        .toString(strArr[i + 1]));
                        i++;
                    } else {
                        number = Integer
                                .parseInt(Character.toString(strArr[i]));
                        // System.out.println(number);
                    }
                    if (numberSet.get(number) == null
                            || numberSet.get(number) < 1) {
                        System.out.println(numberSet.get(number));
                        return -1 + "";
                    } else {
                        numberSet.put(number, numberSet.get(number) - 1);
                        operands.add(number);
                    }
                } else {
                    operations.add(strArr[i]);
                }
            }
            result = operands.elementAt(0);
            for (int i = 1; i < operands.size(); i++) {
                if (operations.elementAt(i - 1) == '+') {
                    result += operands.elementAt(i);
                } else if (operations.elementAt(i - 1) == '-') {
                    result -= operands.elementAt(i);
                } else if (operations.elementAt(i - 1) == '*') {
                    result *= operands.elementAt(i);
                } else {
                    result /= operands.elementAt(i);
                }
            }
            if (numberSet.containsKey(result)) {
                numberSet.put(result, numberSet.get(result) + 1);
            } else {
                numberSet.put(result, 1);
            }
            System.out.println("result "+result);
        } else {
            System.out.println("priority");
            int begin = 0;
            int end = 0;
            int index=0;
            if (str.contains("*")&&str.contains("/")){
                index = str.indexOf("*");
                if (str.contains("/")&&str.indexOf("/")<index){
                    index=str.indexOf("/");
                }
            }
            else if (!str.contains("/")){
                index = str.indexOf("*");
            }
            else {
                index=str.indexOf("/");
            }
            
            begin = index;
            while (begin > 0 && Character.isDigit(strArr[begin - 1])) {
                --begin;
            }
            end = index + 1;
            while (end < strArr.length
                    && Character.isDigit(strArr[end])) {
                ++end;
            }
            

            String resultStr = getResult(
                    String.copyValueOf(strArr).substring(begin, end));
            result = Integer.valueOf(resultStr);
            //System.out.println("result string: "+result);
            if (result == -1)
                return "-1";
            else {
                if (begin == 0 && end == strArr.length) {
                    return result + "";
                } else {
                    return getResult(
                            String.copyValueOf(strArr).substring(0, begin)
                                    + result
                                    + String.copyValueOf(strArr).substring(
                                            end, strArr.length));
                }
            }
        }

        return result + "";
    }
}

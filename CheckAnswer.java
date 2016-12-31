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

    //to store initial cards and temporary results and their corresponding times
    static HashMap<Integer, Integer> numberSet;
    
    static int CheckAnswer(String input, int[] cards) {
        if (input.equals(""))
            return -1;
        char[] inputArr = input.toCharArray();
        numberSet = new HashMap<Integer, Integer>();
        
        //to store initial cards and their corresponding times in the Hashmap
        for (int i = 0; i < 4; i++) {
            if (numberSet.containsKey(cards[i])) {
                numberSet.put(cards[i], numberSet.get(cards[i]) + 1);
            } else {
                numberSet.put(cards[i], 1);
            }
        }

        //use two stacks to remove the innermost layer of  brackets
        //normal stack is to store input array in order
        Stack<Character> normalStack = new Stack<Character>();
        //when encountering ")", it means the innermost layer of brackets
        //appear. Move the content within the innermost layer of brackets
        //from normal Stack to PriorityStack, and calculate the result of 
        //the content within the innermost layer of brackets, return a
        //temporary result
        Stack<Character> priorityStack = new Stack<Character>();

        //read the whole char array, and remove all brackets
        for (int i = 0; i < inputArr.length; i++) {
            //if not encountering ")",keep push the character in normalStack
            if (inputArr[i] != ')') {
                normalStack.push(inputArr[i]);
            } else {
                //if encountering ")",move the content within the innermost layer of brackets
                //from normal Stack to PriorityStack
                while (normalStack.peek() != '(') {
                    priorityStack.push(normalStack.peek());
                    normalStack.pop();
                }
                //pop "("
                normalStack.pop();
                char[] resultArr = new char[priorityStack.size()];
                int j = 0;
                // move the content in the priorityStack to a vector,
                // which is in the order of calculation
                while (!priorityStack.isEmpty()) {
                    resultArr[j] = priorityStack.peek();
                    priorityStack.pop();
                    j++;
                }
                //get the temporary result by "getRsult(string)" function
                String result = getResult(String.copyValueOf(resultArr));
                resultArr = result.toCharArray();
                //push the temporary result in normalStack
                for (int k = 0; k < resultArr.length; k++) {
                    normalStack.push(resultArr[k]);
                }
            }
        }

        //calculate the remaining formula in normalStack by "getResult"

        //move the formula in normalStack to an array with a normal calculating order
        while (!normalStack.empty()) {
            //first move it to the priorityStack with a reverse order
            priorityStack.push(normalStack.peek());
            normalStack.pop();
        }
        char[] resultArr = new char[priorityStack.size()];
        int len = priorityStack.size();
        for (int i = 0; i < len; i++) {
            //move the formula to an array, and the order is normal now
            resultArr[i] = priorityStack.peek();
            priorityStack.pop();
        }

        //get final result by "getResult" function
        int result = Integer
                .valueOf(getResult(String.copyValueOf(resultArr)));
        //check whether the only thing left in Hashmap is the result
        //if there exist some variables not being used, return -1
        //otherwise, check the result
        for (Map.Entry<Integer, Integer> entry: numberSet.entrySet()) {
            if (entry.getValue() != 0) {
                if (entry.getKey() != result)
                    return -1;
            }
        }
        //if result is 24, the answer is correct. Return 1
        if (result == 24)
            return 1;
        //if result is -1, there are some syntax errors in the formula. Return -1
        else if (result == -1)
            return -1;
        //wrong answer. Return 0;
        else
            return 0;
    }

    static String getResult(String str) {
        //System.out.println(str);

        // if calculation is not needed, just return str
        if (str == "-1" || (!str.contains("*") && !str.contains("/")
                && !str.contains("+") && !str.contains("-")))
            return str;
        // check whether the calculation has priority or not
        //if the calculation only has * and /, or +/-，it has no priority
        boolean priorty = (str.contains("*") || str.contains("/"))
                && (str.contains("+") || str.contains("-"));
        char[] strArr = str.toCharArray();
        int result = 0;

        //if the calculation has no priority, just do the calculation in order
        if (!priorty) {
            //System.out.println("Not priority");
            //store operands in order
            Vector<Integer> operands = new Vector<Integer>();
            //store operations in order
            Vector<Character> operations = new Vector<Character>();
            for (int i = 0; i < strArr.length; i++) {
                //System.out.println(strArr[i]);

                //if the character is a digit, check it's a one-bit number or two-bit number
                if (Character.isDigit(strArr[i])) {
                    int number = 0;
                    //if the next bit is also a digit, read the two-bit number
                    if (i + 1 < strArr.length
                            && Character.isDigit(strArr[i + 1])) {
                        number = Integer.parseInt(
                                Character.toString(strArr[i]) + Character
                                        .toString(strArr[i + 1]));
                        i++;
                    } else {
                        //read one bit number
                        number = Integer
                                .parseInt(Character.toString(strArr[i]));
                        // System.out.println(number);
                    }

                    //check whether the number is in the hashmap
                    if (numberSet.get(number) == null
                            || numberSet.get(number) < 1) {
                        System.out.println(numberSet.get(number));
                        return -1 + "";
                    } else {
                        // if the number is in the hashmap, reduce the remaining times by 1
                        numberSet.put(number, numberSet.get(number) - 1);
                        //add the number to the operands vector
                        operands.add(number);
                    }
                } else {
                    //if it is not a digit, add it to operations vector
                    operations.add(strArr[i]);
                }
            }

            //do the calculation in order
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
            //store the temporary result in hashmap
            if (numberSet.containsKey(result)) {
                numberSet.put(result, numberSet.get(result) + 1);
            } else {
                numberSet.put(result, 1);
            }
            //System.out.println("result "+result);
        } else {
            //System.out.println("priority");

            /*if the calculation has some priority
             *seperate the prior calculation
             */
            int begin = 0;
            int end = 0;
            int index=0;
            //find the first prior calculation
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
            
            //find where the prior calculation begins
            begin = index;
            while (begin > 0 && Character.isDigit(strArr[begin - 1])) {
                --begin;
            }
            //find where the prior calculation ends
            end = index + 1;
            while (end < strArr.length
                    && Character.isDigit(strArr[end])) {
                ++end;
            }
            
            //calculate the seperated calculation by recursion
            String resultStr = getResult(
                    String.copyValueOf(strArr).substring(begin, end));
            result = Integer.valueOf(resultStr);
            //System.out.println("result string: "+result);
            
            //if the result==-1, which means some syntax error, 
            //no need to calculate remaining things, just return -1
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

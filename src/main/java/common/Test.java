package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.protobuf.Enum;
import com.sun.org.apache.xpath.internal.objects.XNumber;

public class Test {

    public static void main(String[] args) {

        List<String> answerList = new ArrayList();
        for (int i = 1; i < 10000; i++) {
            answerList.add(reformatToFourDigit(Integer.toString(i)));
        }

        System.out.println("Size = " + answerList.size());
/*        for (String answer : answerList) {
            System.out.println(answer);
        }*/

        List<String> netList = new ArrayList();
        for (String answer : answerList) {
            if (testCorrectAnswerFormat(answer)) {
                netList.add(answer);
            }
        }

        System.out.println("Size = " + netList.size());
        for (String answer : netList) {
            System.out.println(answer);
        }

    }

    static boolean testCorrectAnswerFormat(String answer) {
        for (int i = 0; i < answer.length(); i++) {
 /*           if (String.valueOf(answer.charAt(i)).equals("0")) {
                return false;
            }*/
            boolean isUniqueChar = !answer.substring(i + 1, 4)
                    .contains(String.valueOf(answer.charAt(i)));
            if (!isUniqueChar) return false;
        }
        return true;
    }

    static String reformatToFourDigit(String number) {
        int missingChar = 4 - number.length();
        String formattedString = "";
        for (int i = 0; i < missingChar; i++) {
            formattedString += "0";
        }
        return formattedString + number;
    }

    static double getDuplicatesInPercent(List listToCheck) {
        double allElements = listToCheck.size();
        double duplicates = allElements - new HashSet<>(listToCheck).size();
        System.out.println("Number of duplicates = " + duplicates);
        System.out.println("Percent = " + duplicates / allElements);
        return duplicates / allElements;
    }

}

package com.kassisdion;

import java.util.Scanner;

public class FloatToIEEE754 {

    /**********
     * * Constructor
     ***********/
    /*Simple constructor*/
    public FloatToIEEE754() {

    }

    /***********
     * * Internal method
     **********/
    private String getNormalized(final float value) {
        double tmp = value;
        String normalizedfractionalPart = "";

        for (int i = 0; i < 23; i++) {
            tmp = tmp * 2;
            if (tmp > 1.0) {
                tmp -= 1.0;
                normalizedfractionalPart += "1";
            } else if (tmp < 1.0) {
                normalizedfractionalPart += "0";
            } else {
                normalizedfractionalPart += "1";
                break;
            }
        }
        return normalizedfractionalPart;
    }

    private String decimalToXbitBinary(final int value, final int numberOfByte) {
        String binaryRepresentation = Integer.toBinaryString(value);
        /*We complete le string with some 0 for having the representation of 8 bits*/
        for (int i = binaryRepresentation.length(); i < numberOfByte; i++) {
            binaryRepresentation = "0" + binaryRepresentation;
        }
        return binaryRepresentation;
    }

    private String shift(String s) {
        return s.substring(1, s.length());
    }

    /***********
     * * Public method
     ************/
    public String convert(final float value) {
        final float absValue = Math.abs(value);

        /*Get the left and right part of the float*/
        float fractionalPart = absValue % 1;
        float integralPart = absValue - fractionalPart;

        System.out.println("fractionalPart = " + fractionalPart);
        System.out.println("integralPart = " + integralPart);

        /*We convert the left part to binary*/
        String integralPartBinary = Integer.toBinaryString((int) integralPart);
        System.out.println("\n" + integralPart + " -> " + integralPartBinary);

        /* We normalize the fractionalPart*/
        String normalizedfractionalPart = getNormalized(fractionalPart);
        System.out.println(fractionalPart + " -> " + normalizedfractionalPart);

        /*We build the mantisa*/
        String mantisa = integralPartBinary + normalizedfractionalPart;
        System.out.println("\n" + "Mantissa pre-shift = " + mantisa);

        /*We calculate the exponent*/
        final int currentPosition = integralPartBinary.length();
        final int wantedPosition = mantisa.indexOf("1");
        final int shiftNeeded = currentPosition - wantedPosition - 1;
        System.out.println("shiftNeeded " + shiftNeeded);
        final String exponent = decimalToXbitBinary(shiftNeeded + 127, 8);

        /*We shift the mantissa*/
        final int maxShift = mantisa.length();
        for (int i = 0; i < maxShift; i++) {
            if (mantisa.charAt(0) == '0') {
                //We shift the mantissa
                mantisa = shift(mantisa);
            } else {
                /*The mantisa have the from 1.xxxx*/
                /*We shift for having .xxxx*/
                mantisa = shift(mantisa);
                break;
            }
        }

        /*We complete the mantissa with some 0 to make him look 23 byte*/
        for (int i = mantisa.length(); i < 23; i++) {
            mantisa = mantisa + "0";
        }

        /*We check the sign*/
        final String sign = value < 0.0 ? "1" : "0";

        return sign + exponent + mantisa;
    }

    /***********
     * * Main function
     ***********/
    public static void main(String[] args) {
        /*Create a scanner so we can read the command-line input*/
        Scanner scanner = new Scanner(System.in);

        /*Just ask for a float*/
        System.out.println("Enter the float you want to convert :");
        System.out.print("-> ");

        /* Get the float and then convert it*/
        final Float input = scanner.nextFloat();
        System.out.println("Let's converting " + input + "\n");
        System.out.println("Result : " + new FloatToIEEE754().convert(input));
    }
}

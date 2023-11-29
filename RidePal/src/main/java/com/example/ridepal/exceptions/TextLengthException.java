package com.example.ridepal.exceptions;

public class TextLengthException extends RuntimeException{
    public TextLengthException(int number1, int number2) {
        super(String.format("Length should be between %s and %s symbols", number1, number2));
    }
    public TextLengthException() {
        super("Text cannot be empty!");
    }
}

package org.example;
public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello grpc client!\n");

        for (int i = 1; i <= 5; i++) {
            System.out.println("grpc version: " + i);
        }
    }
}
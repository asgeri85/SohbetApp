package com.example.sohbetapp.Claslar;

import java.util.Random;

public class RandomName {
    public static String getRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length()<18){
            int index=(int)(random.nextFloat()*alphabet.length());
            salt.append(alphabet.charAt(index));
        }
        String saltstr=salt.toString();

        return saltstr;

    }
}

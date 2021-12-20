package com.nure.backGardens;

import java.util.stream.IntStream;

public class ghoul {
    public static void main(String[] args) {
        IntStream.rangeClosed(0, 142).map(i -> ( (142 - i )*7 +6)).forEach(a->System.out.println(a +" - 7 ="+ (a -7)));
    }
}

// package Lab;

import java.math.BigInteger;
import java.util.Scanner;

public class Lab1 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    long n = sc.nextLong();
    long m = sc.nextLong();
    int k = sc.nextInt();
    long x = sc.nextLong();
    long y = sc.nextLong();

    int skipped = 0;

    for (int i = 0; i < k; i++) {
      int a = sc.nextInt();
      int b = sc.nextInt();

      if (x > a) {
        skipped++;
      } else if (a == x) {
        if (b < y) {
          skipped++;
        }
      }
    }

    System.out.println(BigInteger.valueOf(m * (x - 1) + y - skipped).toString(10));
  }
}
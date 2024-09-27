import java.util.Scanner;

public class Lab2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String st = scanner.nextLine();

        int s_count = 0;
        long sd_count = 0;
        long sda_count = 0;

        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) == 's') {
                s_count++;
            } else if (st.charAt(i) == 'd') {
                sd_count += s_count;
            } else if (st.charAt(i) == 'a') {
                sda_count += sd_count;
            }
        }
        System.out.println(sda_count);
    }
}

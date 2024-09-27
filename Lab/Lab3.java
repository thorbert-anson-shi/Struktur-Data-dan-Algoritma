import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);

        // Read inputs
        int N = in.nextInteger();

        int[] heights = new int[N];

        // Process inputs
        for (int i = 0; i < N; i++) {
            int height = in.nextInteger();
            heights[i] = height;
        }

        // Call the function to get the result (if needed)
        int[] res = canSeePersonsCount(heights);

        // Output the result
        // TODO: Output the processed result
        for (int i : res) {
            System.out.print(i + " ");
        }
    }

    // TODO: Implement the logic here as required (e.g., a method to calculate
    public static int[] canSeePersonsCount(int[] height) {
        int n = height.length;
        int[] ans = new int[n];
        Stack<Integer> stac = new Stack<>();
        ans[n - 1] = 0;
        stac.push(height[n - 1]);
        for (int i = n - 2; i >= 0; i--) {
            int count = 0;
            while (!stac.isEmpty() && stac.peek() < height[i]) {
                stac.pop();
                count++;
            }
            if (stac.isEmpty()) {
                ans[i] = count;
            } else {
                ans[i] = count + 1;
            }
            stac.push(height[i]);
        }
        return ans;
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }
}

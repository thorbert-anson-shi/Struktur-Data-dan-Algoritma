import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;
    public static String a, b;
    public static int n, m;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read inputs
        a = in.next();
        b = in.next();

        n = a.length();
        m = b.length();

        // Call the function to get the result
        solve();

        // Output the result
        // TODO: Output the processed result

        // don't forget to close/flush the output
        out.close();
    }

    public static void solve() {
        // TODO: Implement the logic here as required
        long dp[][] = new long[n + 1][m + 1];

        for (int j = 0; j < Math.min(2, n); j++) {
            if (j == 0) {
                if (b.charAt(0) == a.charAt(0))
                    dp[0][0] = 1;
                else
                    dp[0][0] = 0;
            } else if (j == 1) {
                dp[1][0] = dp[0][0];
                if (b.charAt(0) == a.charAt(1))
                    dp[1][0]++;
            }
            for (int i = 1; i < m; i++) {
                dp[j][i] = 0;
            }
        }

        for (int i = 2; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j == 0) {
                    if (b.charAt(j) == a.charAt(i)) {
                        dp[i][0]++;
                    }
                } else if (b.charAt(j) == a.charAt(i)) {
                    dp[i][j] += dp[i - 2][j - 1];
                }
                dp[i][j] %= 1000000007L;
            }
        }

        out.print(dp[n - 1][m - 1]);
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
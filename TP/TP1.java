import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class TP1 {
    public static int n, m, q;
    public static int[] p, v, h;
    public static PriorityQueue<Customer> queue = new PriorityQueue<>(new SortByPriority());
    public static int id = 0;
    public static int timeElapsed = 0;
    public static ArrayDeque<Integer> discounts = new ArrayDeque<>();
    public static long[][] knapsack;
    public static int[][] choices;

    static InputReader in;
    static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read inputs
        n = in.nextInteger();
        m = in.nextInteger();
        q = in.nextInteger();

        p = new int[n];
        v = new int[m];
        h = new int[m];

        // Read prices of fish
        for (int i = 0; i < n; i++) {
            p[i] = in.nextInteger();
        }

        // Read prices of souvenirs
        for (int i = 0; i < m; i++) {
            h[m - i - 1] = in.nextInteger();
        }

        // Read happiness values of souvenirs
        for (int i = 0; i < m; i++) {
            v[m - i - 1] = in.nextInteger();
        }

        knapsack = new long[m + 1][100000 / m + 1];
        choices = new int[m + 1][100000 / m + 1];

        knapsack();

        // Read q queries from the user
        for (int i = 0; i < q; i++) {
            String query = in.next();

            switch (query) {
                case "A":
                    A(in.nextInteger(), in.nextInteger());
                    break;
                case "S":
                    S(in.nextInteger());
                    break;
                case "L":
                    L(in.nextInteger());
                    break;
                case "D":
                    D(in.nextInteger());
                    break;
                case "B":
                    B();
                    break;
                case "O":
                    O(in.nextInteger(), in.nextInteger());
                    break;
            }
            timeElapsed++;
        }

        out.close();
    }

    public static void A(int budget, int patience) {
        queue.add(Customer.createCustomer(id, budget, patience, timeElapsed));
        out.println(id++);
    }

    // Runs binary search across array p
    // No sorting needed as fish prices are guaranteed to be sorted
    public static void S(int target) {
        // Corner cases of binary search
        if (n == 1) {
            out.println(Math.abs(p[0] - target));
            return;
        }

        if (p[0] >= target) {
            out.println(p[0] - target);
            return;
        }

        if (p[n - 1] <= target) {
            out.println(target - p[n - 1]);
            return;
        }

        // Woe woe woe binary search time ✨
        int l = 0, r = n - 1, mid;

        while (p[l] < target) {
            mid = (l + r) / 2;

            if (p[mid] > target) {
                r = mid;
                // Check if right pointer has overshot the target value
                if (p[mid - 1] < target) {
                    out.println(getDiffToClosest(p[mid], p[mid - 1], target));
                    break;
                }
            } else {
                l = mid;
                // Check if left pointer has overshot the target value
                if (p[mid + 1] > target) {
                    out.println(getDiffToClosest(p[mid], p[mid + 1], target));
                    break;
                }
            }
        }
    }

    // Search for customer presence in queue, display customer budget, then remove
    // said customer
    public static void L(int id) {
        Customer currentCustomer = Customer.customers.get(id);

        // Check if customer is valid
        if (currentCustomer == null) {
            out.println(-1);
            return;
        }

        // Check if customer is still in queue
        if (currentCustomer.expirationTime <= timeElapsed) {
            Customer.customers.remove(id);
            out.println(-1);
            return;
        }

        // If a valid customer is found,
        // Print customer budget and remove customer from queue
        out.println(currentCustomer.budget);
        Customer.customers.remove(id);
        queue.remove(currentCustomer);
    }

    // Adds a discount to the stack and prints current stack size
    public static void D(int newDiscount) {
        discounts.addFirst(newDiscount);
        out.println(discounts.size());
    }

    // Process the order of the next person in the queue
    public static void B() {
        Customer currentCustomer = null;

        while (queue.peek() != null && currentCustomer == null) {
            // Remove customer from queue to check for budget
            currentCustomer = queue.poll();

            if (currentCustomer.expirationTime <= timeElapsed) {
                Customer.customers.remove(currentCustomer.id);
                currentCustomer = null;
            }
        }

        if (currentCustomer == null) {
            out.println(-1);
            return;
        }

        int priceTarget = currentCustomer.budget;
        // Binary search for first item less than budget
        int l = 0, r = n - 1, mid, bill = -1;

        if (p[n - 1] < priceTarget) {
            bill = p[n - 1];
        } else if (p[0] > priceTarget) {
            out.println(currentCustomer.id);
            Customer.customers.remove(currentCustomer.id);
            return;
        } else {
            while (l < r) {
                mid = l + (r - l) / 2;
                if (p[mid] > priceTarget) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }

            if (p[l] > priceTarget) {
                bill = p[l - 1]; // Use the value before l if l overshot
            } else {
                bill = p[l]; // l is valid, use the current value
            }
        }

        if (bill == -1) {
            out.println(currentCustomer.id);
            Customer.customers.remove(currentCustomer.id);
        } else if (bill == priceTarget) {
            int transactionDiscount = discounts.peekFirst() == null ? 0 : discounts.pollFirst();
            currentCustomer.budget -= Math.max(1, bill - transactionDiscount);
            // Re-add customer to queue
            currentCustomer.expirationTime = timeElapsed + currentCustomer.patience;
            queue.add(currentCustomer);
            out.println(currentCustomer.budget);
        } else {
            currentCustomer.budget -= bill;
            discounts.addFirst(currentCustomer.budget);
            // Re-add customer to queue
            currentCustomer.expirationTime = timeElapsed + currentCustomer.patience;
            queue.add(currentCustomer);
            out.println(currentCustomer.budget);
        }
    }

    public static void O(int type, int budget) {
        long res = knapsack[m][budget];
        if (type == 1) {
            out.println(res);
        } else {
            out.print(res);
            int level = m;
            int remainingBudget = budget;

            // Iterate down to find the last item taken
            while (remainingBudget > 0 && knapsack[level][remainingBudget] == knapsack[level][remainingBudget - 1]) {
                remainingBudget--;
            }

            while (level > 0) {
                if (choices[level][remainingBudget] == 0) {
                    level--;
                } else if (choices[level][remainingBudget] == 1) {
                    out.print(" " + (m - level + 1));
                    remainingBudget -= h[level - 1];
                    level--;
                } else if (choices[level][remainingBudget] == 2) {
                    out.print(" " + (m - level + 1));
                    remainingBudget -= h[level - 1];
                    level -= 2;
                } else {
                    out.print(" " + (m - level + 1));
                    remainingBudget -= h[level - 1];
                    level--;
                    out.print(" " + (m - level + 1));
                    remainingBudget -= h[level - 1];
                    level -= 2;
                }
            }

            out.println();
        }
    }

    public static void knapsack() {
        long takeBoth, takeOne, noTake;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= 100000 / m; j++) {
                takeBoth = takeOne = noTake = 0;

                if (i >= 3) {
                    if (j >= h[i - 1] + h[i - 2]) {
                        takeBoth = v[i - 1] + v[i - 2] + knapsack[i - 3][j - h[i - 1] - h[i - 2]];
                        takeOne = v[i - 1] + knapsack[i - 2][j - h[i - 1]];
                        noTake = knapsack[i - 1][j];
                    } else if (j >= h[i - 1]) {
                        takeOne = v[i - 1] + knapsack[i - 2][j - h[i - 1]];
                        noTake = knapsack[i - 1][j];
                    } else {
                        noTake = knapsack[i - 1][j];
                    }

                    knapsack[i][j] = getMax(takeBoth, takeOne, noTake);

                    if (knapsack[i][j] == takeBoth) {
                        choices[i][j] = 3;
                    } else if (knapsack[i][j] == takeOne) {
                        choices[i][j] = 2;
                    } else {
                        choices[i][j] = 0;
                    }
                    continue;
                }

                if (i == 2) {
                    if (j >= h[i - 1]) {
                        takeOne = v[i - 1] + knapsack[i - 1][j - h[i - 1]];
                        noTake = knapsack[i - 1][j];
                    } else {
                        noTake = knapsack[i - 1][j];
                    }

                    knapsack[i][j] = Math.max(takeOne, noTake);
                    choices[i][j] = takeOne > noTake ? 1 : 0;
                    continue;
                }

                if (i == 1) {
                    if (j >= h[i - 1]) {
                        takeOne = v[i - 1];
                        noTake = knapsack[i - 1][j];
                    } else {
                        noTake = knapsack[i - 1][j];
                    }

                    knapsack[i][j] = Math.max(takeOne, noTake);
                    choices[i][j] = takeOne > noTake ? 1 : 0;
                }
            }
        }
    }

    public static long getMax(long a, long b, long c) {
        return Math.max(a, Math.max(b, c));
    }

    // Helper function to get closest value to target
    public static int getDiffToClosest(int a, int b, int target) {
        int diffA = Math.abs(a - target);
        int diffB = Math.abs(b - target);
        if (diffA > diffB) {
            return diffB;
        } else
            return diffA;
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

class Customer {
    public int id;
    public int budget;
    public int expirationTime;
    public int patience;

    static HashMap<Integer, Customer> customers = new HashMap<>();

    private Customer(int id, int budget, int patience) {
        this.id = id;
        this.budget = budget;
        this.patience = patience;
    }

    public static Customer createCustomer(int id, int budget, int patience, int currentTime) {
        Customer customer = new Customer(id, budget, patience);
        customer.expirationTime = customer.patience + currentTime;
        customers.put(id, customer);
        return customer;
    }
}

// Create a comparator that sorts the customers by priority specifications
class SortByPriority implements Comparator<Customer> {
    @Override
    public int compare(Customer a, Customer b) {
        // Sort ascending by budget
        if (a.budget != b.budget) {
            return Long.compare(b.budget, a.budget);
        }

        // Sort descending by expirationTime
        if (a.expirationTime != b.expirationTime) {
            return Long.compare(a.expirationTime, b.expirationTime);
        }

        // Sort descending by id
        return Long.compare(a.id, b.id);
    }
}

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class TP1 {
    public static int n, m, q;
    public static int[] p, v, h;
    public static PriorityQueue<Customer> queue = new PriorityQueue<>(new SortByPriority());
    public static int id = 0;
    public static int timeElapsed = 0;

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            // Read inputs
            n = in.nextInt();
            m = in.nextInt();
            q = in.nextInt();

            p = new int[n];
            v = new int[m];
            h = new int[m];

            // Read prices of fish
            for (int i = 0; i < n; i++) {
                p[i] = in.nextInt();
            }

            // Read prices of souvenirs
            for (int i = 0; i < m; i++) {
                h[i] = in.nextInt();
            }

            // Read happiness values of souvenirs
            for (int i = 0; i < m; i++) {
                v[i] = in.nextInt();
            }

            // Read the newline character
            in.nextLine();

            // Read q queries from the user
            for (int i = 0; i < q; i++) {
                System.out.println("i: " + i);
                String[] query = in.nextLine().split(" ");

                switch (query[0]) {
                    case "A" -> {
                        A(Integer.parseInt(query[1]), Integer.parseInt(query[2]));
                    }
                    case "S" -> {
                        S(Integer.parseInt(query[1]));
                    }
                    case "L" -> {
                    }
                    case "D" -> {
                    }
                    case "B" -> {
                    }
                    case "O" -> {
                    }
                    case "Check" -> {
                        while (!queue.isEmpty()) {
                            System.out.print(queue.poll().id + " ");
                        }
                        System.out.println();
                    }
                }
                timeElapsed++;
            }
        }
    }

    public static void A(int budget, int patience) {
        int expirationTime = patience + timeElapsed;
        queue.add(Customer.createCustomer(id, budget, expirationTime));
        System.out.println(id++);
    }

    // Runs binary search across array p
    // No sorting needed as fish prices are guaranteed to be sorted
    public static void S(int target) {
        // Corner cases of binary search
        if (n == 1) {
            System.out.println(Math.abs(p[0] - target));
            return;
        }

        if (p[0] >= target) {
            System.out.println(p[0] - target);
            return;
        }

        if (p[n - 1] <= target) {
            System.out.println(target - p[n - 1]);
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
                    System.out.println(getDiffToClosest(p[mid], p[mid - 1], target));
                    break;
                }
            } else {
                l = mid;
                // Check if left pointer has overshot the target value
                if (p[mid + 1] > target) {
                    System.out.println(getDiffToClosest(p[mid], p[mid + 1], target));
                    break;
                }
            }
        }
    }

    public static void L(int id) {
        System.out.println(Customer.getCustomerById(id).budget);
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
}

class Customer {
    public int id;
    public int budget;
    public int expirationTime;

    static HashMap<Integer, Customer> customers = new HashMap<>();

    private Customer(int id, int budget, int expirationTime) {
        this.id = id;
        this.budget = budget;
        this.expirationTime = expirationTime;
    }

    public static Customer createCustomer(int id, int budget, int expirationTime) {
        Customer customer = new Customer(id, budget, expirationTime);
        customers.put(id, customer);
        return customer;
    }

    public static Customer getCustomerById(int id) {
        return customers.get(id);
    }

    public static void deleteCustomerById(int id) {
        customers.remove(id);
    }
}

// Create a comparator that sorts the customers by priority specifications
class SortByPriority implements Comparator<Customer> {
    @Override
    public int compare(Customer a, Customer b) {
        // Sort ascending by budget
        if (a.budget != b.budget) {
            return a.budget - b.budget;
        }

        // Sort descending by expirationTime
        if (a.expirationTime != b.expirationTime) {
            return b.expirationTime - a.expirationTime;
        }

        // Sort descending by id
        return b.id - a.id;
    }
}

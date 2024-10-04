import java.io.*;
import java.util.StringTokenizer;

/**
 * Note:
 * 1. Mahasiswa tidak diperkenankan menggunakan data struktur dari library
 * seperti ArrayList, LinkedList, dll.
 * 2. Mahasiswa diperkenankan membuat/mengubah/menambahkan class, class
 * attribute, instance attribute, tipe data, dan method
 * yang sekiranya perlu untuk menyelesaikan permasalahan.
 * 3. Mahasiswa dapat menggunakan method {@code printList()} dari class
 * {@code DoublyLinkedList}
 * untuk membantu melakukan print statement debugging dan print hasil akhir.
 **/
public class Lab5 {

    private static InputReader in;
    private static PrintWriter out;
    private static DoublyLinkedList keyboard = new DoublyLinkedList();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        for (int i = 0; i < N; i++) {
            String command = in.next();
            char data;
            char direction;

            switch (command) {
                case "ADD":
                    direction = in.nextChar();
                    data = in.nextChar();
                    keyboard.add(data, direction);
                    break;

                case "DEL":
                    keyboard.delete();
                    break;

                case "MOVE":
                    direction = in.nextChar();
                    keyboard.move(direction);
                    break;

                case "START":
                    keyboard.start();
                    break;

                case "END":
                    keyboard.end();
                    break;

                case "SWAP":
                    keyboard.swap();
                    break;
            }
        }

        keyboard.printList();
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    private static class InputReader {

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

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

class DoublyLinkedList {

    ListNode first;
    ListNode current;
    ListNode last;
    int size = 0;

    public DoublyLinkedList() {
        this.first = null;
        this.current = null;
        this.last = null;
    }

    public void printList() {
        ListNode current = first;
        do {
            System.out.print(current.data);
            current = current.next;
        } while (current != null && current != first);
        System.out.println();
    }

    /*
     * Method untuk menambahkan ListNode dari {@code current} ListNode
     */
    public void add(char data, char direction) {
        // TODO: Implement this method
        if (this.current == null) {
            this.current = new ListNode(data);
            this.first = this.current;
            this.last = this.current;
            this.current.next = null;
            this.current.prev = null;
            size++;
            return;
        }

        switch (direction) {
            case 'L':
                ListNode newNode = new ListNode(data);
                if (current.prev == null) {
                    first = newNode;
                    newNode.prev = null;
                } else {
                    current.prev.next = newNode;
                    newNode.prev = current.prev;
                }

                newNode.next = current;
                current.prev = newNode;
                current = newNode;
                break;
            case 'R':
                ListNode newNodeR = new ListNode(data);
                if (current.next == null) {
                    last = newNodeR;
                    newNodeR.next = null;
                } else {
                    current.next.prev = newNodeR;
                    newNodeR.next = current.next;
                }

                newNodeR.prev = current;
                current.next = newNodeR;
                current = newNodeR;
                break;
        }

        size++;
    }

    /*
     * Method untuk menghapus ListNode dari {@code current} ListNode
     */

    public void delete() {
        if (current == null) {
            return; // List is empty
        }

        if (current.prev == null) { // Deleting first node
            first = current.next;
            if (first != null) {
                first.prev = null;
            } else {
                last = null; // List became empty
            }
        } else {
            current.prev.next = current.next;
        }

        if (current.next == null) { // Deleting last node
            last = current.prev;
        } else {
            current.next.prev = current.prev;
        }

        current = current.prev != null ? current.prev : first; // Move current to previous node or to first
        size--;
    }

    /*
     * Method untuk berpindah ke kiri (prev) atau kanan (next) dari {@code current}
     * ListNode
     */
    public void move(char direction) {
        // TODO: Implement this method
        if (this.current == null) {
            return;
        }

        switch (direction) {
            case 'L':
                if (this.current.prev == null) {
                    return;
                }
                this.current = this.current.prev;
                break;
            case 'R':
                if (this.current.next == null) {
                    return;
                }
                this.current = this.current.next;
                break;
        }
    }

    /*
     * Method untuk berpindah ke node paling pertama (first) dari DoublyLinkedList
     */
    public void start() {
        // TODO: Implement this method
        while (this.current != this.first) {
            this.current = this.current.prev;
        }
    }

    /*
     * Method untuk berpindah ke node paling terakhir (end) dari DoublyLinkedList
     */
    public void end() {
        // TODO: Implement this method
        while (this.current != this.last) {
            this.current = this.current.next;
        }
    }

    /*
     * Method untuk memindahkan semua ListNode dari kiri {@code current} ListNode ke
     * kanan {@code current} ListNode
     */
    public void swap() {
        // TODO: Implement this method
        if (current == first) {
            return;
        }

        ListNode tempFirst = first;
        ListNode tempCurrentPrev = current.prev;

        if (tempCurrentPrev != null) {
            tempCurrentPrev.next = null;
        }

        first = current;
        current.prev = null;

        last.next = tempFirst;
        tempFirst.prev = last;

        last = tempCurrentPrev;

    }
}

class ListNode {

    char data;
    ListNode next;
    ListNode prev;

    ListNode(char data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
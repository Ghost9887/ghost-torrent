package ghost.bencode.encode;

import java.util.HashMap;

public class GhostQueue {

    private Node head;
    private Node tail;
    private int size;

    public GhostQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void push(String key, String value) {
        Node newNode = new Node(key, value);

        if (head == null) {
            head = tail = newNode;
            size++;
            return;
        }else if (isSmaller(key, head.value[0])){
            newNode.next = head;
            head = newNode;
            size++;
            return;
        }else {
            Node current = head;
            while (current.next != null && !isSmaller(key, current.next.value[0])) {
                current = current.next;
            }

            newNode.next = current.next;
            current.next = newNode;

            if (newNode.next == null) {
                tail = newNode;
            }

            size++;
        }
    }

    public String pop() {
        Node temp = head;
        head = head.next;
        size--;
        return temp.value[0] + temp.value[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printQueue() {
        Node current = head;
        System.out.println("Size: " + size);
        while (current != null) {
            System.out.println(current.value[0] + " : " + current.value[1]);
            current = current.next;
        }
    }

    private boolean isSmaller(String key, String current) {
        
        int keyIndex = 0;
        int currentIndex = 0;
        while (isNum(key.charAt(keyIndex))) keyIndex++;
        keyIndex++;
        while(isNum(current.charAt(currentIndex))) currentIndex++;
        currentIndex++;

        String newRaw = key.substring(keyIndex, key.length());
        String currentRaw = current.substring(currentIndex, current.length());

        return newRaw.compareTo(currentRaw) < 0;
    }

    public boolean isNum(char c) {
        return '0' <= c && c <= '9';
    }

}

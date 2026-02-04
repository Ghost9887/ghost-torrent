package ghost.bencode.encode;

public class Node {
    public String[] value = new String[2];
    public Node next;

    public Node(String key, String value) {
        this.value[0] = key;
        this.value[1] = value;
        next = null;
    }
}

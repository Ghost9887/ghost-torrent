package ghost.bencode;

import ghost.bencode.encode.Bencode;
import java.nio.file.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file given");
            System.out.println("usage: <filename> <action>");
            System.exit(1);
        }else if(args.length == 1) {
            System.out.println("No action given");
            System.out.println("usage: <filename> <action>");
            System.exit(1);
        }
        else if(args.length > 2) {
            System.out.println("Too many args given");
            System.out.println("usage: <filename> <action>");
            System.exit(1);
        }else {
            try {
                if (args[1].equals("decode")) {
                    byte[] content = Files.readAllBytes(Path.of(args[0]));
                    System.out.println("Decoded: " + new Bencode(content).decode());
                }
                else if (args[1].equals("encode")) {
                    byte[] content = Files.readAllBytes(Path.of(args[0]));
                    System.out.println("Encoded: " + new Bencode(content).encode());
                }
                else {
                    System.out.println("Invalid action try 'decode' or 'encode'");
                    System.exit(1);
                }
            }catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

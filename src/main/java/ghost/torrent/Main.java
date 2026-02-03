package ghost.torrent;

import ghost.torrent.encode.Bencode;
import java.nio.file.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No file given");
            System.exit(1);
        }else if(args.length > 1) {
            System.out.println("Too many args given");
            System.exit(1);
        }else {
            try {
                byte[] torrentBytes = Files.readAllBytes(Path.of(args[0]));
                String decoded = new Bencode(torrentBytes).decode();
                System.out.println("Decoded: " + decoded);
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }
}

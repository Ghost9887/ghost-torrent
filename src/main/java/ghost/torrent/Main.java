package ghost.torrent;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No arguments passed");
            System.exit(-1);
        }
        else if (args.length > 1) {
            System.out.println("Too many arguments passed");
            System.exit(-1);
        }
        else {
            printArgs(args);
        }
    }

    private static void printArgs(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}

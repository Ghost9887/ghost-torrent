package ghost.torrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ghost.torrent.encode.*;

public class AppTest {

    @Test
    public void EncodeString() {
        Bencode ben = new Bencode("'coding'");
        String expected = "6:coding";

        assertEquals(expected, ben.encode());
    }

    @Test
    public void EnocdeInt() {
        Bencode ben = new Bencode("100");
        String expected = "i100e";

        assertEquals(expected, ben.encode());
    } 

    @Test
    public void EncodeList() {
        Bencode ben = new Bencode("[\"Coding\", \"Challenges\"]");
        String expected = "l6:Coding10:Challengese";

        assertEquals(expected, ben.encode());
    }

    @Test void EncodeNestedList() {
        Bencode ben = new Bencode("[\"Coding\", [\"Challenges\", [\"Test\"]], \"Test1\"]");
        String expected = "l6:Codingl10:Challengesl4:Testee5:Test1e";

        assertEquals(expected, ben.encode());
    }

    @Test
    public void Encode() {
        Bencode ben = new Bencode("'coding' 100 [\"Coding\", \"Challenges\"]");
        String expected = "6:codingi100el6:Coding10:Challengese";

        assertEquals(expected, ben.encode());
    }
    
    /*
    @Test
    public void Encode() {
        
        Bencode ben = new Bencode(
            "'coding' 100 [\"Coding\", \"Challenges\"] " +
            "{\"Coding Challenges\": \"Torrent\", \"Day\": \"Sunday\", \"Api\": \"No\"}"
        );
        
        String expected =
            "6:codingi100el6:Coding10:Challengesed3:Api2:No17:Coding Challenges"+   
            "7:Torrent3:Day6:Sundaye";


        assertEquals(expected, ben.encode());
    }
    */

}

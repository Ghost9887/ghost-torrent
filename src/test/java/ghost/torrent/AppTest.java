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
        Bencode ben = new Bencode(
            "[ " +
            "  'hello' " +
            "  123 " +
            "  [ 'nested' 456 [ 'deep' 789 ] ] " +
            "  [] " +
            "  [ 'end' ] " +
            "]"
        );

        String expected = "l5:helloi123el6:nestedi456el4:deepi789eeelel3:endee";
        assertEquals(expected, ben.encode());
    }

    /*
    @Test
    public void EncodeDict() {
        Bencode ben = new Bencode ("{\"Day\": \"sunday\", \"challenge\": \"torrent\"}");
        String expected = "d3:Day6:sunday9:challenge7:torrente";

        assertEquals(expected, ben.encode());
    }

    @Test
    public void EncodeNestedDict() {
        Bencode ben = new Bencode ("{\"Coding Challenges\": {\"website:\": \"codingchallenges.fyi\", \"Rating\": \"Awesome\"}}");
        String expected = "d17:Coding Challengesd6:Rating7:Awesome8:website:20:codingchallenges.fyiee";

        assertEquals(expected, ben.encode());
    }  

    @Test
    public void Encode() {
        Bencode ben = new Bencode(
            "{ " +
            "  'title' 'Example File' " +
            "  'year' 2024 " +
            "  'tags' ['bencode' 'java' 'encoding'] " +
            "  'author' { " +
            "     'name' 'Alice' " +
            "     'id' 42 " +
            "  } " +
            "  'ratings' [5 4 5] " +
            "}"
        );
        String expected = "d6:authord2:idi42e4:name5:Aliceee7:ratingsli5ei4ei5ee4:tagsl7:bencode4:java8:encodinge5:title12:Example File4:yeari2024ee";

        assertEquals(expected, ben.encode());
    }
    */
}

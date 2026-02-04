package ghost.bencode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ghost.bencode.encode.*;

public class AppTest {

    @Test
    public void decodeString() {
        Bencode ben = new Bencode("6:coding".getBytes());
        String expected = "'coding' ";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeNum() {
        Bencode ben = new Bencode("i100e".getBytes());
        String expected = "100 ";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeStringNum() {
        Bencode ben = new Bencode("6:codingi100e".getBytes());
        String expected = "'coding' 100 ";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeList() {
        Bencode ben = new Bencode("l6:Coding10:Challengese".getBytes());
        String expected = "['Coding' 'Challenges' ]";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeNestedList() {
        Bencode ben = new Bencode("l5:helloi123el6:nestedi456el4:deepi789eeelel3:endee".getBytes());
        String expected = "[" +
            "'hello' " +
            "123 " +
            "['nested' 456 ['deep' 789 ]]" +
            "[]" +
            "['end' ]" +
            "]";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeDict() {
        Bencode ben = new Bencode("d3:Day6:sunday9:challenge7:torrente".getBytes());
        String expected = "{'Day' : 'sunday' 'challenge' : 'torrent' }";

        assertEquals(expected, ben.decode());
    }

    @Test
    public void decodeNestedDict() {
        Bencode ben = new Bencode("d17:Coding Challengesd6:Rating7:Awesome8:website:20:codingchallenges.fyiee".getBytes());
        String expected = "{'Coding Challenges' : {'Rating' : 'Awesome' 'website:' : 'codingchallenges.fyi' }}";

        assertEquals(expected, ben.decode());
    }

    

    @Test
    public void EncodeString() {
        Bencode ben = new Bencode("'coding'".getBytes());
        String expected = "6:coding";

        assertEquals(expected, ben.encode());
    }

    @Test
    public void EnocdeInt() {
        Bencode ben = new Bencode("100".getBytes());
        String expected = "i100e";

        assertEquals(expected, ben.encode());
    } 

    @Test
    public void EncodeList() {
        Bencode ben = new Bencode("[\"Coding\", \"Challenges\"]".getBytes());
        String expected = "l6:Coding10:Challengese";

        assertEquals(expected, ben.encode());
    }

    @Test void EncodeNestedList() {
        Bencode ben = new Bencode(
            "['hello' 123 [ 'nested' 456 ['deep' 789 ]] [] [ 'end' ]]".getBytes()
        );

        String expected = "l5:helloi123el6:nestedi456el4:deepi789eeelel3:endee";
        assertEquals(expected, ben.encode());
    }

    
    @Test
    public void EncodeDict() {
        Bencode ben = new Bencode ("{\"Day\": \"sunday\", \"challenge\": \"torrent\"}".getBytes());
        String expected = "d3:Day6:sunday9:challenge7:torrente";

        assertEquals(expected, ben.encode());
    }

    @Test
    public void EncodeNestedDict() {
        Bencode ben = new Bencode ("{\"Coding Challenges\": {\"website:\": \"codingchallenges.fyi\", \"Rating\": \"Awesome\"}}".getBytes());
        String expected = "d17:Coding Challengesd6:Rating7:Awesome8:website:20:codingchallenges.fyiee";

        assertEquals(expected, ben.encode());
    }  

    
    @Test
    public void Encode() {
        Bencode ben = new Bencode(
            "{'title' 'Example File' 'year' 2024 'tags' ['bencode' 'java' 'encoding'] 'author' { 'name' 'Alice' 'id' 42 } 'ratings' [5 4 5]}".getBytes()
        );
        String expected = "d6:authord2:idi42e4:name5:Alicee7:ratingsli5ei4ei5ee4:tagsl7:bencode4:java8:encodinge5:title12:Example File4:yeari2024ee";

        assertEquals(expected, ben.encode());
    }


    @Test
    public void decodeEncode() {
        Bencode ben = new Bencode("d6:authord2:idi42e4:name5:Alicee7:ratingsli5ei4ei5ee4:tagsl7:bencode4:java8:encodinge5:title12:Example File4:yeari2024ee".getBytes());
        Bencode ben2 = new Bencode(ben.decode().getBytes());

        String expected = "d6:authord2:idi42e4:name5:Alicee7:ratingsli5ei4ei5ee4:tagsl7:bencode4:java8:encodinge5:title12:Example File4:yeari2024ee";
        assertEquals(expected, ben2.encode());
    }
}

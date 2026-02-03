package ghost.torrent.encode;

import java.util.Optional;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;

public class Bencode {
    
    private final byte[] content;
    private int ip = 0;

    public Bencode(byte[] content) {
        this.content = content;
    }

    private Optional<Byte> peek() {
        if (ip < content.length) {
             return Optional.of(content[ip]);
        }
        return Optional.empty();
    }

    private void advance() {
        ip++;
    }

    private void retreat() {
        ip--;
    }

    public String decode() {
        StringBuilder res = new StringBuilder();
    
        while(true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case 'i':
                        res.append(decodeNum());
                        break;
                    case 'l':
                        res.append(decodeList());
                        break;
                    case 'd':
                        res.append(decodeDict());
                        break;
                    default:
                        if(isNum(c)) {
                            res.append(decodeWord());
                        }
                        break;
                }
                advance();
            }else {
                break;
            }
        }

        return res.toString();
    }

    public String decodeDict() {
        StringBuilder str = new StringBuilder();
        str.append('{');
        int counter = 0;

        advance();
        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case 'e':
                        str.append('}');
                        return str.toString();
                    case 'l':
                        str.append(decodeList());
                        break;
                    case 'd':
                        str.append(decodeDict());
                        counter = 0;
                        break;
                    case 'i':
                        str.append(decodeNum());
                        counter++;
                        break;
                    default:
                        if (isNum(c)) {
                            str.append(decodeWord());
                            counter++;
                        }
                        break;
                }
                if (counter % 2 == 1){
                    str.append(':');
                }else {
                    counter = 0;
                }
                advance();
            }else break;
        }

        return null;
    }

    public String decodeList() {
        StringBuilder str = new StringBuilder();
        str.append('[');

        advance();
        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case 'e':
                        str.append(']');
                        return str.toString();
                    case 'd':
                        str.append(decodeDict());
                        break;
                    case 'l':
                        str.append(decodeList());
                        break;
                    case 'i':
                        str.append(decodeNum());
                        break;
                    default:
                        if (isNum(c)) {
                            str.append(decodeWord());
                        }
                        break;
                }
                advance();
            }else break;
        }

        return null;
    }

    public String decodeNum() {
        StringBuilder str = new StringBuilder();

        advance();
        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                if (isNum(c)) {
                    str.append((char)c);
                    advance();
                }
                else{
                    break;
                }
            }else return null;
        }

        str.append(' ');
        return str.toString();
    }

    public String decodeWord() {
        StringBuilder str = new StringBuilder();
        StringBuilder length = new StringBuilder();

        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                if (isNum(c)) {
                    length.append((char) c);
                    advance();
                }else {
                    str.append("'");
                    advance();
                    break;
                }
            }else return null;
        }

        int l = Integer.parseInt(length.toString());
        //skip the pecies
        if (l > 1000) {
            for (int i = 0; i < l - 1; i++) {
                advance();
            }
            str.append("pieces' ");
        }else {
            for (int i = 0; i < l - 1; i++) {
                byte b = peek().get();
                str.append((char) b);
                advance();
            }
            byte b = peek().get();
            str.append((char) b);
            str.append("' ");
        }

        return str.toString();
    }


    public String encode() {
        StringBuilder res = new StringBuilder();
    
        while(true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case ' ':
                        break;
                    case '[':
                        res.append(encodeList());
                        break;
                    case '{':
                        res.append(encodeDict());
                        break;
                    case '\'':
                    case '"':
                        res.append(encodeWord());
                        break;
                    default:
                        if(isNum(c)) {
                            res.append(encodeNum());
                        }
                        break;
                }
                advance();
            }else {
                break;
            }
        }

        return res.toString();
    }

    private String encodeDict() {
        StringBuilder str = new StringBuilder();
        List<String> list = new ArrayList<>();
        GhostQueue q = new GhostQueue();
        
        str.append('d');

        advance();
        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case '"':
                    case '\'':
                        list.add(encodeWord());
                        break;
                    case '[':
                        list.add(encodeList());
                        break;
                    case '{':
                        list.add(encodeDict());
                        break;
                    case '}':
                        while(!q.isEmpty()) {
                            str.append(q.pop());
                        }
                        str.append('e');
                        return str.toString();
                    default:
                        if (isNum(c)){
                            list.add(encodeNum());    
                            continue;
                        }
                        break;
                }
                if (list.size() >= 2) {
                    q.push(list.get(0), list.get(1));
                    list.clear();
                }
                advance();
            }else {
                break;
            }
        }
        return null;
    }

    private String encodeList() {
        StringBuilder str = new StringBuilder();
        str.append('l');

        advance();
        while (true) {
            Optional<Byte> cOpt = peek();
            if (cOpt.isPresent()) {
                byte c = cOpt.get();
                switch (c) {
                    case '{':
                        str.append(encodeDict());
                        break;
                    case '\'':
                    case '"':
                        str.append(encodeWord());
                        break;
                    case '[':
                        str.append(encodeList());
                        break;
                    case ']':
                        str.append('e');
                        return str.toString();
                    default:
                        if (isNum(c)) {
                            str.append(encodeNum());
                            continue;
                        };
                        break;
                }
                advance();
            }else {
                break;
            }
        }
        return null;
    }

    private String encodeWord() {
        advance();
        StringBuilder str = new StringBuilder();
        StringBuilder word = new StringBuilder();

        while(true) {
            Optional<Byte> c = peek();
            if (c.isPresent() && c.get() != '"' && c.isPresent() && c.get() != '\'') {
                byte b = c.get();
                word.append((char) b);       
                advance();
            }else {
                break;
            }
        }
        
        str.append(String.valueOf(word.length()));
        str.append(':');
        str.append(word);

        return str.toString();
    }

    private String encodeNum() {
        StringBuilder str = new StringBuilder();
        str.append('i');
        while(true) {
            Optional<Byte> c = peek();
            if (c.isPresent() && isNum(c.get())) {
                byte b = c.get();
                str.append((char) b);       
                advance();
            }else {
                break;
            }
        }
        str.append('e');
        return str.toString();
    }

    private boolean isNum(byte c) {
        return '0' <= c && c <= '9' || c == '-';
    }
}

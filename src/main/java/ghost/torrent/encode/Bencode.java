package ghost.torrent.encode;

import java.util.Optional;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Collections;

public class Bencode {
    
    private final String content;
    private int ip = 0;

    public Bencode(String content) {
        this.content = content;
    }

    private Optional<Character> peek() {
        if (ip < content.length()) {
            char c = content.charAt(ip);
            return Optional.of(c);
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
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
                switch (c) {
                    case 'i':
                        res.append(decodeNum());
                        break;
                    default:
                        if(isNum(c)) {
                            res.append(decodeWord());
                        }
                        break;
                }
                System.out.println(res.toString());
                advance();
            }else {
                break;
            }
        }

        return res.toString();
    }

    public String decodeNum() {
        StringBuilder str = new StringBuilder();

        advance();
        while (true) {
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
                if (isNum(c)) {
                    str.append(c);
                    advance();
                }
                else{
                    advance();
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
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
                if (isNum(c)) {
                    length.append(c);
                    advance();
                }else {
                    str.append("'");
                    advance();
                    break;
                }
            }else return null;
        }

        int l = Integer.parseInt(length.toString());
        for (int i = 0; i < l - 1; i++) {
            str.append(peek().get());
            advance();
        }
        str.append(peek().get());
        str.append("' ");
        return str.toString();
    }

    public String encode() {
        StringBuilder res = new StringBuilder();
    
        while(true) {
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
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
                        }else {
                            System.out.println("Invalid char: " + c);
                            return null;
                        }
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
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
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
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent()) {
                char c = cOpt.get();
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
            Optional<Character> c = peek();
            if (c.isPresent() && c.get() != '"' && c.isPresent() && c.get() != '\'') {
                word.append(c.get());       
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
            Optional<Character> c = peek();
            if (c.isPresent() && isNum(c.get())) {
                str.append(c.get());       
                advance();
            }else {
                break;
            }
        }
        str.append('e');
        return str.toString();
    }

    private boolean isNum(char c) {
        return '0' <= c && c <= '9' || c == '-';
    }
}

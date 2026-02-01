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
                        res.append(parseList());
                        break;
                    case '{':
                        res.append(parseDict());
                        break;
                    case '\'':
                    case '"':
                        res.append(parseWord());
                        break;
                    default:
                        if(isNum(c)) {
                            res.append(parseNum());
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

    public void decode() {
    }

    private String parseDict() {
        StringBuilder str = new StringBuilder();
        return str.toString();
    }

    private String parseList() {
        StringBuilder str = new StringBuilder();

        Stack<Character> stack = new Stack<>();
        stack.push('[');
        str.append('l');

        advance();
        while (true) {
            Optional<Character> cOpt = peek();
            if (cOpt.isPresent() && !stack.isEmpty()) {
                char c = cOpt.get();
                switch (c) {
                    case '{':
                        str.append(parseDict());
                        break;
                    case '\'':
                    case '"':
                        str.append(parseWord());
                        break;
                    case '[':
                        stack.push('[');
                        str.append('l');
                        break;
                    case ']':
                        stack.pop();
                        str.append('e');
                    default:
                        if (isNum(c)) {
                            str.append(parseNum());
                        }
                        break;
                }
                advance();
            }else {
                break;
            }
        }

        return str.toString();
    }

    private String parseWord() {
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

    private String parseNum() {
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
        return '0' <= c && c <= '9';
    }
}

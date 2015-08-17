package chat.transcripts.reader;

import java.io.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 *
 * @author Ted Streit
 */

// Converts HTML to raw text
public class HTML2Text extends HTMLEditorKit.ParserCallback {
    StringBuffer s;
    public HTML2Text() {}
    public void parse(Reader in) throws IOException {
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        delegator.parse(in, this, Boolean.TRUE);
    }
    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
        s.append("\n");
    }
    public String getText() {
        return s.toString();
    }
}

package chat.transcripts.reader;

/**
 *
 * @author Ted Streit
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GetInfo gi = new GetInfo();
        gi.doSomeWork();
        ChatTranscripts ct = new ChatTranscripts();
        ct.show();
    }
}
package anthony.mail.usable;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;


public class Mail implements Serializable{

    private String from;
    private String to;

    private String subject;
    private String text;

    //https://docs.oracle.com/javase/8/docs/api/java/sql/Timestamp.html
    private final Timestamp timestamp;

    public Mail(String from, String to, String subject, String text){

        setFrom(from);
        setTo(to);
        setSubject(subject);
        setText(text);
        this.timestamp = new Timestamp(System.currentTimeMillis());

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void createMail(){

    }
    public void saveDraft() {
        String fileName = "/drafts/" + getFrom() + "/" + getSubject() + "-" + timestamp.getTime() + ".ser";
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
            System.out.println("Draft saved successfully at: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while saving draft.");
        }
    }
}

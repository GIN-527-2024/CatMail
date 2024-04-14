package anthony;

import anthony.mail.usable.Mail;

public class test {
    public static void main(String[] args){

        Mail mail = new Mail("a", "t", "aa","aaaa");
        System.out.println(mail.getTimestamp());


    }
}

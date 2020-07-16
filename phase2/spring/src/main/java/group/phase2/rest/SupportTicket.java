package group.phase2.rest;

public class SupportTicket {

    private final String title;
    private final String content;

    public SupportTicket(String title,String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}

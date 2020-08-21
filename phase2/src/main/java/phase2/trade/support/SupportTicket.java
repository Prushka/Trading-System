package phase2.trade.support;

import phase2.trade.user.User;

import javax.persistence.*;

/**
 * The Support ticket.
 *
 * @author Dan Lyu
 */
@Entity
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    private User submitter;

    @OneToOne
    private User handler;

    private TicketType type;

    private TicketPriority ticketPriority;

    private String content;

    private TicketState ticketState;

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * Gets submitter.
     *
     * @return the submitter
     */
    public User getSubmitter() {
        return submitter;
    }

    /**
     * Sets submitter.
     *
     * @param submitter the submitter
     */
    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TicketType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(TicketType type) {
        this.type = type;
    }

    /**
     * Gets ticket priority.
     *
     * @return the ticket priority
     */
    public TicketPriority getTicketPriority() {
        return ticketPriority;
    }

    /**
     * Sets ticket priority.
     *
     * @param ticketPriority the ticket priority
     */
    public void setTicketPriority(TicketPriority ticketPriority) {
        this.ticketPriority = ticketPriority;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets ticket state.
     *
     * @return the ticket state
     */
    public TicketState getTicketState() {
        return ticketState;
    }

    /**
     * Sets ticket state.
     *
     * @param ticketState the ticket state
     */
    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    /**
     * Gets handler.
     *
     * @return the handler
     */
    public User getHandler() {
        return handler;
    }

    /**
     * Sets handler.
     *
     * @param handler the handler
     */
    public void setHandler(User handler) {
        this.handler = handler;
    }
}

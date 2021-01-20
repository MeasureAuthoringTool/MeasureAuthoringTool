package mat.client.umls.service;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class VsacTicketInformation implements IsSerializable {
    private String ticket;

    private String apiKey;

    private Date timeout;

    public VsacTicketInformation() {

    }

    public VsacTicketInformation(String eightHourTicketForUser, Date date, String apiKey) {
        this.ticket = eightHourTicketForUser;
        this.timeout = date;
        this.apiKey = apiKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setTicketIfNotBlank(String ticket) {
        if (ticket != null) {
            setTicket(ticket);
        }
    }
}

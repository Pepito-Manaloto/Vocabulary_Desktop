/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.others;

import com.aaron.desktop.model.log.LogManager;
import java.nio.file.Paths;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Class that handles Emails. 
 * @author Aaron
 */
public class Mailer
{
    private final LogManager logger = LogManager.getInstance();
    private final String className = this.getClass().getSimpleName();

    private final String sender;
    private final String recipient;
    private String errorMessage;

    public Mailer(final String sender, final String recipient)
    {
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * Sends email with optional file attachments.
     * @param subject the email subject
     * @param body the email body
     * @param attachments 0..N file attachments
     * @return true on success, else false
     */
    public boolean sendMail(final String subject, final String body, final String... attachments)
    {
        EmailAttachment[] emailAttachments = this.createAttachments(attachments);
        int count = emailAttachments.length;

        try
        {
            Email email;
            if(count >= 1)
            {
                email = new MultiPartEmail();

                for(EmailAttachment attach: emailAttachments)
                {
                   ((MultiPartEmail)email).attach(attach);
                }
            }
            else
            {
                email = new SimpleEmail();
            }

            email.setHostName("localhost");
            email.setSmtpPort(587);
            email.setAuthentication("admin", "admin");
            email.addTo(this.recipient);
            email.setFrom(this.sender);
            email.setSubject(subject);
            email.setMsg(body);

            email.send();
        }
        catch (final EmailException ex)
        {
            ex.printStackTrace();
            logger.error(this.className, "sendMail(String, String, String...)", ex.toString(), ex);
            this.errorMessage = ex.toString();
            return false;
        }

        return true;
    }

    /**
     * Creates file attachments objects and returns as array.
     * @param attachments 0..N file attachments
     * @return EmailAttachment[]
     */
    private EmailAttachment[] createAttachments(String... attachments)
    {
        int count = attachments.length;
        EmailAttachment[] emailAttachments = new EmailAttachment[count];

        for(int i = 0; i < count; i++)
        {
            emailAttachments[i] = new EmailAttachment();
            emailAttachments[i].setPath(attachments[i]);
            emailAttachments[i].setDisposition(EmailAttachment.ATTACHMENT);
            emailAttachments[i].setDescription("MySQL dump backup of Vocabulary database.");
            emailAttachments[i].setName(Paths.get(attachments[i]).getFileName().toString());
        }

        return emailAttachments;
    }

    /**
     * Returns the error messag encountered by using the methods of this class.
     * @return String
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}

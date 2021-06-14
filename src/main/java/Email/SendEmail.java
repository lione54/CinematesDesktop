package Email;


import org.example.BuildConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SendEmail {

    public static void send(String host, String port, final String userName, final String password, String toAddress, String subject, String htmlBody, Map<String, String> mapInlineImages, String Tipo) throws AddressException, MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);


        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        MimeBodyPart messageBodyPart1 = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        if(Tipo.equals("Consigliati")) {
            messageBodyPart1.attachFile(BuildConfig.Promozioni);
            multipart.addBodyPart(messageBodyPart1);
        }
        messageBodyPart.setContent(htmlBody, "text/html");
        multipart.addBodyPart(messageBodyPart);
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();
            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                multipart.addBodyPart(imagePart);
            }
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }

}

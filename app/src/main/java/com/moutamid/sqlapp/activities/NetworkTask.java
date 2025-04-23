package com.moutamid.sqlapp.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class NetworkTask extends AsyncTask<List<String>, Void, Boolean> {
    private Activity mContext;

    public NetworkTask(Activity context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(List<String>... imageUriLists) {
        try {
            final String username = "arbkan05@gmail.com";
            final String password = "trmjndgxgbjvfegj";
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            String storedDocumentTitle = Stash.getString("documentTitle", "");
            String storedCountryDocument = Stash.getString("countryDocument", "");
            String storedDocumentNumber = Stash.getString("documentNumber", "");
            String storedExpireDate = Stash.getString("expireDate", "");
            String storedIssuedBy = Stash.getString("issuedBy", "");
            String storedIssuedDate = Stash.getString("issuedDate", "");
            String storedNote = Stash.getString("note", "");
            String storedDocumentType = Stash.getString("documentType", "");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("arbkan05@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Stash.getString("email")));
            message.setSubject(Stash.getString("name"));

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String messageBody = "<p>" + Stash.getString("message") + "<br>" +
                    "<b>" + storedDocumentTitle + "</b><br>" +
                    "<b>Type:  </b>" + storedDocumentType + "<br>";
            if (storedDocumentNumber != null && !storedDocumentNumber.isEmpty()) {
                messageBody += "<b>Number:  </b>" + storedDocumentNumber + "<br>";
            }
            if (storedIssuedDate != null && !storedIssuedDate.isEmpty()) {
                messageBody += "<b>Issue Date:  </b>" + storedIssuedDate + "<br>";
            }
            if (storedExpireDate != null && !storedExpireDate.isEmpty()) {
                messageBody += "<b>Expire Date:  </b>" + storedExpireDate + "<br>";
            }
            if (storedIssuedBy != null && !storedIssuedBy.isEmpty()) {
                messageBody += "<b>Issued by:  </b>" + storedIssuedBy + "<br>";
            }
            if (storedCountryDocument != null && !storedCountryDocument.isEmpty()) {
                messageBody += "<b>Country of issue:  </b>" + storedCountryDocument + "<br>";
            }
            if (storedNote != null && !storedNote.isEmpty()) {
                messageBody += "<b>Note:</b>  " + storedNote + "<br>";
            }
            messageBody += "</p>";
            messageBodyPart.setContent(messageBody, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (List<String> imageUriList : imageUriLists) {
                for (String imageUri : imageUriList) {
                    Log.d("images ", imageUri + "  1");
                    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                    try {
                        DataSource source = new FileDataSource(imageUri);
                        attachmentBodyPart.setDataHandler(new DataHandler(source));
                        attachmentBodyPart.setFileName("image.jpg");
                        multipart.addBodyPart(attachmentBodyPart);
                    } catch (Exception e) {
                        Log.e("SendEmailTask", "Error sending image", e);
                    }
                }
            }
//            MimeBodyPart pdfBodyPart = new MimeBodyPart();
//            try {
//                DataSource pdfSource = new FileDataSource("/storage/emulated/0/Download/Fiza CV-1.pdf");
//                pdfBodyPart.setDataHandler(new DataHandler(pdfSource));
//                pdfBodyPart.setFileName("document.pdf");
//                multipart.addBodyPart(pdfBodyPart);
//            } catch (Exception e) {
//                Log.e("SendEmailTask", "Error sending PDF", e);
//            }


            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            Log.e("SendEmailTask", "Error sending email", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            // Email sent successfully, show success toast
            Toast.makeText(mContext, "Email sent successfully!", Toast.LENGTH_SHORT).show();
            mContext.finish();
        } else {
            // Error sending email, show error toast
            Toast.makeText(mContext, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
        }
        // You can also finish the activity here if needed
        // SendActivity.activity.finish();
    }
}

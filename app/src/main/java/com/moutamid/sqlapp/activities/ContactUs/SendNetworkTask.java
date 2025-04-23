package com.moutamid.sqlapp.activities.ContactUs;

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
public class SendNetworkTask extends AsyncTask<Void, Void, Boolean> { // Changed parameter type

    private Activity mContext;

    public SendNetworkTask(Activity context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) { // Removed imageUriLists parameter
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("arbkan05@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(Stash.getString("email_address")));
            message.setSubject(Stash.getString("email_first_name")+" "+Stash.getString("email_last_name"));


            message.setText(Stash.getString("email_message"));
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
        }
    }
}
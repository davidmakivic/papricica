package com.restaurant.papricica.service;

import com.restaurant.papricica.entity.EmailVerificationToken;
import com.restaurant.papricica.entity.ForgotPasswordToken;
import com.restaurant.papricica.repository.EmailVerificationTokenRepository;
import com.restaurant.papricica.repository.ForgotPasswordTokenRepository;
import com.restaurant.papricica.security.TokenHasher;
import com.restaurant.papricica.util.UserStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.Instant;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final TokenHasher tokenHasher;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;


    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from}") String fromAddress, TokenHasher tokenHasher, EmailVerificationTokenRepository emailVerificationTokenRepository, ForgotPasswordTokenRepository forgotPasswordTokenRepository, PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.tokenHasher = tokenHasher;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendVerificationEmail(String recipient, String confirmationUrl) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(recipient);
            helper.setSubject("Confirm your account");

            String html = """
                <html>
                  <body>
                    <h2>Confirm your account</h2>
                    <p>
                      Click the link below to verify your email address:
                    </p>
                    <p>
                      <a href="%s">Confirm account</a>
                    </p>
                    <p>
                      This link expires in 30 minutes.
                    </p>
                    <p>
                      If you did not create this account,
                      you can ignore this email.
                    </p>
                  </body>
                </html>
                """.formatted(
                    HtmlUtils.htmlEscape(confirmationUrl)
            );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException(  // CREATE NEW SPECIFIC EXCEPTION
                    "Verification email could not be created."
            );
        } catch (MailException exception) {
            throw new RuntimeException( // SAME
                    "Verification email could not be sent."
            );
        }
    }

    @Transactional
    public ResponseEntity<String> verify (String rawToken) {
        String hashedToken = tokenHasher.hash(rawToken);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(hashedToken);

        if(emailVerificationToken == null) {
            return ResponseEntity.
                    badRequest().
                    body("""
            <html>
                <body>
                    <h2>There has been a problem with your request</h2>
                </body>
            </html>
            """);
        }

        if(emailVerificationToken.isExpired()){
            return ResponseEntity.
                    badRequest().
                    body("""
            <html>
                <body>
                    <h2>There has been a problem with your request</h2>
                </body>
            </html>
            """);
        }

        emailVerificationToken.getUser().setStatus(UserStatus.UNLOCKED);
        emailVerificationTokenRepository.deleteById(emailVerificationToken.getId());

        return ResponseEntity.ok("""
        <html>
            <body>
                <h2>Email successfully verified</h2>
                <p>You can return to the app and log in.</p>
            </body>
        </html>
        """);
    }


    public void sendForgotPasswordEmail(String recipient, String confirmationUrl) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(recipient);
            helper.setSubject("Reset your password");

            String html = """
                <html>
                  <body>
                    <h2>Reset your password</h2>
                    <p>
                      Click the link below to reset your password:
                    </p>
                    <p>
                      <a href="%s">Link to reset your password</a>
                    </p>
                    <p>
                      This link expires in 15 minutes.
                    </p>
                    <p>
                      If you did not request a reset of your password,
                      you can ignore this email.
                    </p>
                  </body>
                </html>
                """.formatted(
                    HtmlUtils.htmlEscape(confirmationUrl)
            );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException(  // CREATE NEW SPECIFIC EXCEPTION
                    "Verification email could not be created."
            );
        } catch (MailException exception) {
            throw new RuntimeException( // SAME
                    "Verification email could not be sent."
            );
        }
    }

    @Transactional
    public void verifyTokenAndResetPassword(String rawToken, String password) {
        String hashedToken = tokenHasher.hash(rawToken);

        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(hashedToken);

        if(forgotPasswordToken == null) {
            throw new RuntimeException("This link is not valid, it might have expired");
        }

        if(forgotPasswordToken.isExpired()){
            throw new RuntimeException("This link is not valid, it might have expired");
        }

        forgotPasswordToken.getUser().setPasswordHash(passwordEncoder.encode(password));
        forgotPasswordTokenRepository.deleteById(forgotPasswordToken.getId());
    }
}

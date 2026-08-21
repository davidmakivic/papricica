package com.restaurant.papricica.endpoints;

import com.restaurant.papricica.service.EmailService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("api/v1/auth")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PermitAll
    @GetMapping(value = "/verify-email", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        return emailService.verify(token);
    }

    @PermitAll
    @GetMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> resetPasswordPage(@RequestParam String token) {
        String html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Reset password</title>
        </head>

        <body>
            <h2>Set a new password</h2>

            <form method="post" action="/api/v1/auth/reset-password">
                <input
                    type="hidden"
                    name="token"
                    value="%s"
                />

                <label>New password</label>
                <input
                    type="password"
                    name="password"
                    required
                />

                <br><br>

                <label>Confirm password</label>
                <input
                    type="password"
                    name="confirmPassword"
                    required
                />

                <br><br>

                <button type="submit">
                    Change password
                </button>
            </form>
        </body>
        </html>
        """.formatted(
                HtmlUtils.htmlEscape(token)
        );

        return ResponseEntity.ok(html);
    }

    @PermitAll
    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password, @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return ResponseEntity
                    .badRequest()
                    .body("""
                <html>
                    <body>
                        <h2>Passwords do not match.</h2>
                    </body>
                </html>
            """);
        }

        emailService.verifyTokenAndResetPassword(token, password);

        return ResponseEntity.ok("""
        <html>
            <body>
                <h2>Password changed successfully</h2>
                <p>You can return to the app and log in.</p>
            </body>
        </html>
    """);
    }
}

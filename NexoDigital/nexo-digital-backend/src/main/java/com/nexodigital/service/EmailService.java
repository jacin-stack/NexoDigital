package com.nexodigital.service;

import com.nexodigital.dto.ContactFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.destination}")
    private String destinationEmail;

    public void sendContactEmail(ContactFormDto contactForm) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinationEmail);
            helper.setSubject("Nueva consulta de " + contactForm.getNombre() + " - Nexo Digital");
            helper.setReplyTo(contactForm.getEmail());

            // Crear el contenido HTML del email
            String htmlContent = createEmailContent(contactForm);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }

    private String createEmailContent(ContactFormDto contactForm) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: linear-gradient(135deg, #3b82f6, #06b6d4); color: white; padding: 20px; border-radius: 8px; }
                        .content { background: #f8fafc; padding: 20px; border-radius: 8px; margin-top: 20px; }
                        .field { margin-bottom: 15px; }
                        .label { font-weight: bold; color: #1e293b; }
                        .value { color: #64748b; }
                        .footer { margin-top: 20px; padding-top: 20px; border-top: 1px solid #e2e8f0; font-size: 12px; color: #64748b; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Nueva Consulta - Nexo Digital</h1>
                            <p>Fecha: %s</p>
                        </div>

                        <div class="content">
                            <div class="field">
                                <div class="label">Nombre:</div>
                                <div class="value">%s</div>
                            </div>

                            <div class="field">
                                <div class="label">Email:</div>
                                <div class="value">%s</div>
                            </div>

                            <div class="field">
                                <div class="label">Teléfono:</div>
                                <div class="value">%s</div>
                            </div>

                            <div class="field">
                                <div class="label">Servicio:</div>
                                <div class="value">%s</div>
                            </div>

                            <div class="field">
                                <div class="label">Mensaje:</div>
                                <div class="value">%s</div>
                            </div>
                        </div>

                        <div class="footer">
                            <p>Este email fue enviado desde el formulario de contacto de Nexo Digital.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(
                        fecha,
                        contactForm.getNombre(),
                        contactForm.getEmail(),
                        contactForm.getTelefono() != null ? contactForm.getTelefono() : "No proporcionado",
                        contactForm.getServicio() != null ? contactForm.getServicio() : "Consulta general",
                        contactForm.getMensaje().replace("\n", "<br>"));
    }
}
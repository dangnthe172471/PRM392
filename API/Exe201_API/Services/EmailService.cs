using System.Net.Mail;
using System.Net;

namespace Exe201_API.Services
{
    public class EmailService : IEmailService
    {
        private readonly IConfiguration _configuration;
        private readonly ILogger<EmailService> _logger;

        public EmailService(IConfiguration configuration, ILogger<EmailService> logger)
        {
            _configuration = configuration;
            _logger = logger;
        }

        public async Task<bool> SendPasswordResetPinAsync(string to, string pin)
        {
            try
            {
                var emailSettings = _configuration.GetSection("EmailSettings");
                var smtpServer = emailSettings["SmtpServer"];
                var port = int.Parse(emailSettings["Port"]);
                var username = emailSettings["Username"];
                var password = emailSettings["Password"];
                var fromEmail = emailSettings["FromEmail"];
                var fromName = emailSettings["FromName"];
                var enableSsl = bool.Parse(emailSettings["EnableSsl"]);

                using var client = new SmtpClient(smtpServer, port)
                {
                    Credentials = new NetworkCredential(username, password),
                    EnableSsl = enableSsl
                };

                var fromAddress = new MailAddress(fromEmail, fromName);
                var toAddress = new MailAddress(to);

                var subject = "Mã PIN đặt lại mật khẩu - CareU";
                var body = GeneratePasswordResetPinHtml(pin);

                using var message = new MailMessage(fromAddress, toAddress)
                {
                    Subject = subject,
                    Body = body,
                    IsBodyHtml = true
                };

                await client.SendMailAsync(message);
                _logger.LogInformation($"Password reset PIN sent successfully to {to}");
                return true;
            }
            catch (Exception ex)
            {
                _logger.LogError($"Failed to send password reset PIN to {to}: {ex.Message}");
                return false;
            }
        }

        private string GeneratePasswordResetPinHtml(string pin)
        {
            return $@"
                <html>
                <body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>
                    <div style='background-color: #f8f9fa; padding: 30px; border-radius: 10px; text-align: center;'>
                        <h2 style='color: #333; margin-bottom: 20px;'>Đặt lại mật khẩu</h2>
                        <p style='color: #666; margin-bottom: 20px;'>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản CareU.</p>
                        
                        <div style='background-color: #007bff; color: white; padding: 20px; border-radius: 8px; margin: 20px 0;'>
                            <h3 style='margin: 0; font-size: 24px;'>Mã PIN của bạn</h3>
                            <div style='font-size: 32px; font-weight: bold; letter-spacing: 5px; margin: 15px 0;'>{pin}</div>
                        </div>
                        
                        <p style='color: #dc3545; font-weight: bold;'>⚠️ Mã này có hiệu lực trong 5 phút</p>
                        <p style='color: #666; font-size: 14px;'>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
                        
                        <hr style='margin: 30px 0; border: none; border-top: 1px solid #ddd;'>
                        <p style='color: #999; font-size: 12px;'>Trân trọng,<br>Đội ngũ CareU</p>
                    </div>
                </body>
                </html>";
        }
    }
} 
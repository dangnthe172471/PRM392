using Exe201_API.DTOs;
using Exe201_API.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System.Text;

namespace Exe201_API.Services
{
    public class AuthService : IAuthService
    {
        private readonly CareUContext _context;
        private readonly IEmailService _emailService;
        private readonly Random _random;

        public AuthService(CareUContext context, IEmailService emailService)
        {
            _context = context;
            _emailService = emailService;
            _random = new Random();
        }

        public async Task<LoginResponseDto> LoginAsync(LoginRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Password))
                throw new ArgumentException("Email và mật khẩu không được để trống");

            var user = await _context.Users.FirstOrDefaultAsync(u => u.Email == request.Email);
            if (user == null || user.Password != request.Password)
                throw new UnauthorizedAccessException("Email hoặc mật khẩu không đúng");

            return new LoginResponseDto
            {
                UserId = user.Id,
                Email = user.Email,
                Role = user.Role,
                Name = user.Name,
				Phone = user.Phone,
				Address = user.Address
			};
        }

        public async Task<LoginResponseDto> RegisterAsync(RegisterRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Password) || string.IsNullOrWhiteSpace(request.Name))
                throw new ArgumentException("Thông tin đăng ký không hợp lệ");
            if (await _context.Users.AnyAsync(u => u.Email == request.Email))
                throw new ArgumentException("Email đã tồn tại");
            var user = new User
            {
                Name = request.Name,
                Email = request.Email,
                Password = request.Password, // Note: In a real app, hash this password!
                Phone = request.Phone,
                Address = request.Address,
                Role = request.Role,
                CreatedAt = DateTime.UtcNow
            };
            _context.Users.Add(user);
            await _context.SaveChangesAsync();
			return new LoginResponseDto
			{
				UserId = user.Id,
				Email = user.Email,
				Role = user.Role,
				Name = user.Name,
				Phone = user.Phone,
				Address = user.Address
			};
        }

        public async Task<bool> ChangePasswordAsync(ChangePasswordRequestDto request)
        {
            if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.OldPassword) || string.IsNullOrWhiteSpace(request.NewPassword))
                throw new ArgumentException("Thông tin đổi mật khẩu không hợp lệ");
            var user = await _context.Users.FirstOrDefaultAsync(u => u.Email == request.Email);
            if (user == null || user.Password != request.OldPassword) // Note: In a real app, compare hashed passwords!
                throw new UnauthorizedAccessException("Email hoặc mật khẩu cũ không đúng");
            user.Password = request.NewPassword; // Note: Hash this new password!
            user.UpdatedAt = DateTime.UtcNow;
            _context.Users.Update(user);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<UserProfileResponseDto> GetUserProfileAsync(int userId)
        {
            var user = await _context.Users.FindAsync(userId);
            if (user == null)
            {
                throw new ArgumentException("Không tìm thấy người dùng");
            }

            return new UserProfileResponseDto
            {
                Id = user.Id,
                Name = user.Name,
                Email = user.Email,
                Phone = user.Phone,
                Address = user.Address,
                Role = user.Role,
                Status = user.Status,
                Experience = user.Experience,
                CreatedAt = user.CreatedAt
            };
        }

        public async Task<UserProfileResponseDto> UpdateUserProfileAsync(int userId, UserProfileUpdateRequestDto request)
        {
            var user = await _context.Users.FindAsync(userId);
            if (user == null)
            {
                throw new ArgumentException("Không tìm thấy người dùng");
            }

            user.Name = request.Name;
            user.Phone = request.Phone;
            user.Address = request.Address;
            user.UpdatedAt = DateTime.UtcNow;

            await _context.SaveChangesAsync();

            return await GetUserProfileAsync(userId);
        }

        public async Task<bool> ForgotPasswordAsync(ForgotPasswordRequestDto request)
        {
            // Kiểm tra email có tồn tại không
            var user = await _context.Users.FirstOrDefaultAsync(u => u.Email == request.Email);
            if (user == null)
                throw new ArgumentException("Email không tồn tại trong hệ thống");

            // Xóa các PIN cũ của email này
            var oldPins = await _context.PasswordResetPins
                .Where(p => p.Email == request.Email)
                .ToListAsync();
            _context.PasswordResetPins.RemoveRange(oldPins);

            // Tạo mã PIN mới (6 số)
            var pin = _random.Next(100000, 999999).ToString();
            
            // Tạo record mới với thời gian hết hạn 5 phút
            var resetPin = new PasswordResetPin
            {
                Email = request.Email,
                Pin = pin,
                CreatedAt = DateTime.UtcNow,
                ExpiresAt = DateTime.UtcNow.AddMinutes(5),
                IsUsed = false
            };

            _context.PasswordResetPins.Add(resetPin);
            await _context.SaveChangesAsync();

            // Gửi email với mã PIN
            var emailSent = await _emailService.SendPasswordResetPinAsync(request.Email, pin);
            if (!emailSent)
                throw new Exception("Không thể gửi email. Vui lòng thử lại sau.");

            return true;
        }

        public async Task<bool> VerifyPinAsync(VerifyPinRequestDto request)
        {
            // Tìm PIN hợp lệ
            var resetPin = await _context.PasswordResetPins
                .Where(p => p.Email == request.Email && 
                           p.Pin == request.Pin && 
                           !p.IsUsed && 
                           p.ExpiresAt > DateTime.UtcNow)
                .OrderByDescending(p => p.CreatedAt)
                .FirstOrDefaultAsync();

            if (resetPin == null)
                throw new ArgumentException("Mã PIN không hợp lệ hoặc đã hết hạn");

            return true;
        }

        public async Task<bool> ResetPasswordAsync(ResetPasswordRequestDto request)
        {
            // Tìm PIN hợp lệ
            var resetPin = await _context.PasswordResetPins
                .Where(p => p.Email == request.Email && 
                           p.Pin == request.Pin && 
                           !p.IsUsed && 
                           p.ExpiresAt > DateTime.UtcNow)
                .OrderByDescending(p => p.CreatedAt)
                .FirstOrDefaultAsync();

            if (resetPin == null)
                throw new ArgumentException("Mã PIN không hợp lệ hoặc đã hết hạn");

            // Tìm user
            var user = await _context.Users.FirstOrDefaultAsync(u => u.Email == request.Email);
            if (user == null)
                throw new ArgumentException("Không tìm thấy người dùng");

            // Cập nhật mật khẩu mới
            user.Password = request.NewPassword; // Note: In a real app, hash this password!
            user.UpdatedAt = DateTime.UtcNow;

            // Đánh dấu PIN đã sử dụng
            resetPin.IsUsed = true;

            await _context.SaveChangesAsync();

            return true;
        }
    }
} 
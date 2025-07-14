using Exe201_API.DTOs;
using Exe201_API.Models;
using System.Threading.Tasks;

namespace Exe201_API.Services
{
    public interface IAuthService
    {
        Task<LoginResponseDto> LoginAsync(LoginRequestDto request);
        Task<LoginResponseDto> RegisterAsync(RegisterRequestDto request);
        Task<bool> ChangePasswordAsync(ChangePasswordRequestDto request);
        Task<UserProfileResponseDto> GetUserProfileAsync(int userId);
        Task<UserProfileResponseDto> UpdateUserProfileAsync(int userId, UserProfileUpdateRequestDto request);
        Task<bool> ForgotPasswordAsync(ForgotPasswordRequestDto request);
        Task<bool> VerifyPinAsync(VerifyPinRequestDto request);
        Task<bool> ResetPasswordAsync(ResetPasswordRequestDto request);
    }
} 
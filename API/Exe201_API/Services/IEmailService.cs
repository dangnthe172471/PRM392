using System.Threading.Tasks;

namespace Exe201_API.Services
{
    public interface IEmailService
    {
        Task<bool> SendPasswordResetPinAsync(string to, string pin);
    }
} 
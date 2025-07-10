using System.ComponentModel.DataAnnotations;

namespace Exe201_API.DTOs
{
    public class ForgotPasswordRequestDto
    {
        [Required(ErrorMessage = "Email không được để trống")]
        [EmailAddress(ErrorMessage = "Email không hợp lệ")]
        public string Email { get; set; } = string.Empty;
    }
} 
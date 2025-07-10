using System.ComponentModel.DataAnnotations;

namespace Exe201_API.DTOs
{
    public class VerifyPinRequestDto
    {
        [Required(ErrorMessage = "Email không được để trống")]
        [EmailAddress(ErrorMessage = "Email không hợp lệ")]
        public string Email { get; set; } = string.Empty;

        [Required(ErrorMessage = "Mã PIN không được để trống")]
        [StringLength(6, MinimumLength = 6, ErrorMessage = "Mã PIN phải có 6 ký tự")]
        public string Pin { get; set; } = string.Empty;
    }
} 
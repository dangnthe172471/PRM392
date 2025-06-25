namespace Exe201_API.DTOs
{
    public class LoginResponseDto
    {
        public int UserId { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
        public string Role { get; set; }
        public string Phone { get; set; }
        public string Address { get; set; }
    }

}
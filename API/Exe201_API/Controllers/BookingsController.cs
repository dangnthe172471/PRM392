using Exe201_API.DTOs;
using Exe201_API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Exe201_API.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class BookingsController : ControllerBase
    {
        private readonly IBookingService _bookingService;

        public BookingsController(IBookingService bookingService)
        {
            _bookingService = bookingService;
        }

        [HttpPost]
        public async Task<IActionResult> CreateBooking([FromBody] CreateBookingRequestDto request, int? userId = null)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                var result = await _bookingService.CreateBookingAsync(request, userId ?? 1);
                return CreatedAtAction(nameof(CreateBooking), new { id = result.Id }, result);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet]
        public async Task<IActionResult> GetUserBookings([FromQuery] string? status = "all", int? userId = null)
        {
            try
            {
                var bookings = await _bookingService.GetUserBookingsAsync(userId ?? 1, status);
                return Ok(bookings);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetBookingByIdForUser(int id, int? userId = null)
        {
            try
            {
                var booking = await _bookingService.GetUserBookingByIdAsync(id, userId ?? 1);
                if (booking == null)
                {
                    return NotFound(new { message = "Không tìm thấy đơn hàng hoặc bạn không có quyền truy cập." });
                }
                return Ok(booking);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet("dashboard-stats")]
        public async Task<IActionResult> GetDashboardStats(int? userId = null)
        {
            try
            {
                var stats = await _bookingService.GetDashboardStatsAsync(userId ?? 1);
                return Ok(stats);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }
    }
} 
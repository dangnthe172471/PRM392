using Exe201_API.DTOs;
using Exe201_API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System;

namespace Exe201_API.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CleanerController : ControllerBase
    {
        private readonly IBookingService _bookingService;
        private readonly IAuthService _authService;

        public CleanerController(IBookingService bookingService, IAuthService authService)
        {
            _bookingService = bookingService;
            _authService = authService;
        }

        [HttpGet("profile")]
        public async Task<IActionResult> GetProfile(int? cleanerId = null)
        {
            try
            {
                var profile = await _authService.GetUserProfileAsync(cleanerId ?? 1);
                if (profile == null)
                {
                    return NotFound(new { message = "Không tìm thấy thông tin cleaner" });
                }
                return Ok(profile);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet("available-jobs")]
        public async Task<IActionResult> GetAvailableJobs()
        {
            try
            {
                var jobs = await _bookingService.GetAvailableJobsAsync();
                return Ok(jobs);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet("my-jobs")]
        public async Task<IActionResult> GetMyJobs([FromQuery] string? status = "all", int? cleanerId = null)
        {
            try
            {
                var jobs = await _bookingService.GetCleanerJobsAsync(cleanerId ?? 1, status);
                return Ok(jobs);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpPost("accept-job/{bookingId}")]
        public async Task<IActionResult> AcceptJob(int bookingId, int? cleanerId = null)
        {
            try
            {
                var result = await _bookingService.AssignCleanerToBookingAsync(bookingId, cleanerId ?? 1);
                return Ok(result);
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

        [HttpPut("update-job-status/{bookingId}")]
        public async Task<IActionResult> UpdateJobStatus(int bookingId, [FromBody] UpdateJobStatusRequest request, int? cleanerId = null)
        {
            try
            {
                var result = await _bookingService.UpdateBookingStatusAsync(bookingId, request.Status, cleanerId ?? 1);
                return Ok(result);
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { message = ex.Message });
            }
            catch (UnauthorizedAccessException ex)
            {
                return Unauthorized(new { message = ex.Message });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }

        [HttpGet("dashboard-stats")]
        public async Task<IActionResult> GetDashboardStats(int? cleanerId = null)
        {
            try
            {
                var stats = await _bookingService.GetCleanerDashboardStatsAsync(cleanerId ?? 1);
                return Ok(stats);
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "Lỗi hệ thống", detail = ex.Message });
            }
        }
    }
} 
using Exe201_API.DTOs.News;
using Exe201_API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;

namespace Exe201_API.Controllers
{
    [ApiController]
    [Route("api/news")]
    public class NewsController : ControllerBase
    {
        private readonly INewsService _newsService;

        public NewsController(INewsService newsService)
        {
            _newsService = newsService;
        }

        [HttpGet]
        public async Task<IActionResult> GetArticles([FromQuery] int page = 1, [FromQuery] int pageSize = 10, [FromQuery] string? category = null, [FromQuery] string? tag = null)
        {
            var result = await _newsService.GetNewsArticles(page, pageSize, category, tag);
            return Ok(result);
        }

        [HttpGet("featured")]
        public async Task<IActionResult> GetFeaturedArticles()
        {
            var result = await _newsService.GetFeaturedArticles();
            return Ok(result);
        }

        [HttpGet("categories")]
        public async Task<IActionResult> GetCategories()
        {
            var result = await _newsService.GetCategories();
            return Ok(result);
        }

        [HttpGet("tags")]
        public async Task<IActionResult> GetTags()
        {
            var result = await _newsService.GetTags();
            return Ok(result);
        }
        
        [HttpGet("{idOrSlug}")]
        public async Task<IActionResult> GetArticle(string idOrSlug)
        {
            var result = await _newsService.GetNewsArticleByIdOrSlug(idOrSlug);
            if (result == null) return NotFound();
            return Ok(result);
        }
        
        // Admin routes
        [HttpPost]
        public async Task<IActionResult> CreateArticle([FromBody] CreateNewsArticleDto dto)
        {
            var authorId = GetCurrentUserId();
            var result = await _newsService.CreateArticle(dto, authorId);
            return CreatedAtAction(nameof(GetArticle), new { idOrSlug = result.Id }, result);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateArticle(int id, [FromBody] UpdateNewsArticleDto dto)
        {
            try
            {
                var result = await _newsService.UpdateArticle(id, dto);
                return Ok(result);
            }
            catch (ArgumentException ex)
            {
                return NotFound(new { message = ex.Message });
            }
        }
        
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteArticle(int id)
        {
            var success = await _newsService.DeleteArticle(id);
            if (!success) return NotFound();
            return NoContent();
        }

        private int GetCurrentUserId()
        {
            // Should not happen if authorized
            throw new UnauthorizedAccessException("ID người dùng không hợp lệ hoặc không tìm thấy.");
        }
    }
} 
using Microsoft.AspNetCore.Mvc;
using UserAuthApi.Api.Models;
using UserAuthApi.Api.Services;
using UserAuthApi.Api.Helpers;

namespace UserAuthApi.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly IConfiguration _configuration;

        public AuthController(IUserService userService, IConfiguration configuration)
        {
            _userService = userService;
            _configuration = configuration;
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.Username) || string.IsNullOrWhiteSpace(request.Password))
                return BadRequest("username and password required");

            if (await _userService.GetByUsernameAsync(request.Username) != null)
                return Conflict("username already exists");

            var user = await _userService.RegisterAsync(request.Username, request.Password);
            return Ok(new { user.Id, Username = user.Username! });
        }

        

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            var user = await _userService.ValidateCredentialsAsync(request.Username, request.Password);
            if (user == null) return Unauthorized();

            var token = JwtHelpers.CreateToken(user.Username!, _configuration);
            return Ok(new { token });
        }
    }
}

using UserAuthApi.Api.Models;

namespace UserAuthApi.Api.Services;

public interface IUserService
{
    Task<User?> GetByUsernameAsync(string username);
    Task<User?> GetByIdAsync(int id);
    Task<List<User>> GetAllUsersAsync();
    Task<User> RegisterAsync(string username, string password);
    Task<User?> ValidateCredentialsAsync(string username, string password);
}
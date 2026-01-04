using Microsoft.EntityFrameworkCore;
using UserAuthApi.Api.Data;
using UserAuthApi.Api.Helpers;
using UserAuthApi.Api.Models;

namespace UserAuthApi.Api.Services;

public class UserService : IUserService
{
    private readonly AppDbContext _db;

    public UserService(AppDbContext db)
    {
        _db = db;
    }

    public async Task<User?> GetByUsernameAsync(string username)
        => await _db.Users.SingleOrDefaultAsync(u => u.Username == username);

    public async Task<User?> GetByIdAsync(int id)
        => await _db.Users.FindAsync(id);

    public async Task<List<User>> GetAllUsersAsync()
        => await _db.Users.ToListAsync();

    public async Task<User> RegisterAsync(string username, string password)
    {
        var (salt, hash) = PasswordHasher.HashPassword(password);
        var user = new User
        {
            Username = username,
            PasswordSalt = salt,
            PasswordHash = hash
        };
        _db.Users.Add(user);
        await _db.SaveChangesAsync();
        return user;
    }

    public async Task<User?> ValidateCredentialsAsync(string username, string password)
    {
        var user = await GetByUsernameAsync(username);
        if (user == null) return null;
        if (!PasswordHasher.VerifyPassword(password, user.PasswordSalt, user.PasswordHash)) return null;
        return user;
    }
}

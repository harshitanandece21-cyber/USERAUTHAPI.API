UserAuthApi.Api

A minimal ASP.NET Core API with JWT auth and EF Core persistence.

Quick push instructions

1. From the project root:

```powershell
git init
git add .
git commit -m "Initial commit"
```

2a. If you have GitHub CLI (`gh`) installed and authenticated:

```powershell
gh repo create <your-username>/UserAuthApi.Api --public --source=. --remote=origin --push
```

2b. Or create a repo on GitHub via web, then:

```powershell
git branch -M main
git remote add origin https://github.com/<your-username>/UserAuthApi.Api.git
git push -u origin main
```

Notes:
- Replace `<your-username>` with your GitHub username.
- If using HTTPS and 2FA, create a personal access token (PAT) and use it when prompted.
- Consider excluding secrets and sensitive files from commits (they should be in user-secrets or environment variables).

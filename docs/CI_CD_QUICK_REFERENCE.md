# CI/CD Quick Reference Card

## 🚦 Pipeline Status

| Status | Meaning | Action Required |
|--------|---------|-----------------|
| ✅ Passed | All checks passed | Ready to merge |
| ❌ Failed | One or more jobs failed | Fix issues and push |
| ⚠️ Warning | Optional checks failed | Review and decide |
| 🔄 Running | Pipeline in progress | Wait for completion |
| ⏸️ Manual | Awaiting manual trigger | Click to run |

## 🔄 Developer Workflow

```
┌─────────────────────────────────────────────────┐
│  1. Create Feature Branch                       │
│     git checkout -b feature/my-feature          │
└─────────────────┬───────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│  2. Make Changes & Commit                       │
│     git add .                                   │
│     git commit -m "Add feature"                 │
└─────────────────┬───────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│  3. Push to GitLab                              │
│     git push origin feature/my-feature          │
│     → Pipeline starts automatically             │
└─────────────────┬───────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│  4. Pipeline Runs                               │
│     Build → Test → Quality → Package            │
│     (Takes ~8-10 minutes)                       │
└─────────────────┬───────────────────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
         ▼                 ▼
    ✅ Passed         ❌ Failed
         │                 │
         │                 ▼
         │     ┌─────────────────────┐
         │     │  5. Fix Issues       │
         │     │     - Check logs     │
         │     │     - Fix locally    │
         │     │     - Push again     │
         │     └──────────┬───────────┘
         │                │
         │                ▼
         └────────►  Back to Step 4
                        │
                        ▼
┌─────────────────────────────────────────────────┐
│  6. Create Merge Request                        │
│     - Pipeline must pass ✅                     │
│     - Get 1+ approval 👍                        │
│     - Resolve discussions 💬                    │
└─────────────────┬───────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│  7. Merge to Main 🎉                            │
│     - Code is now in production                 │
│     - Delete feature branch                     │
└─────────────────────────────────────────────────┘
```

## 🧪 Testing Locally Before Push

### Backend Tests
```bash
# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=AlbumServiceTest

# Generate coverage report
mvn test jacoco:report
# View: target/site/jacoco/index.html

# Check code style
mvn checkstyle:check
```

### Frontend Tests
```bash
# Run all tests
npm test

# Run with coverage
npm run test:coverage
# View: coverage/lcov-report/index.html

# Run linting
npm run lint

# Fix linting issues
npm run lint:fix

# Build to check for errors
npm run build
```

## 📊 Coverage Requirements

| Component | Minimum Coverage | Pipeline Behavior |
|-----------|------------------|-------------------|
| Backend | 80% | ❌ Fails if below |
| Frontend | 70% | ⚠️ Warning if below |

## 🔧 Common Pipeline Failures

### ❌ Backend Build Failed
```bash
# Check compilation errors
mvn clean compile

# Common causes:
# - Syntax errors
# - Missing dependencies
# - Import errors
```

### ❌ Backend Tests Failed
```bash
# Run tests locally
mvn test

# Common causes:
# - Test logic errors
# - Database connection issues
# - Mock configuration problems
```

### ❌ Frontend Build Failed
```bash
# Check build errors
npm run build

# Common causes:
# - Syntax errors
# - Missing imports
# - Type errors (if using TypeScript)
```

### ❌ Frontend Tests Failed
```bash
# Run tests locally
npm test

# Common causes:
# - Test assertions wrong
# - Component rendering issues
# - Mock API calls incorrect
```

### ❌ Coverage Too Low
```bash
# Backend: Add more unit tests
# Frontend: Add component tests

# Focus on:
# - Service layer methods
# - Controller endpoints
# - Component logic
# - Utility functions
```

### ❌ Checkstyle Failed
```bash
# Check style violations
mvn checkstyle:check

# Common issues:
# - Line too long (>120 chars)
# - Missing Javadoc
# - Incorrect formatting
# - Unused imports
```

### ❌ ESLint Failed
```bash
# Auto-fix what can be fixed
npm run lint:fix

# Common issues:
# - Unused variables
# - Missing prop-types
# - Console.log statements
# - Incorrect spacing
```

## 🎯 Pipeline Jobs Quick Reference

### Build Stage
- **backend-build**: Compile Java code with Maven
- **frontend-build**: Build React app with npm
- **frontend-lint**: Run ESLint checks

### Test Stage
- **backend-test**: Run JUnit tests + coverage
- **backend-integration-test**: Run integration tests
- **frontend-test**: Run Jest tests + coverage

### Quality Stage
- **backend-checkstyle**: Code style checks
- **backend-code-quality**: PMD/SpotBugs analysis
- **validate-coverage**: Check coverage thresholds
- **mr-summary**: Merge request summary

### Package Stage (main/develop only)
- **docker-backend**: Build backend Docker image
- **docker-frontend**: Build frontend Docker image

## 📈 Viewing Results in GitLab

### Pipeline Overview
```
Project → CI/CD → Pipelines → [Select Pipeline]
```

### Test Results
```
Pipeline → Tests tab
```

### Coverage Report
```
Pipeline → Jobs → backend-test → Browse → target/site/jacoco/index.html
Pipeline → Jobs → frontend-test → Browse → coverage/lcov-report/index.html
```

### Job Logs
```
Pipeline → Jobs → [Select Job] → View logs
```

## 🚨 Troubleshooting

### Pipeline Stuck on "Pending"
- Check if GitLab Runners are available
- Check runner tags match job requirements

### Can't Merge Even Though Pipeline Passed
- Check if approval is required
- Check if all discussions are resolved
- Check if branch is up to date with main

### Pipeline Not Running
- Check workflow rules in .gitlab-ci.yml
- Verify branch name matches rules
- Check if pipeline is disabled in settings

## 💡 Tips & Best Practices

### Before Pushing
✅ Run tests locally: `mvn test && npm test`  
✅ Check code style: `mvn checkstyle:check && npm run lint`  
✅ Build successfully: `mvn compile && npm run build`  
✅ Review your changes: `git diff`  

### During Code Review
✅ Respond to comments promptly  
✅ Make changes in new commits  
✅ Resolve discussions when fixed  
✅ Keep commits focused and small  

### After Merge
✅ Delete feature branch  
✅ Pull latest main: `git checkout main && git pull`  
✅ Verify deployment if auto-deploy is enabled  

## 📞 Getting Help

### Check Pipeline Logs
1. Go to failing job
2. Click "Show complete raw" for full log
3. Search for "ERROR" or "FAILED"

### Common Log Locations
- Maven errors: Look for `[ERROR]`
- Test failures: Look for `FAILED` or `AssertionError`
- Build errors: Look for compilation errors
- Coverage: Look for "Coverage threshold not met"

### Ask for Help
Include in your question:
- Link to pipeline
- Link to job that failed
- Error message from logs
- What you've tried

---

**Quick Links**:
- [Full CI/CD Guide](./CI_CD_GUIDE.md)
- [Project Plan](./PROJECT_PLAN.md)
- [GitLab Issues](./GITLAB_ISSUES.md)
- [Database Schema](./DATABASE_SCHEMA.md)


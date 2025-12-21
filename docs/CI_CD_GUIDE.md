# CI/CD Setup Guide for Music Store Application

## 📋 Overview

This guide explains how to setup and use the GitLab CI/CD pipeline for the Music Store project. The pipeline ensures code quality by automatically building and testing every commit and merge request.

## 🎯 Pipeline Goals

1. **Prevent Broken Code**: All tests must pass before merge
2. **Maintain Quality**: Code quality checks on every commit
3. **Track Coverage**: Test coverage must meet minimum thresholds
4. **Automate Testing**: No manual test runs needed
5. **Fast Feedback**: Get results in < 10 minutes

## 🏗️ Pipeline Architecture

### Pipeline Stages

```
┌─────────────────────────────────────────────────────────────┐
│                    Commit / Push                            │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  Stage 1: BUILD                                             │
│  ├── Backend Build (Maven compile)                          │
│  ├── Frontend Build (npm build)                             │
│  └── Frontend Lint (ESLint)                                 │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  Stage 2: TEST                                              │
│  ├── Backend Unit Tests (JUnit + Mockito)                   │
│  ├── Backend Integration Tests (MockMvc)                    │
│  ├── Frontend Tests (Jest + React Testing Library)          │
│  └── Coverage Reports (JaCoCo + Jest)                       │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  Stage 3: QUALITY                                           │
│  ├── Checkstyle (code style)                                │
│  ├── PMD/SpotBugs (optional)                                │
│  ├── Coverage Validation (80% backend, 70% frontend)        │
│  └── Merge Request Summary                                  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  Stage 4: PACKAGE (main/develop only)                       │
│  ├── Build Backend Docker Image                             │
│  └── Build Frontend Docker Image                            │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Setup

### Step 1: Copy CI/CD Configuration

```bash
# Copy the template to .gitlab-ci.yml
cp .gitlab-ci.yml.template .gitlab-ci.yml

# Commit and push
git add .gitlab-ci.yml
git commit -m "Add GitLab CI/CD pipeline"
git push origin main
```

### Step 2: Configure Branch Protection

1. Go to **Settings → Repository → Branch Rules**
2. Click **Add branch rule** for `main` branch
3. Configure:
   - ✅ **Allowed to merge**: Maintainers only
   - ✅ **Allowed to push**: No one
   - ✅ **Require approval from code owners**: Yes
   - ✅ **Pipelines must succeed**: Yes

### Step 3: Configure Merge Request Settings

1. Go to **Settings → Merge Requests**
2. Configure **Merge request approvals**:
   - Approvals required: **1**
   - ✅ Reset approvals on new commits
   - ✅ Prevent approval by author
3. Configure **Merge options**:
   - ✅ Enable "Delete source branch" option by default
   - ✅ Pipelines must succeed
   - ✅ All discussions must be resolved

### Step 4: Enable Coverage Visualization

1. Go to **Settings → CI/CD → General pipelines**
2. Set **Test coverage parsing**:
   - Backend: `(\d+\.?\d*) % covered`
   - (Already configured in .gitlab-ci.yml)
3. Coverage badges will appear in README automatically

## 📝 Backend Configuration

### Required Maven Plugins

Add to `pom.xml`:

```xml
<build>
    <plugins>
        <!-- JaCoCo for test coverage -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
                <execution>
                    <id>jacoco-check</id>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <configuration>
                        <rules>
                            <rule>
                                <element>PACKAGE</element>
                                <limits>
                                    <limit>
                                        <counter>LINE</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.80</minimum>
                                    </limit>
                                </limits>
                            </rule>
                        </rules>
                    </configuration>
                </execution>
            </executions>
        </plugin>

        <!-- Checkstyle for code quality -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.3.1</version>
            <configuration>
                <configLocation>checkstyle.xml</configLocation>
                <encoding>UTF-8</encoding>
                <consoleOutput>true</consoleOutput>
                <failsOnError>true</failsOnError>
            </configuration>
        </plugin>

        <!-- Surefire for unit tests -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.2</version>
            <configuration>
                <includes>
                    <include>**/*Test.java</include>
                    <include>**/*Tests.java</include>
                </includes>
            </configuration>
        </plugin>

        <!-- Failsafe for integration tests -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>3.2.2</version>
            <configuration>
                <includes>
                    <include>**/*IT.java</include>
                    <include>**/*IntegrationTest.java</include>
                </includes>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>integration-test</goal>
                        <goal>verify</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Checkstyle Configuration

Create `checkstyle.xml` in project root:

```xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
          "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
          "https://checkstyle.org/dtds/configuration_1_3.dtd">

<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="warning"/>
    <property name="fileExtensions" value="java"/>

    <module name="TreeWalker">
        <!-- Naming Conventions -->
        <module name="ConstantName"/>
        <module name="LocalFinalVariableName"/>
        <module name="LocalVariableName"/>
        <module name="MemberName"/>
        <module name="MethodName"/>
        <module name="PackageName"/>
        <module name="ParameterName"/>
        <module name="StaticVariableName"/>
        <module name="TypeName"/>

        <!-- Imports -->
        <module name="AvoidStarImport"/>
        <module name="IllegalImport"/>
        <module name="RedundantImport"/>
        <module name="UnusedImports"/>

        <!-- Size Violations -->
        <module name="LineLength">
            <property name="max" value="120"/>
        </module>
        <module name="MethodLength">
            <property name="max" value="150"/>
        </module>

        <!-- Whitespace -->
        <module name="EmptyForIteratorPad"/>
        <module name="GenericWhitespace"/>
        <module name="MethodParamPad"/>
        <module name="NoWhitespaceAfter"/>
        <module name="NoWhitespaceBefore"/>
        <module name="WhitespaceAfter"/>
        <module name="WhitespaceAround"/>
    </module>
</module>
```

### Test Application Properties

Create `src/test/resources/application-test.properties`:

```properties
# Test Database Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:h2:mem:testdb}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:sa}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:}
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:create-drop}
spring.jpa.show-sql=false

# Disable Swagger in tests
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

## 🎨 Frontend Configuration

### Package.json Scripts

Add to `frontend/package.json`:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "test": "jest",
    "test:watch": "jest --watch",
    "test:coverage": "jest --coverage",
    "lint": "eslint . --ext .js,.jsx,.ts,.tsx",
    "lint:fix": "eslint . --ext .js,.jsx,.ts,.tsx --fix"
  }
}
```

### Jest Configuration

Create `frontend/jest.config.js`:

```javascript
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
    '\\.(jpg|jpeg|png|gif|svg)$': '<rootDir>/__mocks__/fileMock.js'
  },
  collectCoverageFrom: [
    'src/**/*.{js,jsx,ts,tsx}',
    '!src/**/*.test.{js,jsx,ts,tsx}',
    '!src/index.js',
    '!src/reportWebVitals.js'
  ],
  coverageThreshold: {
    global: {
      branches: 70,
      functions: 70,
      lines: 70,
      statements: 70
    }
  },
  coverageReporters: ['text', 'lcov', 'cobertura']
};
```

### ESLint Configuration

Create `frontend/.eslintrc.json`:

```json
{
  "extends": [
    "eslint:recommended",
    "plugin:react/recommended",
    "plugin:react-hooks/recommended"
  ],
  "parserOptions": {
    "ecmaVersion": 2021,
    "sourceType": "module",
    "ecmaFeatures": {
      "jsx": true
    }
  },
  "env": {
    "browser": true,
    "es2021": true,
    "node": true,
    "jest": true
  },
  "rules": {
    "react/prop-types": "off",
    "react/react-in-jsx-scope": "off"
  },
  "settings": {
    "react": {
      "version": "detect"
    }
  }
}
```

## 👨‍💻 Developer Workflow

### 1. Create Feature Branch

```bash
# Create and checkout new branch
git checkout -b feature/add-album-search

# Make your changes
# ...

# Commit changes
git add .
git commit -m "Add album search functionality"
```

### 2. Push and Watch Pipeline

```bash
# Push to GitLab
git push origin feature/add-album-search

# Pipeline starts automatically
# Watch progress: Project → CI/CD → Pipelines
```

### 3. Fix Issues if Pipeline Fails

```bash
# If tests fail, fix them locally
npm test                    # Frontend tests
mvn test                    # Backend tests

# If build fails, fix compilation errors
mvn compile                 # Backend
npm run build               # Frontend

# If linting fails
npm run lint:fix            # Frontend
mvn checkstyle:check        # Backend

# Commit fixes and push again
git add .
git commit -m "Fix failing tests"
git push origin feature/add-album-search
```

### 4. Create Merge Request

```bash
# When pipeline passes, create MR
# Go to: Project → Merge Requests → New merge request

# Or use CLI:
git push origin feature/add-album-search -o merge_request.create
```

### 5. Code Review and Merge

1. Pipeline must pass ✅
2. Get 1+ approval ✅
3. All discussions resolved ✅
4. Merge to main 🎉

## 📊 Viewing Test Results

### Coverage Reports in GitLab

1. Go to **Project → Analytics → Repository Analytics**
2. View coverage trend over time
3. Click on specific pipeline to see detailed coverage

### Coverage in Merge Request

Coverage reports appear directly in MR:
- Coverage badge in MR header
- Coverage diff (what your MR changes)
- Link to full coverage report

### JUnit Test Reports

1. Go to **CI/CD → Pipelines → [Your Pipeline]**
2. Click on **Tests** tab
3. View all test results, failures, and execution times

### Local Coverage Reports

```bash
# Backend
mvn test jacoco:report
# Open: target/site/jacoco/index.html

# Frontend
npm run test:coverage
# Open: coverage/lcov-report/index.html
```

## 🛠️ Troubleshooting

### Pipeline Fails with "No Space Left on Device"

**Solution**: Clear GitLab Runner cache

```yaml
# Add to job that's failing
before_script:
  - docker system prune -af
```

### Tests Pass Locally but Fail in Pipeline

**Common causes**:
1. **Database differences**: Use same DB version locally and in CI
2. **Timezone issues**: Set explicit timezone in tests
3. **File permissions**: Check file access in Docker container
4. **Environment variables**: Verify all env vars are set

### Coverage Not Showing in GitLab

**Check**:
1. Coverage regex is correct in `.gitlab-ci.yml`
2. Coverage report artifact path is correct
3. Tests are actually running (check job logs)

### Pipeline is Too Slow

**Optimizations**:
1. Use smaller Docker images (alpine variants)
2. Cache dependencies properly
3. Run jobs in parallel
4. Use local artifact cache

## 🔒 Security Best Practices

### Secrets Management

```yaml
# Use GitLab CI/CD Variables for secrets
# Settings → CI/CD → Variables

variables:
  DATABASE_PASSWORD: $DB_PASSWORD  # Protected variable
  API_KEY: $API_KEY                # Protected variable
```

### Protected Branches

- Only run sensitive jobs on protected branches
- Use `only: - main` for deployment jobs
- Require pipeline to pass before merge

### Docker Security

```dockerfile
# Use specific versions, not "latest"
FROM maven:3.9-eclipse-temurin-17

# Run as non-root user
RUN useradd -m appuser
USER appuser

# Don't include secrets in images
# Use environment variables instead
```

## 📚 Additional Resources

### GitLab Documentation
- [GitLab CI/CD Documentation](https://docs.gitlab.com/ee/ci/)
- [GitLab CI/CD Pipelines](https://docs.gitlab.com/ee/ci/pipelines/)
- [GitLab Test Coverage](https://docs.gitlab.com/ee/ci/testing/test_coverage_visualization.html)

### Testing Frameworks
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/)

### Code Quality
- [Checkstyle Checks](https://checkstyle.sourceforge.io/checks.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [ESLint Rules](https://eslint.org/docs/latest/rules/)

## 🎯 Success Metrics

Track these metrics to ensure pipeline effectiveness:

- ✅ **Pipeline Success Rate**: > 90%
- ✅ **Average Pipeline Duration**: < 10 minutes
- ✅ **Test Coverage**: Backend > 80%, Frontend > 70%
- ✅ **Code Review Time**: < 24 hours
- ✅ **Mean Time to Fix**: < 2 hours
- ✅ **Zero Production Bugs**: From merged code

## 📞 Support

If you encounter issues:

1. Check this documentation
2. Review GitLab CI/CD logs
3. Ask team members
4. Open an issue in GitLab

---

**Last Updated**: December 2024  
**Maintained by**: Music Store Development Team


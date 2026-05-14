# HR Management System - CI/CD and AI Tooling

## CI/CD

GitHub Actions now provides:

- `CI`: Maven verify, test reports, Jacoco coverage artifact, Docker image build, dependency review on pull requests.
- `CD`: publish Docker image to GitHub Container Registry on `v*.*.*` tags or manual dispatch.
- `CodeQL`: scheduled and PR security analysis for Java.
- `Trivy`: filesystem dependency/configuration vulnerability scan with SARIF upload.
- `Quality Gate`: optional SonarQube/SonarCloud scan when `SONAR_HOST_URL` repository variable and `SONAR_TOKEN` secret are configured.
- `Dependabot`: weekly update PRs for Maven, npm, and GitHub Actions.

Local checks:

```powershell
.\scripts\ci-local.ps1
```

Run Docker/Testcontainers integration tests explicitly:

```powershell
.\scripts\ci-local.ps1 -WithIntegrationTests
```

## AI

The existing HR chatbot already has a Gemini-backed mode and a rule-based fallback. The new admin status endpoint shows which mode is active:

```http
GET /api/ai/status
```

The endpoint requires an authenticated admin user and returns provider, model, enabled state, availability, and active mode.

Admin and manager users can also request an AI operational summary:

```http
GET /api/ai/hr-insights
```

Admins receive organization-wide metrics. Managers receive department-scoped metrics. When Gemini is unavailable, the endpoint still returns local rule-based recommendations.

Enable Gemini in `.env` for Docker:

```env
AI_GEMINI_ENABLED=true
GEMINI_API_KEY=your-key
AI_GEMINI_MODEL=gemini-1.5-flash
```

Then start the stack:

```powershell
docker compose up --build
```

If no key is configured, chatbot continues to work with local HR rules and database context.

## Operational Notes

- CI uses the `ci` Maven profile and skips integration tests by default to keep pull requests fast and deterministic.
- `integration-tests` profile enables `*IntegrationTest.java` tests when Docker is available.
- Docker build context is smaller through `.dockerignore`, excluding `target`, `node_modules`, local secrets, and IDE metadata.
- Kubernetes manifests live in `deploy/k8s`. Replace `ghcr.io/OWNER/REPOSITORY:latest`, copy `secret.example.yml` to a real secret manifest or use your cluster secret manager, then apply the manifests.

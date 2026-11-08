# SLCA Stress Test — Spring Boot Java 17

A deliberately **behavior-rich but compilable** fixture for validating source-code logging transformations. Contains multiple service classes, integrations, middleware, DTOs, models, controllers, exception handling, nested branches, early returns, void methods, loops, and intentionally triggered runtime/validation failures. The original project is expected to compile. **Do not inject syntax errors into the baseline.**

## Run

```bash
mvn clean test
mvn spring-boot:run
```

Server: http://localhost:8091. Default external API mode is **mock** (offline, deterministic). For live calls to the public [JSONPlaceholder](https://jsonplaceholder.typicode.com/) API, run:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--external.api.live=true
```

Live requests require internet access and can fail due to network/upstream issues. JSONPlaceholder POST is simulated remotely; it does not persist data.

## Example requests and expected responses

### Create order (success, request/response + sensitive fields)
```bash
curl -i -X POST http://localhost:8091/api/orders -H 'Content-Type: application/json' -H 'X-Demo-Key: demo-key' -H 'X-Correlation-ID: test-123' -d '{"customerId":"cust-1","item":"Book","quantity":2,"unitPrice":12.50,"token":"DO_NOT_LOG_SECRET","email":"person@example.com"}'
```
Expected 200, JSON `{"success":true,"message":"Created","data":{"id":101,"customerId":"cust-1","item":"BOOK","quantity":2,"total":25.00,"status":"CREATED"}}` (field formatting may differ). Verify logs never expose token/email/whole request DTO. The response intentionally excludes token/email.

### Get order (error)
```bash
curl -i http://localhost:8091/api/orders/99999
```
Expected HTTP 400, `{"success":false,"code":"BUSINESS_VALIDATION","message":"Order not found","timestamp":"..."}`.

### Missing API key (middleware failure)
```bash
curl -i -X POST http://localhost:8091/api/orders -H 'Content-Type: application/json' -d '{"customerId":"cust-1","item":"Book","quantity":1,"unitPrice":10}'
```
Expected HTTP 400, `BUSINESS_VALIDATION` / `Missing X-Demo-Key`.

### External GET (integration success)
```bash
curl -i http://localhost:8091/api/external/posts/1
```
Expected HTTP 200, `{"success":true,"source":"JSONPlaceholder or mock","data":{"userId":1,"id":1,"title":"Mock public post 1","body":"Offline deterministic response"}}` in mock mode.

### External POST (integration request + response)
```bash
curl -i -X POST http://localhost:8091/api/external/posts -H 'Content-Type: application/json' -d '{"userId":1,"title":"Integration Test","body":"Test payload"}'
```
Expected HTTP 200 with `data.id=101` in mock mode.

### Integration exception
```bash
curl -i http://localhost:8091/api/external/posts/999
```
Expected HTTP 500, `INTERNAL_ERROR`, simulated upstream timeout.

### Other failure/branch scenarios
```bash
curl -i http://localhost:8091/api/scenarios/validation
curl -i http://localhost:8091/api/scenarios/null
curl -i http://localhost:8091/api/scenarios/runtime
curl -i http://localhost:8091/api/scenarios/early
curl -i http://localhost:8091/api/scenarios/branch/7
curl -i http://localhost:8091/api/scenarios/branch/13
curl -i 'http://localhost:8091/api/scenarios/risky?input=explode'
curl -i http://localhost:8091/api/scenarios/void/true
curl -i -H 'X-Block-Request: true' http://localhost:8091/api/orders
```
Expect a mix of 200, 400, 429, and 500 responses. Filter-level 429 does not invoke service methods.

## SLCA acceptance checklist

1. Analyze **all** eligible methods in `service/`, `integration/`, `middleware/` (not just named fixtures); do not instrument controllers.
2. Original business statements appear **once**, with no misplaced wrappers, invalid braces, unreachable statements, or double external requests/writes.
3. Preserve return values, exceptions, early returns, and response JSON.
4. Exact Service events: `Start method`, `End method`. Integration/Middleware: `Start integration method`, `End integration method`.
5. `MethodName`, masked `Parameters`, `Status`, `DurationMs` are independent structured fields when supported by configured logging backend; don't claim plain-text logs are Seq-ready.
6. No secrets or PII in logs (including `token`, `email`, entire DTO). Don't blindly stringify requests/responses.
7. After Accept/Finalize/Save, re-analyzing should propose **no duplicate instrumentation**.
8. Run `mvn clean test` and exercise curl requests **before and after** transformation; compare responses and side-effect counts.
9. Do not change this baseline project's business logic to make generated logging compile.

**Important:** This fixture deliberately contains simulated business/runtime errors; it is not an insecure application or a guarantee that SLCA can safely transform all constructs. For Seq structured-property verification, configure a compatible encoder/transport separately; Spring Boot console logging alone is not a Seq integration.

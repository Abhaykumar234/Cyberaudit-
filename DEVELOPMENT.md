# CyberAudit Pro - Development Guide

## Development Environment Setup

### IDE Setup

#### IntelliJ IDEA (Recommended)
1. Open project root
2. Mark `src/main/java` as Sources Root
3. Mark `src/main/resources` as Resources Root
4. Install plugins:
   - Lombok
   - Spring Boot
   - Database Tools

#### VS Code
1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - REST Client
   - Thunder Client

### Build & Run

#### Maven Commands
```bash
# Clean build
mvn clean install

# Run tests
mvn test

# Run application
mvn spring-boot:run

# Build JAR
mvn clean package

# Skip tests during build
mvn clean package -DskipTests

# Run specific test class
mvn test -Dtest=VulnerabilityServiceTest
```

#### Frontend Commands
```bash
# Install dependencies
npm install

# Development server with hot reload
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint

# Format code
npm run format
```

## Code Structure & Conventions

### Backend (Java)

#### Package Organization
```
com.cyberaudit
├── config/          # Configuration classes
├── controller/      # REST controllers
├── service/         # Business logic
├── model/           # JPA entities
├── dto/             # Data transfer objects
├── repository/      # Data access layer
├── exception/       # Custom exceptions
└── util/            # Utility classes
```

#### Naming Conventions
- **Classes**: PascalCase (e.g., `VulnerabilityService`)
- **Methods**: camelCase (e.g., `getVulnerabilityById`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_PAGE_SIZE`)
- **Variables**: camelCase (e.g., `vulnerabilityList`)

#### Code Style
```java
// Use Lombok annotations
@Service
@RequiredArgsConstructor
@Transactional
public class VulnerabilityService {
    
    private final VulnerabilityRepository repository;
    
    // Use meaningful method names
    public Vulnerability createVulnerability(Vulnerability vuln) {
        return repository.save(vuln);
    }
    
    // Use @Transactional for read operations
    @Transactional(readOnly = true)
    public Optional<Vulnerability> getById(Long id) {
        return repository.findById(id);
    }
}
```

#### Entity Design
```java
@Entity
@Table(name = "vulnerabilities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vulnerability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String cveId;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

#### DTO Design
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VulnerabilityDto {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotNull(message = "Severity is required")
    private Severity severity;
}
```

#### Controller Design
```java
@RestController
@RequestMapping("/vulnerabilities")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class VulnerabilityController {
    
    private final VulnerabilityService service;
    
    @GetMapping
    public ResponseEntity<Page<Vulnerability>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }
    
    @PostMapping
    public ResponseEntity<Vulnerability> create(@Valid @RequestBody VulnerabilityDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(dto));
    }
}
```

### Frontend (React/TypeScript)

#### Component Structure
```typescript
// src/components/VulnerabilityCard.tsx
import React from 'react'
import { Vulnerability } from '../api/vulnerabilityApi'

interface Props {
  vulnerability: Vulnerability
  onSelect: (id: number) => void
}

export const VulnerabilityCard: React.FC<Props> = ({ 
  vulnerability, 
  onSelect 
}) => {
  return (
    <div className="card">
      <h3>{vulnerability.title}</h3>
      <p>{vulnerability.description}</p>
      <button onClick={() => onSelect(vulnerability.id)}>
        View Details
      </button>
    </div>
  )
}
```

#### API Integration
```typescript
// src/api/vulnerabilityApi.ts
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true
})

export const getVulnerabilities = async (page: number = 0) => {
  const response = await api.get(`/vulnerabilities?page=${page}`)
  return response.data
}
```

#### Hook Pattern
```typescript
// src/hooks/useVulnerabilities.ts
import { useState, useEffect } from 'react'
import { getVulnerabilities } from '../api/vulnerabilityApi'

export const useVulnerabilities = () => {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    getVulnerabilities()
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false))
  }, [])

  return { data, loading, error }
}
```

## Testing

### Backend Testing

#### Unit Tests
```java
@SpringBootTest
class VulnerabilityServiceTest {
    
    @MockBean
    private VulnerabilityRepository repository;
    
    @InjectMocks
    private VulnerabilityService service;
    
    @Test
    void testGetVulnerabilityById() {
        // Arrange
        Vulnerability vuln = Vulnerability.builder()
            .id(1L)
            .cveId("A01:2021")
            .build();
        when(repository.findById(1L)).thenReturn(Optional.of(vuln));
        
        // Act
        Optional<Vulnerability> result = service.getById(1L);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("A01:2021", result.get().getCveId());
    }
}
```

#### Integration Tests
```java
@SpringBootTest
@AutoConfigureMockMvc
class VulnerabilityControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetAllVulnerabilities() throws Exception {
        mockMvc.perform(get("/api/vulnerabilities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }
}
```

### Frontend Testing

#### Component Tests
```typescript
import { render, screen } from '@testing-library/react'
import { VulnerabilityCard } from './VulnerabilityCard'

describe('VulnerabilityCard', () => {
  it('renders vulnerability title', () => {
    const vuln = {
      id: 1,
      title: 'Test Vulnerability',
      severity: 'CRITICAL'
    }
    
    render(<VulnerabilityCard vulnerability={vuln} onSelect={() => {}} />)
    
    expect(screen.getByText('Test Vulnerability')).toBeInTheDocument()
  })
})
```

## Debugging

### Backend Debugging

#### Enable Debug Logging
```yaml
# application.yml
logging:
  level:
    com.cyberaudit: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

#### Debug in IDE
1. Set breakpoint in code
2. Run → Debug (or Shift+F9)
3. Use Debug panel to step through code

#### Remote Debugging
```bash
# Start with debug port
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
  -jar target/cyberaudit-pro-1.0.0.jar
```

### Frontend Debugging

#### Browser DevTools
1. Open Chrome DevTools (F12)
2. Sources tab for breakpoints
3. Console for logs
4. Network tab for API calls

#### VS Code Debugging
```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "chrome",
      "request": "launch",
      "name": "Launch Chrome",
      "url": "http://localhost:5173",
      "webRoot": "${workspaceFolder}/frontend/src"
    }
  ]
}
```

## Common Development Tasks

### Adding a New Endpoint

1. **Create DTO** (if needed)
```java
// src/main/java/com/cyberaudit/dto/NewFeatureDto.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFeatureDto {
    private String name;
}
```

2. **Add Repository Method** (if needed)
```java
// src/main/java/com/cyberaudit/repository/NewFeatureRepository.java
public interface NewFeatureRepository extends JpaRepository<NewFeature, Long> {
    Optional<NewFeature> findByName(String name);
}
```

3. **Add Service Method**
```java
// src/main/java/com/cyberaudit/service/NewFeatureService.java
public NewFeature create(NewFeatureDto dto) {
    return repository.save(new NewFeature(dto.getName()));
}
```

4. **Add Controller Endpoint**
```java
// src/main/java/com/cyberaudit/controller/NewFeatureController.java
@PostMapping
public ResponseEntity<NewFeature> create(@Valid @RequestBody NewFeatureDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.create(dto));
}
```

5. **Test the Endpoint**
```bash
curl -X POST http://localhost:8080/api/newfeature \
  -H "Content-Type: application/json" \
  -d '{"name": "test"}'
```

### Adding a New React Component

1. **Create Component**
```typescript
// frontend/src/components/NewComponent.tsx
export const NewComponent: React.FC = () => {
  return <div>New Component</div>
}
```

2. **Create API Service** (if needed)
```typescript
// frontend/src/api/newFeatureApi.ts
export const getNewFeatures = () => {
  return api.get('/newfeature')
}
```

3. **Use in Page**
```typescript
// frontend/src/pages/NewFeaturePage.tsx
import { NewComponent } from '../components/NewComponent'

export const NewFeaturePage = () => {
  return <NewComponent />
}
```

### Database Migration

1. **Modify Entity**
```java
@Entity
public class Vulnerability {
    @Column(nullable = false)
    private String newField;
}
```

2. **For Development (H2)**
- Automatic schema update (ddl-auto: create-drop)

3. **For Production (PostgreSQL)**
- Use Flyway or Liquibase for migrations
- Create migration file: `V1__initial_schema.sql`

## Performance Optimization

### Backend

#### Query Optimization
```java
// Use @Query for complex queries
@Query("SELECT v FROM Vulnerability v WHERE v.severity = :severity")
Page<Vulnerability> findBySeverity(@Param("severity") Severity severity, Pageable pageable);

// Use projection for partial data
@Query("SELECT new com.cyberaudit.dto.VulnerabilityDto(v.id, v.title) FROM Vulnerability v")
List<VulnerabilityDto> findAllProjected();
```

#### Caching
```java
@Service
@CacheConfig(cacheNames = "vulnerabilities")
public class VulnerabilityService {
    
    @Cacheable
    public Vulnerability getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @CacheEvict(allEntries = true)
    public Vulnerability update(Vulnerability vuln) {
        return repository.save(vuln);
    }
}
```

### Frontend

#### Code Splitting
```typescript
// Use React.lazy for route-based splitting
const VulnerabilityPage = React.lazy(() => 
  import('./pages/VulnerabilityPage')
)
```

#### Memoization
```typescript
// Prevent unnecessary re-renders
const VulnerabilityCard = React.memo(({ vuln }) => {
  return <div>{vuln.title}</div>
})
```

## Troubleshooting Common Issues

### Issue: Port Already in Use
```bash
# Find process
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Issue: Database Connection Failed
```bash
# Check PostgreSQL is running
psql -U postgres -d cyberaudit_db

# Or use H2 for development (default)
```

### Issue: CORS Error
```
Access to XMLHttpRequest blocked by CORS policy
```
Solution: Check `SecurityConfig.corsConfigurationSource()` includes your frontend URL

### Issue: Hot Reload Not Working
```bash
# Restart Spring Boot
mvn spring-boot:run

# Or use Spring Boot DevTools
# (automatically included in pom.xml)
```

### Issue: Frontend API Calls Failing
```bash
# Check backend is running
curl http://localhost:8080/api/audit/health

# Check CORS headers
curl -i http://localhost:8080/api/vulnerabilities

# Check Vite proxy config
# frontend/vite.config.ts
```

## Git Workflow

### Branch Naming
```
feature/add-new-endpoint
bugfix/fix-cors-issue
docs/update-readme
```

### Commit Messages
```
feat: add vulnerability filtering by severity
fix: resolve CORS issue with frontend
docs: update API documentation
test: add unit tests for VulnerabilityService
```

### Pull Request Process
1. Create feature branch
2. Make changes and commit
3. Push to remote
4. Create pull request
5. Request review
6. Merge after approval

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [JPA/Hibernate Guide](https://hibernate.org/orm/documentation/)
- [Vite Documentation](https://vitejs.dev/)

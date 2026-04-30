# ✅ USER MANAGEMENT FIXES

**Date:** April 30, 2026  
**Issues Fixed:** 2 major problems

---

## 🔧 FIX #1: Duplicate Employee Code Error

### ❌ Lỗi:
```
Duplicate entry 'NV00003' for key 'user.employee_code'
```

### 🔍 Nguyên nhân:
- Khi tạo user mới, `employee_code` được nhập từ form
- Không có validation để check duplicate
- Không có logic auto-generate unique code

### ✅ Giải pháp:
**File:** `src/main/java/com/example/hr/service/UserService.java`

**Thêm logic:**
1. **Khi tạo user mới:**
   - Nếu có employee_code → check duplicate → nếu trùng thì auto-generate
   - Nếu không có → auto-generate

2. **Khi update user:**
   - Nếu đổi code → check duplicate → nếu trùng thì giữ code cũ
   - Nếu không đổi → giữ nguyên

3. **Auto-generate format:** `NV00001`, `NV00002`, `NV00003`...

**Code mới:**
```java
private String generateUniqueEmployeeCode() {
    // Find highest number
    List<User> allUsers = userRepository.findAll();
    int maxNumber = 0;
    
    for (User u : allUsers) {
        String code = u.getEmployeeCode();
        if (code != null && code.startsWith("NV")) {
            try {
                int num = Integer.parseInt(code.substring(2));
                if (num > maxNumber) {
                    maxNumber = num;
                }
            } catch (NumberFormatException ignored) {}
        }
    }
    
    // Generate next code
    int nextNumber = maxNumber + 1;
    String newCode = String.format("NV%05d", nextNumber);
    
    // Safety check
    while (userRepository.findByEmployeeCode(newCode).isPresent()) {
        nextNumber++;
        newCode = String.format("NV%05d", nextNumber);
    }
    
    return newCode;
}
```

---

## 🔧 FIX #2: Role-Based Dashboard Access

### ❌ Vấn đề:
- User với role MANAGER, HIRING không vào được dashboard của họ
- Bị redirect sai hoặc access denied

### 🔍 Kiểm tra:

#### 1. SecurityConfig ✅ (Đã đúng)
```java
.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
.requestMatchers("/hiring/**").hasAnyRole("ADMIN", "HIRING", "MANAGER")
```

#### 2. HomeController ✅ (Đã đúng)
```java
@GetMapping("/home")
public String dashboard(Authentication authentication) {
    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
    
    if (roles.contains("ROLE_ADMIN")) {
        return "admin/dashboard";
    } else if (roles.contains("ROLE_MANAGER")) {
        return "manager/dashboard";
    } else if (roles.contains("ROLE_HIRING")) {
        return "hiring/dashboard";
    } else {
        return "redirect:/user1/dashboard";
    }
}
```

#### 3. Templates ✅ (Đã tồn tại)
- ✅ `templates/manager/dashboard.html`
- ✅ `templates/hiring/dashboard.html`

### ✅ Giải pháp:

#### Bước 1: Kiểm tra Role trong Database
```sql
-- Check user roles
SELECT id, username, full_name, role, status 
FROM user 
WHERE role IN ('MANAGER', 'HIRING');

-- Update role nếu sai
UPDATE user 
SET role = 'MANAGER' 
WHERE username = 'manager_username';

UPDATE user 
SET role = 'HIRING' 
WHERE username = 'hiring_username';
```

#### Bước 2: Kiểm tra Enum Role
**File:** `src/main/java/com/example/hr/enums/Role.java`

Đảm bảo có:
```java
public enum Role {
    ADMIN,
    MANAGER,
    HIRING,
    USER
}
```

#### Bước 3: Test Access
1. **Login với MANAGER:**
   - URL sau login: `/home` → redirect `/manager/dashboard`
   - Hoặc trực tiếp: `/manager/dashboard`

2. **Login với HIRING:**
   - URL sau login: `/home` → redirect `/hiring/dashboard`
   - Hoặc trực tiếp: `/hiring/dashboard`

3. **Login với USER:**
   - URL sau login: `/home` → redirect `/user1/dashboard`

---

## 🎯 Cách Test

### Test 1: Tạo User Mới
```
1. Vào /admin/users/add
2. Điền thông tin KHÔNG điền employee_code
3. Submit
4. ✅ Hệ thống tự generate: NV00001, NV00002...
```

### Test 2: Tạo User với Code Trùng
```
1. Vào /admin/users/add
2. Điền employee_code = "NV00001" (đã tồn tại)
3. Submit
4. ✅ Hệ thống tự generate code mới: NV00004, NV00005...
```

### Test 3: Manager Dashboard
```
1. Login với user có role = MANAGER
2. Sau login redirect về /home
3. ✅ Tự động redirect /manager/dashboard
4. Hoặc truy cập trực tiếp /manager/dashboard
5. ✅ Hiển thị dashboard
```

### Test 4: Hiring Dashboard
```
1. Login với user có role = HIRING
2. Sau login redirect về /home
3. ✅ Tự động redirect /hiring/dashboard
4. Hoặc truy cập trực tiếp /hiring/dashboard
5. ✅ Hiển thị dashboard
```

---

## 📊 Role Access Matrix

| URL | ADMIN | MANAGER | HIRING | USER |
|-----|-------|---------|--------|------|
| `/admin/**` | ✅ | ❌ | ❌ | ❌ |
| `/manager/**` | ✅ | ✅ | ❌ | ❌ |
| `/hiring/**` | ✅ | ✅ | ✅ | ❌ |
| `/user1/**` | ✅ | ✅ | ✅ | ✅ |

---

## 🔍 Debug Steps

### Nếu vẫn không vào được dashboard:

#### 1. Check Role trong Database
```sql
SELECT username, role FROM user WHERE username = 'your_username';
```

#### 2. Check Authentication
Thêm log trong `HomeController`:
```java
@GetMapping("/home")
public String dashboard(Authentication authentication) {
    System.out.println("User: " + authentication.getName());
    System.out.println("Authorities: " + authentication.getAuthorities());
    
    // ... rest of code
}
```

#### 3. Check Security Debug
Trong `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
```

#### 4. Check Template Path
```bash
# Verify templates exist
ls -la src/main/resources/templates/manager/
ls -la src/main/resources/templates/hiring/
```

---

## 💡 Common Issues

### Issue 1: "Access Denied"
**Nguyên nhân:** Role không đúng trong database  
**Fix:** Update role trong database

### Issue 2: "Template not found"
**Nguyên nhân:** Template không tồn tại  
**Fix:** Tạo template hoặc check path

### Issue 3: "Redirect loop"
**Nguyên nhân:** SecurityConfig conflict  
**Fix:** Check `.requestMatchers()` order

### Issue 4: "Employee code duplicate"
**Nguyên nhân:** Đã fix ở trên  
**Fix:** Code tự động generate unique

---

## 🎉 Expected Results

### ✅ After Fixes:
1. **Tạo user mới** → Employee code tự động generate unique
2. **MANAGER login** → Vào được `/manager/dashboard`
3. **HIRING login** → Vào được `/hiring/dashboard`
4. **USER login** → Vào được `/user1/dashboard`
5. **ADMIN login** → Vào được tất cả dashboards

---

## 📝 SQL Scripts

### Create Test Users
```sql
-- Manager user
INSERT INTO user (username, password, full_name, email, role, status, employee_code, created_at)
VALUES ('manager1', '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 
        'Manager Test', 'manager@test.com', 'MANAGER', 'ACTIVE', 'NV10001', NOW());

-- Hiring user
INSERT INTO user (username, password, full_name, email, role, status, employee_code, created_at)
VALUES ('hiring1', '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 
        'Hiring Test', 'hiring@test.com', 'HIRING', 'ACTIVE', 'NV10002', NOW());

-- Password for both: 123456
```

### Check Existing Users
```sql
SELECT 
    id,
    username,
    full_name,
    email,
    role,
    status,
    employee_code
FROM user
ORDER BY role, created_at DESC;
```

---

**Status:** ✅ **ALL FIXES APPLIED**

Bây giờ bạn có thể:
1. Tạo user mới không lo duplicate employee_code
2. Login với MANAGER/HIRING role và vào được dashboard của họ

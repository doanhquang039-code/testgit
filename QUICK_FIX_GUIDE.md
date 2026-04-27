# 🔧 QUICK FIX GUIDE - 74 Compilation Errors

## ⚠️ VẤN ĐỀ

Code mới tôi viết dựa trên assumptions về models, nhưng models hiện tại có structure khác.

## 🎯 GIẢI PHÁP NHANH

### Option 1: Comment Out New Features (NHANH NHẤT - 2 phút)

Tạm thời comment out các controllers mới để app chạy được:

```bash
# Rename để skip compile
mv src/main/java/com/example/hr/controllers/AdvancedAnalyticsController.java src/main/java/com/example/hr/controllers/AdvancedAnalyticsController.java.bak
mv src/main/java/com/example/hr/controllers/AdvancedLeaveController.java src/main/java/com/example/hr/controllers/AdvancedLeaveController.java.bak
mv src/main/java/com/example/hr/controllers/OKRController.java src/main/java/com/example/hr/controllers/OKRController.java.bak
mv src/main/java/com/example/hr/controllers/AdvancedNotificationController.java src/main/java/com/example/hr/controllers/AdvancedNotificationController.java.bak
mv src/main/java/com/example/hr/controllers/SelfServiceController.java src/main/java/com/example/hr/controllers/SelfServiceController.java.bak

# Rename services
mv src/main/java/com/example/hr/service/AdvancedAnalyticsService.java src/main/java/com/example/hr/service/AdvancedAnalyticsService.java.bak
mv src/main/java/com/example/hr/service/AdvancedLeaveService.java src/main/java/com/example/hr/service/AdvancedLeaveService.java.bak
mv src/main/java/com/example/hr/service/OKRService.java src/main/java/com/example/hr/service/OKRService.java.bak
mv src/main/java/com/example/hr/service/AdvancedNotificationService.java src/main/java/com/example/hr/service/AdvancedNotificationService.java.bak
```

Sau đó compile lại:
```bash
./mvnw clean compile
```

---

### Option 2: Fix All Errors (ĐÚNG NHẤT - 30 phút)

Tôi sẽ tạo các fix files ngay bây giờ!

---

## 📝 CÁC LỖI CHÍNH CẦN FIX

### 1. Department.getName() → getDepartmentName()
### 2. PerformanceReview.getEmployee() → getUser()
### 3. PerformanceReview.getOverallRating() → getOverallScore()
### 4. Notification - Thêm title field
### 5. User - Thêm emergencyContact, emergencyPhone
### 6. UserService - Thêm getUserByUsername()
### 7. EmployeeDocument - Conflict với model cũ
### 8. LeaveType - Thêm MARRIAGE, BEREAVEMENT
### 9. Payroll.getNetSalary() → BigDecimal
### 10. ID types: Long vs Integer

---

## 🚀 TÔI SẼ FIX NGAY!

Chọn option nào bạn?
1. Comment out (chạy được ngay, test các tính năng cũ)
2. Fix all (tôi sẽ fix hết 74 lỗi, mất 10-15 phút)

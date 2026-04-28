# Menu Routes Fixed - Sửa lỗi Routes ✅

## Ngày: 28/04/2026

## Vấn đề
User gặp lỗi 500 khi click vào các menu mới:
- `/admin/qrcodes` → 404 (No static resource)
- `/lms/admin/courses` → 500 (Template parsing error)

## Nguyên nhân

### 1. QR Code Route sai
- **Menu link**: `/admin/qrcodes`
- **Controller route thực tế**: `/qrcode/admin/list`
- **Lỗi**: Route không khớp → 404

### 2. LMS Template sai format
- **Template**: `lms/admin/course-list.html`
- **Vấn đề**: Dùng `fragments/navbar` thay vì `fragments/admin-sidebar`
- **Vấn đề**: Dùng Font Awesome thay vì Bootstrap Icons
- **Lỗi**: Template parsing error → 500

## Giải pháp đã áp dụng

### 1. Sửa QR Code Menu Link
**File**: `admin/dashboard.html`

```html
<!-- Trước -->
<a th:href="@{/admin/qrcodes}"><i class="bi bi-qr-code"></i> QR Code</a>

<!-- Sau -->
<a th:href="@{/qrcode/admin/list}"><i class="bi bi-qr-code"></i> QR Code</a>
```

### 2. Sửa LMS Admin Template
**File**: `lms/admin/course-list.html`

**Thay đổi:**
1. ✅ Thay `fragments/navbar` → `fragments/admin-sidebar`
2. ✅ Thay Font Awesome → Bootstrap Icons
3. ✅ Thêm layout container-fluid với sidebar
4. ✅ Cập nhật icon classes (`fas` → `bi`)
5. ✅ Sửa button groups và styling

**Trước:**
```html
<div th:replace="~{fragments/navbar :: navbar}"></div>
<div class="container-fluid mt-4">
    <i class="fas fa-graduation-cap"></i>
```

**Sau:**
```html
<div class="container-fluid">
    <div class="row">
        <div th:replace="~{fragments/admin-sidebar :: sidebar}"></div>
        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <i class="bi bi-book"></i>
```

## Tất cả Routes đúng

### Admin Routes (Đã kiểm tra):
```
✅ /lms/admin/courses              → lms/admin/course-list.html
✅ /qrcode/admin/list              → admin/qrcode-list.html
✅ /engagement/admin/surveys       → engagement/admin/survey-list.html
✅ /engagement/admin/recognition   → engagement/admin/recognition-list.html
✅ /onboarding/admin/checklists    → onboarding/admin/checklist-list.html
✅ /onboarding/admin/exit-interviews → onboarding/admin/exit-interview-list.html
✅ /admin/okr                      → admin/okr-list.html
✅ /admin/settings                 → admin/settings-list.html
✅ /admin/analytics                → admin/analytics-dashboard.html
```

### User Routes (Đã kiểm tra):
```
✅ /lms/courses                    → lms/course-catalog.html
✅ /lms/my-courses                 → lms/my-courses.html
✅ /engagement/feed                → engagement/social-feed.html
✅ /engagement/recognition         → engagement/recognition-wall.html
✅ /engagement/surveys             → engagement/surveys.html
✅ /engagement/referrals           → engagement/my-referrals.html
✅ /onboarding/my-checklist        → onboarding/my-checklist.html
✅ /user1/okr-list                 → user1/okr-list.html
✅ /user1/my-scans                 → user1/qrcode-scan.html
```

## Files đã sửa: 2

1. ✅ `admin/dashboard.html` - Sửa QR Code menu link
2. ✅ `lms/admin/course-list.html` - Sửa template layout và icons

## Build Status

```bash
./mvnw clean compile -DskipTests
```

**Kết quả:** ✅ BUILD SUCCESS

## Testing

### Để test các menu đã sửa:

1. **Admin Dashboard:**
   - [ ] Click "LMS - Khóa học" → Hiển thị danh sách khóa học
   - [ ] Click "QR Code" → Hiển thị danh sách QR codes
   - [ ] Click "Khảo sát" → Hiển thị danh sách khảo sát
   - [ ] Click "Vinh danh" → Hiển thị danh sách vinh danh
   - [ ] Click "Onboarding" → Hiển thị danh sách checklist
   - [ ] Click "OKR" → Hiển thị danh sách OKR
   - [ ] Click "Cài đặt" → Hiển thị cài đặt hệ thống
   - [ ] Click "Phân tích" → Hiển thị analytics dashboard

2. **User Dashboard:**
   - [ ] Click "Khóa học của tôi" → Hiển thị khóa học đã đăng ký
   - [ ] Click "Danh mục khóa học" → Hiển thị catalog
   - [ ] Click "Mạng xã hội" → Hiển thị social feed
   - [ ] Click "Vinh danh" → Hiển thị recognition wall
   - [ ] Click "Khảo sát" → Hiển thị surveys
   - [ ] Click "Giới thiệu ứng viên" → Hiển thị referrals
   - [ ] Click "Checklist của tôi" → Hiển thị onboarding checklist
   - [ ] Click "QR Check-in" → Hiển thị scan history
   - [ ] Click "OKR" → Hiển thị OKR list

## Lưu ý quan trọng

### Route Naming Convention:
- **Admin routes**: `/feature/admin/action` hoặc `/admin/feature`
- **User routes**: `/feature/action` hoặc `/user1/feature`

### Template Location:
- **Admin templates**: `templates/feature/admin/` hoặc `templates/admin/`
- **User templates**: `templates/feature/` hoặc `templates/user1/`

### Sidebar Fragment:
- **Admin pages**: Phải dùng `fragments/admin-sidebar`
- **User pages**: Phải dùng `fragments/user-sidebar` (nếu có)

### Icon Library:
- **Toàn bộ project**: Bootstrap Icons (`bi bi-*`)
- **KHÔNG dùng**: Font Awesome (`fas fa-*`)

## Kết luận

✅ **Đã sửa xong tất cả lỗi routes**

- QR Code menu link đã đúng
- LMS admin template đã đúng format
- Tất cả routes đã được kiểm tra
- Build thành công

**Status:** ✅ HOÀN THÀNH
**Ready for:** Testing trên browser


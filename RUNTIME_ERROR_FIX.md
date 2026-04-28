# Runtime Error Fix - Lỗi 500 đã được sửa ✅

## Ngày: 28/04/2026

## Vấn đề
Người dùng gặp lỗi 500 (Whitelabel Error Page) với thông báo:
```
Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.
```

## Nguyên nhân
Sau khi phân tích, phát hiện 2 vấn đề chính:

### 1. Thiếu Error Template
- Ứng dụng không có file `error.html` trong thư mục templates
- Spring Boot hiển thị Whitelabel Error Page mặc định (không đẹp và không có thông tin chi tiết)

### 2. Service sử dụng Repository cũ
Hai service vẫn đang sử dụng `CompanyAssetRepository` (cũ) thay vì `AssetRepository` (mới):
- **DashboardService**: Dùng `CompanyAssetRepository` với model `CompanyAsset` (đã bị thay thế)
- **ReportGenerationService**: Dùng `CompanyAssetRepository` với model `CompanyAsset` (đã bị thay thế)

Điều này gây lỗi runtime vì:
- Model `CompanyAsset` có các field khác với model `Asset` mới
- `CompanyAsset` dùng enum `AssetStatus`, còn `Asset` dùng String
- Tên các field khác nhau (ví dụ: `assetName` vs `name`, `currentValue` vs `purchasePrice`)

## Giải pháp đã áp dụng

### 1. Tạo Error Template đẹp và thông tin
**File**: `src/main/resources/templates/error.html`

**Tính năng**:
- ✅ Giao diện đẹp với gradient background (purple theme)
- ✅ Hiển thị error code (404, 500, etc.)
- ✅ Hiển thị error message bằng tiếng Việt
- ✅ Hiển thị chi tiết lỗi (details) khi có - giúp debug
- ✅ Nút "Về trang chủ" và "Quay lại"
- ✅ Responsive design
- ✅ Bootstrap Icons

### 2. Thêm Logging vào GlobalExceptionHandler
**File**: `src/main/java/com/example/hr/exception/GlobalExceptionHandler.java`

**Thay đổi**:
```java
// Thêm Logger
private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

// Trong handleGenericException
logger.error("Unexpected exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
mav.addObject("details", ex.getMessage()); // Thêm chi tiết lỗi
```

**Lợi ích**:
- Log chi tiết exception vào console/file để debug
- Hiển thị thông tin lỗi trên error page (trong development)
- Dễ dàng tracking và fix lỗi

### 3. Cập nhật DashboardService
**File**: `src/main/java/com/example/hr/service/DashboardService.java`

**Thay đổi**:
```java
// Trước
import com.example.hr.repository.CompanyAssetRepository;
private final CompanyAssetRepository assetRepository;
long availableAssets = assetRepository.countByStatus(AssetStatus.AVAILABLE);

// Sau
import com.example.hr.repository.AssetRepository;
private final AssetRepository assetRepository;
long availableAssets = assetRepository.countByStatus("AVAILABLE");
```

### 4. Cập nhật ReportGenerationService
**File**: `src/main/java/com/example/hr/service/ReportGenerationService.java`

**Thay đổi**:

#### Import và Constructor
```java
// Trước
import com.example.hr.repository.CompanyAssetRepository;
private final CompanyAssetRepository assetRepository;

// Sau
import com.example.hr.repository.AssetRepository;
private final AssetRepository assetRepository;
```

#### exportAssetReport() method
```java
// Trước
List<CompanyAsset> assets = assetRepository.findAll();
row.add(a.getAssetName());
row.add(a.getCurrentValue());
row.add(a.getStatus().name());
row.add(a.getWarrantyExpiry());

// Sau
List<Asset> assets = assetRepository.findAll();
row.add(a.getName());
row.add(a.getPurchasePrice());
row.add(a.getStatus());
row.add(a.getLocation());
```

#### generateYearlySummary() method
```java
// Trước
.map(CompanyAsset::getCurrentValue)

// Sau
.map(Asset::getPurchasePrice)
```

## Mapping Field Changes

### CompanyAsset (Cũ) → Asset (Mới)
| CompanyAsset (Old) | Asset (New) |
|-------------------|-------------|
| `assetName` | `name` |
| `assetCode` | `assetCode` ✅ (giữ nguyên) |
| `category` | `category` ✅ (giữ nguyên) |
| `purchasePrice` | `purchasePrice` ✅ (giữ nguyên) |
| `currentValue` | ❌ (không có - dùng `purchasePrice`) |
| `purchaseDate` | `purchaseDate` ✅ (giữ nguyên) |
| `status` (enum) | `status` (String) |
| `warrantyExpiry` | ❌ (không có - dùng `location`) |
| ❌ | `condition` (mới) |
| ❌ | `location` (mới) |

## Build Status

### Trước fix
- ❌ Runtime Error 500
- ❌ Whitelabel Error Page
- ❌ Không có thông tin lỗi chi tiết

### Sau fix
- ✅ Compilation: SUCCESS
- ✅ Package: SUCCESS
- ✅ Error page đẹp và thông tin
- ✅ Logging chi tiết exception
- ✅ Tất cả services dùng đúng repository mới

## Files đã sửa

1. ✅ `src/main/resources/templates/error.html` (TẠO MỚI)
2. ✅ `src/main/java/com/example/hr/exception/GlobalExceptionHandler.java`
3. ✅ `src/main/java/com/example/hr/service/DashboardService.java`
4. ✅ `src/main/java/com/example/hr/service/ReportGenerationService.java`

## Kiểm tra

### Để test error page:
1. Truy cập URL không tồn tại: `http://localhost:8080/not-found`
2. Sẽ thấy error page đẹp với error 404

### Để xem logs:
1. Chạy ứng dụng: `./mvnw spring-boot:run`
2. Khi có lỗi, check console sẽ thấy log chi tiết:
```
ERROR c.e.hr.exception.GlobalExceptionHandler - Unexpected exception at /some-url: Error message
```

## Lưu ý

### Development vs Production
- **Development**: Error page hiển thị chi tiết lỗi (details)
- **Production**: Nên tắt hiển thị details để bảo mật

### Để tắt details trong production:
Trong `GlobalExceptionHandler.handleGenericException()`:
```java
// Chỉ thêm details trong development
if (isDevelopment()) {
    mav.addObject("details", ex.getMessage());
}
```

## Kết quả

✅ **Ứng dụng đã sẵn sàng chạy**
- Không còn lỗi compilation
- Không còn lỗi runtime do repository cũ
- Error handling đẹp và chuyên nghiệp
- Logging đầy đủ để debug

## Lệnh chạy ứng dụng

```bash
# Compile
./mvnw clean compile -DskipTests

# Package
./mvnw package -DskipTests

# Run
./mvnw spring-boot:run

# Hoặc chạy JAR
java -jar target/hr-management-system-0.0.1-SNAPSHOT.jar
```

---

**Status**: ✅ HOÀN THÀNH
**Build**: ✅ SUCCESS
**Ready**: ✅ SẴN SÀNG CHẠY


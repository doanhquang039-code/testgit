# 🧪 TESTING GUIDE - Advanced Features

## 📋 Hướng dẫn test các tính năng mới

### 🚀 Khởi động server
```bash
npm run dev
```

Server sẽ chạy tại: `http://localhost:8080`

---

## 🤖 Test AI Content Generation

### 1. Tạo nội dung bằng AI
```bash
curl -X POST http://localhost:8080/api/ai/generate-content \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "topic": "JavaScript ES6 Features",
    "category": "programming", 
    "tone": "professional",
    "length": "medium"
  }'
```

### 2. Tạo tiêu đề SEO
```bash
curl -X POST http://localhost:8080/api/ai/generate-seo-title \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "content": "This is a tutorial about JavaScript",
    "keywords": ["javascript", "tutorial", "programming"]
  }'
```

### 3. Tạo tags tự động
```bash
curl -X POST http://localhost:8080/api/ai/generate-tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "content": "This article covers React hooks, useState, useEffect and custom hooks",
    "title": "Complete Guide to React Hooks"
  }'
```

### 4. Tối ưu hóa SEO
```bash
curl -X POST http://localhost:8080/api/ai/optimize-seo \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "content": "React hooks are a powerful feature...",
    "targetKeyword": "react hooks"
  }'
```

---

## 📱 Test Social Media Integration

### 1. Chia sẻ bài viết
```bash
curl -X POST http://localhost:8080/api/social/share \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "postId": 1,
    "platforms": ["facebook", "twitter", "linkedin"]
  }'
```

### 2. Lấy thống kê social
```bash
curl -X GET http://localhost:8080/api/social/stats/1
```

### 3. Tạo preview social media
```bash
curl -X GET http://localhost:8080/api/social/preview/1
```

### 4. Lên lịch đăng social
```bash
curl -X POST http://localhost:8080/api/social/schedule \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "postId": 1,
    "platforms": ["twitter", "facebook"],
    "scheduledAt": "2024-04-15T10:00:00Z",
    "content": "Check out this amazing article!"
  }'
```

### 5. Lấy trending hashtags
```bash
curl -X GET "http://localhost:8080/api/social/trending-hashtags?platform=twitter"
```

---

## 📊 Test Advanced Analytics

### 1. Real-time Dashboard
```bash
curl -X GET http://localhost:8080/api/advanced-analytics/dashboard/realtime \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2. Phân tích hành vi người dùng
```bash
curl -X GET "http://localhost:8080/api/advanced-analytics/user-behavior?days=30" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3. Tạo báo cáo (JSON)
```bash
curl -X GET "http://localhost:8080/api/advanced-analytics/report?startDate=2024-04-01&endDate=2024-04-10&format=json" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 4. Tạo báo cáo Excel
```bash
curl -X GET "http://localhost:8080/api/advanced-analytics/report?startDate=2024-04-01&endDate=2024-04-10&format=excel" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  --output report.xlsx
```

### 5. Phân tích SEO
```bash
curl -X GET http://localhost:8080/api/advanced-analytics/seo-analysis \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 6. Dự đoán xu hướng
```bash
curl -X GET "http://localhost:8080/api/advanced-analytics/trend-prediction?category=technology&days=30" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🎨 Test Advanced CMS

### 1. Bulk Operations - Xuất bản hàng loạt
```bash
curl -X POST http://localhost:8080/api/cms/bulk-operations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "action": "publish",
    "postIds": [1, 2, 3]
  }'
```

### 2. Bulk Operations - Thêm tags
```bash
curl -X POST http://localhost:8080/api/cms/bulk-operations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "action": "addTags",
    "postIds": [1, 2, 3],
    "data": {
      "tags": ["featured", "trending"]
    }
  }'
```

### 3. Tạo template
```bash
curl -X POST http://localhost:8080/api/cms/templates \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "Blog Post Template",
    "description": "Standard blog post template",
    "content": "<h1>{{title}}</h1><p>{{content}}</p><p>Author: {{author}}</p>",
    "type": "post",
    "variables": [
      {"name": "title", "type": "string", "defaultValue": ""},
      {"name": "content", "type": "text", "defaultValue": ""},
      {"name": "author", "type": "string", "defaultValue": "Anonymous"}
    ]
  }'
```

### 4. Lấy danh sách templates
```bash
curl -X GET "http://localhost:8080/api/cms/templates?type=post&page=1&limit=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 5. Tạo post từ template
```bash
curl -X POST http://localhost:8080/api/cms/templates/create-post \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "templateId": 1,
    "variables": {
      "title": "My New Post from Template",
      "content": "This is the content of my new post created from a template.",
      "author": "John Doe"
    },
    "title": "My New Post from Template"
  }'
```

### 6. Lấy media library
```bash
curl -X GET "http://localhost:8080/api/cms/media?type=image&page=1&limit=20&search=logo" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 7. Tạo version cho post
```bash
curl -X POST http://localhost:8080/api/cms/posts/1/versions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "reason": "Major content update with new examples"
  }'
```

### 8. Submit post for review
```bash
curl -X POST http://localhost:8080/api/cms/posts/1/submit-review \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "reviewerId": 2,
    "notes": "Please review for SEO optimization and grammar"
  }'
```

### 9. Lấy content analytics
```bash
curl -X GET "http://localhost:8080/api/cms/content-analytics?period=30d" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 10. Lấy optimization suggestions
```bash
curl -X GET http://localhost:8080/api/cms/posts/1/optimization-suggestions \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🔐 Test Authentication & Authorization

### 1. Test với token không hợp lệ
```bash
curl -X GET http://localhost:8080/api/advanced-analytics/dashboard/realtime \
  -H "Authorization: Bearer INVALID_TOKEN"
```
**Expected:** 403 Forbidden

### 2. Test rate limiting
```bash
# Gửi nhiều requests liên tiếp để test rate limit
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/ai/generate-content \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer YOUR_TOKEN" \
    -d '{"topic": "Test '$i'"}' &
done
```

### 3. Test role-based access
```bash
# Test với user role thấp hơn
curl -X POST http://localhost:8080/api/cms/bulk-operations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer USER_TOKEN" \
  -d '{
    "action": "publish",
    "postIds": [1, 2, 3]
  }'
```
**Expected:** 403 Insufficient permissions

---

## 🧪 Test Scenarios

### Scenario 1: Complete Content Creation Workflow
```bash
# 1. Tạo nội dung bằng AI
AI_CONTENT=$(curl -s -X POST http://localhost:8080/api/ai/generate-content \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"topic": "Node.js Best Practices", "tone": "professional"}')

# 2. Tối ưu hóa SEO
SEO_OPTIMIZED=$(curl -s -X POST http://localhost:8080/api/ai/optimize-seo \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"content": "'"$(echo $AI_CONTENT | jq -r .content.content)"'", "targetKeyword": "nodejs"}')

# 3. Tạo post (sử dụng API posts hiện có)
# 4. Chia sẻ lên social media
# 5. Theo dõi analytics
```

### Scenario 2: Bulk Content Management
```bash
# 1. Lấy danh sách posts cần xử lý
# 2. Bulk publish
# 3. Bulk add tags
# 4. Generate analytics report
```

### Scenario 3: Template-based Content Creation
```bash
# 1. Tạo template
# 2. Tạo multiple posts từ template
# 3. Bulk operations trên các posts đó
# 4. Analytics cho template usage
```

---

## 📊 Expected Results

### AI Content Generation
- ✅ Trả về nội dung có cấu trúc
- ✅ Metadata đầy đủ (word count, reading time)
- ✅ SEO suggestions hợp lý
- ✅ Tags relevant với nội dung

### Social Media Integration
- ✅ Share URLs được tạo đúng format
- ✅ Preview data đầy đủ cho từng platform
- ✅ Stats tracking chính xác
- ✅ Hashtags trending cập nhật

### Advanced Analytics
- ✅ Dashboard real-time data
- ✅ Charts và metrics chính xác
- ✅ Export files (Excel/PDF) hoạt động
- ✅ Trend predictions có logic

### CMS Features
- ✅ Bulk operations thành công
- ✅ Templates render đúng variables
- ✅ Media library pagination
- ✅ Versioning tracking changes
- ✅ Workflow status updates

---

## 🐛 Common Issues & Solutions

### Issue 1: Token Authentication Failed
**Solution:** Đảm bảo JWT_SECRET trong .env và token được tạo đúng

### Issue 2: Rate Limit Exceeded
**Solution:** Đợi window time hoặc tăng limit trong middleware

### Issue 3: Database Connection Error
**Solution:** Kiểm tra MySQL service và connection string

### Issue 4: File Upload/Media Issues
**Solution:** Kiểm tra permissions thư mục uploads và Cloudinary config

### Issue 5: AI Generation Timeout
**Solution:** Tăng timeout hoặc implement queue system

---

## 📈 Performance Testing

### Load Testing với Artillery
```bash
npm install -g artillery

# Tạo file artillery-config.yml
artillery run artillery-config.yml
```

### Memory Usage Monitoring
```bash
# Monitor memory usage
node --inspect app.js
```

### Database Query Performance
```bash
# Enable MySQL slow query log
# Monitor query execution times
```

---

## ✅ Test Checklist

### Functional Tests
- [ ] AI content generation works
- [ ] Social media sharing creates correct URLs
- [ ] Analytics dashboard loads data
- [ ] Bulk operations execute successfully
- [ ] Templates render variables correctly
- [ ] Media library pagination works
- [ ] Authentication blocks unauthorized access
- [ ] Rate limiting prevents abuse

### Performance Tests
- [ ] API response times < 2s
- [ ] Database queries optimized
- [ ] Memory usage stable
- [ ] File uploads handle large files
- [ ] Concurrent requests handled properly

### Security Tests
- [ ] JWT tokens validated properly
- [ ] Role-based access enforced
- [ ] SQL injection prevented
- [ ] XSS protection active
- [ ] Rate limiting functional
- [ ] Audit logging working

### Integration Tests
- [ ] All new routes accessible
- [ ] Database models sync correctly
- [ ] External API integrations work
- [ ] Email notifications sent
- [ ] File storage operations succeed

---

**🎉 Happy Testing! Chúc bạn test thành công tất cả tính năng mới!**
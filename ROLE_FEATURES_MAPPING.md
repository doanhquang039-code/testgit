# 🎭 ROLE-BASED FEATURES MAPPING

## 📊 OVERVIEW

This document maps all features and pages for each role in the HR Management System.

---

## 👤 ROLE 1: USER (Nhân viên)

### **Dashboard:** `/user1/dashboard`
**Status:** ✅ EXISTS

### **Personal Management (Quản lý cá nhân)**
1. ✅ `/user1/profile` - Hồ sơ cá nhân
2. ✅ `/user/attendance` - Chấm công
3. ✅ `/user1/my-shifts` - Ca làm việc
4. ✅ `/user/leaves` - Nghỉ phép
5. ✅ `/user1/overtime` - Làm thêm giờ
6. ✅ `/user1/payroll` - Phiếu lương
7. ✅ `/user1/payslips` - Bảng lương chi tiết

### **Work Management (Quản lý công việc)**
8. ✅ `/user1/tasks` - Công việc của tôi (My Tasks)
9. ✅ `/user1/kpi` - KPI Goals
10. ✅ `/user1/okr-list` - OKR List
11. ✅ `/user1/okr-detail` - OKR Detail
12. ✅ `/user1/reviews` - Đánh giá hiệu suất
13. ✅ `/user1/skills` - Kỹ năng

### **Learning & Development (Học tập & Phát triển)**
14. ✅ `/lms/my-courses` - Khóa học của tôi
15. ✅ `/lms/course-catalog` - Danh mục khóa học
16. ✅ `/user1/course-catalog` - Course Catalog
17. ✅ `/user1/course-detail` - Course Detail
18. ✅ `/videos` - Video đào tạo

### **Financial (Tài chính)**
19. ✅ `/user1/expenses` - Chi phí
20. ✅ `/self-service-enhanced/expenses` - Hoàn tiền
21. ✅ `/user1/documents` - Tài liệu
22. ✅ `/user1/my-assets` - Tài sản

### **Engagement (Tương tác)**
23. ✅ `/engagement/social-feed` - Mạng xã hội
24. ✅ `/user1/social-feed` - Social Feed
25. ✅ `/engagement/recognition-wall` - Vinh danh
26. ✅ `/user1/recognition-wall` - Recognition Wall
27. ✅ `/engagement/my-referrals` - Giới thiệu ứng viên
28. ✅ `/user1/my-referrals` - My Referrals
29. ✅ `/engagement/surveys` - Khảo sát
30. ✅ `/user1/surveys` - Surveys

### **Onboarding**
31. ✅ `/onboarding/my-checklist` - Checklist của tôi
32. ✅ `/user1/my-checklist` - My Checklist
33. ✅ `/attendance/my-schedule` - Lịch làm việc
34. ✅ `/user1/my-scans` - QR Check-in
35. ✅ `/user1/qrcode-scan` - QR Code Scan

### **Support (Hỗ trợ)**
36. ✅ `/user1/announcements` - Thông báo công ty
37. ✅ `/user1/announcement-detail` - Announcement Detail
38. ✅ `/user1/chatbot` - Trợ lý HR
39. ✅ `/notifications` - Thông báo
40. ✅ `/user1/notifications` - Notifications

### **Self-Service Portal**
41. ✅ `/user1/self-service-portal` - Self Service Portal
42. ✅ `/user1/exit-interview` - Exit Interview
43. ✅ `/user1/leave-balances` - Leave Balances
44. ✅ `/user1/leave-request` - Leave Request
45. ✅ `/user1/list` - User List

**Total USER Features:** 45 pages

---

## 👔 ROLE 2: MANAGER (Quản lý)

### **Dashboard:** `/manager/dashboard`
**Status:** ✅ EXISTS

### **Team Management (Quản lý nhóm)**
1. ✅ `/manager/team` - Team Overview
2. ⚠️ `/manager/team-members` - Team Members List (NEED TO CREATE)
3. ⚠️ `/manager/team-performance` - Team Performance (NEED TO CREATE)
4. ⚠️ `/manager/team-attendance` - Team Attendance (NEED TO CREATE)

### **Goals & OKR Management**
5. ⚠️ `/manager/goals/list` - Goals List (NEED TO CREATE)
6. ⚠️ `/manager/goals/create` - Create Goal (NEED TO CREATE)
7. ⚠️ `/manager/goals/edit/{id}` - Edit Goal (NEED TO CREATE)
8. ⚠️ `/manager/goals/detail/{id}` - Goal Detail (NEED TO CREATE)

### **Meeting Management**
9. ⚠️ `/manager/meetings/list` - Meetings List (NEED TO CREATE)
10. ⚠️ `/manager/meetings/create` - Schedule Meeting (NEED TO CREATE)
11. ⚠️ `/manager/meetings/edit/{id}` - Edit Meeting (NEED TO CREATE)
12. ⚠️ `/manager/meetings/detail/{id}` - Meeting Detail (NEED TO CREATE)

### **Budget Management**
13. ⚠️ `/manager/budget/list` - Budget List (NEED TO CREATE)
14. ⚠️ `/manager/budget/create` - Create Budget (NEED TO CREATE)
15. ⚠️ `/manager/budget/edit/{id}` - Edit Budget (NEED TO CREATE)
16. ⚠️ `/manager/budget/detail/{id}` - Budget Detail (NEED TO CREATE)

### **Leave & Attendance Approval**
17. ⚠️ `/manager/leave-requests` - Leave Requests (NEED TO CREATE)
18. ⚠️ `/manager/leave-approve/{id}` - Approve Leave (NEED TO CREATE)
19. ⚠️ `/manager/overtime-requests` - Overtime Requests (NEED TO CREATE)
20. ⚠️ `/manager/overtime-approve/{id}` - Approve Overtime (NEED TO CREATE)

### **Performance Management**
21. ⚠️ `/manager/reviews/list` - Performance Reviews (NEED TO CREATE)
22. ⚠️ `/manager/reviews/create` - Create Review (NEED TO CREATE)
23. ⚠️ `/manager/reviews/edit/{id}` - Edit Review (NEED TO CREATE)
24. ⚠️ `/manager/kpi/team` - Team KPI Dashboard (NEED TO CREATE)

### **Analytics & Reports**
25. ✅ `/manager/analytics` - Analytics Dashboard
26. ✅ `/manager/overtime` - Overtime Management
27. ⚠️ `/manager/reports/attendance` - Attendance Report (NEED TO CREATE)
28. ⚠️ `/manager/reports/performance` - Performance Report (NEED TO CREATE)
29. ⚠️ `/manager/reports/budget` - Budget Report (NEED TO CREATE)

### **Task Assignment**
30. ⚠️ `/manager/tasks/assign` - Assign Task (NEED TO CREATE)
31. ⚠️ `/manager/tasks/team` - Team Tasks (NEED TO CREATE)
32. ⚠️ `/manager/tasks/track` - Task Tracking (NEED TO CREATE)

### **Training & Development**
33. ⚠️ `/manager/training/team` - Team Training (NEED TO CREATE)
34. ⚠️ `/manager/training/assign` - Assign Training (NEED TO CREATE)
35. ⚠️ `/manager/training/progress` - Training Progress (NEED TO CREATE)

**Total MANAGER Features:** 35 pages (3 exist, 32 need to create)

---

## 🎯 ROLE 3: HIRING (Nhân sự/Tuyển dụng)

### **Dashboard:** `/hiring/dashboard`
**Status:** ✅ EXISTS

### **Job Posting Management**
1. ✅ `/hiring/posting-list` - Job Postings List
2. ✅ `/hiring/posting-form` - Create/Edit Job Posting
3. ⚠️ `/hiring/jobs/list` - Jobs List (NEED TO CREATE)
4. ⚠️ `/hiring/jobs/create` - Create Job (NEED TO CREATE)
5. ⚠️ `/hiring/jobs/edit/{id}` - Edit Job (NEED TO CREATE)
6. ⚠️ `/hiring/jobs/detail/{id}` - Job Detail (NEED TO CREATE)
7. ⚠️ `/hiring/jobs/closing-soon` - Jobs Closing Soon (NEED TO CREATE)

### **Candidate Management**
8. ✅ `/hiring/candidate-list` - Candidates List
9. ✅ `/hiring/candidate-form` - Add/Edit Candidate
10. ⚠️ `/hiring/candidates/list` - Candidates List (NEED TO CREATE)
11. ⚠️ `/hiring/candidates/create` - Add Candidate (NEED TO CREATE)
12. ⚠️ `/hiring/candidates/edit/{id}` - Edit Candidate (NEED TO CREATE)
13. ⚠️ `/hiring/candidates/detail/{id}` - Candidate Detail (NEED TO CREATE)
14. ⚠️ `/hiring/candidates/pipeline` - Candidate Pipeline (NEED TO CREATE)

### **Interview Management**
15. ⚠️ `/hiring/interviews/list` - Interviews List (NEED TO CREATE)
16. ⚠️ `/hiring/interviews/create` - Schedule Interview (NEED TO CREATE)
17. ⚠️ `/hiring/interviews/edit/{id}` - Edit Interview (NEED TO CREATE)
18. ⚠️ `/hiring/interviews/detail/{id}` - Interview Detail (NEED TO CREATE)
19. ⚠️ `/hiring/interviews/calendar` - Interview Calendar (NEED TO CREATE)

### **Offer Management**
20. ⚠️ `/hiring/offers/list` - Offers List (NEED TO CREATE)
21. ⚠️ `/hiring/offers/create` - Create Offer (NEED TO CREATE)
22. ⚠️ `/hiring/offers/edit/{id}` - Edit Offer (NEED TO CREATE)
23. ⚠️ `/hiring/offers/detail/{id}` - Offer Detail (NEED TO CREATE)

### **Recruitment Analytics**
24. ⚠️ `/hiring/analytics/overview` - Recruitment Analytics (NEED TO CREATE)
25. ⚠️ `/hiring/analytics/pipeline` - Pipeline Analytics (NEED TO CREATE)
26. ⚠️ `/hiring/analytics/source` - Source Analytics (NEED TO CREATE)
27. ⚠️ `/hiring/analytics/time-to-hire` - Time to Hire (NEED TO CREATE)

### **Onboarding**
28. ⚠️ `/hiring/onboarding/list` - Onboarding List (NEED TO CREATE)
29. ⚠️ `/hiring/onboarding/create` - Create Onboarding (NEED TO CREATE)
30. ⚠️ `/hiring/onboarding/track/{id}` - Track Onboarding (NEED TO CREATE)

### **Reports**
31. ⚠️ `/hiring/reports/recruitment` - Recruitment Report (NEED TO CREATE)
32. ⚠️ `/hiring/reports/candidates` - Candidates Report (NEED TO CREATE)
33. ⚠️ `/hiring/reports/interviews` - Interviews Report (NEED TO CREATE)

**Total HIRING Features:** 33 pages (5 exist, 28 need to create)

---

## 👑 ROLE 4: ADMIN (Quản trị viên)

### **Dashboard:** `/admin/dashboard`
**Status:** ✅ EXISTS

### **User Management**
1. ✅ `/admin/user-list` - Users List
2. ✅ `/admin/user-form` - Create/Edit User
3. ✅ `/admin/users-advanced` - Advanced User Management

### **Department & Position**
4. ✅ `/admin/department-list` - Departments List
5. ✅ `/admin/department-form` - Create/Edit Department
6. ✅ `/admin/position-list` - Positions List
7. ✅ `/admin/position-form` - Create/Edit Position

### **Attendance Management**
8. ✅ `/admin/attendance-list` - Attendance List
9. ✅ `/admin/attendance-form` - Create/Edit Attendance
10. ✅ `/admin/shift-list` - Shifts List
11. ✅ `/admin/shift-form` - Create/Edit Shift
12. ✅ `/admin/shift-assignment-list` - Shift Assignments

### **Leave Management**
13. ✅ `/admin/leave-list` - Leave Requests List
14. ✅ `/admin/holiday-list` - Holidays List
15. ✅ `/admin/holiday-form` - Create/Edit Holiday

### **Payroll Management**
16. ✅ `/admin/payroll-list` - Payroll List
17. ✅ `/admin/expenses` - Expenses Management
18. ✅ `/admin/expense-list` - Expenses List

### **Training & Development**
19. ✅ `/admin/training-list` - Training Programs List
20. ✅ `/admin/training-form` - Create/Edit Training
21. ✅ `/admin/course-list` - Courses List
22. ✅ `/admin/course-form` - Create/Edit Course

### **Performance Management**
23. ✅ `/admin/review-list` - Performance Reviews List
24. ✅ `/admin/review-form` - Create/Edit Review
25. ✅ `/admin/kpi-list` - KPI List
26. ✅ `/admin/kpi-form` - Create/Edit KPI
27. ✅ `/admin/okr-list` - OKR List
28. ✅ `/admin/okr-form` - Create/Edit OKR

### **Asset Management**
29. ✅ `/admin/asset-list` - Assets List
30. ✅ `/admin/asset-form` - Create/Edit Asset
31. ✅ `/admin/asset-assign-form` - Assign Asset

### **Document Management**
32. ✅ `/admin/document-list` - Documents List
33. ✅ `/admin/document-upload` - Upload Document

### **Contract Management**
34. ✅ `/admin/contract-list` - Contracts List
35. ✅ `/admin/contract-form` - Create/Edit Contract

### **Task Management**
36. ✅ `/admin/task-list` - Tasks List
37. ✅ `/admin/assignment-list` - Assignments List
38. ✅ `/admin/assignment-form` - Create Assignment

### **Skills Management**
39. ✅ `/admin/skill-list` - Skills List
40. ✅ `/admin/skill-form` - Create/Edit Skill

### **Announcement Management**
41. ✅ `/admin/announcement-list` - Announcements List
42. ✅ `/admin/announcement-form` - Create/Edit Announcement

### **Survey Management**
43. ✅ `/admin/survey-list` - Surveys List
44. ✅ `/admin/survey-form` - Create/Edit Survey

### **Recognition Management**
45. ✅ `/admin/recognition-list` - Recognition List

### **QR Code Management**
46. ✅ `/admin/qrcode-list` - QR Codes List
47. ✅ `/admin/qrcode-form` - Create QR Code
48. ✅ `/admin/qrcode-view` - View QR Code

### **Video Management**
49. ✅ `/admin/video-list` - Videos List
50. ✅ `/admin/video-upload` - Upload Video
51. ✅ `/admin/video-edit` - Edit Video

### **Payment Management**
52. ✅ `/admin/payment-list` - Payments List
53. ✅ `/admin/payment-create` - Create Payment
54. ✅ `/admin/payment-detail` - Payment Detail
55. ✅ `/admin/payment-callback-success` - Payment Success
56. ✅ `/admin/payment-callback-error` - Payment Error

### **System Settings**
57. ✅ `/admin/settings-list` - System Settings
58. ✅ `/admin/system-config` - System Configuration
59. ✅ `/admin/system-monitor` - System Monitor
60. ✅ `/admin/cache-dashboard` - Cache Dashboard
61. ✅ `/admin/cloud-dashboard` - Cloud Dashboard
62. ✅ `/admin/backup-restore` - Backup & Restore

### **Analytics & Reports**
63. ✅ `/admin/analytics-dashboard` - Analytics Dashboard
64. ✅ `/admin/dashboard-advanced` - Advanced Dashboard
65. ✅ `/admin/report-dashboard` - Report Dashboard

### **Audit & Logs**
66. ✅ `/admin/audit-logs` - Audit Logs
67. ✅ `/admin/audit-log-list` - Audit Log List

### **Onboarding & Checklist**
68. ✅ `/admin/checklist-list` - Checklist List

**Total ADMIN Features:** 68 pages (ALL EXIST)

---

## 📊 SUMMARY

| Role | Total Features | Existing | Need to Create |
|------|---------------|----------|----------------|
| **USER** | 45 | 45 | 0 |
| **MANAGER** | 35 | 3 | 32 |
| **HIRING** | 33 | 5 | 28 |
| **ADMIN** | 68 | 68 | 0 |
| **TOTAL** | 181 | 121 | 60 |

---

## 🎯 PRIORITY CREATION LIST

### **HIGH PRIORITY (Core Features)**

#### **MANAGER Role:**
1. `/manager/team-members` - Team Members List
2. `/manager/leave-requests` - Leave Requests Approval
3. `/manager/goals/list` - Goals List
4. `/manager/goals/create` - Create Goal
5. `/manager/meetings/list` - Meetings List
6. `/manager/meetings/create` - Schedule Meeting
7. `/manager/budget/list` - Budget List
8. `/manager/budget/create` - Create Budget
9. `/manager/reviews/list` - Performance Reviews
10. `/manager/tasks/team` - Team Tasks

#### **HIRING Role:**
1. `/hiring/jobs/list` - Jobs List
2. `/hiring/jobs/create` - Create Job
3. `/hiring/candidates/list` - Candidates List
4. `/hiring/candidates/detail/{id}` - Candidate Detail
5. `/hiring/interviews/list` - Interviews List
6. `/hiring/interviews/create` - Schedule Interview
7. `/hiring/offers/list` - Offers List
8. `/hiring/analytics/overview` - Recruitment Analytics

### **MEDIUM PRIORITY (Enhanced Features)**

#### **MANAGER Role:**
11. `/manager/team-performance` - Team Performance
12. `/manager/team-attendance` - Team Attendance
13. `/manager/overtime-requests` - Overtime Requests
14. `/manager/kpi/team` - Team KPI Dashboard
15. `/manager/reports/attendance` - Attendance Report

#### **HIRING Role:**
9. `/hiring/candidates/pipeline` - Candidate Pipeline
10. `/hiring/interviews/calendar` - Interview Calendar
11. `/hiring/offers/create` - Create Offer
12. `/hiring/onboarding/list` - Onboarding List

### **LOW PRIORITY (Advanced Features)**

#### **MANAGER Role:**
16. `/manager/training/team` - Team Training
17. `/manager/training/assign` - Assign Training
18. `/manager/reports/performance` - Performance Report
19. `/manager/reports/budget` - Budget Report

#### **HIRING Role:**
13. `/hiring/analytics/pipeline` - Pipeline Analytics
14. `/hiring/analytics/source` - Source Analytics
15. `/hiring/reports/recruitment` - Recruitment Report

---

## 📝 NOTES

- All ADMIN and USER features are complete
- MANAGER needs 32 new pages (91% missing)
- HIRING needs 28 new pages (85% missing)
- Total 60 pages need to be created
- Focus on HIGH PRIORITY features first for MVP

---

**Generated:** May 3, 2026  
**Status:** Documentation Complete  
**Next Step:** Create missing templates

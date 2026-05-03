# 🚀 CREATE MISSING TEMPLATES - EXECUTION PLAN

## 📋 SUMMARY

Based on ROLE_FEATURES_MAPPING.md, we need to create **60 missing templates**:
- **MANAGER Role:** 32 templates
- **HIRING Role:** 28 templates

---

## ✅ COMPLETED (1/60)

1. ✅ `/manager/team-members.html` - Team Members List

---

## 🎯 HIGH PRIORITY - MANAGER (10 templates)

### Team Management
2. `/manager/team-performance.html` - Team Performance Dashboard
3. `/manager/team-attendance.html` - Team Attendance Overview

### Goals Management
4. `/manager/goals/list.html` - Goals List
5. `/manager/goals/create.html` - Create Goal Form
6. `/manager/goals/edit.html` - Edit Goal Form
7. `/manager/goals/detail.html` - Goal Detail View

### Meeting Management
8. `/manager/meetings/list.html` - Meetings List
9. `/manager/meetings/create.html` - Schedule Meeting Form
10. `/manager/meetings/edit.html` - Edit Meeting Form
11. `/manager/meetings/detail.html` - Meeting Detail View

---

## 🎯 HIGH PRIORITY - HIRING (8 templates)

### Job Management
12. `/hiring/jobs/list.html` - Jobs List
13. `/hiring/jobs/create.html` - Create Job Form
14. `/hiring/jobs/edit.html` - Edit Job Form
15. `/hiring/jobs/detail.html` - Job Detail View
16. `/hiring/jobs/closing-soon.html` - Jobs Closing Soon

### Candidate Management
17. `/hiring/candidates/list.html` - Candidates List
18. `/hiring/candidates/create.html` - Add Candidate Form
19. `/hiring/candidates/detail.html` - Candidate Detail View

---

## 📊 MEDIUM PRIORITY - MANAGER (12 templates)

### Budget Management
20. `/manager/budget/list.html` - Budget List
21. `/manager/budget/create.html` - Create Budget Form
22. `/manager/budget/edit.html` - Edit Budget Form
23. `/manager/budget/detail.html` - Budget Detail View

### Leave & Attendance Approval
24. `/manager/leave-requests.html` - Leave Requests List
25. `/manager/leave-approve.html` - Approve Leave Form
26. `/manager/overtime-requests.html` - Overtime Requests List
27. `/manager/overtime-approve.html` - Approve Overtime Form

### Performance Management
28. `/manager/reviews/list.html` - Performance Reviews List
29. `/manager/reviews/create.html` - Create Review Form
30. `/manager/reviews/edit.html` - Edit Review Form
31. `/manager/kpi/team.html` - Team KPI Dashboard

---

## 📊 MEDIUM PRIORITY - HIRING (10 templates)

### Interview Management
32. `/hiring/interviews/list.html` - Interviews List
33. `/hiring/interviews/create.html` - Schedule Interview Form
34. `/hiring/interviews/edit.html` - Edit Interview Form
35. `/hiring/interviews/detail.html` - Interview Detail View
36. `/hiring/interviews/calendar.html` - Interview Calendar

### Candidate Pipeline
37. `/hiring/candidates/edit.html` - Edit Candidate Form
38. `/hiring/candidates/pipeline.html` - Candidate Pipeline View

### Offer Management
39. `/hiring/offers/list.html` - Offers List
40. `/hiring/offers/create.html` - Create Offer Form
41. `/hiring/offers/detail.html` - Offer Detail View

---

## 🔧 LOW PRIORITY - MANAGER (10 templates)

### Task Assignment
42. `/manager/tasks/assign.html` - Assign Task Form
43. `/manager/tasks/team.html` - Team Tasks List
44. `/manager/tasks/track.html` - Task Tracking Dashboard

### Training & Development
45. `/manager/training/team.html` - Team Training Overview
46. `/manager/training/assign.html` - Assign Training Form
47. `/manager/training/progress.html` - Training Progress Tracker

### Reports
48. `/manager/reports/attendance.html` - Attendance Report
49. `/manager/reports/performance.html` - Performance Report
50. `/manager/reports/budget.html` - Budget Report
51. `/manager/dashboard-simple.html` - Simple Dashboard (Already exists ✅)

---

## 🔧 LOW PRIORITY - HIRING (10 templates)

### Offer Management (continued)
52. `/hiring/offers/edit.html` - Edit Offer Form

### Onboarding
53. `/hiring/onboarding/list.html` - Onboarding List
54. `/hiring/onboarding/create.html` - Create Onboarding Plan
55. `/hiring/onboarding/track.html` - Track Onboarding Progress

### Analytics
56. `/hiring/analytics/overview.html` - Recruitment Analytics Overview
57. `/hiring/analytics/pipeline.html` - Pipeline Analytics
58. `/hiring/analytics/source.html` - Source Analytics
59. `/hiring/analytics/time-to-hire.html` - Time to Hire Analytics

### Reports
60. `/hiring/reports/recruitment.html` - Recruitment Report
61. `/hiring/reports/candidates.html` - Candidates Report
62. `/hiring/reports/interviews.html` - Interviews Report

---

## 📁 DIRECTORY STRUCTURE TO CREATE

```
hr-management-system/src/main/resources/templates/
├── manager/
│   ├── goals/
│   │   ├── list.html
│   │   ├── create.html
│   │   ├── edit.html
│   │   └── detail.html
│   ├── meetings/
│   │   ├── list.html
│   │   ├── create.html
│   │   ├── edit.html
│   │   └── detail.html
│   ├── budget/
│   │   ├── list.html
│   │   ├── create.html
│   │   ├── edit.html
│   │   └── detail.html
│   ├── reviews/
│   │   ├── list.html
│   │   ├── create.html
│   │   └── edit.html
│   ├── tasks/
│   │   ├── assign.html
│   │   ├── team.html
│   │   └── track.html
│   ├── training/
│   │   ├── team.html
│   │   ├── assign.html
│   │   └── progress.html
│   ├── reports/
│   │   ├── attendance.html
│   │   ├── performance.html
│   │   └── budget.html
│   ├── kpi/
│   │   └── team.html
│   ├── team-members.html ✅
│   ├── team-performance.html
│   ├── team-attendance.html
│   ├── leave-requests.html
│   ├── leave-approve.html
│   ├── overtime-requests.html
│   └── overtime-approve.html
│
└── hiring/
    ├── jobs/
    │   ├── list.html
    │   ├── create.html
    │   ├── edit.html
    │   ├── detail.html
    │   └── closing-soon.html
    ├── candidates/
    │   ├── list.html
    │   ├── create.html
    │   ├── edit.html
    │   ├── detail.html
    │   └── pipeline.html
    ├── interviews/
    │   ├── list.html
    │   ├── create.html
    │   ├── edit.html
    │   ├── detail.html
    │   └── calendar.html
    ├── offers/
    │   ├── list.html
    │   ├── create.html
    │   ├── edit.html
    │   └── detail.html
    ├── onboarding/
    │   ├── list.html
    │   ├── create.html
    │   └── track.html
    ├── analytics/
    │   ├── overview.html
    │   ├── pipeline.html
    │   ├── source.html
    │   └── time-to-hire.html
    └── reports/
        ├── recruitment.html
        ├── candidates.html
        └── interviews.html
```

---

## 🎨 TEMPLATE STANDARDS

All templates should follow these standards:

### 1. **Header Section**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>[Page Title] - HR Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
```

### 2. **Sidebar Integration**
```html
<div th:replace="fragments/manager-sidebar :: sidebar"></div>
<!-- OR -->
<div th:replace="fragments/hiring-sidebar :: sidebar"></div>
```

### 3. **Page Header**
```html
<div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
    <h1 class="h2"><i class="bi bi-[icon] me-2"></i>[Page Title]</h1>
    <div class="btn-toolbar mb-2 mb-md-0">
        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.reload()">
            <i class="bi bi-arrow-clockwise me-1"></i>Refresh
        </button>
    </div>
</div>
```

### 4. **Bootstrap Icons**
- Use Bootstrap Icons for all icons
- Common icons:
  - `bi-people` - Team/Users
  - `bi-bullseye` - Goals
  - `bi-calendar-event` - Meetings
  - `bi-cash-stack` - Budget
  - `bi-briefcase` - Jobs
  - `bi-person-plus` - Candidates
  - `bi-clipboard-check` - Tasks
  - `bi-graph-up` - Analytics

### 5. **Color Scheme**
- Primary: `#007bff` (Blue)
- Success: `#28a745` (Green)
- Warning: `#ffc107` (Yellow)
- Danger: `#dc3545` (Red)
- Info: `#17a2b8` (Cyan)

---

## 🚀 EXECUTION STRATEGY

### Phase 1: HIGH PRIORITY (18 templates)
**Timeline:** Day 1-2
- Create all HIGH PRIORITY templates for MANAGER (10)
- Create all HIGH PRIORITY templates for HIRING (8)

### Phase 2: MEDIUM PRIORITY (22 templates)
**Timeline:** Day 3-4
- Create all MEDIUM PRIORITY templates for MANAGER (12)
- Create all MEDIUM PRIORITY templates for HIRING (10)

### Phase 3: LOW PRIORITY (20 templates)
**Timeline:** Day 5-6
- Create all LOW PRIORITY templates for MANAGER (10)
- Create all LOW PRIORITY templates for HIRING (10)

---

## 📝 NOTES

- All templates use Thymeleaf template engine
- Bootstrap 5.3.0 for styling
- Bootstrap Icons 1.11.0 for icons
- Responsive design (mobile-friendly)
- CSRF protection included in forms
- Consistent navigation with sidebar fragments

---

**Generated:** May 3, 2026  
**Status:** 1/60 Complete (1.67%)  
**Next:** Create HIGH PRIORITY templates

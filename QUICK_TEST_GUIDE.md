# Quick Test Guide - All Dashboards

## 🚀 Start Application

```bash
cd hr-management-system
./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Wait for: `Started HrManagementSystemApplication in XX seconds`

## 🌐 Access Application

Open browser: `http://localhost:8080/login`

## 👥 Test Accounts

### 1. ADMIN Account
```
Email: admin@example.com
Password: [your admin password]
Expected: Redirect to /admin/dashboard
```

**What to check:**
- ✅ System overview metrics
- ✅ User management link
- ✅ All modules accessible
- ✅ Settings and reports

### 2. MANAGER Account
```
Email: [manager email]
Password: [manager password]
Expected: Redirect to /manager/dashboard
```

**What to check:**
- ✅ Team overview (Total Employees, Pending Leaves, Active Tasks, Absent Today)
- ✅ Task statistics (Completed, Pending, Overtime)
- ✅ Attendance chart (7 days)
- ✅ Recent leave requests table
- ✅ Quick action buttons work

### 3. HIRING Account
```
Email: [hiring email]
Password: [hiring password]
Expected: Redirect to /hiring
```

**What to check:**
- ✅ Hiring overview (Active Jobs, Total Candidates, Interviews, Avg Applications)
- ✅ Candidate pipeline (Applied, Screening, Interview, Offer, Hired, Rejected)
- ✅ Recent job postings (5 latest)
- ✅ Recent candidates table (10 latest)
- ✅ Quick actions (Create Job, Add Candidate, Schedule Interview)

### 4. USER Account
```
Email: [user email]
Password: [user password]
Expected: Redirect to /user1/dashboard
```

**What to check:**
- ✅ Personal attendance
- ✅ Leave requests
- ✅ Tasks assigned
- ✅ Profile information

## 🧪 Test Scenarios

### Scenario 1: Manager Dashboard
1. Login as MANAGER
2. Check metrics display correctly
3. Click "View Team" → Should show team members
4. Click "Leave Requests" → Should show pending leaves
5. Check attendance chart renders
6. Verify recent leave requests table

### Scenario 2: Hiring Dashboard
1. Login as HIRING
2. Verify all metrics show numbers (not errors)
3. Check candidate pipeline shows counts
4. Click "Create Job Posting" → Should go to job form
5. Click "Add Candidate" → Should go to candidate form
6. Verify recent candidates table shows data
7. Check each candidate "View" button works

### Scenario 3: Cross-Role Access
1. Login as ADMIN
2. Navigate to `/manager/dashboard` → Should work
3. Navigate to `/hiring` → Should work
4. Navigate to `/user1/dashboard` → Should work
5. Logout
6. Login as MANAGER
7. Try to access `/admin/dashboard` → Should get 403 Forbidden

### Scenario 4: Responsive Design
1. Open dashboard on desktop (1920x1080)
2. Resize to tablet (768x1024)
3. Resize to mobile (375x667)
4. Verify all cards stack properly
5. Check sidebar collapses on mobile
6. Verify charts are responsive

## 🐛 Common Issues & Solutions

### Issue 1: 500 Error on Dashboard
**Cause:** Missing data or enum mismatch
**Solution:** 
- Check console logs for specific error
- Verify database has sample data
- Check TaskType enum values

### Issue 2: Template Not Found
**Cause:** Wrong template path
**Solution:**
- Verify template exists in `src/main/resources/templates/`
- Check controller returns correct template name
- Restart application

### Issue 3: 403 Forbidden
**Cause:** Insufficient permissions
**Solution:**
- Check user role in database
- Verify SecurityConfig allows role access
- Check @PreAuthorize annotations

### Issue 4: Blank Dashboard
**Cause:** No data in database
**Solution:**
- Run Flyway migrations
- Insert sample data
- Check repository methods

## ✅ Success Criteria

### All Dashboards Should:
- [ ] Load without errors
- [ ] Display metrics correctly
- [ ] Show charts/graphs
- [ ] Have working navigation
- [ ] Be responsive on mobile
- [ ] Have working quick actions
- [ ] Show recent activity
- [ ] Handle empty data gracefully

### Performance:
- [ ] Page load < 2 seconds
- [ ] No console errors
- [ ] Charts render smoothly
- [ ] Navigation is instant

## 📊 Test Results Template

```
Date: ___________
Tester: ___________

ADMIN Dashboard:    ✅ / ❌
MANAGER Dashboard:  ✅ / ❌
HIRING Dashboard:   ✅ / ❌
USER Dashboard:     ✅ / ❌

Issues Found:
1. ___________
2. ___________
3. ___________

Notes:
___________
___________
```

## 🔍 Debug Commands

### Check Application Status
```bash
# Check if app is running
curl http://localhost:8080/actuator/health

# Check database connection
mysql -u root -proot hr_management_system -e "SELECT COUNT(*) FROM user;"

# View recent logs
tail -f logs/spring.log
```

### Database Queries
```sql
-- Check user roles
SELECT username, email, role FROM user;

-- Check job postings
SELECT id, title, status FROM job_posting;

-- Check candidates
SELECT id, full_name, current_stage FROM candidate;

-- Check tasks
SELECT id, title, task_type FROM task;
```

## 🎯 Quick Verification Checklist

Before marking as complete:
- [ ] All 4 dashboards load successfully
- [ ] No 500 errors
- [ ] No template parsing errors
- [ ] All metrics show data
- [ ] Charts render correctly
- [ ] Quick actions work
- [ ] Navigation works
- [ ] Logout works
- [ ] Login redirect works
- [ ] Mobile responsive

**If all checked: READY FOR PRODUCTION!** 🚀

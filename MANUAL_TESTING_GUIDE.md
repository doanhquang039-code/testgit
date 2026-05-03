# 🧪 MANUAL TESTING GUIDE

## 📋 OVERVIEW

This guide provides step-by-step instructions for manually testing all 6 newly implemented features.

**Date:** May 3, 2026  
**Application:** HR Management System  
**Base URL:** http://localhost:8080  
**Status:** ✅ Application Running

---

## 🚀 PREREQUISITES

### **1. Start Application:**
```bash
cd hr-management-system
./mvnw.cmd spring-boot:run
```

### **2. Wait for Startup:**
Look for this message in console:
```
Started HrManagementSystemApplication in X seconds
```

### **3. Login Credentials:**

#### **MANAGER Role:**
- **Username:** `manager@example.com` (or your manager account)
- **Password:** Your password
- **Required Role:** `MANAGER` or `ADMIN`

#### **HIRING Role:**
- **Username:** `hiring@example.com` (or your hiring account)
- **Password:** Your password
- **Required Role:** `HIRING` or `ADMIN`

---

## 🧪 TEST CASES

### **TEST 1: Manager - Team Members List**

#### **Route:** `GET /manager/team-members`

#### **Steps:**
1. Login as MANAGER
2. Navigate to: http://localhost:8080/manager/team-members
3. Verify page loads successfully
4. Check displayed data:
   - ✅ Team members list
   - ✅ Total members count
   - ✅ Active members count
   - ✅ On leave count
   - ✅ Average performance score

#### **Expected Results:**
- ✅ Page displays without errors
- ✅ Statistics cards show correct numbers
- ✅ Team members table displays all active users
- ✅ Each member shows: Name, Email, Department, Position, Performance Score
- ✅ Bootstrap styling applied correctly

#### **Test Data:**
- Should display all users with `UserStatus.ACTIVE`
- Performance scores from `PerformanceReview` table
- Leave data from `LeaveRequest` table

---

### **TEST 2: Manager - Leave Requests Management**

#### **Route:** `GET /manager/leave-requests`

#### **Steps:**
1. Login as MANAGER
2. Navigate to: http://localhost:8080/manager/leave-requests
3. Verify page loads successfully
4. Check tabs:
   - ✅ Pending tab
   - ✅ Approved tab
   - ✅ Rejected tab
   - ✅ All tab
5. Test approve workflow:
   - Click "Approve" button on a pending leave
   - Verify confirmation modal appears
   - Confirm approval
   - Verify leave status changes to APPROVED
6. Test reject workflow:
   - Click "Reject" button on a pending leave
   - Verify rejection reason modal appears
   - Enter reason and confirm
   - Verify leave status changes to REJECTED

#### **Expected Results:**
- ✅ Page displays without errors
- ✅ Statistics show correct counts
- ✅ Tabs filter leaves correctly
- ✅ Approve workflow works
- ✅ Reject workflow works
- ✅ Notifications sent to employees
- ✅ Flash messages display success/error

#### **Test Data:**
- Create test leave requests with different statuses
- Verify filtering works correctly

---

### **TEST 3: Manager - Goals List**

#### **Route:** `GET /manager/goals` (TeamGoalController)

#### **Steps:**
1. Login as MANAGER
2. Navigate to: http://localhost:8080/manager/goals
3. Verify page loads successfully
4. Check displayed data:
   - ✅ Goals list
   - ✅ Active goals count
   - ✅ Completed goals count
   - ✅ In progress goals count
   - ✅ Average progress percentage
5. Check each goal displays:
   - ✅ Title
   - ✅ Description
   - ✅ Status badge
   - ✅ Progress bar
   - ✅ Start/End dates
   - ✅ Action buttons (Edit, View, Complete, Cancel)

#### **Expected Results:**
- ✅ Page displays without errors
- ✅ Statistics show correct numbers
- ✅ Goals filtered by manager's department
- ✅ Progress bars show correct percentages
- ✅ Status badges colored correctly
- ✅ Action buttons work

#### **Test Data:**
- Create test goals with different statuses
- Verify only department goals shown

---

### **TEST 4: Manager - Create Goal**

#### **Route:** `GET /manager/goals/create` (TeamGoalController)

#### **Steps:**
1. Login as MANAGER
2. Navigate to: http://localhost:8080/manager/goals/create
3. Verify form loads successfully
4. Fill in form:
   - ✅ Title: "Q2 Sales Target"
   - ✅ Description: "Achieve 20% growth in Q2"
   - ✅ Target Value: 100
   - ✅ Start Date: 2026-04-01
   - ✅ End Date: 2026-06-30
5. Submit form
6. Verify redirect to goals list
7. Verify new goal appears in list
8. Verify success flash message

#### **Expected Results:**
- ✅ Form displays without errors
- ✅ All fields render correctly
- ✅ Date pickers work
- ✅ Form validation works
- ✅ CSRF token included
- ✅ Goal created successfully
- ✅ Redirect to goals list
- ✅ Success message displayed

#### **Test Data:**
- Try valid data
- Try invalid data (empty fields, past dates)
- Verify validation messages

---

### **TEST 5: Manager - Meetings List**

#### **Route:** `GET /manager/meetings/list`

#### **Steps:**
1. Login as MANAGER
2. Navigate to: http://localhost:8080/manager/meetings/list
3. Verify page loads successfully
4. Check tabs:
   - ✅ Upcoming tab
   - ✅ Past tab
   - ✅ All tab
5. Check statistics:
   - ✅ Upcoming count
   - ✅ This week count
   - ✅ Today count
   - ✅ This month count
6. Check each meeting displays:
   - ✅ Title
   - ✅ Date and time
   - ✅ Location/Link
   - ✅ Meeting type badge
   - ✅ Participants count
   - ✅ Action buttons

#### **Expected Results:**
- ✅ Page displays without errors
- ✅ Statistics show correct numbers
- ✅ Tabs filter meetings correctly
- ✅ Upcoming meetings sorted by date (ascending)
- ✅ Past meetings sorted by date (descending)
- ✅ Meeting type badges colored correctly
- ✅ Calendar view works (if implemented)

#### **Test Data:**
- Create test meetings with different dates
- Verify filtering and sorting work

---

### **TEST 6: Hiring - Jobs List**

#### **Route:** `GET /hiring/jobs/list`

#### **Steps:**
1. Login as HIRING
2. Navigate to: http://localhost:8080/hiring/jobs/list
3. Verify page loads successfully
4. Check tabs:
   - ✅ Active tab
   - ✅ Draft tab
   - ✅ Closed tab
   - ✅ All tab
5. Check statistics:
   - ✅ Total jobs count
   - ✅ Active jobs count
   - ✅ Draft jobs count
   - ✅ Closed jobs count
   - ✅ Total applications count
6. Check each job displays:
   - ✅ Job title
   - ✅ Department
   - ✅ Location
   - ✅ Status badge
   - ✅ Applications count
   - ✅ Posted date
   - ✅ Closing date
   - ✅ Action buttons (View, Edit, Publish)

#### **Expected Results:**
- ✅ Page displays without errors
- ✅ Statistics show correct numbers
- ✅ Tabs filter jobs correctly
- ✅ Status badges colored correctly
- ✅ Applications count accurate
- ✅ Publish button works for draft jobs
- ✅ Action buttons work

#### **Test Data:**
- Create test job postings with different statuses
- Create test applications
- Verify filtering works

---

## 🔒 SECURITY TESTING

### **TEST 7: Role-Based Access Control**

#### **Steps:**
1. **Test MANAGER routes as USER:**
   - Login as USER
   - Try to access: http://localhost:8080/manager/team-members
   - **Expected:** 403 Forbidden or redirect to login

2. **Test HIRING routes as USER:**
   - Login as USER
   - Try to access: http://localhost:8080/hiring/jobs/list
   - **Expected:** 403 Forbidden or redirect to login

3. **Test MANAGER routes as HIRING:**
   - Login as HIRING
   - Try to access: http://localhost:8080/manager/team-members
   - **Expected:** 403 Forbidden (unless HIRING has MANAGER role)

4. **Test without authentication:**
   - Logout
   - Try to access any protected route
   - **Expected:** Redirect to login page

#### **Expected Results:**
- ✅ Only authorized roles can access routes
- ✅ Unauthorized access blocked
- ✅ Proper error messages displayed
- ✅ No sensitive data leaked

---

### **TEST 8: CSRF Protection**

#### **Steps:**
1. Open browser developer tools
2. Navigate to any form (e.g., create goal)
3. Inspect form HTML
4. Verify CSRF token present:
   ```html
   <input type="hidden" name="_csrf" value="..." />
   ```
5. Try to submit form without CSRF token
6. **Expected:** 403 Forbidden

#### **Expected Results:**
- ✅ All forms include CSRF token
- ✅ Forms without CSRF token rejected
- ✅ CSRF validation works

---

## 📊 DATA VALIDATION TESTING

### **TEST 9: Form Validation**

#### **Test Create Goal Form:**
1. Leave all fields empty → Submit
   - **Expected:** Validation errors displayed
2. Enter invalid dates (end before start) → Submit
   - **Expected:** Validation error
3. Enter negative target value → Submit
   - **Expected:** Validation error
4. Enter valid data → Submit
   - **Expected:** Success

#### **Test Leave Approval:**
1. Try to approve already approved leave
   - **Expected:** Error message or disabled button
2. Try to reject without reason
   - **Expected:** Validation error

---

## 🎨 UI/UX TESTING

### **TEST 10: Responsive Design**

#### **Steps:**
1. Test on different screen sizes:
   - Desktop (1920x1080)
   - Tablet (768x1024)
   - Mobile (375x667)
2. Verify:
   - ✅ Layout adapts correctly
   - ✅ Tables scroll horizontally on mobile
   - ✅ Buttons stack vertically on mobile
   - ✅ Navigation menu collapses on mobile
   - ✅ Cards stack on mobile

---

### **TEST 11: Browser Compatibility**

#### **Test on:**
- ✅ Chrome (latest)
- ✅ Firefox (latest)
- ✅ Edge (latest)
- ✅ Safari (latest)

#### **Verify:**
- ✅ All features work
- ✅ Styling consistent
- ✅ No console errors

---

## 📝 TEST RESULTS TEMPLATE

### **Test Execution Log:**

```
Date: May 3, 2026
Tester: [Your Name]
Environment: Local Development

TEST 1: Manager - Team Members List
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 2: Manager - Leave Requests
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 3: Manager - Goals List
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 4: Manager - Create Goal
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 5: Manager - Meetings List
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 6: Hiring - Jobs List
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 7: Role-Based Access Control
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 8: CSRF Protection
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 9: Form Validation
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 10: Responsive Design
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

TEST 11: Browser Compatibility
Status: [ ] PASS [ ] FAIL
Notes: _______________________________

OVERALL RESULT: [ ] ALL PASS [ ] SOME FAIL
```

---

## 🐛 BUG REPORTING TEMPLATE

### **If you find a bug:**

```
Bug ID: BUG-001
Date: May 3, 2026
Feature: [Feature Name]
Route: [URL]
Severity: [ ] Critical [ ] High [ ] Medium [ ] Low

Description:
_______________________________

Steps to Reproduce:
1. _______________________________
2. _______________________________
3. _______________________________

Expected Result:
_______________________________

Actual Result:
_______________________________

Screenshots:
[Attach screenshots]

Console Errors:
[Paste console errors]

Environment:
- Browser: _______________________________
- OS: _______________________________
- Screen Size: _______________________________
```

---

## ✅ ACCEPTANCE CRITERIA

### **Feature is considered PASSED if:**
1. ✅ Page loads without errors
2. ✅ All data displays correctly
3. ✅ All buttons/links work
4. ✅ Forms submit successfully
5. ✅ Validation works correctly
6. ✅ Security checks pass
7. ✅ Responsive design works
8. ✅ No console errors
9. ✅ Flash messages display correctly
10. ✅ Navigation works

### **Feature is considered FAILED if:**
1. ❌ Page throws errors
2. ❌ Data doesn't display
3. ❌ Buttons/links don't work
4. ❌ Forms don't submit
5. ❌ Validation doesn't work
6. ❌ Security vulnerabilities found
7. ❌ Layout breaks on mobile
8. ❌ Console errors present
9. ❌ Flash messages don't show
10. ❌ Navigation broken

---

## 🎯 TESTING CHECKLIST

### **Before Testing:**
- [ ] Application is running
- [ ] Database has test data
- [ ] Test accounts created
- [ ] Browser developer tools open

### **During Testing:**
- [ ] Follow test steps exactly
- [ ] Document all issues
- [ ] Take screenshots of bugs
- [ ] Note console errors
- [ ] Test edge cases

### **After Testing:**
- [ ] Complete test results log
- [ ] Report all bugs found
- [ ] Verify all features tested
- [ ] Share results with team
- [ ] Update documentation

---

## 📞 SUPPORT

### **If you encounter issues:**
1. Check console for errors
2. Check application logs
3. Verify database connection
4. Restart application
5. Clear browser cache
6. Try different browser

### **Common Issues:**

#### **Issue 1: Page not loading**
- **Solution:** Check if application is running
- **Solution:** Verify route is correct
- **Solution:** Check if logged in with correct role

#### **Issue 2: Form not submitting**
- **Solution:** Check console for JavaScript errors
- **Solution:** Verify CSRF token present
- **Solution:** Check form validation

#### **Issue 3: Data not displaying**
- **Solution:** Check if database has data
- **Solution:** Verify repository queries
- **Solution:** Check controller logic

---

**Generated:** May 3, 2026  
**Status:** ✅ READY FOR TESTING  
**Estimated Time:** 2-3 hours for complete testing


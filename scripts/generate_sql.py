import os
import re

java_dir = r"d:\Users\admoi\Desktop\SpringBoot\hr-management-system\src\main\java\com\example\hr\models"
sql_file = r"d:\Users\admoi\Desktop\SpringBoot\hr-management-system\src\main\resources\db\migration\V25__Add_Audit_Fields_To_Remaining_Tables.sql"

updated_models = [
    "AnalyticsMetric.java", "Attendance.java", "AuditLog.java", "BackupHistory.java", "Candidate.java",
    "ChatbotMessage.java", "CompanyAnnouncement.java", "CompanyAsset.java", "ContractExpiryReminder.java",
    "CourseEnrollment.java", "EmployeeBenefit.java", "EmployeeWarning.java", "HrAuditLog.java", "LeaveRequest.java",
    "Notification.java", "OKRProgress.java", "QRCode.java", "QRCodeScan.java", "QuizAttempt.java", "QuizQuestion.java",
    "Recognition.java", "Shift.java", "ShiftAssignment.java", "SurveyResponse.java", "SystemSetting.java",
    "TrainingEnrollment.java", "TrainingProgram.java"
]

sql_statements = []

for filename in updated_models:
    filepath = os.path.join(java_dir, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
        
    # Find table name
    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"\)', content)
    if table_match:
        table_name = table_match.group(1)
    else:
        # fallback to snake_case of class name
        class_name = filename[:-5]
        table_name = re.sub(r'(?<!^)(?=[A-Z])', '_', class_name).lower()
        
    # Check if we added created_at, updated_at
    added_created = bool(re.search(r'@Column\(name\s*=\s*"created_at".*\)', content))
    added_updated = bool(re.search(r'@Column\(name\s*=\s*"updated_at".*\)', content))
    
    if added_created or added_updated:
        sql = f"-- Add audit fields to {table_name}\n"
        if added_created:
            sql += f"ALTER TABLE {table_name} ADD COLUMN created_at DATETIME;\n"
        if added_updated:
            sql += f"ALTER TABLE {table_name} ADD COLUMN updated_at DATETIME;\n"
        sql_statements.append(sql)

with open(sql_file, "w", encoding="utf-8") as f:
    f.write("\n".join(sql_statements))
    
print(f"Generated {sql_file}")

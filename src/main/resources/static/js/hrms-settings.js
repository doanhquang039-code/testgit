/**
 * HRMS Settings Panel v1
 * - 5 languages: VI, EN, ZH (Chinese), JA (Japanese), KO (Korean)
 * - Background themes
 * - Brightness control
 * - All client-side, saved to localStorage
 */
(function () {
  'use strict';

  // ==================== DICTIONARIES ====================

  var DICT = {
    en: {
      // Navigation
      'Dashboard': 'Dashboard', 'Nhân viên': 'Employees', 'Phòng ban': 'Departments',
      'Chức vụ': 'Positions', 'Hợp đồng': 'Contracts', 'Bảng lương': 'Payroll',
      'Thanh toán': 'Payments', 'Tài liệu': 'Documents', 'Nghỉ phép': 'Leave Requests',
      'Chấm công': 'Attendance', 'Công việc': 'Tasks', 'Phân công': 'Assignments',
      'Đánh giá KPI': 'KPI Reviews', 'Báo cáo': 'Reports', 'Thông báo công ty': 'Announcements',
      'Tuyển dụng': 'Recruitment', 'Video đào tạo': 'Training Videos', 'Thêm video mới': 'Upload Video',
      'Nhật ký hệ thống': 'Audit Log', 'Nhật ký': 'Audit Log', 'KPI Goals': 'KPI Goals',
      'Mục tiêu KPI': 'KPI Goals', 'Chi phí': 'Expenses',
      'Kỹ năng': 'Skills', 'Kỹ năng NV': 'Employee Skills', 'Ca làm việc': 'Work Shifts', 'Phân ca': 'Shift Assignments',
      'Đăng xuất': 'Logout', 'Hồ sơ cá nhân': 'My Profile', 'Làm thêm giờ': 'Overtime',
      'Phiếu lương': 'Payslip', 'Công việc của tôi': 'My Tasks', 'Thông báo': 'Notifications',
      'Nhóm của tôi': 'My Team', 'Duyệt nghỉ phép': 'Approve Leaves', 'Duyệt OT': 'Approve OT',
      'Tin tuyển dụng': 'Job Postings', 'Ứng viên': 'Candidates',
      'LMS - Khóa học': 'LMS - Courses', 'QR Code': 'QR Code', 'Khảo sát': 'Surveys',
      'Vinh danh': 'Recognition', 'Onboarding': 'Onboarding', 'OKR': 'OKR',
      'Cài đặt': 'Settings', 'Phân tích': 'Analytics',
      'Advanced Dashboard': 'Advanced Dashboard', 'Quản lý User Nâng cao': 'Advanced User Management',
      'Lương': 'Salary', 'Video': 'Video', 'Analytics': 'Analytics', 'Khóa học': 'Courses',
      'Khen thưởng': 'Recognition', 'Cấu hình Hệ thống': 'System Configuration',
      'Audit Logs': 'Audit Logs', 'Backup & Restore': 'Backup & Restore',
      'System Monitor': 'System Monitor', 'Cache & Email': 'Cache & Email',
      'Cloud Storage': 'Cloud Storage',
      // Section labels
      'Cá nhân': 'Personal', 'Hỗ trợ': 'Support', 'Đào tạo': 'Training',
      // Page headings
      'Bảng Điều Khiển': 'Dashboard', 'NHÂN VIÊN': 'EMPLOYEES', 'PHÒNG BAN': 'DEPARTMENTS',
      'CHỨC VỤ': 'POSITIONS', 'CHỜ DUYỆT NGHỈ': 'PENDING LEAVES', 'CÔNG VIỆC': 'TASKS',
      'Chấm Công 7 Ngày Qua': 'Attendance Last 7 Days', 'Nhân Viên Theo Phòng Ban': 'Employees by Dept',
      'Hôm nay:': 'Today:', 'Xem chi tiết': 'View Details', 'Tổng quan đi làm theo ngày': 'Daily attendance',
      'Phân bổ nhân lực': 'Workforce distribution',
      // Buttons
      'Lưu': 'Save', 'Hủy': 'Cancel', 'Xóa': 'Delete', 'Sửa': 'Edit', 'Thêm': 'Add',
      'Tìm kiếm': 'Search', 'Quay lại': 'Back', 'Xác nhận': 'Confirm', 'Lọc': 'Filter',
      'Tất cả': 'All', 'Trạng thái': 'Status', 'Thao tác': 'Action', 'Tìm': 'Search',
      // Status
      'Chờ duyệt': 'Pending', 'Đã duyệt': 'Approved', 'Từ chối': 'Rejected',
      'Đang thực hiện': 'In Progress', 'Hoàn thành': 'Completed', 'Đã hủy': 'Cancelled',
      'Đã thanh toán': 'Paid', 'Chưa thanh toán': 'Unpaid',
      // Table headers
      'Họ và tên': 'Full Name', 'Số điện thoại': 'Phone', 'Ngày vào làm': 'Hire Date',
      'Mã nhân viên': 'Employee Code', 'Số tiền': 'Amount', 'Ghi chú': 'Note',
      'Mô tả': 'Description', 'Từ ngày': 'From', 'Đến ngày': 'To', 'Số ngày': 'Days',
      'Lý do': 'Reason', 'Giờ vào': 'Check In', 'Giờ ra': 'Check Out',
      'Lương cơ bản': 'Base Salary', 'Thưởng': 'Bonus', 'Khấu trừ': 'Deductions',
      'Lương thực nhận': 'Net Salary', 'Không có dữ liệu': 'No data',
      // New sidebar sections
      'Công việc': 'Tasks', 'Tài chính': 'Finance', 'Đánh giá': 'Reviews',
      'Tài sản': 'Assets', 'Trợ lý HR': 'HR Assistant',
      'Danh sách nhân viên': 'Employee List', 'Quản lý': 'Management', 'Nhân sự': 'HR',
      // Page headings
      'Thiết Bị & Tài Sản': 'Equipment & Assets', 'Chấm Công': 'Attendance',
      'Thông Báo Công Ty': 'Company Announcements', 'Yêu Cầu Chi Phí': 'Expense Claims',
      'Tài Liệu Của Tôi': 'My Documents', 'Ca Làm Việc Của Tôi': 'My Work Shifts',
      'Đăng Ký Làm Thêm Giờ': 'Overtime Registration', 'Lịch Sử Chấm Công': 'Attendance History',
      'Duyệt Đơn Làm Thêm Giờ': 'Overtime Approval', 'Nhóm Của Tôi': 'My Team',
      'Trung Tâm Tuyển Dụng': 'Recruitment Center',
      // Labels
      'Tiêu đề': 'Title', 'Danh mục': 'Category', 'Ngày chi': 'Expense Date',
      'Chứng từ': 'Receipt', 'Nộp Yêu Cầu Mới': 'Submit New Claim',
      'Lịch Sử Yêu Cầu': 'Claim History', 'Gửi yêu cầu': 'Submit',
      'Đang sử dụng': 'In Use', 'Đã trả': 'Returned', 'Tổng tài sản': 'Total Assets',
      'Ngày nhận': 'Received', 'Tình trạng': 'Condition', 'Bàn giao bởi': 'Assigned by',
      'Chưa check-in': 'Not checked in', 'Đã hoàn thành ca': 'Shift completed',
      'Xem tất cả': 'View All', 'Thêm nhân viên': 'Add Employee',
      'Giao việc mới': 'New Assignment', 'Truy cập nhanh': 'Quick Access',
      'Hành Động Nhanh': 'Quick Actions', 'Xem chấm công': 'View Attendance',
      'Thêm đánh giá KPI': 'Add KPI Review', 'Đăng tin tuyển dụng': 'Post Job',
    },
    zh: {
      // Navigation
      'Dashboard': '仪表板', 'Nhân viên': '员工', 'Phòng ban': '部门',
      'Chức vụ': '职位', 'Hợp đồng': '合同', 'Bảng lương': '工资单',
      'Thanh toán': '付款', 'Tài liệu': '文件', 'Nghỉ phép': '请假',
      'Chấm công': '考勤', 'Công việc': '任务', 'Phân công': '分配',
      'Đánh giá KPI': 'KPI评估', 'Báo cáo': '报告', 'Thông báo công ty': '公司公告',
      'Tuyển dụng': '招聘', 'Video đào tạo': '培训视频', 'Thêm video mới': '上传视频',
      'Nhật ký hệ thống': '系统日志', 'Nhật ký': '审计日志', 'KPI Goals': 'KPI目标',
      'Mục tiêu KPI': 'KPI目标', 'Chi phí': '费用',
      'Kỹ năng': '技能', 'Kỹ năng NV': '员工技能', 'Ca làm việc': '班次', 'Phân ca': '排班',
      'Đăng xuất': '退出登录', 'Hồ sơ cá nhân': '个人资料', 'Làm thêm giờ': '加班',
      'Phiếu lương': '工资条', 'Công việc của tôi': '我的任务', 'Thông báo': '通知',
      'Nhóm của tôi': '我的团队', 'Duyệt nghỉ phép': '审批请假', 'Duyệt OT': '审批加班',
      'Tin tuyển dụng': '招聘信息', 'Ứng viên': '候选人',
      'LMS - Khóa học': 'LMS - 课程', 'QR Code': '二维码', 'Khảo sát': '调查',
      'Vinh danh': '表彰', 'Onboarding': '入职', 'OKR': 'OKR',
      'Cài đặt': '设置', 'Phân tích': '分析',
      'Advanced Dashboard': '高级仪表板', 'Quản lý User Nâng cao': '高级用户管理',
      'Lương': '工资', 'Video': '视频', 'Analytics': '分析', 'Khóa học': '课程',
      'Khen thưởng': '奖励', 'Cấu hình Hệ thống': '系统配置',
      'Audit Logs': '审计日志', 'Backup & Restore': '备份与恢复',
      'System Monitor': '系统监控', 'Cache & Email': '缓存和邮件',
      'Cloud Storage': '云存储',
      // Section labels
      'Cá nhân': '个人', 'Hỗ trợ': '支持', 'Đào tạo': '培训',
      // Page headings
      'Bảng Điều Khiển': '仪表板', 'NHÂN VIÊN': '员工', 'PHÒNG BAN': '部门',
      'CHỨC VỤ': '职位', 'CHỜ DUYỆT NGHỈ': '待审批', 'CÔNG VIỆC': '任务',
      'Chấm Công 7 Ngày Qua': '近7天考勤', 'Nhân Viên Theo Phòng Ban': '按部门员工',
      'Hôm nay:': '今天:', 'Xem chi tiết': '查看详情',
      // Buttons
      'Lưu': '保存', 'Hủy': '取消', 'Xóa': '删除', 'Sửa': '编辑', 'Thêm': '添加',
      'Tìm kiếm': '搜索', 'Quay lại': '返回', 'Xác nhận': '确认', 'Lọc': '筛选',
      'Tất cả': '全部', 'Trạng thái': '状态', 'Thao tác': '操作', 'Tìm': '搜索',
      // Status
      'Chờ duyệt': '待审批', 'Đã duyệt': '已批准', 'Từ chối': '已拒绝',
      'Đang thực hiện': '进行中', 'Hoàn thành': '已完成', 'Đã hủy': '已取消',
      'Đã thanh toán': '已付款', 'Chưa thanh toán': '未付款',
      // Table headers
      'Họ và tên': '姓名', 'Số điện thoại': '电话', 'Ngày vào làm': '入职日期',
      'Mã nhân viên': '员工编号', 'Số tiền': '金额', 'Ghi chú': '备注',
      'Lý do': '原因', 'Giờ vào': '签到', 'Giờ ra': '签退',
      'Lương cơ bản': '基本工资', 'Thưởng': '奖金', 'Khấu trừ': '扣款',
      'Lương thực nhận': '实发工资', 'Không có dữ liệu': '暂无数据',
      // New
      'Tài chính': '财务', 'Đánh giá': '评价', 'Tài sản': '资产', 'Trợ lý HR': 'HR助手',
      'Danh sách nhân viên': '员工列表', 'Quản lý': '管理', 'Nhân sự': '人事',
      'Thiết Bị & Tài Sản': '设备与资产', 'Thông Báo Công Ty': '公司公告',
      'Yêu Cầu Chi Phí': '费用申请', 'Tài Liệu Của Tôi': '我的文件',
      'Ca Làm Việc Của Tôi': '我的班次', 'Đăng Ký Làm Thêm Giờ': '加班申请',
      'Tiêu đề': '标题', 'Danh mục': '类别', 'Ngày chi': '消费日期',
      'Chứng từ': '凭证', 'Đang sử dụng': '使用中', 'Đã trả': '已归还',
      'Chưa check-in': '未签到', 'Đã hoàn thành ca': '班次已完成',
      'Xem tất cả': '查看全部', 'Thêm nhân viên': '添加员工',
      'Giao việc mới': '新分配', 'Truy cập nhanh': '快速访问',
    },
    ja: {
      // Navigation
      'Dashboard': 'ダッシュボード', 'Nhân viên': '従業員', 'Phòng ban': '部署',
      'Chức vụ': '役職', 'Hợp đồng': '契約', 'Bảng lương': '給与',
      'Thanh toán': '支払い', 'Tài liệu': '書類', 'Nghỉ phép': '休暇申請',
      'Chấm công': '勤怠', 'Công việc': 'タスク', 'Phân công': '割り当て',
      'Đánh giá KPI': 'KPI評価', 'Báo cáo': 'レポート', 'Thông báo công ty': 'お知らせ',
      'Tuyển dụng': '採用', 'Video đào tạo': 'トレーニング動画', 'Thêm video mới': '動画アップロード',
      'Nhật ký hệ thống': '監査ログ', 'Nhật ký': '監査ログ', 'KPI Goals': 'KPI目標',
      'Mục tiêu KPI': 'KPI目標', 'Chi phí': '経費',
      'Kỹ năng': 'スキル', 'Kỹ năng NV': '従業員スキル', 'Ca làm việc': 'シフト', 'Phân ca': 'シフト割り当て',
      'Đăng xuất': 'ログアウト', 'Hồ sơ cá nhân': 'プロフィール', 'Làm thêm giờ': '残業',
      'Phiếu lương': '給与明細', 'Công việc của tôi': '自分のタスク', 'Thông báo': '通知',
      'Nhóm của tôi': '自分のチーム', 'Duyệt nghỉ phép': '休暇承認', 'Duyệt OT': '残業承認',
      'Tin tuyển dụng': '求人情報', 'Ứng viên': '候補者',
      'LMS - Khóa học': 'LMS - コース', 'QR Code': 'QRコード', 'Khảo sát': 'アンケート',
      'Vinh danh': '表彰', 'Onboarding': 'オンボーディング', 'OKR': 'OKR',
      'Cài đặt': '設定', 'Phân tích': '分析',
      'Advanced Dashboard': '詳細ダッシュボード', 'Quản lý User Nâng cao': '高度なユーザー管理',
      'Lương': '給与', 'Video': '動画', 'Analytics': '分析', 'Khóa học': 'コース',
      'Khen thưởng': '報奨', 'Cấu hình Hệ thống': 'システム設定',
      'Audit Logs': '監査ログ', 'Backup & Restore': 'バックアップと復元',
      'System Monitor': 'システム監視', 'Cache & Email': 'キャッシュとメール',
      'Cloud Storage': 'クラウドストレージ',
      // Section labels
      'Cá nhân': '個人', 'Hỗ trợ': 'サポート', 'Đào tạo': 'トレーニング',
      // Page headings
      'Bảng Điều Khiển': 'ダッシュボード', 'NHÂN VIÊN': '従業員', 'PHÒNG BAN': '部署',
      'CHỨC VỤ': '役職', 'CHỜ DUYỆT NGHỈ': '承認待ち', 'CÔNG VIỆC': 'タスク',
      'Chấm Công 7 Ngày Qua': '過去7日間の勤怠', 'Nhân Viên Theo Phòng Ban': '部署別従業員',
      'Hôm nay:': '今日:', 'Xem chi tiết': '詳細を見る',
      // Buttons
      'Lưu': '保存', 'Hủy': 'キャンセル', 'Xóa': '削除', 'Sửa': '編集', 'Thêm': '追加',
      'Tìm kiếm': '検索', 'Quay lại': '戻る', 'Xác nhận': '確認', 'Lọc': 'フィルター',
      'Tất cả': 'すべて', 'Trạng thái': 'ステータス', 'Thao tác': '操作', 'Tìm': '検索',
      // Status
      'Chờ duyệt': '承認待ち', 'Đã duyệt': '承認済み', 'Từ chối': '却下',
      'Đang thực hiện': '進行中', 'Hoàn thành': '完了', 'Đã hủy': 'キャンセル',
      'Đã thanh toán': '支払済み', 'Chưa thanh toán': '未払い',
      // Table headers
      'Họ và tên': '氏名', 'Số điện thoại': '電話番号', 'Ngày vào làm': '入社日',
      'Mã nhân viên': '社員番号', 'Số tiền': '金額', 'Ghi chú': 'メモ',
      'Lý do': '理由', 'Giờ vào': '出勤', 'Giờ ra': '退勤',
      'Lương cơ bản': '基本給', 'Thưởng': 'ボーナス', 'Khấu trừ': '控除',
      'Lương thực nhận': '手取り', 'Không có dữ liệu': 'データなし',
      // New
      'Tài chính': '財務', 'Đánh giá': '評価', 'Tài sản': '資産', 'Trợ lý HR': 'HRアシスタント',
      'Danh sách nhân viên': '従業員一覧', 'Quản lý': '管理', 'Nhân sự': '人事',
      'Thiết Bị & Tài Sản': '設備と資産', 'Thông Báo Công Ty': '社内通知',
      'Yêu Cầu Chi Phí': '経費請求', 'Tài Liệu Của Tôi': '自分の書類',
      'Ca Làm Việc Của Tôi': '自分のシフト', 'Đăng Ký Làm Thêm Giờ': '残業申請',
      'Tiêu đề': 'タイトル', 'Danh mục': 'カテゴリ', 'Ngày chi': '支出日',
      'Chứng từ': '領収書', 'Đang sử dụng': '使用中', 'Đã trả': '返却済み',
      'Chưa check-in': '未出勤', 'Đã hoàn thành ca': 'シフト完了',
      'Xem tất cả': 'すべて見る', 'Thêm nhân viên': '従業員追加',
      'Giao việc mới': '新しい割り当て', 'Truy cập nhanh': 'クイックアクセス',
    }
    ,
    ko: {
      // Navigation
      'Dashboard': '대시보드', 'Nhân viên': '직원', 'Phòng ban': '부서',
      'Chức vụ': '직책', 'Hợp đồng': '계약', 'Bảng lương': '급여',
      'Thanh toán': '지급', 'Tài liệu': '문서', 'Nghỉ phép': '휴가 요청',
      'Chấm công': '근태', 'Công việc': '업무', 'Phân công': '배정',
      'Đánh giá KPI': 'KPI 평가', 'Báo cáo': '보고서', 'Thông báo công ty': '회사 공지',
      'Tuyển dụng': '채용', 'Video đào tạo': '교육 영상', 'Thêm video mới': '영상 업로드',
      'Nhật ký hệ thống': '시스템 로그', 'Nhật ký': '감사 로그', 'KPI Goals': 'KPI 목표',
      'Mục tiêu KPI': 'KPI 목표', 'Chi phí': '비용',
      'Kỹ năng': '기술', 'Kỹ năng NV': '직원 기술', 'Ca làm việc': '근무 교대', 'Phân ca': '교대 배정',
      'Đăng xuất': '로그아웃', 'Hồ sơ cá nhân': '내 프로필', 'Làm thêm giờ': '초과근무',
      'Phiếu lương': '급여명세서', 'Công việc của tôi': '내 업무', 'Thông báo': '알림',
      'Nhóm của tôi': '내 팀', 'Duyệt nghỉ phép': '휴가 승인', 'Duyệt OT': '초과근무 승인',
      'Tin tuyển dụng': '채용 공고', 'Ứng viên': '지원자',
      'LMS - Khóa học': 'LMS - 과정', 'QR Code': 'QR 코드', 'Khảo sát': '설문조사',
      'Vinh danh': '인정', 'Onboarding': '온보딩', 'OKR': 'OKR',
      'Cài đặt': '설정', 'Phân tích': '분석',
      'Advanced Dashboard': '고급 대시보드', 'Quản lý User Nâng cao': '고급 사용자 관리',
      'Lương': '급여', 'Video': '영상', 'Analytics': '분석', 'Khóa học': '과정',
      'Khen thưởng': '포상', 'Cấu hình Hệ thống': '시스템 구성',
      'Audit Logs': '감사 로그', 'Backup & Restore': '백업 및 복원',
      'System Monitor': '시스템 모니터', 'Cache & Email': '캐시 및 이메일',
      'Cloud Storage': '클라우드 스토리지',
      // Section labels
      'Cá nhân': '개인', 'Hỗ trợ': '지원', 'Đào tạo': '교육',
      // Page headings
      'Bảng Điều Khiển': '대시보드', 'NHÂN VIÊN': '직원', 'PHÒNG BAN': '부서',
      'CHỨC VỤ': '직책', 'CHỜ DUYỆT NGHỈ': '승인 대기 휴가', 'CÔNG VIỆC': '업무',
      'Chấm Công 7 Ngày Qua': '최근 7일 근태', 'Nhân Viên Theo Phòng Ban': '부서별 직원',
      'Hôm nay:': '오늘:', 'Xem chi tiết': '자세히 보기',
      // Buttons
      'Lưu': '저장', 'Hủy': '취소', 'Xóa': '삭제', 'Sửa': '수정', 'Thêm': '추가',
      'Tìm kiếm': '검색', 'Quay lại': '뒤로', 'Xác nhận': '확인', 'Lọc': '필터',
      'Tất cả': '전체', 'Trạng thái': '상태', 'Thao tác': '작업', 'Tìm': '검색',
      // Status
      'Chờ duyệt': '승인 대기', 'Đã duyệt': '승인됨', 'Từ chối': '거절됨',
      'Đang thực hiện': '진행 중', 'Hoàn thành': '완료', 'Đã hủy': '취소됨',
      'Đã thanh toán': '지급 완료', 'Chưa thanh toán': '미지급',
      // Table headers
      'Họ và tên': '성명', 'Số điện thoại': '전화번호', 'Ngày vào làm': '입사일',
      'Mã nhân viên': '직원 코드', 'Số tiền': '금액', 'Ghi chú': '메모',
      'Lý do': '사유', 'Giờ vào': '출근', 'Giờ ra': '퇴근',
      'Lương cơ bản': '기본급', 'Thưởng': '상여금', 'Khấu trừ': '공제',
      'Lương thực nhận': '실수령액', 'Không có dữ liệu': '데이터 없음',
      // New
      'Tài chính': '재무', 'Đánh giá': '평가', 'Tài sản': '자산', 'Trợ lý HR': 'HR 도우미',
      'Danh sách nhân viên': '직원 목록', 'Quản lý': '관리', 'Nhân sự': '인사',
      'Thiết Bị & Tài Sản': '장비 및 자산', 'Thông Báo Công Ty': '회사 공지',
      'Yêu Cầu Chi Phí': '비용 청구', 'Tài Liệu Của Tôi': '내 문서',
      'Ca Làm Việc Của Tôi': '내 근무 교대', 'Đăng Ký Làm Thêm Giờ': '초과근무 신청',
      'Tiêu đề': '제목', 'Danh mục': '카테고리', 'Ngày chi': '지출일',
      'Chứng từ': '영수증', 'Đang sử dụng': '사용 중', 'Đã trả': '반납됨',
      'Chưa check-in': '미출근', 'Đã hoàn thành ca': '근무 완료',
      'Xem tất cả': '전체 보기', 'Thêm nhân viên': '직원 추가',
      'Giao việc mới': '새 배정', 'Truy cập nhanh': '빠른 접근',
    }
  };

  // ==================== BACKGROUNDS ====================
  var BACKGROUNDS = [
    { id: 'default', label: 'Default Dark', color: 'linear-gradient(180deg,#0f172a,#1e293b)' },
    { id: 'ocean', label: 'Ocean Blue', color: 'linear-gradient(180deg,#0c1445,#1a237e)' },
    { id: 'forest', label: 'Forest Green', color: 'linear-gradient(180deg,#0a2e1a,#1b5e20)' },
    { id: 'sunset', label: 'Sunset', color: 'linear-gradient(180deg,#4a0e2e,#7b1fa2)' },
    { id: 'midnight', label: 'Midnight', color: 'linear-gradient(180deg,#000000,#1a1a2e)' },
    { id: 'slate', label: 'Slate Gray', color: 'linear-gradient(180deg,#1c2333,#2d3748)' },
    { id: 'crimson', label: 'Crimson', color: 'linear-gradient(180deg,#3b0000,#7f0000)' },
    { id: 'teal', label: 'Teal', color: 'linear-gradient(180deg,#003333,#006064)' },
  ];

  // ==================== STORAGE ====================
  function getSetting(key, def) {
    try { return localStorage.getItem('hrms_' + key) || def; } catch(e) { return def; }
  }
  function setSetting(key, val) {
    try { localStorage.setItem('hrms_' + key, val); } catch(e) {}
  }

  // ==================== TRANSLATION ====================
  function getLang() {
    var urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('lang') || getSetting('lang', 'vi');
  }

  function translatePage(lang) {
    if (lang === 'vi' || !DICT[lang]) return;
    var dict = DICT[lang];
    var keys = Object.keys(dict).sort(function(a,b){ return b.length - a.length; });

    var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
    var nodes = [];
    var n;
    while ((n = walker.nextNode())) {
      var p = n.parentNode;
      if (p && p.nodeName !== 'SCRIPT' && p.nodeName !== 'STYLE' && n.nodeValue.trim()) {
        nodes.push(n);
      }
    }
    nodes.forEach(function(node) {
      var t = node.nodeValue;
      for (var i = 0; i < keys.length; i++) {
        var vi = keys[i];
        if (t.indexOf(vi) !== -1) {
          t = t.split(vi).join(dict[vi]);
        }
      }
      if (t !== node.nodeValue) node.nodeValue = t;
    });

    // Placeholders
    document.querySelectorAll('[placeholder]').forEach(function(el) {
      var ph = el.getAttribute('placeholder');
      for (var i = 0; i < keys.length; i++) {
        if (ph.indexOf(keys[i]) !== -1) ph = ph.split(keys[i]).join(dict[keys[i]]);
      }
      el.setAttribute('placeholder', ph);
    });
  }

  // ==================== BACKGROUND ====================
  function applyBackground(bgId) {
    var bg = BACKGROUNDS.find(function(b){ return b.id === bgId; }) || BACKGROUNDS[0];
    var sidebars = document.querySelectorAll('.sidebar, [class*="sidebar"]');
    sidebars.forEach(function(s) {
      s.style.background = bg.color;
    });
    setSetting('bg', bgId);
  }

  // ==================== FONT SIZE ====================
  function applyFontSize(size) {
    var sizes = { small: '13px', medium: '14px', large: '16px' };
    document.documentElement.style.fontSize = sizes[size] || '14px';
    setSetting('fontSize', size);
  }

  // ==================== COMPACT MODE ====================
  function applyCompactMode(enabled) {
    if (enabled === 'true' || enabled === true) {
      document.body.classList.add('hrms-compact');
      if (!document.getElementById('hrms-compact-style')) {
        var s = document.createElement('style');
        s.id = 'hrms-compact-style';
        s.textContent = '.hrms-compact .sidebar a{padding:6px 20px!important;} .hrms-compact .data-table td{padding:8px 12px!important;} .hrms-compact .stat-card{padding:16px!important;}';
        document.head.appendChild(s);
      }
    } else {
      document.body.classList.remove('hrms-compact');
    }
    setSetting('compact', enabled ? 'true' : 'false');
  }
  function applyBrightness(val) {
    document.documentElement.style.filter = 'brightness(' + val + ')';
    setSetting('brightness', val);
  }

  // ==================== SETTINGS PANEL UI ====================
  function createSettingsPanel() {
    // Remove existing
    var existing = document.getElementById('hrms-settings-panel');
    if (existing) existing.remove();

    var currentLang = getLang();
    var currentBg = getSetting('bg', 'default');
    var currentBrightness = getSetting('brightness', '1');

    var langFlags = { vi: '🇻🇳', en: '🇬🇧', zh: '🇨🇳', ja: '🇯🇵', ko: '🇰🇷' };
    var langNames = { vi: 'Tiếng Việt', en: 'English', zh: '中文', ja: '日本語', ko: '한국어' };

    var panel = document.createElement('div');
    panel.id = 'hrms-settings-panel';
    panel.innerHTML = [
      '<div id="hrms-settings-overlay" style="position:fixed;inset:0;z-index:9998;background:rgba(0,0,0,0.5);" onclick="document.getElementById(\'hrms-settings-panel\').remove()"></div>',
      '<div style="position:fixed;right:22px;bottom:210px;z-index:10002;width:320px;background:#1e293b;border-radius:16px;box-shadow:0 20px 60px rgba(0,0,0,0.5);border:1px solid rgba(255,255,255,0.1);overflow:hidden;">',
        // Header
        '<div style="background:linear-gradient(135deg,#6366f1,#8b5cf6);padding:16px 20px;display:flex;align-items:center;justify-content:space-between;">',
          '<div style="display:flex;align-items:center;gap:10px;">',
            '<span style="font-size:1.3rem;">⚙️</span>',
            '<span style="color:white;font-weight:700;font-size:1rem;">Settings</span>',
          '</div>',
          '<button onclick="document.getElementById(\'hrms-settings-panel\').remove()" style="background:rgba(255,255,255,0.2);border:none;color:white;width:28px;height:28px;border-radius:50%;cursor:pointer;font-size:1rem;display:flex;align-items:center;justify-content:center;">✕</button>',
        '</div>',
        '<div style="padding:20px;max-height:70vh;overflow-y:auto;">',

          // Language section
          '<div style="margin-bottom:20px;">',
            '<div style="color:#94a3b8;font-size:0.72rem;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin-bottom:10px;">🌐 Language / 语言 / 言語</div>',
            '<div style="display:grid;grid-template-columns:1fr 1fr;gap:8px;" id="hrms-lang-grid">',
              Object.keys(langFlags).map(function(l) {
                var isActive = l === currentLang;
                return '<button data-lang="' + l + '" onclick="HRMS.settings.setLang(\'' + l + '\')" style="' +
                  'background:' + (isActive ? 'linear-gradient(135deg,#6366f1,#8b5cf6)' : 'rgba(255,255,255,0.06)') + ';' +
                  'border:1px solid ' + (isActive ? '#6366f1' : 'rgba(255,255,255,0.1)') + ';' +
                  'color:' + (isActive ? 'white' : '#94a3b8') + ';' +
                  'border-radius:10px;padding:10px 12px;cursor:pointer;text-align:left;transition:all 0.2s;font-size:0.85rem;font-weight:' + (isActive ? '700' : '500') + ';">' +
                  langFlags[l] + ' ' + langNames[l] + '</button>';
              }).join(''),
            '</div>',
          '</div>',

          // Background section
          '<div style="margin-bottom:20px;">',
            '<div style="color:#94a3b8;font-size:0.72rem;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin-bottom:10px;">🎨 Background Theme</div>',
            '<div style="display:grid;grid-template-columns:repeat(4,1fr);gap:8px;">',
              BACKGROUNDS.map(function(bg) {
                var isActive = bg.id === currentBg;
                return '<div onclick="HRMS.settings.setBg(\'' + bg.id + '\')" title="' + bg.label + '" style="' +
                  'width:100%;aspect-ratio:1;border-radius:10px;cursor:pointer;' +
                  'background:' + bg.color + ';' +
                  'border:2px solid ' + (isActive ? '#6366f1' : 'transparent') + ';' +
                  'box-shadow:' + (isActive ? '0 0 0 2px rgba(99,102,241,0.5)' : 'none') + ';' +
                  'transition:all 0.2s;position:relative;">' +
                  (isActive ? '<div style="position:absolute;inset:0;display:flex;align-items:center;justify-content:center;color:white;font-size:1rem;">✓</div>' : '') +
                  '</div>';
              }).join(''),
            '</div>',
          '</div>',

          // Brightness section
          '<div style="margin-bottom:16px;">',
            '<div style="color:#94a3b8;font-size:0.72rem;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin-bottom:10px;">☀️ Brightness</div>',
            '<div style="display:flex;align-items:center;gap:12px;">',
              '<span style="color:#64748b;font-size:0.8rem;">🌑</span>',
              '<input type="range" id="hrms-brightness-slider" min="0.5" max="1.2" step="0.05" value="' + currentBrightness + '" ' +
                'oninput="HRMS.settings.setBrightness(this.value)" ' +
                'style="flex:1;accent-color:#6366f1;cursor:pointer;">',
              '<span style="color:#64748b;font-size:0.8rem;">☀️</span>',
              '<span id="hrms-brightness-val" style="color:#94a3b8;font-size:0.78rem;min-width:36px;">' + Math.round(currentBrightness * 100) + '%</span>',
            '</div>',
          '</div>',

          // Font size section
          '<div style="margin-bottom:16px;">',
            '<div style="color:#94a3b8;font-size:0.72rem;font-weight:700;text-transform:uppercase;letter-spacing:1px;margin-bottom:10px;">🔤 Font Size</div>',
            '<div style="display:flex;gap:8px;">',
              ['small','medium','large'].map(function(s) {
                var isActive = getSetting('fontSize','medium') === s;
                var labels = {small:'Small',medium:'Medium',large:'Large'};
                var sizes = {small:'0.78rem',medium:'0.85rem',large:'0.95rem'};
                return '<button onclick="HRMS.settings.setFontSize(\'' + s + '\')" style="' +
                  'flex:1;padding:8px;border-radius:8px;cursor:pointer;font-size:' + sizes[s] + ';' +
                  'background:' + (isActive ? 'linear-gradient(135deg,#6366f1,#8b5cf6)' : 'rgba(255,255,255,0.06)') + ';' +
                  'border:1px solid ' + (isActive ? '#6366f1' : 'rgba(255,255,255,0.1)') + ';' +
                  'color:' + (isActive ? 'white' : '#94a3b8') + ';font-weight:' + (isActive ? '700' : '500') + ';">' +
                  labels[s] + '</button>';
              }).join(''),
            '</div>',
          '</div>',

          // Compact mode
          '<div style="margin-bottom:8px;">',
            '<div style="display:flex;align-items:center;justify-content:space-between;">',
              '<div>',
                '<div style="color:#e2e8f0;font-size:0.88rem;font-weight:600;">📐 Compact Mode</div>',
                '<div style="color:#64748b;font-size:0.75rem;margin-top:2px;">Reduce padding & spacing</div>',
              '</div>',
              '<label style="position:relative;display:inline-block;width:44px;height:24px;cursor:pointer;">',
                '<input type="checkbox" id="hrms-compact-toggle" ' + (getSetting('compact','false')==='true' ? 'checked' : '') + ' ' +
                  'onchange="HRMS.settings.setCompact(this.checked)" ' +
                  'style="opacity:0;width:0;height:0;">',
                '<span style="position:absolute;inset:0;background:' + (getSetting('compact','false')==='true' ? '#6366f1' : 'rgba(255,255,255,0.15)') + ';border-radius:24px;transition:0.3s;">' +
                  '<span style="position:absolute;left:' + (getSetting('compact','false')==='true' ? '22px' : '2px') + ';top:2px;width:20px;height:20px;background:white;border-radius:50%;transition:0.3s;"></span>' +
                '</span>',
              '</label>',
            '</div>',
          '</div>',

        '</div>',
      '</div>',
    ].join('');

    document.body.appendChild(panel);
  }

  // ==================== GEAR BUTTON ====================
  function injectGearButton() {
    if (document.getElementById('hrms-gear-btn')) return;

    var btn = document.createElement('button');
    btn.id = 'hrms-gear-btn';
    btn.innerHTML = '<i class="bi bi-gear-fill" aria-hidden="true"></i>';
    btn.title = 'Settings';
    btn.setAttribute('onclick', 'HRMS.settings.openPanel()');
    btn.style.cssText = [
      'position:fixed',
      'bottom:152px',
      'right:22px',
      'z-index:9998',
      'width:52px',
      'height:52px',
      'border-radius:50%',
      'background:linear-gradient(135deg,#f59e0b,#f97316)',
      'border:none',
      'color:white',
      'font-size:1.15rem',
      'cursor:pointer',
      'box-shadow:0 12px 28px rgba(249,115,22,0.36)',
      'transition:all 0.2s',
      'display:flex',
      'align-items:center',
      'justify-content:center',
    ].join(';');

    btn.addEventListener('mouseenter', function() {
      this.style.transform = 'scale(1.1) rotate(30deg)';
      this.style.boxShadow = '0 16px 34px rgba(249,115,22,0.5)';
    });
    btn.addEventListener('mouseleave', function() {
      this.style.transform = 'scale(1) rotate(0deg)';
      this.style.boxShadow = '0 12px 28px rgba(249,115,22,0.36)';
    });

    document.body.appendChild(btn);
  }

  // ==================== INIT ====================
  function init() {
    var lang = getLang();

    // Apply saved settings
    applyBackground(getSetting('bg', 'default'));
    applyBrightness(getSetting('brightness', '1'));
    applyFontSize(getSetting('fontSize', 'medium'));
    applyCompactMode(getSetting('compact', 'false'));

    // Translate if not VI
    if (lang !== 'vi') {
      translatePage(lang);
    }

    // Inject gear button
    injectGearButton();
  }

  // ==================== PUBLIC API ====================
  window.HRMS = window.HRMS || {};
  window.HRMS.settings = {
    openPanel: function() { createSettingsPanel(); },

    setLang: function(lang) {
      setSetting('lang', lang);
      var url = new URL(window.location.href);
      url.searchParams.set('lang', lang);
      window.location.href = url.toString();
    },

    setBg: function(bgId) {
      applyBackground(bgId);
      createSettingsPanel();
    },

    setBrightness: function(val) {
      applyBrightness(val);
      var el = document.getElementById('hrms-brightness-val');
      if (el) el.textContent = Math.round(val * 100) + '%';
    },

    setFontSize: function(size) {
      applyFontSize(size);
      createSettingsPanel();
    },

    setCompact: function(enabled) {
      applyCompactMode(enabled);
    }
  };

  // Auto-init
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();

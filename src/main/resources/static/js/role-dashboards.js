(function () {
  const e = React.createElement;

  function StatCard({ title, value, href, bg }) {
    return e("div", { className: "col-md-3 col-sm-6 mb-3" }, e("a", { href: href || "#", className: "role-stat-card", style: { background: bg || "linear-gradient(135deg,#6366f1,#8b5cf6)" } }, e("h6", null, title), e("h3", null, value ?? 0)));
  }

  function Panel({ title, right, children }) {
    return e("div", { className: "role-panel mb-3" }, e("div", { className: "role-panel-header d-flex justify-content-between align-items-center" }, e("span", null, title), right || null), e("div", { className: "role-panel-body" }, children));
  }

  function Sidebar({ brand, nav }) {
    return e("aside", { className: "col-md-2 role-sidebar d-none d-md-block" }, e("div", { className: "role-brand" }, brand), nav.map((item) => e("a", { key: item.href + item.label, className: "role-nav-link" + (item.active ? " active" : ""), href: item.href }, item.label)));
  }

  function AdminView({ data }) {
    const stats = [
      { title: "Nhan vien", value: data.totalUsers, href: "/admin/users", bg: "linear-gradient(135deg,#667eea,#764ba2)" },
      { title: "Phong ban", value: data.totalDepartments, href: "/admin/departments", bg: "linear-gradient(135deg,#11998e,#38ef7d)" },
      { title: "Chuc vu", value: data.totalPositions, href: "/admin/positions", bg: "linear-gradient(135deg,#f093fb,#f5576c)" },
      { title: "Nghi cho duyet", value: data.pendingLeaves, href: "/admin/leaves", bg: "linear-gradient(135deg,#f7971e,#ffd200)" },
    ];
    return e(React.Fragment, null, e("div", { className: "row mb-2" }, stats.map((s) => e(StatCard, { key: s.title, ...s }))), e(Panel, { title: "Don nghi cho duyet", right: e("a", { href: "/admin/leaves", className: "btn btn-sm btn-outline-primary" }, "Xem tat ca") }, !data.recentLeaves?.length ? e("div", { className: "text-muted" }, "Khong co don cho duyet.") : data.recentLeaves.map((l) => e("div", { className: "role-item d-flex justify-content-between align-items-center", key: l.id }, e("div", null, e("div", { className: "fw-semibold" }, l.userName || "N/A"), e("small", { className: "text-muted" }, `${l.leaveType} | ${l.startDate} -> ${l.endDate}`)), e("div", { className: "d-flex gap-1" }, e("a", { href: `/admin/leaves/approve/${l.id}`, className: "btn btn-sm btn-success" }, "Duyet"), e("a", { href: `/admin/leaves/reject/${l.id}`, className: "btn btn-sm btn-danger" }, "Tu choi"))))));
  }

  function ManagerView({ data }) {
    const stats = [
      { title: "Tong nhan vien", value: data.totalEmployees, href: "/admin/users" },
      { title: "Don cho duyet", value: data.pendingLeaves, href: "/admin/leaves", bg: "linear-gradient(135deg,#f59e0b,#f97316)" },
      { title: "Dang thuc hien", value: data.activeTasks, href: "/admin/tasks", bg: "linear-gradient(135deg,#10b981,#059669)" },
      { title: "Vang hom nay", value: data.absentToday, href: "#", bg: "linear-gradient(135deg,#ef4444,#dc2626)" },
    ];
    return e(React.Fragment, null, e("div", { className: "row mb-2" }, stats.map((s) => e(StatCard, { key: s.title, ...s }))), e(Panel, { title: "Top KPI", right: e("a", { href: "/admin/reviews", className: "btn btn-sm btn-outline-primary" }, "Xem KPI") }, !data.topPerformers?.length ? e("div", { className: "text-muted" }, "Chua co du lieu KPI.") : data.topPerformers.map((p, idx) => e("div", { key: idx, className: "role-item d-flex justify-content-between" }, e("div", null, e("strong", null, p.userName), e("div", { className: "small text-muted" }, p.reviewPeriod || "")), e("span", { className: "badge bg-primary" }, p.overallScore ?? 0)))));
  }

  function HiringView({ data }) {
    const stats = [
      { title: "Tin dang mo", value: data.openCount, href: "/hiring/postings" },
      { title: "Tong ung vien", value: data.totalCandidates, href: "/hiring/candidates", bg: "linear-gradient(135deg,#10b981,#059669)" },
      { title: "Ung vien moi", value: data.newCandidates, href: "/hiring/candidates?status=NEW", bg: "linear-gradient(135deg,#f59e0b,#f97316)" },
      { title: "Da tuyen", value: data.hiredCount, href: "/hiring/candidates?status=HIRED", bg: "linear-gradient(135deg,#06b6d4,#0891b2)" },
    ];
    return e(React.Fragment, null, e("div", { className: "row mb-2" }, stats.map((s) => e(StatCard, { key: s.title, ...s }))), e(Panel, { title: "Tin tuyen dung gan day", right: e("a", { href: "/hiring/postings", className: "btn btn-sm btn-outline-primary" }, "Xem tat ca") }, !data.postings?.length ? e("div", { className: "text-muted" }, "Chua co tin tuyen dung.") : data.postings.map((p, idx) => e("div", { key: idx, className: "role-item d-flex justify-content-between align-items-center" }, e("div", null, e("div", { className: "fw-semibold" }, p.title), e("small", { className: "text-muted" }, p.departmentName || "Chung")), e("span", { className: "badge bg-secondary" }, p.status || "N/A")))));
  }

  function UserView({ data }) {
    const stats = [
      { title: "Ngay cong", value: data.attendanceDays, href: "/user/attendance" },
      { title: "Don cho duyet", value: data.pendingLeaves, href: "/user/leaves", bg: "linear-gradient(135deg,#f59e0b,#f97316)" },
      { title: "Cong viec", value: data.totalTasks, href: "/user1/tasks", bg: "linear-gradient(135deg,#06b6d4,#22d3ee)" },
      { title: "Luong gan nhat", value: data.latestPayrollMonth || "N/A", href: "/user1/payroll", bg: "linear-gradient(135deg,#10b981,#34d399)" },
    ];
    return e(
      React.Fragment,
      null,
      e("div", { className: "row mb-2" }, stats.map((s) => e(StatCard, { key: s.title, ...s }))),
      e(Panel, { title: "Cham cong hom nay" }, e("div", { className: "d-flex justify-content-between align-items-center" }, e("div", null, e("div", { className: "fw-semibold" }, data.todayAttendanceText || "Chua check-in"), e("small", { className: "text-muted" }, data.today || "")), e("div", null, data.showCheckin && e("a", { href: "/user/attendance", className: "btn btn-primary btn-sm" }, "Check-in"), data.showCheckout && e("a", { href: "/user/attendance", className: "btn btn-warning btn-sm" }, "Check-out")))),
      e(Panel, { title: "Cong viec cua toi", right: e("a", { href: "/user1/tasks", className: "btn btn-sm btn-outline-primary" }, "Xem tat ca") }, !data.myTasks?.length ? e("div", { className: "text-muted" }, "Chua co cong viec.") : data.myTasks.map((t, idx) => e("div", { key: idx, className: "role-item" }, e("div", { className: "fw-semibold" }, t.taskName), e("small", { className: "text-muted" }, `${t.taskType || ""} | ${t.assignedDate || ""} | ${t.status || ""}`))))
    );
  }

  function RoleDashboardApp() {
    const config = window.HRMS_ROLE_DASHBOARD || {};
    const role = config.role || "admin";
    let content = e("div", null, "No dashboard config");
    if (role === "admin") content = e(AdminView, { data: config.data || {} });
    if (role === "manager") content = e(ManagerView, { data: config.data || {} });
    if (role === "hiring") content = e(HiringView, { data: config.data || {} });
    if (role === "user") content = e(UserView, { data: config.data || {} });
    return e("div", { className: "container-fluid role-shell" }, e("div", { className: "row" }, e(Sidebar, { brand: config.brand || "HRMS", nav: config.nav || [] }), e("main", { className: "col-md-10 offset-md-2 role-main" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", { className: "mb-0" }, config.title || "Dashboard"), e("small", { className: "text-muted" }, config.subtitle || "")), content)));
  }

  const root = document.getElementById("role-dashboard-root");
  if (root) ReactDOM.createRoot(root).render(e(RoleDashboardApp));
})();

(function () {
  const e = React.createElement;
  const mount = (id, app) => {
    const el = document.getElementById(id);
    if (el) ReactDOM.createRoot(el).render(e(app));
  };

  function table(headers, rows, emptyText, colSpan) {
    return e(
      "table",
      { className: "table table-hover align-middle mb-0" },
      e("thead", { className: "bg-light" }, e("tr", null, headers.map((h) => e("th", { key: h }, h)))),
      e("tbody", null, rows.length ? rows : e("tr", null, e("td", { colSpan: colSpan || headers.length, className: "text-center py-4 text-muted" }, emptyText || "Khong co du lieu")))
    );
  }

  function PostingListApp() {
    const cfg = window.HRMS_POSTING_LIST_CONFIG || { postings: [] };
    const rows = cfg.postings.map((p) =>
      e("tr", { key: p.id }, e("td", null, e("div", { className: "fw-semibold" }, p.title), e("small", { className: "text-muted" }, p.positionName || "")), e("td", null, p.departmentName || "---"), e("td", null, p.salaryRange || "Thuong luong"), e("td", null, p.deadline || "--"), e("td", null, e("span", { className: "badge bg-secondary" }, p.statusLabel || p.status)), e("td", null, e("a", { href: `/hiring/postings/edit/${p.id}`, className: "btn btn-sm btn-outline-warning me-1" }, "Sua"), e("a", { href: `/hiring/postings/delete/${p.id}`, className: "btn btn-sm btn-outline-danger", onClick: (ev) => { if (!confirm("Xoa tin nay?")) ev.preventDefault(); } }, "Xoa")))
    );
    return e("div", { className: "container py-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Tin Tuyen Dung (React)"), e("a", { href: "/hiring/postings/add", className: "btn btn-primary" }, "Dang tin moi")), e("div", { className: "card border-0 shadow-sm" }, e("div", { className: "table-responsive" }, table(["Vi tri", "Phong ban", "Luong", "Deadline", "Trang thai", "Hanh dong"], rows, "Chua co tin tuyen dung"))));
  }

  function PostingFormApp() {
    const cfg = window.HRMS_POSTING_FORM_CONFIG || { posting: {}, departments: [], positions: [] };
    const [f, setF] = React.useState({
      id: cfg.posting.id || "",
      title: cfg.posting.title || "",
      departmentId: cfg.posting.departmentId || "",
      positionId: cfg.posting.positionId || "",
      salaryMin: cfg.posting.salaryMin || "",
      salaryMax: cfg.posting.salaryMax || "",
      deadline: cfg.posting.deadline || "",
      status: cfg.posting.status || "OPEN",
      description: cfg.posting.description || "",
      requirements: cfg.posting.requirements || "",
    });
    return e("form", { action: "/hiring/postings/save", method: "post", className: "container py-4" },
      e("input", { type: "hidden", name: "id", value: f.id }),
      e("div", { className: "card border-0 shadow-sm" }, e("div", { className: "card-header fw-bold" }, "Form Tin Tuyen Dung (React)"),
        e("div", { className: "card-body row g-3" },
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Tieu de"), e("input", { className: "form-control", name: "title", required: true, value: f.title, onChange: (ev) => setF({ ...f, title: ev.target.value }) })),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Phong ban"), e("select", { className: "form-select", name: "department.id", value: f.departmentId, onChange: (ev) => setF({ ...f, departmentId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.departments.map((d) => e("option", { key: d.id, value: d.id }, d.name)))),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Chuc vu"), e("select", { className: "form-select", name: "position.id", value: f.positionId, onChange: (ev) => setF({ ...f, positionId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.positions.map((p) => e("option", { key: p.id, value: p.id }, p.name)))),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Luong min"), e("input", { type: "number", className: "form-control", name: "salaryMin", value: f.salaryMin, onChange: (ev) => setF({ ...f, salaryMin: ev.target.value }) })),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Luong max"), e("input", { type: "number", className: "form-control", name: "salaryMax", value: f.salaryMax, onChange: (ev) => setF({ ...f, salaryMax: ev.target.value }) })),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Deadline"), e("input", { type: "date", className: "form-control", name: "deadline", value: f.deadline, onChange: (ev) => setF({ ...f, deadline: ev.target.value }) })),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Status"), e("select", { className: "form-select", name: "status", value: f.status, onChange: (ev) => setF({ ...f, status: ev.target.value }) }, ["OPEN", "CLOSED", "FILLED"].map((s) => e("option", { key: s, value: s }, s)))),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Mo ta"), e("textarea", { className: "form-control", rows: 4, name: "description", value: f.description, onChange: (ev) => setF({ ...f, description: ev.target.value }) })),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Yeu cau"), e("textarea", { className: "form-control", rows: 3, name: "requirements", value: f.requirements, onChange: (ev) => setF({ ...f, requirements: ev.target.value }) })),
          e("div", { className: "col-12 d-flex gap-2" }, e("button", { type: "submit", className: "btn btn-primary" }, "Luu"), e("a", { href: "/hiring/postings", className: "btn btn-outline-secondary" }, "Quay lai"))
        )
      )
    );
  }

  function TaskListApp() {
    const cfg = window.HRMS_TASK_LIST_CONFIG || { tasks: [] };
    const rows = cfg.tasks.map((t) => e("tr", { key: t.id }, e("td", null, t.id), e("td", { className: "fw-bold text-start" }, t.taskName), e("td", null, t.taskType), e("td", null, t.maxParticipants), e("td", null, t.baseReward), e("td", null, t.isExtraShift ? "Co" : "Khong"), e("td", null, e("a", { href: `/admin/tasks/edit/${t.id}`, className: "btn btn-sm btn-warning me-1" }, "Sua"), e("a", { href: `/admin/tasks/delete/${t.id}`, className: "btn btn-sm btn-danger" }, "Xoa"))));
    return e("div", { className: "card shadow-sm border-0" }, e("div", { className: "card-header bg-white py-3 d-flex justify-content-between align-items-center" }, e("h5", { className: "mb-0" }, "Quan Ly Cong Viec (React)"), e("a", { href: "/admin/tasks/add", className: "btn btn-success" }, "Them cong viec")), e("div", { className: "table-responsive" }, table(["ID", "Ten", "Loai", "So nguoi", "Luong", "Tang ca", "Thao tac"], rows, "Khong co cong viec")));
  }

  function AssignmentListApp() {
    const cfg = window.HRMS_ASSIGNMENT_LIST_CONFIG || { assignments: [] };
    const rows = cfg.assignments.map((a) => e("tr", { key: a.id }, e("td", null, a.id), e("td", null, a.userName), e("td", null, a.taskName), e("td", null, a.assignedDate), e("td", null, e("span", { className: "badge bg-warning text-dark" }, a.status)), e("td", null, a.actualHours || "0h"), e("td", null, e("a", { href: `/admin/assignments/edit/${a.id}`, className: "btn btn-sm btn-outline-primary me-1" }, "Sua"), e("form", { action: `/admin/assignments/delete/${a.id}`, method: "post", className: "d-inline" }, e("button", { type: "submit", className: "btn btn-sm btn-outline-danger" }, "Xoa")))));
    return e("div", { className: "container mt-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Phan Cong Cong Viec (React)"), e("a", { href: "/admin/assignments/new", className: "btn btn-primary" }, "Giao viec moi")), e("div", { className: "card shadow-sm" }, e("div", { className: "table-responsive" }, table(["ID", "Nhan vien", "Cong viec", "Ngay giao", "Trang thai", "So gio", "Thao tac"], rows, "Chua co du lieu"))));
  }

  function AssignmentFormApp() {
    const cfg = window.HRMS_ASSIGNMENT_FORM_CONFIG || { assignment: {}, users: [], tasks: [] };
    const [f, setF] = React.useState({ id: cfg.assignment.id || "", userId: cfg.assignment.userId || "", taskId: cfg.assignment.taskId || "", assignedDate: cfg.assignment.assignedDate || "", status: cfg.assignment.status || "PENDING", actualHours: cfg.assignment.actualHours || "" });
    return e("form", { action: "/admin/assignments/save", method: "post", className: "container py-4" }, e("input", { type: "hidden", name: "id", value: f.id }), e("div", { className: "card shadow-sm border-0" }, e("div", { className: "card-header fw-bold" }, "Giao Cong Viec (React)"), e("div", { className: "card-body row g-3" }, e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Nhan vien"), e("select", { className: "form-select", name: "user.id", value: f.userId, onChange: (ev) => setF({ ...f, userId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.users.map((u) => e("option", { key: u.id, value: u.id }, u.name)))), e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Cong viec"), e("select", { className: "form-select", name: "task.id", value: f.taskId, onChange: (ev) => setF({ ...f, taskId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.tasks.map((t) => e("option", { key: t.id, value: t.id }, t.name)))), e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Ngay giao"), e("input", { className: "form-control", type: "date", name: "assignedDate", value: f.assignedDate, onChange: (ev) => setF({ ...f, assignedDate: ev.target.value }) })), e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Trang thai"), e("select", { className: "form-select", name: "status", value: f.status, onChange: (ev) => setF({ ...f, status: ev.target.value }) }, ["PENDING", "IN_PROGRESS", "COMPLETED"].map((s) => e("option", { key: s, value: s }, s)))), e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "So gio"), e("input", { className: "form-control", type: "number", step: "0.5", name: "actualHours", value: f.actualHours, onChange: (ev) => setF({ ...f, actualHours: ev.target.value }) })), e("div", { className: "col-12 d-flex gap-2" }, e("button", { type: "submit", className: "btn btn-primary" }, "Luu"), e("a", { href: "/admin/assignments", className: "btn btn-outline-secondary" }, "Quay lai")))));
  }

  function LeaveListApp() {
    const cfg = window.HRMS_LEAVE_LIST_CONFIG || { leaves: [] };
    const rows = cfg.leaves.map((l) => e("tr", { key: l.id }, e("td", null, l.userName), e("td", null, l.leaveType), e("td", null, l.startDate), e("td", null, l.endDate), e("td", null, l.reason || ""), e("td", null, e("span", { className: "badge bg-secondary" }, l.status)), e("td", null, e("a", { href: `/admin/leaves/edit/${l.id}`, className: "btn btn-sm btn-outline-primary me-1" }, "Sua"), e("a", { href: `/admin/leaves/delete/${l.id}`, className: "btn btn-sm btn-outline-danger" }, "Xoa"))));
    return e("div", { className: "container py-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Quan Ly Nghi Phep (React)"), e("a", { href: "/admin/leaves/add", className: "btn btn-warning" }, "Tao don")), e("div", { className: "card shadow-sm border-0" }, e("div", { className: "table-responsive" }, table(["Nhan vien", "Loai", "Tu ngay", "Den ngay", "Ly do", "Trang thai", "Thao tac"], rows, "Khong co don nghi phep"))));
  }

  function AttendanceListApp() {
    const cfg = window.HRMS_ATTENDANCE_LIST_CONFIG || { attendances: [] };
    const rows = cfg.attendances.map((a) => e("tr", { key: a.id }, e("td", null, a.userName), e("td", null, a.date), e("td", null, a.checkIn || "--"), e("td", null, a.checkOut || "--"), e("td", null, e("span", { className: "badge bg-secondary" }, a.status)), e("td", null, a.note || ""), e("td", null, e("a", { href: `/admin/attendance/delete/${a.id}`, className: "btn btn-sm btn-outline-danger" }, "Xoa"))));
    return e("div", { className: "container py-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Quan Ly Cham Cong (React)"), e("a", { href: "/admin/attendance/add", className: "btn btn-info text-white" }, "Them ban ghi")), e("div", { className: "card shadow-sm border-0" }, e("div", { className: "table-responsive" }, table(["Nhan vien", "Ngay", "Check-in", "Check-out", "Trang thai", "Ghi chu", "Thao tac"], rows, "Khong co du lieu cham cong"))));
  }

  if (window.HRMS_POSTING_LIST_PAGE) mount("posting-list-root", PostingListApp);
  if (window.HRMS_POSTING_FORM_PAGE) mount("posting-form-root", PostingFormApp);
  if (window.HRMS_TASK_LIST_PAGE) mount("task-list-root", TaskListApp);
  if (window.HRMS_ASSIGNMENT_LIST_PAGE) mount("assignment-list-root", AssignmentListApp);
  if (window.HRMS_ASSIGNMENT_FORM_PAGE) mount("assignment-form-root", AssignmentFormApp);
  if (window.HRMS_LEAVE_LIST_PAGE) mount("leave-list-root", LeaveListApp);
  if (window.HRMS_ATTENDANCE_LIST_PAGE) mount("attendance-list-root", AttendanceListApp);
})();

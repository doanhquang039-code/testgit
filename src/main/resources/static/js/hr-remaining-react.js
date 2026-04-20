(function () {
  const e = React.createElement;

  function card(title, body) {
    return e("div", { className: "card shadow-sm border-0 mb-3" }, e("div", { className: "card-header fw-bold" }, title), e("div", { className: "card-body" }, body));
  }

  function AuditLogApp() {
    const cfg = window.HRMS_AUDIT_LOG_CONFIG || { rows: [], q: "" };
    return e("div", { className: "container py-4" },
      e("h3", { className: "mb-3" }, "Nhat ky he thong (React)"),
      e("form", { className: "row g-2 mb-3", method: "get", action: "/admin/audit-log" },
        e("div", { className: "col-md-8" }, e("input", { className: "form-control", name: "q", defaultValue: cfg.q, placeholder: "Tim kiem..." })),
        e("div", { className: "col-md-4 d-flex gap-2" }, e("button", { className: "btn btn-dark", type: "submit" }, "Loc"), e("a", { href: "/admin/audit-log", className: "btn btn-outline-secondary" }, "Xoa"))
      ),
      card("Danh sach log",
        e("div", { className: "table-responsive" }, e("table", { className: "table table-sm table-hover" },
          e("thead", null, e("tr", null, ["Thoi diem", "Actor", "Action", "Entity", "ID", "Chi tiet"].map((h) => e("th", { key: h }, h)))),
          e("tbody", null, cfg.rows.length ? cfg.rows.map((r, i) => e("tr", { key: i }, e("td", null, r.createdAt), e("td", null, r.actor), e("td", null, r.action), e("td", null, r.entityType), e("td", null, r.entityId), e("td", null, r.detail))) : e("tr", null, e("td", { colSpan: 6, className: "text-center text-muted py-3" }, "Chua co du lieu")))
        ))
      )
    );
  }

  function PaymentListApp() {
    const cfg = window.HRMS_PAYMENT_LIST_CONFIG || { payments: [] };
    return e("div", { className: "container py-4" },
      e("div", { className: "d-flex justify-content-between mb-3" }, e("h3", null, "Quan ly thanh toan (React)"), e("a", { href: "/admin/payments/create", className: "btn btn-primary" }, "Tao thanh toan")),
      card("Danh sach",
        e("div", { className: "table-responsive" }, e("table", { className: "table table-hover" },
          e("thead", null, e("tr", null, ["Nhan vien", "Loai", "So tien", "Phuong thuc", "Trang thai", "Ma GD", "Ngay", "Thao tac"].map((h) => e("th", { key: h }, h)))),
          e("tbody", null, cfg.payments.length ? cfg.payments.map((p) => e("tr", { key: p.id },
            e("td", null, p.userName),
            e("td", null, p.paymentType),
            e("td", null, p.amount),
            e("td", null, p.paymentMethod),
            e("td", null, e("span", { className: "badge bg-secondary" }, p.paymentStatus)),
            e("td", null, p.transactionId || "-"),
            e("td", null, p.paymentDate || "-"),
            e("td", null, e("a", { href: `/admin/payments/detail/${p.id}`, className: "btn btn-sm btn-outline-info me-1" }, "Xem"), e("a", { href: `/admin/payments/delete/${p.id}`, className: "btn btn-sm btn-outline-danger" }, "Xoa"))
          )) : e("tr", null, e("td", { colSpan: 8, className: "text-center text-muted py-3" }, "Khong co du lieu")))
        ))
      )
    );
  }

  function PaymentCreateApp() {
    const cfg = window.HRMS_PAYMENT_CREATE_CONFIG || { users: [], payrolls: [], csrfName: "_csrf", csrfToken: "" };
    const [method, setMethod] = React.useState("BANK_TRANSFER");
    return e("form", { action: "/admin/payments/save", method: "post", className: "container py-4" },
      e("input", { type: "hidden", name: cfg.csrfName, value: cfg.csrfToken }),
      e("h3", { className: "mb-3" }, "Tao thanh toan (React)"),
      card("Thong tin thanh toan", e("div", { className: "row g-3" },
        e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Nhan vien"), e("select", { className: "form-select", name: "userId", required: true }, e("option", { value: "" }, "-- Chon --"), cfg.users.map((u) => e("option", { key: u.id, value: u.id }, u.name)))),
        e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Loai"), e("select", { className: "form-select", name: "paymentType" }, ["SALARY", "BONUS", "REWARD", "ADVANCE"].map((t) => e("option", { key: t, value: t }, t)))),
        e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "So tien"), e("input", { className: "form-control", type: "number", step: "0.01", name: "amount", required: true })),
        e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Payroll"), e("select", { className: "form-select", name: "payrollId" }, e("option", { value: "" }, "-- Khong lien ket --"), cfg.payrolls.map((p) => e("option", { key: p.id, value: p.id }, p.label)))),
        e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Phuong thuc"), e("select", { className: "form-select", name: "paymentMethod", value: method, onChange: (ev) => setMethod(ev.target.value) }, ["BANK_TRANSFER", "MOMO", "VNPAY", "CASH"].map((m) => e("option", { key: m, value: m }, m)))),
        method === "BANK_TRANSFER" ? e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "So tai khoan"), e("input", { className: "form-control", name: "accountNumber" }), e("label", { className: "form-label mt-2" }, "Ngan hang"), e("input", { className: "form-control", name: "bankName" })) : null,
        e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Ghi chu"), e("textarea", { className: "form-control", rows: 3, name: "notes" })),
        e("div", { className: "col-12 d-flex gap-2" }, e("button", { className: "btn btn-primary", type: "submit" }, "Luu"), e("a", { href: "/admin/payments", className: "btn btn-outline-secondary" }, "Huy"))
      ))
    );
  }

  function PaymentDetailApp() {
    const p = (window.HRMS_PAYMENT_DETAIL_CONFIG || {}).payment || {};
    return e("div", { className: "container py-4" }, e("a", { href: "/admin/payments", className: "btn btn-outline-secondary btn-sm mb-3" }, "Danh sach"), card(`Giao dich #${p.id || ""}`, e("div", null,
      e("p", null, e("strong", null, "Nhan vien: "), p.userName || "-"),
      e("p", null, e("strong", null, "So tien: "), p.amount || "-"),
      e("p", null, e("strong", null, "Loai: "), p.paymentType || "-"),
      e("p", null, e("strong", null, "Phuong thuc: "), p.paymentMethod || "-"),
      e("p", null, e("strong", null, "Trang thai: "), p.paymentStatus || "-"),
      e("p", null, e("strong", null, "Ma GD: "), p.transactionId || "-"),
      e("p", null, e("strong", null, "Ghi chu: "), p.notes || "-")
    )));
  }

  function ReportDashboardApp() {
    const cfg = window.HRMS_REPORT_CONFIG || {};
    const stats = [
      ["Tong nhan vien", cfg.totalEmployees],
      ["KPI TB", cfg.avgKpiScore],
      ["Quy luong thang", cfg.totalPayrollThisMonth],
      ["Don nghi thang", cfg.leaveThisMonth],
    ];
    return e("div", { className: "container py-4" }, e("h3", { className: "mb-3" }, "Bao cao tong hop (React)"),
      e("div", { className: "row g-3 mb-3" }, stats.map((s) => e("div", { key: s[0], className: "col-md-3" }, card(s[0], e("h4", { className: "mb-0" }, s[1] ?? "-"))))),
      card("Top performers", e("ul", { className: "mb-0" }, (cfg.topPerformers || []).length ? cfg.topPerformers.map((t, i) => e("li", { key: i }, `${t.name} - ${t.score}`)) : e("li", null, "Chua co du lieu")))
    );
  }

  function PaymentResultApp() {
    const cfg = window.HRMS_PAYMENT_RESULT_CONFIG || {};
    const ok = !!cfg.success;
    return e("div", { className: "container py-5" },
      e("div", { className: "card shadow-sm mx-auto", style: { maxWidth: "560px" } },
        e("div", { className: "card-body text-center p-4" },
          e("div", { style: { fontSize: "42px" } }, ok ? "✅" : "❌"),
          e("h4", { className: "mt-2" }, ok ? "Thanh toan thanh cong" : "Loi thanh toan"),
          e("p", { className: "text-muted" }, cfg.message || ""),
          cfg.payment ? e("div", { className: "text-start small bg-light p-3 rounded" },
            e("div", null, `Nhan vien: ${cfg.payment.userName || "-"}`),
            e("div", null, `So tien: ${cfg.payment.amount || "-"}`),
            e("div", null, `Ma GD: ${cfg.payment.transactionId || "-"}`)
          ) : null,
          e("div", { className: "d-flex gap-2 justify-content-center mt-3" },
            e("a", { href: "/admin/payments", className: "btn btn-primary" }, "Danh sach thanh toan"),
            e("a", { href: "/admin/dashboard", className: "btn btn-outline-secondary" }, "Dashboard")
          )
        )
      )
    );
  }

  function LoginApp() {
    const cfg = window.HRMS_LOGIN_CONFIG || {};
    return e("div", { className: "container py-5" }, e("div", { className: "card shadow-sm mx-auto", style: { maxWidth: "420px" } }, e("div", { className: "card-body p-4" },
      e("h4", { className: "text-center mb-1" }, "HRMS LOGIN"), e("p", { className: "text-center text-muted mb-3" }, "He thong quan ly nhan su"),
      cfg.error ? e("div", { className: "alert alert-danger py-2" }, "Sai tai khoan hoac mat khau") : null,
      cfg.logout ? e("div", { className: "alert alert-success py-2" }, "Dang xuat thanh cong") : null,
      e("form", { method: "post", action: "/login" },
        e("div", { className: "mb-3" }, e("label", { className: "form-label" }, "Username"), e("input", { className: "form-control", name: "username", required: true })),
        e("div", { className: "mb-3" }, e("label", { className: "form-label" }, "Mat khau"), e("input", { className: "form-control", type: "password", name: "password", required: true })),
        e("button", { className: "btn btn-primary w-100", type: "submit" }, "Dang nhap")
      )
    )));
  }

  function UploadVideoApp() {
    const cfg = window.HRMS_UPLOAD_VIDEO_CONFIG || {};
    return e("div", { className: "container py-5" }, e("div", { className: "card shadow-sm mx-auto", style: { maxWidth: "720px" } }, e("div", { className: "card-body p-4" },
      e("h4", null, "Upload video dao tao"),
      e("form", { action: "/videos/upload", method: "post", encType: "multipart/form-data" },
        e("input", { type: "file", className: "form-control mb-3", name: "file", accept: "video/*", required: true }),
        e("button", { className: "btn btn-primary", type: "submit" }, "Bat dau tai len")
      ),
      cfg.message ? e("div", { className: "alert alert-info mt-3" }, cfg.message) : null,
      cfg.videoUrl ? e("div", null, e("video", { controls: true, className: "w-100 rounded", src: cfg.videoUrl }), e("a", { href: cfg.videoUrl, target: "_blank" }, cfg.videoUrl)) : null
    )));
  }

  function UserAnnouncementsApp() {
    const cfg = window.HRMS_USER_ANNOUNCEMENTS_CONFIG || { announcements: [] };
    return e("div", { className: "container py-4" }, e("h3", { className: "mb-3" }, "Thong bao noi bo (React)"),
      cfg.announcements.length ? cfg.announcements.map((a) => e("a", { key: a.id, href: `/user1/announcements/${a.id}`, className: "card shadow-sm border-0 mb-2 text-decoration-none text-dark d-block" }, e("div", { className: "card-body d-flex justify-content-between" }, e("div", null, e("div", { className: "fw-bold" }, a.title), e("small", { className: "text-muted" }, `${a.priority} - ${a.publishedAt}`)), e("i", { className: "bi bi-chevron-right" })))) : e("div", { className: "alert alert-light" }, "Chua co thong bao")
    );
  }

  function UserAnnouncementDetailApp() {
    const a = (window.HRMS_USER_ANNOUNCEMENT_DETAIL_CONFIG || {}).announcement || {};
    return e("div", { className: "container py-4" }, e("a", { href: "/user1/announcements", className: "btn btn-link px-0" }, "Danh sach"), card(a.title || "Chi tiet thong bao", e("div", null,
      e("div", { className: "text-muted small mb-2" }, `${a.priority || ""} - ${a.publishedAt || ""}`),
      e("div", { style: { whiteSpace: "pre-wrap" } }, a.content || "")
    )));
  }

  function ContractListApp() {
    const cfg = window.HRMS_CONTRACT_LIST_CONFIG || { contracts: [] };
    return e("div", { className: "card shadow-sm border-0" },
      e("div", { className: "card-header bg-white d-flex justify-content-between align-items-center py-3" },
        e("h5", { className: "text-primary mb-0 fw-bold" }, "Danh sach hop dong"),
        e("a", { href: "/admin/contracts/add", className: "btn btn-primary" }, "Tao hop dong moi")
      ),
      e("div", { className: "card-body p-0" }, e("table", { className: "table table-hover align-middle mb-0 text-center" },
        e("thead", { className: "table-light" }, e("tr", null, ["So HD", "Nhan vien", "Loai", "Ngay bat dau", "Ngay ket thuc", "Luong co ban", "Thao tac"].map((h) => e("th", { key: h }, h)))),
        e("tbody", null, cfg.contracts.map((c) => e("tr", { key: c.id }, e("td", null, c.id), e("td", null, c.userName), e("td", null, c.contractType), e("td", null, c.signDate), e("td", null, c.expiryDate), e("td", null, c.salary), e("td", null, e("a", { className: "btn btn-sm btn-outline-warning", href: `/admin/contracts/edit/${c.id}` }, "Sua")))))
      ))
    );
  }

  function UserAttendanceApp() {
    const cfg = window.HRMS_USER_ATTENDANCE_CONFIG || { attendances: [], todayRecord: null };
    const today = cfg.todayRecord;
    return e(
      "div",
      { className: "container py-4" },
      e("h3", { className: "mb-4" }, "Lich cham cong cua toi"),
      e(
        "div",
        { className: "card p-4 mb-4" },
        e("h5", null, `Hom nay: ${cfg.today || ""}`),
        !today
          ? e(
              "form",
              { action: "/user/attendance/checkin", method: "post" },
              e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }),
              e("button", { className: "btn btn-success" }, "Check-in")
            )
          : e(
              "div",
              null,
              e("div", null, `Gio vao: ${today.checkInTime || "--:--"}`),
              e("div", null, `Gio ra: ${today.checkOutTime || "--:--"}`),
              e("div", null, `Trang thai: ${today.status || ""}`),
              !today.checkOutTime
                ? e(
                    "form",
                    { action: "/user/attendance/checkout", method: "post", className: "mt-2" },
                    e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }),
                    e("button", { className: "btn btn-warning" }, "Check-out")
                  )
                : null
            )
      ),
      card(
        "Lich su thang nay",
        e(
          "div",
          null,
          e(
            "form",
            { action: "/user/attendance", method: "get", className: "d-flex gap-2 mb-3" },
            e(
              "select",
              { name: "month", className: "form-select form-select-sm", defaultValue: cfg.month || "" },
              ["", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map((m) => e("option", { key: `m-${m}`, value: m }, m === "" ? "Thang" : `Thang ${m}`))
            ),
            e("input", { type: "number", name: "year", className: "form-control form-control-sm", defaultValue: cfg.year || "", placeholder: "Nam", style: { maxWidth: "120px" } }),
            e("button", { type: "submit", className: "btn btn-outline-primary btn-sm" }, "Xem")
          ),
          e(
            "div",
            { className: "table-responsive" },
            e(
              "table",
              { className: "table table-hover mb-0" },
              e("thead", { className: "table-light" }, e("tr", null, ["Ngay", "Check-in", "Check-out", "Trang thai", "Ghi chu"].map((h) => e("th", { key: h }, h)))),
              e(
                "tbody",
                null,
                cfg.attendances.length
                  ? cfg.attendances.map((a, i) =>
                      e("tr", { key: i }, e("td", null, a.date), e("td", null, a.checkInTime || "--:--"), e("td", null, a.checkOutTime || "--:--"), e("td", null, a.status), e("td", null, a.note || ""))
                    )
                  : e("tr", null, e("td", { colSpan: 5, className: "text-center text-muted py-3" }, "Chua co du lieu"))
              )
            )
          )
        )
      )
    );
  }

  function UserLeaveRequestApp() {
    const cfg = window.HRMS_USER_LEAVE_CONFIG || { myLeaves: [] };
    const [startDate, setStartDate] = React.useState("");
    const [endDate, setEndDate] = React.useState("");
    const [leaveType, setLeaveType] = React.useState("");
    const dayDiff = React.useMemo(() => {
      if (!startDate || !endDate) return null;
      const diff = Math.round((new Date(endDate) - new Date(startDate)) / 86400000) + 1;
      return diff > 0 ? diff : null;
    }, [startDate, endDate]);
    const chooseType = (type) => setLeaveType(type);
    return e(
      "div",
      { className: "container py-4" },
      e("h3", { className: "mb-3" }, "Don nghi phep"),
      e(
        "div",
        { className: "row g-4" },
        e(
          "div",
          { className: "col-lg-4" },
          card(
            "Nop don xin nghi",
            e(
              "form",
              { action: "/user/leaves/submit", method: "post" },
              e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }),
              e(
                "div",
                { className: "d-flex flex-wrap gap-2 mb-2" },
                [["ANNUAL", "Phep nam"], ["SICK", "Om benh"], ["UNPAID", "Khong luong"], ["MATERNITY", "Thai san"]].map(([val, label]) =>
                  e("button", { key: val, type: "button", className: `btn btn-sm ${leaveType === val ? "btn-primary" : "btn-outline-secondary"}`, onClick: () => chooseType(val) }, label)
                )
              ),
              e(
                "select",
                { className: "form-select mb-2", name: "leaveType", required: true, value: leaveType, onChange: (ev) => setLeaveType(ev.target.value) },
                e("option", { value: "" }, "-- Chon loai nghi --"),
                ["ANNUAL", "SICK", "UNPAID", "MATERNITY"].map((t) => e("option", { key: t, value: t }, t))
              ),
              e("input", { className: "form-control mb-2", type: "date", name: "startDate", required: true, value: startDate, onChange: (ev) => setStartDate(ev.target.value) }),
              e("input", { className: "form-control mb-2", type: "date", name: "endDate", required: true, value: endDate, onChange: (ev) => setEndDate(ev.target.value) }),
              dayDiff ? e("div", { className: "alert alert-info py-2 small mb-2" }, `Tong cong: ${dayDiff} ngay`) : null,
              e("textarea", { className: "form-control mb-2", rows: 3, name: "reason", placeholder: "Ly do", required: true }),
              e("button", { className: "btn btn-primary w-100", type: "submit" }, "Gui don")
            )
          )
        ),
        e(
          "div",
          { className: "col-lg-8" },
          card(
            "Lich su don nghi",
            e(
              "div",
              { className: "table-responsive" },
              e(
                "table",
                { className: "table table-hover mb-0" },
                e("thead", { className: "table-light" }, e("tr", null, ["Loai", "Tu ngay", "Den ngay", "So ngay", "Ly do", "Trang thai"].map((h) => e("th", { key: h }, h)))),
                e(
                  "tbody",
                  null,
                  cfg.myLeaves.length
                    ? cfg.myLeaves.map((lr, i) => e("tr", { key: i }, e("td", null, lr.leaveType), e("td", null, lr.startDate), e("td", null, lr.endDate), e("td", null, lr.days), e("td", null, lr.reason), e("td", null, lr.status)))
                    : e("tr", null, e("td", { colSpan: 6, className: "text-center text-muted py-3" }, "Chua co don"))
                )
              )
            )
          )
        )
      )
    );
  }

  function UserListApp() {
    const cfg = window.HRMS_USER_LIST_CONFIG || { users: [] };
    return e(
      "div",
      { className: "card p-4" },
      e(
        "table",
        { className: "table table-hover" },
        e("thead", null, e("tr", null, ["ID", "Ten dang nhap", "Ho ten", "Email", "Quyen", "Hanh dong"].map((h) => e("th", { key: h }, h)))),
        e(
          "tbody",
          null,
          cfg.users.map((u) =>
            e("tr", { key: u.id }, e("td", null, u.id), e("td", null, u.username), e("td", null, u.fullName), e("td", null, u.email), e("td", null, e("span", { className: `badge ${u.role === "ADMIN" ? "bg-danger" : "bg-primary"}` }, u.role)), e("td", null, e("a", { href: `/admin/user/edit/${u.id}`, className: "btn btn-sm btn-info text-white me-1" }, "Sua"), e("a", { href: `/admin/user/delete/${u.id}`, className: "btn btn-sm btn-danger" }, "Xoa")))
          )
        )
      )
    );
  }

  function UserMyTasksApp() {
    const cfg = window.HRMS_USER_TASKS_CONFIG || { myTasks: [] };
    const filters = [
      { key: "", label: "Tat ca", href: "/user1/tasks" },
      { key: "PENDING", label: "Cho xu ly", href: "/user1/tasks?filter=PENDING" },
      { key: "IN_PROGRESS", label: "Dang lam", href: "/user1/tasks?filter=IN_PROGRESS" },
      { key: "COMPLETED", label: "Hoan thanh", href: "/user1/tasks?filter=COMPLETED" },
    ];
    return e("div", { className: "container py-4" },
      e("h3", { className: "mb-3" }, "Cong viec cua toi"),
      e("div", { className: "d-flex gap-2 flex-wrap mb-3" }, filters.map((f) =>
        e("a", { key: f.label, href: f.href, className: `btn btn-sm ${cfg.filter === f.key || (!cfg.filter && f.key === "") ? "btn-primary" : "btn-outline-secondary"}` }, f.label)
      )),
      e("div", { className: "row g-2 mb-3" },
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Tong"), e("div", { className: "fw-bold" }, cfg.totalTasks || 0)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Cho xu ly"), e("div", { className: "fw-bold text-warning" }, cfg.pendingCount || 0)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Dang lam"), e("div", { className: "fw-bold text-primary" }, cfg.inProgressCount || 0)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Hoan thanh"), e("div", { className: "fw-bold text-success" }, cfg.doneCount || 0))))
      ),
      cfg.myTasks.length ? cfg.myTasks.map((a) => e("div", { key: a.id, className: "card mb-2" }, e("div", { className: "card-body d-flex justify-content-between align-items-start" },
        e("div", null, e("div", { className: "fw-bold" }, a.taskName), e("div", { className: "text-muted small" }, a.description || ""), e("div", { className: "small text-secondary mt-1" }, `Loai: ${a.taskType || ""} - Ngay giao: ${a.assignedDate || ""}${a.actualHours != null ? ` - Gio thuc te: ${a.actualHours}h` : ""}`)),
        e("div", null, e("span", { className: "badge bg-secondary mb-2 d-block" }, a.status), (a.status !== "COMPLETED" && a.status !== "CANCELED") ? e("form", { action: `/user1/tasks/${a.id}/update-status`, method: "post" }, e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }), e("select", { name: "status", className: "form-select form-select-sm", onChange: (ev) => ev.target.form.submit() }, e("option", { value: "" }, "Cap nhat"), e("option", { value: "IN_PROGRESS" }, "Dang lam"), e("option", { value: "COMPLETED" }, "Hoan thanh"))) : null)
      ))) : e("div", { className: "alert alert-light" }, "Khong co cong viec nao")
    );
  }

  function UserNotificationsApp() {
    const cfg = window.HRMS_USER_NOTIFICATIONS_CONFIG || { notifications: [] };
    return e("div", { className: "container py-4" },
      e("div", { className: "d-flex justify-content-between mb-3" }, e("h3", null, "Thong bao"), cfg.unreadCount > 0 ? e("form", { action: "/notifications/mark-all-read", method: "post" }, e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }), e("button", { className: "btn btn-primary btn-sm" }, "Danh dau da doc")) : null),
      cfg.notifications.length ? cfg.notifications.map((n, i) => e("div", { key: i, className: `card mb-2 ${n.read ? "" : "border-primary"}` }, e("div", { className: "card-body" }, e("div", { className: "fw-semibold" }, n.message), e("div", { className: "small text-muted" }, n.createdAt), n.link ? e("a", { href: n.link }, "Xem chi tiet") : null))) : e("div", { className: "alert alert-light" }, "Chua co thong bao")
    );
  }

  function UserPayrollApp() {
    const cfg = window.HRMS_USER_PAYROLL_CONFIG || { payrolls: [] };
    return e("div", { className: "container py-4" }, e("h3", { className: "mb-3" }, "Phieu luong cua toi"),
      cfg.payrolls.length ? e("div", { className: "card mb-3" }, e("div", { className: "card-body d-flex flex-wrap gap-4 align-items-center" },
        e("div", null, e("div", { className: "small text-muted" }, "Tong phieu luong"), e("div", { className: "fw-bold fs-5" }, cfg.payrolls.length)),
        e("div", null, e("div", { className: "small text-muted" }, "Da thanh toan"), e("div", { className: "fw-bold fs-5 text-success" }, cfg.totalPaid || 0)),
        e("div", null, e("div", { className: "small text-muted" }, "Luong gan nhat"), e("div", { className: "fw-bold fs-5 text-primary" }, cfg.latestNetSalary || "N/A")),
        e("div", { className: "ms-auto small text-muted" }, cfg.userName || "", cfg.department ? ` - ${cfg.department}` : "")
      )) : null,
      cfg.payrolls.length ? e("div", { className: "row g-3" }, cfg.payrolls.map((p, i) => e("div", { key: i, className: "col-md-6 col-xl-4" }, e("div", { className: "card h-100" }, e("div", { className: "card-header fw-bold" }, `Thang ${p.month}/${p.year}`), e("div", { className: "card-body" }, e("div", null, `Luong co ban: ${p.baseSalary}`), e("div", null, `Thuong: ${p.bonus}`), e("div", null, `Khau tru: ${p.deductions}`), e("div", { className: "fw-bold mt-2 text-success" }, `Thuc nhan: ${p.netSalary}`), e("div", { className: "small text-muted" }, `Trang thai: ${p.paymentStatus}`)))))) : e("div", { className: "alert alert-light" }, "Chua co phieu luong")
    );
  }

  function UserProfileApp() {
    const cfg = window.HRMS_USER_PROFILE_CONFIG || { user: {} };
    const u = cfg.user || {};
    const [avatarPreview, setAvatarPreview] = React.useState(null);
    const onAvatarChange = (ev) => {
      const file = ev.target.files && ev.target.files[0];
      if (!file) {
        setAvatarPreview(null);
        return;
      }
      const reader = new FileReader();
      reader.onload = () => setAvatarPreview(reader.result);
      reader.readAsDataURL(file);
    };
    return e("div", { className: "container py-4" },
      e("h3", { className: "mb-3" }, "Ho so ca nhan"),
      cfg.successMsg ? e("div", { className: "alert alert-success" }, cfg.successMsg) : null,
      e("div", { className: "row g-2 mb-3" },
        e("div", { className: "col-4" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Ngay cong"), e("div", { className: "fw-bold" }, cfg.attendanceDays || 0)))),
        e("div", { className: "col-4" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Don nghi"), e("div", { className: "fw-bold" }, cfg.totalLeaves || 0)))),
        e("div", { className: "col-4" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Cong viec"), e("div", { className: "fw-bold" }, cfg.totalTasks || 0))))
      ),
      card("Thong tin", e("div", { className: "row g-2 small" },
        e("div", { className: "col-md-6" }, `Username: ${u.username || ""}`),
        e("div", { className: "col-md-6" }, `Ho ten: ${u.fullName || ""}`),
        e("div", { className: "col-md-6" }, `Email: ${u.email || ""}`),
        e("div", { className: "col-md-6" }, `Phong ban: ${u.department || ""}`),
        e("div", { className: "col-md-6" }, `Chuc vu: ${u.position || ""}`),
        e("div", { className: "col-md-6" }, `Vai tro: ${u.role || ""}`)
      )),
      e("div", { className: "row g-3" },
        e("div", { className: "col-lg-6" }, card("Cap nhat avatar", e("form", { action: "/user1/profile/update-avatar", method: "post", encType: "multipart/form-data" }, e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }), e("input", { className: "form-control mb-2", type: "file", name: "image", accept: "image/*", onChange: onAvatarChange }), avatarPreview ? e("div", { className: "mb-2 text-center" }, e("img", { src: avatarPreview, alt: "Preview", style: { width: "96px", height: "96px", borderRadius: "50%", objectFit: "cover", border: "2px solid #6366f1" } })) : null, e("button", { className: "btn btn-primary", type: "submit" }, "Luu avatar")))),
        e("div", { className: "col-lg-6" }, card("Doi mat khau", e("form", { action: "/user1/profile/change-password", method: "post" }, e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }), e("input", { className: "form-control mb-2", type: "password", name: "currentPassword", placeholder: "Mat khau hien tai", required: true }), e("input", { className: "form-control mb-2", type: "password", name: "newPassword", placeholder: "Mat khau moi", required: true }), e("input", { className: "form-control mb-2", type: "password", name: "confirmPassword", placeholder: "Xac nhan", required: true }), cfg.passwordError ? e("div", { className: "alert alert-danger py-2 small" }, cfg.passwordError) : null, e("button", { className: "btn btn-primary", type: "submit" }, "Doi mat khau"))))
      )
    );
  }

  function UserReviewsApp() {
    const cfg = window.HRMS_USER_REVIEWS_CONFIG || { reviews: [] };
    const avgScore = cfg.reviews.length ? (cfg.reviews.reduce((sum, r) => sum + (Number(r.overallScore) || 0), 0) / cfg.reviews.length).toFixed(1) : 0;
    const maxScore = cfg.reviews.length ? Math.max(...cfg.reviews.map((r) => Number(r.overallScore) || 0)) : 0;
    const approvedCount = cfg.reviews.filter((r) => r.status === "APPROVED").length;
    return e("div", { className: "container py-4" },
      e("h3", { className: "mb-3" }, "Danh gia KPI cua toi"),
      cfg.reviews.length ? e("div", { className: "row g-2 mb-3" },
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Tong ky"), e("div", { className: "fw-bold" }, cfg.reviews.length)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Trung binh"), e("div", { className: "fw-bold text-primary" }, avgScore)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Cao nhat"), e("div", { className: "fw-bold text-success" }, maxScore)))),
        e("div", { className: "col-6 col-md-3" }, e("div", { className: "card text-center" }, e("div", { className: "card-body py-2" }, e("div", { className: "small text-muted" }, "Da duyet"), e("div", { className: "fw-bold" }, approvedCount))))
      ) : null,
      cfg.reviews.length ? cfg.reviews.map((r, i) => e("div", { key: i, className: "card mb-2" }, e("div", { className: "card-body" },
        e("div", { className: "d-flex justify-content-between" }, e("div", null, e("div", { className: "fw-bold" }, r.reviewPeriod), e("small", { className: "text-muted" }, r.reviewDate)), e("span", { className: "badge bg-secondary" }, r.status)),
        e("div", { className: "mt-2 small" }, `KPI ${r.kpiScore} - Thai do ${r.attitudeScore} - Team ${r.teamworkScore}`),
        e("div", { className: "fw-semibold mt-1" }, `Tong: ${r.overallScore} (${r.ratingLabel || ""})`),
        r.strengths ? e("div", { className: "small mt-1" }, `Diem manh: ${r.strengths}`) : null,
        r.improvements ? e("div", { className: "small" }, `Can cai thien: ${r.improvements}`) : null,
        r.reviewerName ? e("div", { className: "small text-muted mt-1" }, `Danh gia boi: ${r.reviewerName}`) : null
      ))) : e("div", { className: "alert alert-light" }, "Chua co danh gia nao")
    );
  }

  function UserChatbotApp() {
    const cfg = window.HRMS_CHATBOT_CONFIG || {};
    const [messages, setMessages] = React.useState((cfg.history || []).length ? cfg.history.flatMap((m) => [{ role: "user", text: m.userQuery }, { role: "bot", text: m.botResponse, intent: m.intent }]) : [{ role: "bot", text: "Xin chao! Ban can ho tro gi hom nay?" }]);
    const [input, setInput] = React.useState("");
    const [lastMessageId, setLastMessageId] = React.useState(null);
    const [sessionId, setSessionId] = React.useState(() => {
      const k = "hrms_chatbot_session";
      const old = sessionStorage.getItem(k);
      if (old) return old;
      const n = (window.crypto && crypto.randomUUID) ? crypto.randomUUID() : String(Date.now());
      sessionStorage.setItem(k, n);
      return n;
    });
    const readXsrfCookie = () => {
      const name = "XSRF-TOKEN=";
      for (const part of document.cookie.split(";")) {
        const c = part.trim();
        if (c.startsWith(name)) return decodeURIComponent(c.substring(name.length));
      }
      return null;
    };
    const csrfHeaders = () => {
      const h = {};
      const xsrf = readXsrfCookie();
      if (xsrf) {
        h["X-XSRF-TOKEN"] = xsrf;
      } else {
        const tokenMeta = document.querySelector('meta[name="_csrf"]');
        const headerMeta = document.querySelector('meta[name="_csrf_header"]');
        if (tokenMeta && headerMeta) h[headerMeta.getAttribute("content")] = tokenMeta.getAttribute("content");
      }
      return h;
    };
    const send = async (ev) => {
      ev.preventDefault();
      const text = input.trim();
      if (!text) return;
      setInput("");
      setMessages((m) => [...m, { role: "user", text }]);
      try {
        const r = await fetch("/user1/chatbot/api/message", {
          method: "POST",
          credentials: "same-origin",
          headers: Object.assign({ "Content-Type": "application/json" }, csrfHeaders()),
          body: JSON.stringify({ message: text, sessionId }),
        });
        const data = await r.json();
        if (data.sessionId) {
          setSessionId(data.sessionId);
          sessionStorage.setItem("hrms_chatbot_session", data.sessionId);
        }
        setMessages((m) => [...m, { role: "bot", text: data.reply, intent: data.intent }]);
        setLastMessageId(data.messageId || null);
      } catch (_e) {
        setMessages((m) => [...m, { role: "bot", text: "Khong gui duoc tin nhan. Thu lai sau." }]);
      }
    };
    const rate = async (rating) => {
      if (!lastMessageId) return;
      await fetch("/user1/chatbot/api/rate", {
        method: "POST",
        credentials: "same-origin",
        headers: Object.assign({ "Content-Type": "application/json" }, csrfHeaders()),
        body: JSON.stringify({ messageId: lastMessageId, rating }),
      });
      setLastMessageId(null);
    };
    return e("div", { className: "container py-4", style: { maxWidth: "760px" } },
      e("div", { className: "d-flex justify-content-between mb-2" }, e("a", { href: "/user1/dashboard", className: "text-decoration-none" }, "Ve Dashboard"), e("small", { className: "text-muted" }, cfg.currentUser || "")),
      e("div", { className: "card shadow-sm" },
        e("div", { className: "card-header" }, e("strong", null, "Tro ly HR noi bo")),
        e("div", { className: "card-body", style: { height: "420px", overflowY: "auto", background: "#f8fafc" } },
          messages.map((m, i) => e("div", { key: i, className: `mb-2 d-flex ${m.role === "user" ? "justify-content-end" : "justify-content-start"}` },
            e("div", { className: `p-2 rounded ${m.role === "user" ? "text-bg-primary" : "bg-white border"}`, style: { maxWidth: "85%", whiteSpace: "pre-wrap" } }, m.text, m.intent ? e("div", { className: "small text-muted mt-1" }, `#${m.intent}`) : null)
          ))
        ),
        e("div", { className: "card-footer" },
          e("form", { className: "d-flex gap-2", onSubmit: send },
            e("input", { className: "form-control", value: input, onChange: (ev) => setInput(ev.target.value), placeholder: "Nhap cau hoi..." }),
            e("button", { className: "btn btn-primary", type: "submit" }, "Gui")
          ),
          lastMessageId ? e("div", { className: "small mt-2" }, "Danh gia cau tra loi: ", [1, 2, 3, 4, 5].map((s) => e("button", { key: s, type: "button", className: "btn btn-link btn-sm p-0 ms-1 text-warning text-decoration-none", onClick: () => rate(s) }, "★"))) : null
        )
      )
    );
  }

  const mount = (id, app) => {
    const el = document.getElementById(id);
    if (el) ReactDOM.createRoot(el).render(e(app));
  };

  if (window.HRMS_AUDIT_LOG_PAGE) mount("audit-log-root", AuditLogApp);
  if (window.HRMS_PAYMENT_LIST_PAGE) mount("payment-list-root", PaymentListApp);
  if (window.HRMS_PAYMENT_CREATE_PAGE) mount("payment-create-root", PaymentCreateApp);
  if (window.HRMS_PAYMENT_DETAIL_PAGE) mount("payment-detail-root", PaymentDetailApp);
  if (window.HRMS_REPORT_PAGE) mount("report-root", ReportDashboardApp);
  if (window.HRMS_PAYMENT_RESULT_PAGE) mount("payment-result-root", PaymentResultApp);
  if (window.HRMS_LOGIN_PAGE) mount("login-root", LoginApp);
  if (window.HRMS_UPLOAD_VIDEO_PAGE) mount("upload-video-root", UploadVideoApp);
  if (window.HRMS_USER_ANNOUNCEMENTS_PAGE) mount("user-announcements-root", UserAnnouncementsApp);
  if (window.HRMS_USER_ANNOUNCEMENT_DETAIL_PAGE) mount("user-announcement-detail-root", UserAnnouncementDetailApp);
  if (window.HRMS_CONTRACT_LIST_PAGE) mount("contract-list-root", ContractListApp);
  if (window.HRMS_USER_ATTENDANCE_PAGE) mount("user-attendance-root", UserAttendanceApp);
  if (window.HRMS_USER_LEAVE_PAGE) mount("user-leave-root", UserLeaveRequestApp);
  if (window.HRMS_USER_LIST_PAGE) mount("user-list-root", UserListApp);
  if (window.HRMS_USER_TASKS_PAGE) mount("user-tasks-root", UserMyTasksApp);
  if (window.HRMS_USER_NOTIFICATIONS_PAGE) mount("user-notifications-root", UserNotificationsApp);
  if (window.HRMS_USER_PAYROLL_PAGE) mount("user-payroll-root", UserPayrollApp);
  if (window.HRMS_USER_PROFILE_PAGE) mount("user-profile-root", UserProfileApp);
  if (window.HRMS_USER_REVIEWS_PAGE) mount("user-reviews-root", UserReviewsApp);
  if (window.HRMS_CHATBOT_PAGE) mount("user-chatbot-root", UserChatbotApp);
})();

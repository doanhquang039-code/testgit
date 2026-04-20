(function () {
  const e = React.createElement;
  const mount = (id, app) => {
    const el = document.getElementById(id);
    if (el) ReactDOM.createRoot(el).render(e(app));
  };

  function TableCard({ title, headers, rows, emptyText }) {
    return e("div", { className: "card border-0 shadow-sm" },
      e("div", { className: "card-header bg-white fw-bold" }, title),
      e("div", { className: "table-responsive" },
        e("table", { className: "table table-hover align-middle mb-0" },
          e("thead", { className: "bg-light" }, e("tr", null, headers.map((h) => e("th", { key: h }, h)))),
          e("tbody", null, rows.length ? rows : e("tr", null, e("td", { colSpan: headers.length, className: "text-center py-4 text-muted" }, emptyText || "Khong co du lieu")))
        )
      )
    );
  }

  function ReviewListApp() {
    const cfg = window.HRMS_REVIEW_LIST_CONFIG || { reviews: [] };
    const rows = cfg.reviews.map((r) =>
      e("tr", { key: r.id },
        e("td", null, r.userName),
        e("td", null, r.reviewPeriod),
        e("td", null, r.kpiScore),
        e("td", null, r.attitudeScore),
        e("td", null, r.teamworkScore),
        e("td", null, r.overallScore),
        e("td", null, e("span", { className: "badge bg-secondary" }, r.status)),
        e("td", null, e("a", { className: "btn btn-sm btn-outline-warning me-1", href: `/admin/reviews/edit/${r.id}` }, "Sua"), e("a", { className: "btn btn-sm btn-outline-danger", href: `/admin/reviews/delete/${r.id}` }, "Xoa"))
      )
    );
    return e("div", { className: "container py-4" },
      e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Danh gia KPI (React)"), e("a", { href: "/admin/reviews/add", className: "btn btn-primary" }, "Them danh gia")),
      e(TableCard, { title: "Danh sach danh gia", headers: ["Nhan vien", "Ky", "KPI", "Thai do", "Teamwork", "Tong", "Trang thai", "Hanh dong"], rows, emptyText: "Chua co danh gia" })
    );
  }

  function ReviewFormApp() {
    const cfg = window.HRMS_REVIEW_FORM_CONFIG || { review: {}, users: [], periods: [], statuses: [] };
    const [f, setF] = React.useState({
      id: cfg.review.id || "",
      userId: cfg.review.userId || "",
      reviewPeriod: cfg.review.reviewPeriod || "",
      reviewDate: cfg.review.reviewDate || "",
      status: cfg.review.status || "",
      kpiScore: cfg.review.kpiScore || 0,
      attitudeScore: cfg.review.attitudeScore || 0,
      teamworkScore: cfg.review.teamworkScore || 0,
      strengths: cfg.review.strengths || "",
      improvements: cfg.review.improvements || "",
      comments: cfg.review.comments || "",
    });
    const overall = ((Number(f.kpiScore || 0) + Number(f.attitudeScore || 0) + Number(f.teamworkScore || 0)) / 3).toFixed(1);
    return e("form", { action: "/admin/reviews/save", method: "post", className: "container py-4" },
      e("input", { type: "hidden", name: "id", value: f.id }),
      e("div", { className: "card border-0 shadow-sm" },
        e("div", { className: "card-header fw-bold" }, "Form danh gia KPI (React)"),
        e("div", { className: "card-body row g-3" },
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Nhan vien"), e("select", { className: "form-select", name: "user.id", value: f.userId, onChange: (ev) => setF({ ...f, userId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.users.map((u) => e("option", { key: u.id, value: u.id }, u.name)))),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Ky danh gia"), e("select", { className: "form-select", name: "reviewPeriod", value: f.reviewPeriod, onChange: (ev) => setF({ ...f, reviewPeriod: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.periods.map((p) => e("option", { key: p, value: p }, p)))),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Ngay danh gia"), e("input", { className: "form-control", type: "date", name: "reviewDate", value: f.reviewDate, onChange: (ev) => setF({ ...f, reviewDate: ev.target.value }) })),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Trang thai"), e("select", { className: "form-select", name: "status", value: f.status, onChange: (ev) => setF({ ...f, status: ev.target.value }) }, cfg.statuses.map((s) => e("option", { key: s, value: s }, s)))),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "KPI"), e("input", { className: "form-control", type: "number", min: 0, max: 100, name: "kpiScore", value: f.kpiScore, onChange: (ev) => setF({ ...f, kpiScore: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Thai do"), e("input", { className: "form-control", type: "number", min: 0, max: 100, name: "attitudeScore", value: f.attitudeScore, onChange: (ev) => setF({ ...f, attitudeScore: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Teamwork"), e("input", { className: "form-control", type: "number", min: 0, max: 100, name: "teamworkScore", value: f.teamworkScore, onChange: (ev) => setF({ ...f, teamworkScore: ev.target.value }) })),
          e("div", { className: "col-12" }, e("div", { className: "fw-bold text-primary" }, `Tong diem: ${overall}`)),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Diem manh"), e("textarea", { className: "form-control", rows: 2, name: "strengths", value: f.strengths, onChange: (ev) => setF({ ...f, strengths: ev.target.value }) })),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Can cai thien"), e("textarea", { className: "form-control", rows: 2, name: "improvements", value: f.improvements, onChange: (ev) => setF({ ...f, improvements: ev.target.value }) })),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Ghi chu"), e("textarea", { className: "form-control", rows: 2, name: "comments", value: f.comments, onChange: (ev) => setF({ ...f, comments: ev.target.value }) })),
          e("div", { className: "col-12 d-flex gap-2" }, e("button", { className: "btn btn-primary", type: "submit" }, "Luu"), e("a", { href: "/admin/reviews", className: "btn btn-outline-secondary" }, "Quay lai"))
        )
      )
    );
  }

  function PayrollListApp() {
    const cfg = window.HRMS_PAYROLL_LIST_CONFIG || { payrolls: [] };
    const rows = cfg.payrolls.map((p) =>
      e("tr", { key: p.id }, e("td", null, p.userName), e("td", null, `${p.month}/${p.year}`), e("td", null, p.baseSalary), e("td", null, p.bonus), e("td", null, p.deductions), e("td", null, p.netSalary), e("td", null, e("span", { className: "badge bg-secondary" }, p.paymentStatus)), e("td", null, e("a", { href: `/admin/payroll/delete/${p.id}`, className: "btn btn-sm btn-outline-danger" }, "Xoa")))
    );
    return e("div", { className: "container py-4" }, e("h4", { className: "mb-3" }, "Bang luong (React)"), e(TableCard, { title: "Danh sach luong", headers: ["Nhan vien", "Thang", "Luong co ban", "Thuong", "Khau tru", "Thuc nhan", "Trang thai", "Thao tac"], rows, emptyText: "Chua co du lieu" }));
  }

  function ContractListApp() {
    const cfg = window.HRMS_CONTRACT_LIST_CONFIG || { contracts: [] };
    const rows = cfg.contracts.map((c) => e("tr", { key: c.id }, e("td", null, c.id), e("td", null, c.userName), e("td", null, c.contractType), e("td", null, c.signDate), e("td", null, c.expiryDate), e("td", null, c.salary), e("td", null, e("a", { className: "btn btn-sm btn-outline-warning", href: `/admin/contracts/edit/${c.id}` }, "Sua"))));
    return e("div", { className: "container py-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Hop dong (React)"), e("a", { href: "/admin/contracts/add", className: "btn btn-primary" }, "Tao hop dong")), e(TableCard, { title: "Danh sach hop dong", headers: ["So HD", "Nhan vien", "Loai", "Bat dau", "Ket thuc", "Luong", "Thao tac"], rows, emptyText: "Chua co hop dong" }));
  }

  function ContractFormApp() {
    const cfg = window.HRMS_CONTRACT_FORM_CONFIG || { contract: {}, users: [] };
    const [f, setF] = React.useState({
      id: cfg.contract.id || "",
      userId: cfg.contract.userId || "",
      contractType: cfg.contract.contractType || "",
      signDate: cfg.contract.signDate || "",
      expiryDate: cfg.contract.expiryDate || "",
      baseSalaryOnContract: cfg.contract.baseSalaryOnContract || "",
    });
    return e("form", { action: "/admin/contracts/save", method: "post", className: "container py-4" },
      e("input", { type: "hidden", name: "id", value: f.id }),
      e("div", { className: "card border-0 shadow-sm" }, e("div", { className: "card-header fw-bold" }, "Form hop dong (React)"),
        e("div", { className: "card-body row g-3" },
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Nhan vien"), e("select", { className: "form-select", name: "user.id", value: f.userId, onChange: (ev) => setF({ ...f, userId: ev.target.value }) }, e("option", { value: "" }, "-- Chon --"), cfg.users.map((u) => e("option", { key: u.id, value: u.id }, u.name)))),
          e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "Loai hop dong"), e("input", { className: "form-control", name: "contractType", value: f.contractType, onChange: (ev) => setF({ ...f, contractType: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Ngay ky"), e("input", { type: "date", className: "form-control", name: "signDate", value: f.signDate, onChange: (ev) => setF({ ...f, signDate: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Ngay het han"), e("input", { type: "date", className: "form-control", name: "expiryDate", value: f.expiryDate, onChange: (ev) => setF({ ...f, expiryDate: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Luong co ban"), e("input", { type: "number", className: "form-control", name: "baseSalaryOnContract", value: f.baseSalaryOnContract, onChange: (ev) => setF({ ...f, baseSalaryOnContract: ev.target.value }) })),
          e("div", { className: "col-12 d-flex gap-2" }, e("button", { className: "btn btn-primary", type: "submit" }, "Luu"), e("a", { href: "/admin/contracts", className: "btn btn-outline-secondary" }, "Quay lai"))
        )
      )
    );
  }

  function AnnouncementListApp() {
    const cfg = window.HRMS_ANNOUNCEMENT_LIST_CONFIG || { announcements: [] };
    const rows = cfg.announcements.map((a) => e("tr", { key: a.id }, e("td", null, a.title), e("td", null, a.target), e("td", null, a.priority), e("td", null, a.publishedAt), e("td", null, a.active ? "Bat" : "Tat"), e("td", null, e("a", { className: "btn btn-sm btn-outline-primary me-1", href: `/admin/announcements/${a.id}/edit` }, "Sua"), e("form", { action: `/admin/announcements/${a.id}/delete`, method: "post", className: "d-inline" }, e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }), e("button", { className: "btn btn-sm btn-outline-danger", type: "submit" }, "Xoa")))));
    return e("div", { className: "container py-4" }, e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Thong bao noi bo (React)"), e("a", { href: "/admin/announcements/create", className: "btn btn-primary" }, "Tao moi")), e(TableCard, { title: "Danh sach thong bao", headers: ["Tieu de", "Pham vi", "Uu tien", "Dang", "Hien thi", "Thao tac"], rows, emptyText: "Chua co du lieu" }));
  }

  function AnnouncementFormApp() {
    const cfg = window.HRMS_ANNOUNCEMENT_FORM_CONFIG || { announcement: {}, departments: [], priorities: [] };
    const [f, setF] = React.useState({
      id: cfg.announcement.id || "",
      title: cfg.announcement.title || "",
      content: cfg.announcement.content || "",
      departmentId: cfg.announcement.departmentId || "",
      priority: cfg.announcement.priority || "",
      publishedAt: cfg.announcement.publishedAt || "",
      active: cfg.announcement.active !== false,
    });
    return e("form", { action: "/admin/announcements", method: "post", className: "container py-4" },
      e("input", { type: "hidden", name: cfg.csrfName || "_csrf", value: cfg.csrfToken || "" }),
      e("input", { type: "hidden", name: "id", value: f.id }),
      e("div", { className: "card border-0 shadow-sm" }, e("div", { className: "card-header fw-bold" }, "Form thong bao (React)"),
        e("div", { className: "card-body row g-3" },
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Tieu de"), e("input", { className: "form-control", name: "title", value: f.title, onChange: (ev) => setF({ ...f, title: ev.target.value }) })),
          e("div", { className: "col-12" }, e("label", { className: "form-label" }, "Noi dung"), e("textarea", { className: "form-control", rows: 6, name: "content", value: f.content, onChange: (ev) => setF({ ...f, content: ev.target.value }) })),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Phong ban"), e("select", { className: "form-select", name: "departmentId", value: f.departmentId, onChange: (ev) => setF({ ...f, departmentId: ev.target.value }) }, e("option", { value: "" }, "Toan cong ty"), cfg.departments.map((d) => e("option", { key: d.id, value: d.id }, d.name)))),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Uu tien"), e("select", { className: "form-select", name: "priority", value: f.priority, onChange: (ev) => setF({ ...f, priority: ev.target.value }) }, cfg.priorities.map((p) => e("option", { key: p, value: p }, p)))),
          e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "Thoi diem dang"), e("input", { type: "datetime-local", className: "form-control", name: "publishedAt", value: f.publishedAt, onChange: (ev) => setF({ ...f, publishedAt: ev.target.value }) })),
          e("div", { className: "col-12 form-check" }, e("input", { className: "form-check-input", type: "checkbox", id: "active", checked: f.active, onChange: (ev) => setF({ ...f, active: ev.target.checked }) }), e("input", { type: "hidden", name: "active", value: f.active ? "true" : "false" }), e("label", { className: "form-check-label", htmlFor: "active" }, "Dang hien thi")),
          e("div", { className: "col-12 d-flex gap-2" }, e("button", { className: "btn btn-primary", type: "submit" }, "Luu"), e("a", { href: "/admin/announcements", className: "btn btn-outline-secondary" }, "Huy"))
        )
      )
    );
  }

  if (window.HRMS_REVIEW_LIST_PAGE) mount("review-list-root", ReviewListApp);
  if (window.HRMS_REVIEW_FORM_PAGE) mount("review-form-root", ReviewFormApp);
  if (window.HRMS_PAYROLL_LIST_PAGE) mount("payroll-list-root", PayrollListApp);
  if (window.HRMS_CONTRACT_LIST_PAGE) mount("contract-list-root", ContractListApp);
  if (window.HRMS_CONTRACT_FORM_PAGE) mount("contract-form-root", ContractFormApp);
  if (window.HRMS_ANNOUNCEMENT_LIST_PAGE) mount("announcement-list-root", AnnouncementListApp);
  if (window.HRMS_ANNOUNCEMENT_FORM_PAGE) mount("announcement-form-root", AnnouncementFormApp);
})();

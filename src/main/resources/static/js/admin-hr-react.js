(function () {
  const e = React.createElement;
  const mount = (id, app) => {
    const el = document.getElementById(id);
    if (el) ReactDOM.createRoot(el).render(e(app));
  };

  function DepartmentListApp() {
    const cfg = window.HRMS_DEPARTMENT_LIST_CONFIG || { departments: [] };
    return e(
      "div",
      { className: "card shadow-sm border-0" },
      e(
        "div",
        { className: "card-header bg-white d-flex justify-content-between align-items-center py-3" },
        e("h5", { className: "text-primary mb-0 fw-bold" }, "QUAN LY PHONG BAN (React)"),
        e("a", { href: "/admin/departments/add", className: "btn btn-primary" }, "Them phong ban")
      ),
      e(
        "div",
        { className: "card-body p-0" },
        e(
          "table",
          { className: "table table-hover align-middle mb-0" },
          e("thead", { className: "bg-light" }, e("tr", null, e("th", { className: "ps-4" }, "ID"), e("th", null, "Ten phong ban"), e("th", null, "So dien thoai"), e("th", { className: "text-center" }, "Thao tac"))),
          e(
            "tbody",
            null,
            (cfg.departments || []).map((d) =>
              e("tr", { key: d.id }, e("td", { className: "ps-4" }, d.id), e("td", { className: "fw-bold" }, d.departmentName), e("td", null, d.phoneNumber || ""), e("td", { className: "text-center" }, e("a", { href: `/admin/departments/edit/${d.id}`, className: "btn btn-sm btn-outline-warning me-2" }, "Sua"), e("a", { href: `/admin/departments/delete/${d.id}`, className: "btn btn-sm btn-outline-danger", onClick: (ev) => { if (!confirm("Xoa phong ban nay?")) ev.preventDefault(); } }, "Xoa")))
            )
          )
        )
      )
    );
  }

  function DepartmentFormApp() {
    const cfg = window.HRMS_DEPARTMENT_FORM_CONFIG || { department: {} };
    const [form, setForm] = React.useState({
      id: cfg.department.id || "",
      departmentName: cfg.department.departmentName || "",
      phoneNumber: cfg.department.phoneNumber || "",
    });
    return e(
      "div",
      { className: "container py-5" },
      e(
        "div",
        { className: "card shadow-sm border-0 mx-auto", style: { maxWidth: "600px" } },
        e("div", { className: "card-header bg-primary text-white py-3" }, e("h5", { className: "mb-0" }, form.id ? "CHINH SUA PHONG BAN (React)" : "THEM PHONG BAN MOI (React)")),
        e(
          "div",
          { className: "card-body p-4" },
          e(
            "form",
            { action: "/admin/departments/save", method: "post" },
            e("input", { type: "hidden", name: "id", value: form.id }),
            e("div", { className: "mb-3" }, e("label", { className: "form-label fw-bold" }, "Ten phong ban"), e("input", { className: "form-control", name: "departmentName", required: true, value: form.departmentName, onChange: (ev) => setForm({ ...form, departmentName: ev.target.value }) })),
            e("div", { className: "mb-3" }, e("label", { className: "form-label fw-bold" }, "So dien thoai"), e("input", { className: "form-control", name: "phoneNumber", value: form.phoneNumber, onChange: (ev) => setForm({ ...form, phoneNumber: ev.target.value }) })),
            e("div", { className: "d-flex justify-content-between pt-3" }, e("a", { href: "/admin/departments", className: "btn btn-outline-secondary" }, "Quay lai"), e("button", { type: "submit", className: "btn btn-primary px-4" }, "Luu phong ban"))
          )
        )
      )
    );
  }

  function PositionListApp() {
    const cfg = window.HRMS_POSITION_LIST_CONFIG || { positions: [] };
    return e(
      "div",
      { className: "card shadow-sm border-0" },
      e("div", { className: "card-header bg-white d-flex justify-content-between align-items-center py-3" }, e("h5", { className: "text-primary mb-0 fw-bold" }, "QUAN LY CHUC VU (React)"), e("a", { href: "/admin/positions/add", className: "btn btn-success shadow-sm px-3" }, "Them chuc vu moi")),
      e(
        "div",
        { className: "table-responsive" },
        e(
          "table",
          { className: "table table-hover align-middle mb-0" },
          e("thead", { className: "table-light" }, e("tr", null, e("th", null, "ID"), e("th", null, "Ten chuc vu"), e("th", null, "Level"), e("th", null, "He so phu cap"), e("th", null, "Thang tien toi"), e("th", { className: "text-center" }, "Thao tac"))),
          e(
            "tbody",
            null,
            (cfg.positions || []).map((p) =>
              e(
                "tr",
                { key: p.id },
                e("td", null, p.id),
                e("td", { className: "fw-bold text-dark" }, p.positionName),
                e("td", null, p.jobLevel),
                e("td", null, p.allowanceCoeff ?? "0.00"),
                e("td", null, e("span", { className: "badge bg-light text-secondary border" }, p.nextPositionName || "Kich tran")),
                e("td", { className: "text-center" }, e("a", { href: `/admin/positions/edit/${p.id}`, className: "btn btn-sm btn-outline-warning me-2" }, "Sua"), e("a", { href: `/admin/positions/delete/${p.id}`, className: "btn btn-sm btn-outline-danger", onClick: (ev) => { if (!confirm("Xoa chuc vu nay?")) ev.preventDefault(); } }, "Xoa"))
              )
            )
          )
        )
      )
    );
  }

  function PositionFormApp() {
    const cfg = window.HRMS_POSITION_FORM_CONFIG || { position: {}, allPositions: [] };
    const [form, setForm] = React.useState({
      id: cfg.position.id || "",
      positionName: cfg.position.positionName || "",
      jobLevel: cfg.position.jobLevel || 1,
      allowanceCoeff: cfg.position.allowanceCoeff || 0,
      nextPosition: cfg.position.nextPositionId || "",
    });
    return e(
      "form",
      { action: "/admin/positions/save", method: "post" },
      e("input", { type: "hidden", name: "id", value: form.id }),
      e("div", { className: "mb-3" }, e("label", { className: "form-label fw-bold" }, "Ten chuc vu"), e("input", { type: "text", name: "positionName", className: "form-control", required: true, value: form.positionName, onChange: (ev) => setForm({ ...form, positionName: ev.target.value }) })),
      e("div", { className: "row" },
        e("div", { className: "col-md-6 mb-3" }, e("label", { className: "form-label fw-bold" }, "Cap do"), e("input", { type: "number", min: 1, name: "jobLevel", className: "form-control", value: form.jobLevel, onChange: (ev) => setForm({ ...form, jobLevel: ev.target.value }) })),
        e("div", { className: "col-md-6 mb-3" }, e("label", { className: "form-label fw-bold" }, "He so phu cap"), e("input", { type: "number", step: "0.01", name: "allowanceCoeff", className: "form-control", value: form.allowanceCoeff, onChange: (ev) => setForm({ ...form, allowanceCoeff: ev.target.value }) }))),
      e("div", { className: "mb-4" }, e("label", { className: "form-label fw-bold" }, "Chuc vu thang tien tiep theo"), e("select", { className: "form-select", name: "nextPosition", value: form.nextPosition, onChange: (ev) => setForm({ ...form, nextPosition: ev.target.value }) }, e("option", { value: "" }, "-- Khong co (Kich tran) --"), (cfg.allPositions || []).map((p) => e("option", { key: p.id, value: p.id }, p.positionName)))),
      e("div", { className: "d-flex justify-content-between" }, e("a", { href: "/admin/positions", className: "btn btn-outline-secondary px-4" }, "Quay lai"), e("button", { type: "submit", className: "btn btn-success px-4" }, "Luu chuc vu"))
    );
  }

  function CandidateListApp() {
    const cfg = window.HRMS_CANDIDATE_LIST_CONFIG || { candidates: [] };
    return e(
      "div",
      { className: "container-fluid py-4" },
      e("div", { className: "d-flex justify-content-between align-items-center mb-3" }, e("h4", null, "Quan Ly Ung Vien (React)"), e("a", { href: "/hiring/candidates/add", className: "btn btn-success" }, "Them ung vien")),
      e(
        "div",
        { className: "card border-0 shadow-sm" },
        e(
          "div",
          { className: "table-responsive" },
          e(
            "table",
            { className: "table table-hover align-middle mb-0" },
            e("thead", { className: "bg-light" }, e("tr", null, e("th", { className: "ps-4" }, "Ung vien"), e("th", null, "Vi tri"), e("th", null, "Ngay nop"), e("th", null, "Trang thai"), e("th", { className: "text-center" }, "Hanh dong"))),
            e(
              "tbody",
              null,
              cfg.candidates.length === 0
                ? e("tr", null, e("td", { colSpan: 5, className: "text-center py-4 text-muted" }, "Khong co ung vien"))
                : cfg.candidates.map((c) =>
                    e(
                      "tr",
                      { key: c.id },
                      e("td", { className: "ps-4" }, e("div", { className: "fw-semibold" }, c.fullName), e("div", { className: "small text-muted" }, c.email || "")),
                      e("td", null, c.jobTitle || "---"),
                      e("td", null, c.appliedDate || ""),
                      e("td", null, e("span", { className: "badge bg-secondary" }, c.statusLabel || c.status)),
                      e("td", { className: "text-center" }, e("a", { className: "btn btn-sm btn-outline-warning me-2", href: `/hiring/candidates/edit/${c.id}` }, "Sua"), e("a", { className: "btn btn-sm btn-outline-danger", href: `/hiring/candidates/delete/${c.id}`, onClick: (ev) => { if (!confirm("Xoa ung vien nay?")) ev.preventDefault(); } }, "Xoa"))
                    )
                  )
            )
          )
        )
      )
    );
  }

  function CandidateFormApp() {
    const cfg = window.HRMS_CANDIDATE_FORM_CONFIG || { candidate: {}, postings: [] };
    const [form, setForm] = React.useState({
      id: cfg.candidate.id || "",
      fullName: cfg.candidate.fullName || "",
      email: cfg.candidate.email || "",
      phone: cfg.candidate.phone || "",
      appliedDate: cfg.candidate.appliedDate || "",
      jobPostingId: cfg.candidate.jobPostingId || "",
      status: cfg.candidate.status || "NEW",
      cvUrl: cfg.candidate.cvUrl || "",
      notes: cfg.candidate.notes || "",
    });
    return e(
      "form",
      { action: "/hiring/candidates/save", method: "post" },
      e("input", { type: "hidden", name: "id", value: form.id }),
      e("div", { className: "row g-3" },
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "Ho va ten"), e("input", { className: "form-control", name: "fullName", required: true, value: form.fullName, onChange: (ev) => setForm({ ...form, fullName: ev.target.value }) })),
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "Email"), e("input", { type: "email", className: "form-control", name: "email", value: form.email, onChange: (ev) => setForm({ ...form, email: ev.target.value }) })),
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "So dien thoai"), e("input", { className: "form-control", name: "phone", value: form.phone, onChange: (ev) => setForm({ ...form, phone: ev.target.value }) })),
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "Ngay nop"), e("input", { type: "date", className: "form-control", name: "appliedDate", value: form.appliedDate, onChange: (ev) => setForm({ ...form, appliedDate: ev.target.value }) })),
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "Vi tri ung tuyen"), e("select", { className: "form-select", name: "jobPosting.id", value: form.jobPostingId, onChange: (ev) => setForm({ ...form, jobPostingId: ev.target.value }) }, e("option", { value: "" }, "-- Chon vi tri --"), (cfg.postings || []).map((p) => e("option", { key: p.id, value: p.id }, p.title)))),
        e("div", { className: "col-md-6" }, e("label", { className: "form-label fw-semibold" }, "Trang thai"), e("select", { className: "form-select", name: "status", value: form.status, onChange: (ev) => setForm({ ...form, status: ev.target.value }) }, ["NEW", "SCREENING", "INTERVIEW", "OFFER", "HIRED", "REJECTED"].map((s) => e("option", { key: s, value: s }, s)))),
        e("div", { className: "col-12" }, e("label", { className: "form-label fw-semibold" }, "CV URL"), e("input", { className: "form-control", name: "cvUrl", value: form.cvUrl, onChange: (ev) => setForm({ ...form, cvUrl: ev.target.value }) })),
        e("div", { className: "col-12" }, e("label", { className: "form-label fw-semibold" }, "Ghi chu"), e("textarea", { className: "form-control", rows: 3, name: "notes", value: form.notes, onChange: (ev) => setForm({ ...form, notes: ev.target.value }) }))
      ),
      e("div", { className: "d-flex gap-2 mt-3" }, e("button", { type: "submit", className: "btn btn-success px-4" }, "Luu ung vien"), e("a", { href: "/hiring/candidates", className: "btn btn-outline-secondary px-4" }, "Huy"))
    );
  }

  if (window.HRMS_DEPARTMENT_LIST_PAGE) mount("department-list-root", DepartmentListApp);
  if (window.HRMS_DEPARTMENT_FORM_PAGE) mount("department-form-root", DepartmentFormApp);
  if (window.HRMS_POSITION_LIST_PAGE) mount("position-list-root", PositionListApp);
  if (window.HRMS_POSITION_FORM_PAGE) mount("position-form-root", PositionFormApp);
  if (window.HRMS_CANDIDATE_LIST_PAGE) mount("candidate-list-root", CandidateListApp);
  if (window.HRMS_CANDIDATE_FORM_PAGE) mount("candidate-form-root", CandidateFormApp);
})();

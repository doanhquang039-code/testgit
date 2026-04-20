(function () {
  const e = React.createElement;
  const mount = (id, app) => {
    const el = document.getElementById(id);
    if (el) ReactDOM.createRoot(el).render(e(app));
  };

  function Alert({ type, message }) {
    if (!message) return null;
    return e("div", { className: `alert alert-${type} py-2` }, message);
  }

  function UserListApp() {
    const [users, setUsers] = React.useState([]);
    const [keyword, setKeyword] = React.useState("");
    const [error, setError] = React.useState("");
    const [loading, setLoading] = React.useState(false);

    const loadUsers = React.useCallback(async (kw) => {
      setLoading(true);
      setError("");
      try {
        const query = kw ? `?keyword=${encodeURIComponent(kw)}&status=ACTIVE` : "?status=ACTIVE";
        const res = await fetch(`/api/users${query}`, { credentials: "include" });
        if (!res.ok) throw new Error("Khong the tai danh sach nhan vien");
        setUsers(await res.json());
      } catch (err) {
        setError(err.message || "Tai du lieu that bai");
      } finally {
        setLoading(false);
      }
    }, []);

    React.useEffect(() => {
      loadUsers("");
    }, [loadUsers]);

    const deactivate = async (id) => {
      if (!window.confirm("Ban co chac muon an nhan vien nay?")) return;
      try {
        const res = await fetch(`/api/users/${id}/deactivate`, {
          method: "PATCH",
          credentials: "include",
        });
        if (!res.ok) throw new Error("Khong the an nhan vien");
        loadUsers(keyword);
      } catch (err) {
        setError(err.message || "Cap nhat that bai");
      }
    };

    return e(
      "div",
      { className: "container-fluid py-4" },
      e(
        "div",
        { className: "card border-0 shadow-sm" },
        e(
          "div",
          { className: "card-header bg-white py-3 d-flex justify-content-between align-items-center" },
          e("h5", { className: "text-primary mb-0 fw-bold" }, "QUAN LY NHAN VIEN (React)"),
          e("a", { href: "/admin/users/add", className: "btn btn-primary shadow-sm px-3" }, "Them nhan vien moi")
        ),
        e(
          "div",
          { className: "card-header bg-white py-3" },
          e(
            "div",
            { className: "row align-items-center g-2" },
            e(
              "div",
              { className: "col-md-6 d-flex gap-2" },
              e("input", {
                className: "form-control",
                value: keyword,
                onChange: (ev) => setKeyword(ev.target.value),
                placeholder: "Tim theo ten, email, ma NV",
              }),
              e("button", { className: "btn btn-primary", onClick: () => loadUsers(keyword) }, "Tim")
            ),
            e(
              "div",
              { className: "col-md-6 text-end" },
              e("a", { href: "/admin/users/export/excel", className: "btn btn-outline-success border-2 fw-bold me-2" }, "Xuat Excel"),
              e("a", { href: "/admin/users/export/pdf", className: "btn btn-outline-danger border-2 fw-bold" }, "Xuat PDF")
            )
          )
        ),
        e("div", { className: "card-body" }, e(Alert, { type: "danger", message: error }), loading && e("div", { className: "text-muted mb-2" }, "Dang tai...")),
        e(
          "div",
          { className: "table-responsive" },
          e(
            "table",
            { className: "table table-hover table-striped align-middle mb-0" },
            e(
              "thead",
              { className: "bg-light" },
              e("tr", null, e("th", { className: "ps-4" }, "Nhan vien"), e("th", null, "Tai khoan"), e("th", null, "Phong ban"), e("th", null, "Chuc vu"), e("th", { className: "text-center" }, "Thao tac"))
            ),
            e(
              "tbody",
              null,
              users.map((u) =>
                e(
                  "tr",
                  { key: u.id },
                  e(
                    "td",
                    { className: "ps-4" },
                    e("div", { className: "fw-bold text-dark" }, u.fullName || ""),
                    e("div", { className: "small text-muted" }, u.email || ""),
                    e("div", { className: "small text-muted" }, `${u.employeeCode || "Chua co ma"} | ${u.phoneNumber || "Chua co SDT"}`)
                  ),
                  e("td", null, e("span", { className: "badge rounded-pill bg-info text-dark bg-opacity-10 px-3" }, u.username || "")),
                  e("td", null, u.departmentName || "---"),
                  e("td", null, u.positionName || "---"),
                  e(
                    "td",
                    { className: "text-center" },
                    e("a", { href: `/admin/users/edit/${u.id}`, className: "btn btn-sm btn-warning me-2" }, "Sua"),
                    e("button", { className: "btn btn-sm btn-danger", onClick: () => deactivate(u.id) }, "An")
                  )
                )
              )
            )
          )
        ),
        e("div", { className: "card-footer bg-white border-0 py-3 text-muted small" }, `Tong cong ${users.length} nhan vien dang hoat dong.`)
      )
    );
  }

  function UserFormApp() {
    const cfg = window.HRMS_USER_FORM_CONFIG || {};
    const [form, setForm] = React.useState({
      id: cfg.user?.id || null,
      username: cfg.user?.username || "",
      employeeCode: cfg.user?.employeeCode || "",
      fullName: cfg.user?.fullName || "",
      email: cfg.user?.email || "",
      phoneNumber: cfg.user?.phoneNumber || "",
      gender: cfg.user?.gender || "",
      dateOfBirth: cfg.user?.dateOfBirth || "",
      hireDate: cfg.user?.hireDate || "",
      address: cfg.user?.address || "",
      password: "",
      role: cfg.user?.role || "USER",
      status: cfg.user?.status || "ACTIVE",
      departmentId: cfg.user?.departmentId || "",
      positionId: cfg.user?.positionId || "",
    });
    const [error, setError] = React.useState("");
    const [success, setSuccess] = React.useState("");
    const [image, setImage] = React.useState(null);

    const onChange = (ev) => {
      setForm((prev) => ({ ...prev, [ev.target.name]: ev.target.value }));
    };

    const onSubmit = async (ev) => {
      ev.preventDefault();
      setError("");
      setSuccess("");
      try {
        const payload = { ...form };
        if (!payload.password) delete payload.password;
        payload.departmentId = payload.departmentId ? Number(payload.departmentId) : null;
        payload.positionId = payload.positionId ? Number(payload.positionId) : null;

        const url = payload.id ? `/api/users/${payload.id}` : "/api/users";
        const method = payload.id ? "PUT" : "POST";
        const res = await fetch(url, {
          method,
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(payload),
        });
        const data = await res.json().catch(() => ({}));
        if (!res.ok) throw new Error(data.message || "Luu thong tin that bai");

        // still upload avatar through MVC endpoint for compatibility
        if (image) {
          const fd = new FormData();
          Object.keys(form).forEach((k) => fd.append(k, form[k] == null ? "" : form[k]));
          fd.append("id", data.id);
          fd.append("image", image);
          await fetch("/admin/users/save", { method: "POST", body: fd, credentials: "include" });
        }

        setSuccess("Luu nhan vien thanh cong");
        setTimeout(() => {
          window.location.href = "/admin/users";
        }, 800);
      } catch (err) {
        setError(err.message || "Khong the luu nhan vien");
      }
    };

    const depts = cfg.departments || [];
    const positions = cfg.positions || [];

    return e(
      "div",
      { className: "container py-4" },
      e(
        "div",
        { className: "card shadow-sm border-0" },
        e("div", { className: "card-header bg-white fw-bold" }, form.id ? "Cap nhat nhan vien (React)" : "Them nhan vien (React)"),
        e(
          "div",
          { className: "card-body" },
          e(Alert, { type: "danger", message: error }),
          e(Alert, { type: "success", message: success }),
          e(
            "form",
            { onSubmit },
            e("div", { className: "row g-3" },
              ["username", "employeeCode", "fullName", "email", "phoneNumber", "address"].map((field) =>
                e("div", { className: "col-md-6", key: field },
                  e("label", { className: "form-label" }, field),
                  e("input", {
                    className: "form-control",
                    name: field,
                    value: form[field] || "",
                    onChange,
                    required: field === "username" || field === "fullName",
                  })
                )
              ),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "gender"),
                e("select", { className: "form-select", name: "gender", value: form.gender, onChange },
                  e("option", { value: "" }, "-- Chon --"),
                  e("option", { value: "MALE" }, "MALE"),
                  e("option", { value: "FEMALE" }, "FEMALE"),
                  e("option", { value: "OTHER" }, "OTHER"))),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "dateOfBirth"),
                e("input", { type: "date", className: "form-control", name: "dateOfBirth", value: form.dateOfBirth || "", onChange })),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "hireDate"),
                e("input", { type: "date", className: "form-control", name: "hireDate", value: form.hireDate || "", onChange })),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "role"),
                e("select", { className: "form-select", name: "role", value: form.role, onChange },
                  ["ADMIN", "MANAGER", "HIRING", "USER"].map((r) => e("option", { key: r, value: r }, r)))),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "department"),
                e("select", { className: "form-select", name: "departmentId", value: form.departmentId || "", onChange },
                  e("option", { value: "" }, "-- Chon phong ban --"),
                  depts.map((d) => e("option", { key: d.id, value: d.id }, d.name)))),
              e("div", { className: "col-md-4" }, e("label", { className: "form-label" }, "position"),
                e("select", { className: "form-select", name: "positionId", value: form.positionId || "", onChange },
                  e("option", { value: "" }, "-- Chon chuc vu --"),
                  positions.map((p) => e("option", { key: p.id, value: p.id }, p.name)))),
              e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "password"),
                e("input", { className: "form-control", type: "password", name: "password", value: form.password, onChange, required: !form.id })),
              e("div", { className: "col-md-6" }, e("label", { className: "form-label" }, "avatar"),
                e("input", { className: "form-control", type: "file", onChange: (ev) => setImage(ev.target.files?.[0] || null) }))
            ),
            e("div", { className: "mt-3 d-flex gap-2" },
              e("button", { type: "submit", className: "btn btn-primary" }, "Luu"),
              e("a", { className: "btn btn-outline-secondary", href: "/admin/users" }, "Quay lai"))
          )
        )
      )
    );
  }

  if (window.HRMS_USER_LIST_PAGE) mount("admin-user-list-root", UserListApp);
  if (window.HRMS_USER_FORM_PAGE) mount("admin-user-form-root", UserFormApp);
})();

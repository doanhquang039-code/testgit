(function () {
  function normalize(path) {
    return (path || "").replace(/\/+$/, "") || "/";
  }

  function markActiveLinks() {
    var current = normalize(window.location.pathname);
    document.querySelectorAll(".sidebar a[href], .role-sidebar a[href]").forEach(function (link) {
      var href = link.getAttribute("href");
      if (!href || href === "#") return;
      var url;
      try {
        url = new URL(href, window.location.origin);
      } catch (e) {
        return;
      }
      var target = normalize(url.pathname);
      var exact = current === target;
      var nested = target !== "/" && current.indexOf(target + "/") === 0;
      if (exact || nested) link.classList.add("active");
    });
  }

  function tagRole() {
    var path = window.location.pathname;
    if (path.indexOf("/manager") === 0) document.body.classList.add("role-manager");
    if (path.indexOf("/hiring") === 0) document.body.classList.add("role-hiring");
    if (path.indexOf("/user1") === 0 || path.indexOf("/user/") === 0) document.body.classList.add("role-user");
  }

  function init() {
    tagRole();
    markActiveLinks();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();

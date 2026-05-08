(function () {
  "use strict";

  if (window.HRMSChatbotWidgetLoaded) return;
  window.HRMSChatbotWidgetLoaded = true;

  var path = window.location.pathname || "";
  if (path === "/login" || path.indexOf("/user1/chatbot") === 0) return;

  function roleName() {
    if (path.indexOf("/admin") === 0) return "admin";
    if (path.indexOf("/manager") === 0) return "manager";
    if (path.indexOf("/hiring") === 0) return "hiring";
    if (path.indexOf("/user1") === 0 || path.indexOf("/user/") === 0) return "user";
    return "user";
  }

  function chipsForRole(role) {
    if (role === "admin") return ["Tien do cong viec", "Ket qua KPI", "Don nghi cho duyet"];
    if (role === "manager") return ["Tinh hinh team", "Tien do muc tieu", "Ngan sach team"];
    if (role === "hiring") return ["Pipeline tuyen dung", "Lich phong van", "Ung vien moi"];
    return ["Tien do cong viec", "Ket qua KPI", "Don nghi cua toi"];
  }

  function sessionKey() {
    return "hrms_chatbot_session_" + roleName();
  }

  function getSessionId() {
    var key = sessionKey();
    var existing = sessionStorage.getItem(key);
    if (existing) return existing;
    var id = crypto && crypto.randomUUID ? crypto.randomUUID() : String(Date.now()) + Math.random();
    sessionStorage.setItem(key, id);
    return id;
  }

  function setSessionId(id) {
    if (id) sessionStorage.setItem(sessionKey(), id);
  }

  function escapeHtml(value) {
    return String(value == null ? "" : value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  }

  function formatText(value) {
    return escapeHtml(value).replace(/\n/g, "<br>");
  }

  function createWidget() {
    var role = roleName();
    var chips = chipsForRole(role);
    var root = document.createElement("div");
    root.id = "hrms-chatbot";
    root.className = "hrms-chatbot";
    root.innerHTML = [
      '<button type="button" class="hrms-chatbot-toggle" aria-label="Mo tro ly HR">',
      '<i class="bi bi-chat-dots"></i><span>Tro ly HR</span>',
      "</button>",
      '<section class="hrms-chatbot-panel" aria-label="Tro ly HR">',
      '<div class="hrms-chatbot-head">',
      '<div class="hrms-chatbot-title"><div class="hrms-chatbot-avatar"><i class="bi bi-robot"></i></div>',
      '<div><strong>Tro ly HRMS</strong><small>Ho tro theo vai tro ' + escapeHtml(role) + "</small></div></div>",
      '<button type="button" class="hrms-chatbot-close" aria-label="Dong">x</button>',
      "</div>",
      '<div class="hrms-chatbot-messages" id="hrms-chatbot-messages">',
      '<div class="hrms-chatbot-welcome">Hoi nhanh ve cong viec, KPI, cham cong, nghi phep, luong hoac quy trinh trong he thong.</div>',
      "</div>",
      '<div class="hrms-chatbot-chips">',
      chips.map(function (c) {
        return '<button type="button" class="hrms-chatbot-chip" data-chatbot-chip="' + escapeHtml(c) + '">' + escapeHtml(c) + "</button>";
      }).join(""),
      "</div>",
      '<form class="hrms-chatbot-form" id="hrms-chatbot-form">',
      '<input class="hrms-chatbot-input" id="hrms-chatbot-input" autocomplete="off" placeholder="Nhap cau hoi...">',
      '<button type="submit" class="hrms-chatbot-send" aria-label="Gui"><i class="bi bi-send"></i></button>',
      "</form>",
      "</section>"
    ].join("");
    document.body.appendChild(root);
    return root;
  }

  function init() {
    var root = createWidget();
    var toggle = root.querySelector(".hrms-chatbot-toggle");
    var close = root.querySelector(".hrms-chatbot-close");
    var form = root.querySelector("#hrms-chatbot-form");
    var input = root.querySelector("#hrms-chatbot-input");
    var messages = root.querySelector("#hrms-chatbot-messages");
    var sessionId = getSessionId();

    function addMessage(role, text, meta) {
      var row = document.createElement("div");
      row.className = "hrms-chatbot-message " + role;
      row.innerHTML = '<div class="hrms-chatbot-bubble">' + formatText(text) +
        (meta ? '<div class="hrms-chatbot-meta">' + escapeHtml(meta) + "</div>" : "") +
        "</div>";
      messages.appendChild(row);
      messages.scrollTop = messages.scrollHeight;
      return row;
    }

    function setBusy(isBusy) {
      input.disabled = isBusy;
      form.querySelector("button").disabled = isBusy;
    }

    async function send(text) {
      var message = (text || input.value || "").trim();
      if (!message) return;
      input.value = "";
      addMessage("user", message);
      var pending = addMessage("bot", "Dang xu ly...");
      setBusy(true);

      try {
        var res = await fetch("/api/chatbot/message", {
          method: "POST",
          credentials: "same-origin",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ message: message, sessionId: sessionId })
        });
        if (!res.ok) throw new Error("HTTP " + res.status);
        var data = await res.json();
        sessionId = data.sessionId || sessionId;
        setSessionId(sessionId);
        pending.remove();
        addMessage("bot", data.reply || "Chua co phan hoi.", data.intent ? "#" + data.intent : "");
      } catch (err) {
        pending.remove();
        addMessage("bot", "Khong gui duoc tin nhan. Hay thu lai sau hoac dang nhap lai.");
      } finally {
        setBusy(false);
        input.focus();
      }
    }

    toggle.addEventListener("click", function () {
      root.classList.toggle("open");
      if (root.classList.contains("open")) setTimeout(function () { input.focus(); }, 40);
    });
    close.addEventListener("click", function () { root.classList.remove("open"); });
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      send();
    });
    root.querySelectorAll("[data-chatbot-chip]").forEach(function (chip) {
      chip.addEventListener("click", function () {
        root.classList.add("open");
        send(chip.getAttribute("data-chatbot-chip"));
      });
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();

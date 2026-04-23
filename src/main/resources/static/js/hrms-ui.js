/**
 * HRMS UI Enhancements
 * - Breadcrumb auto-generation
 * - Quick Actions menu
 * - Toast notifications
 * - Keyboard shortcuts help
 * - Back-to-top button
 */
(function () {
  'use strict';

  // ==================== BREADCRUMB ====================
  var BREADCRUMB_MAP = {
    '/admin/dashboard': [{ label: 'Dashboard', url: '/admin/dashboard' }],
    '/admin/users': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Employees' }],
    '/admin/users/add': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Employees', url: '/admin/users' }, { label: 'Add Employee' }],
    '/admin/departments': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Departments' }],
    '/admin/positions': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Positions' }],
    '/admin/contracts': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Contracts' }],
    '/admin/payroll': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Payroll' }],
    '/admin/payments': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Payments' }],
    '/admin/leaves': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Leave Requests' }],
    '/admin/attendance': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Attendance' }],
    '/admin/tasks': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Tasks' }],
    '/admin/reviews': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'KPI Reviews' }],
    '/admin/reports': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Reports' }],
    '/admin/kpi': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'KPI Goals' }],
    '/admin/expenses': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Expenses' }],
    '/admin/skills': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Skills' }],
    '/admin/documents': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Documents' }],
    '/admin/shifts': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Work Shifts' }],
    '/admin/videos': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Training Videos' }],
    '/admin/announcements': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'Announcements' }],
    '/admin/cache': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'System', url: null }, { label: 'Cache & Email' }],
    '/admin/cloud': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'System', url: null }, { label: 'Cloud Storage' }],
    '/admin/audit-log': [{ label: 'Dashboard', url: '/admin/dashboard' }, { label: 'System', url: null }, { label: 'Audit Log' }],
    '/manager/dashboard': [{ label: 'Manager Dashboard' }],
    '/manager/team': [{ label: 'Manager Dashboard', url: '/manager/dashboard' }, { label: 'My Team' }],
    '/manager/overtime': [{ label: 'Manager Dashboard', url: '/manager/dashboard' }, { label: 'Overtime Approval' }],
    '/user1/dashboard': [{ label: 'Dashboard' }],
    '/user1/profile': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'My Profile' }],
    '/user1/payroll': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'Payslip' }],
    '/user1/kpi': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'KPI Goals' }],
    '/user1/expenses': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'Expenses' }],
    '/user1/skills': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'Skills' }],
    '/user1/documents': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'Documents' }],
    '/user1/overtime': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'Overtime' }],
    '/user1/my-shifts': [{ label: 'Dashboard', url: '/user1/dashboard' }, { label: 'My Shifts' }],
    '/hiring': [{ label: 'Hiring Dashboard' }],
    '/hiring/postings': [{ label: 'Hiring Dashboard', url: '/hiring' }, { label: 'Job Postings' }],
    '/hiring/candidates': [{ label: 'Hiring Dashboard', url: '/hiring' }, { label: 'Candidates' }],
  };

  function injectBreadcrumb() {
    var path = window.location.pathname;
    // Match exact or prefix
    var crumbs = BREADCRUMB_MAP[path];
    if (!crumbs) {
      // Try prefix match
      var keys = Object.keys(BREADCRUMB_MAP).sort(function(a,b){ return b.length - a.length; });
      for (var i = 0; i < keys.length; i++) {
        if (path.startsWith(keys[i]) && keys[i] !== '/') {
          crumbs = BREADCRUMB_MAP[keys[i]];
          break;
        }
      }
    }
    if (!crumbs || crumbs.length <= 1) return;

    var nav = document.createElement('nav');
    nav.setAttribute('aria-label', 'breadcrumb');
    nav.style.cssText = 'padding:8px 0 0;margin-bottom:-8px;';
    nav.innerHTML = '<ol style="display:flex;align-items:center;gap:6px;list-style:none;margin:0;padding:0;font-size:0.78rem;">' +
      crumbs.map(function(c, i) {
        var isLast = i === crumbs.length - 1;
        return '<li style="display:flex;align-items:center;gap:6px;">' +
          (i > 0 ? '<span style="color:#475569;">›</span>' : '') +
          (isLast || !c.url
            ? '<span style="color:' + (isLast ? '#94a3b8' : '#64748b') + ';font-weight:' + (isLast ? '600' : '400') + ';">' + c.label + '</span>'
            : '<a href="' + c.url + '" style="color:#6366f1;text-decoration:none;font-weight:500;" onmouseover="this.style.textDecoration=\'underline\'" onmouseout="this.style.textDecoration=\'none\'">' + c.label + '</a>') +
          '</li>';
      }).join('') +
      '</ol>';

    // Insert after page header h1
    var h1 = document.querySelector('main h1, .main h1, .main-content h1, .page-header h1');
    if (h1 && h1.parentNode) {
      h1.parentNode.insertBefore(nav, h1);
    }
  }

  // ==================== TOAST ====================
  function showToast(message, type, duration) {
    type = type || 'info';
    duration = duration || 3000;
    var colors = { success: '#10b981', error: '#ef4444', warning: '#f59e0b', info: '#6366f1' };
    var icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };

    var toast = document.createElement('div');
    toast.style.cssText = [
      'position:fixed', 'bottom:' + (80 + document.querySelectorAll('.hrms-toast').length * 60) + 'px',
      'left:50%', 'transform:translateX(-50%)', 'z-index:99999',
      'background:#1e293b', 'border:1px solid ' + colors[type],
      'border-left:4px solid ' + colors[type],
      'color:white', 'padding:12px 20px', 'border-radius:10px',
      'box-shadow:0 8px 24px rgba(0,0,0,0.4)',
      'display:flex', 'align-items:center', 'gap:10px',
      'font-size:0.88rem', 'font-weight:500',
      'animation:hrmsToastIn 0.3s ease',
      'max-width:400px', 'min-width:200px',
    ].join(';');
    toast.className = 'hrms-toast';
    toast.innerHTML = '<span>' + icons[type] + '</span><span>' + message + '</span>';

    if (!document.getElementById('hrms-toast-style')) {
      var s = document.createElement('style');
      s.id = 'hrms-toast-style';
      s.textContent = '@keyframes hrmsToastIn{from{opacity:0;transform:translateX(-50%) translateY(20px)}to{opacity:1;transform:translateX(-50%) translateY(0)}}';
      document.head.appendChild(s);
    }

    document.body.appendChild(toast);
    setTimeout(function() {
      toast.style.opacity = '0';
      toast.style.transition = 'opacity 0.3s';
      setTimeout(function() { toast.remove(); }, 300);
    }, duration);
  }

  // ==================== BACK TO TOP ====================
  function injectBackToTop() {
    if (document.getElementById('hrms-back-top')) return;
    var btn = document.createElement('button');
    btn.id = 'hrms-back-top';
    btn.innerHTML = '↑';
    btn.title = 'Back to top';
    btn.style.cssText = 'position:fixed;bottom:136px;right:24px;z-index:8998;width:36px;height:36px;border-radius:50%;background:rgba(99,102,241,0.3);border:1px solid rgba(99,102,241,0.5);color:#6366f1;font-size:1rem;cursor:pointer;display:none;align-items:center;justify-content:center;transition:all 0.2s;font-weight:700;';
    btn.addEventListener('click', function() { window.scrollTo({ top: 0, behavior: 'smooth' }); });
    btn.addEventListener('mouseenter', function() { this.style.background = '#6366f1'; this.style.color = 'white'; });
    btn.addEventListener('mouseleave', function() { this.style.background = 'rgba(99,102,241,0.3)'; this.style.color = '#6366f1'; });
    document.body.appendChild(btn);

    window.addEventListener('scroll', function() {
      btn.style.display = window.scrollY > 300 ? 'flex' : 'none';
    });
  }

  // ==================== KEYBOARD SHORTCUTS ====================
  function showShortcutsHelp() {
    if (document.getElementById('hrms-shortcuts-modal')) return;
    var modal = document.createElement('div');
    modal.id = 'hrms-shortcuts-modal';
    modal.innerHTML = [
      '<div style="position:fixed;inset:0;z-index:10000;background:rgba(0,0,0,0.6);" onclick="this.parentElement.remove()"></div>',
      '<div style="position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);z-index:10001;width:min(480px,90vw);background:#1e293b;border-radius:16px;box-shadow:0 24px 64px rgba(0,0,0,0.6);border:1px solid rgba(255,255,255,0.1);overflow:hidden;">',
        '<div style="background:linear-gradient(135deg,#6366f1,#8b5cf6);padding:16px 20px;display:flex;justify-content:space-between;align-items:center;">',
          '<span style="color:white;font-weight:700;font-size:1rem;">⌨️ Keyboard Shortcuts</span>',
          '<button onclick="document.getElementById(\'hrms-shortcuts-modal\').remove()" style="background:rgba(255,255,255,0.2);border:none;color:white;width:28px;height:28px;border-radius:50%;cursor:pointer;">✕</button>',
        '</div>',
        '<div style="padding:20px;display:grid;grid-template-columns:1fr 1fr;gap:10px;">',
          [
            ['Ctrl+K', 'Global Search'],
            ['⚙️ Button', 'Settings Panel'],
            ['🔔 Button', 'Notifications'],
            ['↑ Button', 'Back to Top'],
            ['Ctrl+Shift+D', 'Go to Dashboard'],
            ['Ctrl+Shift+U', 'Go to Users'],
            ['Ctrl+Shift+L', 'Go to Leaves'],
            ['Ctrl+Shift+P', 'Go to Payroll'],
            ['?', 'Show this help'],
            ['ESC', 'Close modals'],
          ].map(function(s) {
            return '<div style="display:flex;align-items:center;gap:10px;padding:8px 12px;background:rgba(255,255,255,0.04);border-radius:8px;">' +
              '<kbd style="background:rgba(255,255,255,0.1);border:1px solid rgba(255,255,255,0.15);color:#94a3b8;padding:3px 8px;border-radius:6px;font-size:0.72rem;white-space:nowrap;">' + s[0] + '</kbd>' +
              '<span style="color:#e2e8f0;font-size:0.82rem;">' + s[1] + '</span>' +
              '</div>';
          }).join(''),
        '</div>',
      '</div>',
    ].join('');
    document.body.appendChild(modal);
  }

  // ==================== KEYBOARD SHORTCUTS HANDLER ====================
  document.addEventListener('keydown', function(e) {
    // ? = show shortcuts (when not in input)
    if (e.key === '?' && !['INPUT','TEXTAREA','SELECT'].includes(document.activeElement.tagName)) {
      showShortcutsHelp();
    }
    // Ctrl+Shift+D = Dashboard
    if (e.ctrlKey && e.shiftKey && e.key === 'D') {
      e.preventDefault();
      var dash = window.location.pathname.startsWith('/admin') ? '/admin/dashboard' :
                 window.location.pathname.startsWith('/manager') ? '/manager/dashboard' :
                 '/user1/dashboard';
      window.location.href = dash;
    }
    // Ctrl+Shift+U = Users (admin only)
    if (e.ctrlKey && e.shiftKey && e.key === 'U' && window.location.pathname.startsWith('/admin')) {
      e.preventDefault(); window.location.href = '/admin/users';
    }
    // Ctrl+Shift+L = Leaves
    if (e.ctrlKey && e.shiftKey && e.key === 'L') {
      e.preventDefault();
      window.location.href = window.location.pathname.startsWith('/admin') ? '/admin/leaves' : '/user/leaves';
    }
    // Ctrl+Shift+P = Payroll
    if (e.ctrlKey && e.shiftKey && e.key === 'P' && window.location.pathname.startsWith('/admin')) {
      e.preventDefault(); window.location.href = '/admin/payroll';
    }
  });

  // ==================== INIT ====================
  function init() {
    injectBreadcrumb();
    injectBackToTop();

    // Intercept flash messages and show as toasts
    document.querySelectorAll('.alert-success, .alert-danger, .alert-warning').forEach(function(el) {
      var type = el.classList.contains('alert-success') ? 'success' :
                 el.classList.contains('alert-danger') ? 'error' : 'warning';
      var msg = el.textContent.trim();
      if (msg) showToast(msg, type, 4000);
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  // Expose
  window.HRMS = window.HRMS || {};
  window.HRMS.toast = showToast;
  window.HRMS.shortcuts = showShortcutsHelp;
})();

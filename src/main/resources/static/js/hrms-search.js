/**
 * HRMS Global Search
 * Floating search bar triggered by Ctrl+K or search icon
 */
(function () {
  'use strict';

  function createSearchModal() {
    if (document.getElementById('hrms-search-modal')) return;

    var modal = document.createElement('div');
    modal.id = 'hrms-search-modal';
    modal.innerHTML = [
      '<div style="position:fixed;inset:0;z-index:10000;background:rgba(0,0,0,0.6);backdrop-filter:blur(4px);" id="hrms-search-overlay" onclick="HRMS.search.close()"></div>',
      '<div style="position:fixed;top:80px;left:50%;transform:translateX(-50%);z-index:10001;width:min(600px,90vw);">',
        '<div style="background:#1e293b;border-radius:16px;box-shadow:0 24px 64px rgba(0,0,0,0.6);border:1px solid rgba(255,255,255,0.1);overflow:hidden;">',
          // Search input
          '<div style="display:flex;align-items:center;gap:12px;padding:16px 20px;border-bottom:1px solid rgba(255,255,255,0.08);">',
            '<span style="font-size:1.2rem;color:#6366f1;">🔍</span>',
            '<input id="hrms-search-input" type="text" placeholder="Search employees, departments, tasks..." autofocus ',
              'style="flex:1;background:none;border:none;outline:none;color:white;font-size:1rem;font-family:Inter,sans-serif;" ',
              'oninput="HRMS.search.query(this.value)" ',
              'onkeydown="HRMS.search.handleKey(event)">',
            '<kbd style="background:rgba(255,255,255,0.08);border:1px solid rgba(255,255,255,0.15);color:#94a3b8;padding:2px 8px;border-radius:6px;font-size:0.72rem;cursor:pointer;" onclick="HRMS.search.close()">ESC</kbd>',
          '</div>',
          // Results
          '<div id="hrms-search-results" style="max-height:400px;overflow-y:auto;padding:8px;">',
            '<div style="text-align:center;padding:32px;color:#475569;">',
              '<div style="font-size:2rem;margin-bottom:8px;">🔍</div>',
              '<div style="font-size:0.88rem;">Type to search across the system</div>',
              '<div style="font-size:0.78rem;color:#334155;margin-top:4px;">Employees, Departments, Tasks...</div>',
            '</div>',
          '</div>',
          // Footer
          '<div style="padding:10px 20px;border-top:1px solid rgba(255,255,255,0.06);display:flex;gap:16px;font-size:0.72rem;color:#475569;">',
            '<span>↑↓ Navigate</span>',
            '<span>↵ Open</span>',
            '<span>ESC Close</span>',
            '<span style="margin-left:auto;">Ctrl+K to open</span>',
          '</div>',
        '</div>',
      '</div>',
    ].join('');

    document.body.appendChild(modal);
    setTimeout(function() {
      var input = document.getElementById('hrms-search-input');
      if (input) input.focus();
    }, 50);
  }

  var searchTimeout = null;
  var selectedIndex = -1;

  function renderResults(data) {
    var container = document.getElementById('hrms-search-results');
    if (!container) return;

    if (!data.results || data.results.length === 0) {
      container.innerHTML = '<div style="text-align:center;padding:32px;color:#475569;"><div style="font-size:1.5rem;margin-bottom:8px;">😕</div><div>No results found</div></div>';
      return;
    }

    var html = data.results.map(function(r, i) {
      return '<a href="' + r.url + '" style="display:flex;align-items:center;gap:12px;padding:12px 16px;border-radius:10px;text-decoration:none;transition:background 0.15s;cursor:pointer;" ' +
        'onmouseover="this.style.background=\'rgba(99,102,241,0.15)\'" ' +
        'onmouseout="this.style.background=\'transparent\'" ' +
        'data-result-index="' + i + '">' +
        '<div style="width:36px;height:36px;border-radius:10px;background:rgba(99,102,241,0.15);display:flex;align-items:center;justify-content:center;font-size:1.1rem;flex-shrink:0;">' + r.icon + '</div>' +
        '<div style="flex:1;min-width:0;">' +
          '<div style="color:white;font-weight:600;font-size:0.9rem;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">' + r.title + '</div>' +
          '<div style="color:#64748b;font-size:0.78rem;">' + r.subtitle + '</div>' +
        '</div>' +
        '<span style="background:rgba(255,255,255,0.06);color:#64748b;padding:2px 8px;border-radius:6px;font-size:0.7rem;flex-shrink:0;">' + r.type + '</span>' +
        '</a>';
    }).join('');

    container.innerHTML = html;
    selectedIndex = -1;
  }

  window.HRMS = window.HRMS || {};
  window.HRMS.search = {
    open: function() { createSearchModal(); },

    close: function() {
      var modal = document.getElementById('hrms-search-modal');
      if (modal) modal.remove();
      selectedIndex = -1;
    },

    query: function(val) {
      clearTimeout(searchTimeout);
      if (!val || val.trim().length < 2) {
        var c = document.getElementById('hrms-search-results');
        if (c) c.innerHTML = '<div style="text-align:center;padding:32px;color:#475569;"><div style="font-size:2rem;margin-bottom:8px;">🔍</div><div style="font-size:0.88rem;">Type to search...</div></div>';
        return;
      }
      searchTimeout = setTimeout(function() {
        fetch('/api/search?q=' + encodeURIComponent(val))
          .then(function(r) { return r.json(); })
          .then(renderResults)
          .catch(function() {});
      }, 250);
    },

    handleKey: function(e) {
      var results = document.querySelectorAll('[data-result-index]');
      if (e.key === 'Escape') { this.close(); return; }
      if (e.key === 'ArrowDown') {
        e.preventDefault();
        selectedIndex = Math.min(selectedIndex + 1, results.length - 1);
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        selectedIndex = Math.max(selectedIndex - 1, 0);
      } else if (e.key === 'Enter' && selectedIndex >= 0) {
        var el = results[selectedIndex];
        if (el) { window.location.href = el.getAttribute('href'); }
        return;
      }
      results.forEach(function(r, i) {
        r.style.background = i === selectedIndex ? 'rgba(99,102,241,0.2)' : 'transparent';
      });
    }
  };

  // Keyboard shortcut Ctrl+K
  document.addEventListener('keydown', function(e) {
    if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
      e.preventDefault();
      if (document.getElementById('hrms-search-modal')) {
        window.HRMS.search.close();
      } else {
        window.HRMS.search.open();
      }
    }
    if (e.key === 'Escape' && document.getElementById('hrms-search-modal')) {
      window.HRMS.search.close();
    }
  });
})();

/**
 * HRMS Lang Toggle — Thin wrapper, delegates to hrms-settings.js
 * Kept for backward compatibility with existing templates.
 */
(function () {
  function getCookie(n) {
    var m = document.cookie.match(new RegExp('(^| )' + n + '=([^;]+)'));
    return m ? m[2] : null;
  }
  function getLang() {
    return new URLSearchParams(window.location.search).get('lang') ||
           getCookie('HRMS_LANG') ||
           (typeof localStorage !== 'undefined' ? localStorage.getItem('hrms_lang') : null) || 'vi';
  }
  function switchLang(lang) {
    if (window.HRMS && window.HRMS.settings) {
      window.HRMS.settings.setLang(lang);
    } else {
      var url = new URL(window.location.href);
      url.searchParams.set('lang', lang);
      window.location.href = url.toString();
    }
  }

  document.addEventListener('DOMContentLoaded', function () {
    var lang = getLang();
    // Update old-style lang-btn active state
    document.querySelectorAll('.lang-btn').forEach(function (btn) {
      var bl = btn.getAttribute('data-lang');
      btn.style.background = bl === lang ? '#6366f1' : 'rgba(255,255,255,0.08)';
      btn.style.borderColor = bl === lang ? '#6366f1' : 'rgba(255,255,255,0.15)';
      btn.style.color = bl === lang ? 'white' : '#94a3b8';
      btn.addEventListener('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        switchLang(this.getAttribute('data-lang'));
      });
    });
  });

  window.HRMS = window.HRMS || {};
  window.HRMS.setLang = switchLang;
  window.HRMS.getLang = getLang;
})();

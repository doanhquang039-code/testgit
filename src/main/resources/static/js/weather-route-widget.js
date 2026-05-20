(function () {
    const state = {
        lat: 10.7769,
        lon: 106.7009,
        label: "TP. Ho Chi Minh"
    };

    const weatherCodes = {
        0: "Troi quang",
        1: "It may",
        2: "May rai rac",
        3: "Nhieu may",
        45: "Suong mu",
        48: "Suong mu dong bang",
        51: "Mua phun nhe",
        53: "Mua phun",
        55: "Mua phun day",
        61: "Mua nhe",
        63: "Mua vua",
        65: "Mua lon",
        80: "Mua rao nhe",
        81: "Mua rao",
        82: "Mua rao lon",
        95: "Giong"
    };

    function text(selector, value) {
        document.querySelectorAll(selector).forEach((node) => {
            node.textContent = value;
        });
    }

    function fmtTime(value) {
        if (!value) return "--";
        return new Date(value).toLocaleTimeString("vi-VN", { hour: "2-digit", minute: "2-digit" });
    }

    async function reverseGeocode(lat, lon) {
        try {
            const url = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${encodeURIComponent(lat)}&lon=${encodeURIComponent(lon)}`;
            const response = await fetch(url, { headers: { "Accept": "application/json" } });
            if (!response.ok) return state.label;
            const data = await response.json();
            return data.address?.city || data.address?.town || data.address?.state || data.display_name || state.label;
        } catch (_) {
            return state.label;
        }
    }

    async function loadWeather() {
        text("[data-weather-desc]", "Dang tai du lieu...");
        const label = await reverseGeocode(state.lat, state.lon);
        state.label = label;
        try {
            const url = "https://api.open-meteo.com/v1/forecast"
                + `?latitude=${encodeURIComponent(state.lat)}&longitude=${encodeURIComponent(state.lon)}`
                + "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m"
                + "&timezone=auto";
            const response = await fetch(url);
            if (!response.ok) throw new Error("weather_failed");
            const data = await response.json();
            const current = data.current || {};
            text("[data-weather-temp]", current.temperature_2m != null ? `${Math.round(current.temperature_2m)}°C` : "--");
            text("[data-weather-desc]", weatherCodes[current.weather_code] || "Khong ro trang thai");
            text("[data-weather-location]", label);
            text("[data-weather-wind]", current.wind_speed_10m != null ? `${current.wind_speed_10m} km/h` : "--");
            text("[data-weather-humidity]", current.relative_humidity_2m != null ? `${current.relative_humidity_2m}%` : "--");
            text("[data-weather-time]", fmtTime(current.time));
        } catch (_) {
            text("[data-weather-desc]", "Khong tai duoc thoi tiet");
            text("[data-weather-location]", "Kiem tra ket noi mang hoac thu lai sau.");
        }
    }

    function geocodeDestination(query) {
        const url = `https://nominatim.openstreetmap.org/search?format=jsonv2&limit=1&q=${encodeURIComponent(query)}`;
        return fetch(url, { headers: { "Accept": "application/json" } })
            .then((response) => response.ok ? response.json() : [])
            .then((rows) => rows && rows.length ? rows[0] : null);
    }

    function routeTo(destination) {
        const url = "https://router.project-osrm.org/route/v1/driving/"
            + `${state.lon},${state.lat};${destination.lon},${destination.lat}?overview=false`;
        return fetch(url)
            .then((response) => response.ok ? response.json() : null)
            .then((data) => data?.routes?.[0] || null);
    }

    function mapsUrl(destinationLabel) {
        return "https://www.google.com/maps/dir/?api=1"
            + `&origin=${encodeURIComponent(`${state.lat},${state.lon}`)}`
            + `&destination=${encodeURIComponent(destinationLabel)}`
            + "&travelmode=driving";
    }

    function bindRoutes() {
        document.querySelectorAll("[data-route-form]").forEach((form) => {
            form.addEventListener("submit", async (event) => {
                event.preventDefault();
                const input = form.querySelector("[data-route-destination]");
                const result = form.parentElement.querySelector("[data-route-result]");
                const query = input?.value?.trim();
                if (!query) {
                    result.textContent = "Nhap dia diem can den.";
                    return;
                }
                result.textContent = "Dang tim duong...";
                try {
                    const dest = await geocodeDestination(query);
                    if (!dest) {
                        result.textContent = "Khong tim thay dia diem.";
                        return;
                    }
                    const route = await routeTo(dest);
                    const distance = route ? `${(route.distance / 1000).toFixed(1)} km` : "khong ro";
                    const minutes = route ? `${Math.round(route.duration / 60)} phut` : "khong ro";
                    const label = dest.display_name || query;
                    result.innerHTML = `Khoang cach ${distance}, thoi gian du kien ${minutes}. <a target="_blank" rel="noopener" href="${mapsUrl(label)}">Mo Google Maps</a>`;
                } catch (_) {
                    result.textContent = "Khong tim duoc duong di luc nay.";
                }
            });
        });
    }

    function init() {
        document.querySelectorAll("[data-weather-refresh]").forEach((button) => {
            button.addEventListener("click", loadWeather);
        });
        bindRoutes();
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition((pos) => {
                state.lat = pos.coords.latitude;
                state.lon = pos.coords.longitude;
                loadWeather();
            }, loadWeather, { enableHighAccuracy: false, timeout: 5000, maximumAge: 600000 });
        } else {
            loadWeather();
        }
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();

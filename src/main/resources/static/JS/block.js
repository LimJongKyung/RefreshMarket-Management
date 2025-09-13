document.addEventListener("DOMContentLoaded", function() {
    const timerEl = document.getElementById('sessionTimer');
    if (!timerEl || !sessionExpiryTime || sessionExpiryTime <= 0) return;

    function updateTimer() {
        const now = Date.now();
        let remainingSeconds = Math.floor((sessionExpiryTime - now) / 1000);

        if (remainingSeconds <= 0) {
            timerEl.textContent = "세션 만료!";
            alert("세션이 만료되어 로그아웃 됩니다.");
            window.location.href = "/";
            return;
        }

        const hours = String(Math.floor(remainingSeconds / 3600)).padStart(2, '0');
        const minutes = String(Math.floor((remainingSeconds % 3600) / 60)).padStart(2, '0');
        const seconds = String(remainingSeconds % 60).padStart(2, '0');

        timerEl.textContent = `자동 로그아웃까지 ${hours}:${minutes}:${seconds}`;
    }

    updateTimer();
    setInterval(updateTimer, 1000);
});
// 카카오 주소 검색
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('baseAddress').value = data.address;
        }
    }).open();
}

function combineAddress() {
    const base = document.getElementById('baseAddress').value.trim();
    const detail = document.getElementById('detailAddress').value.trim();
    const full = detail !== '' ? `${base} ${detail}` : base;

    if (!base) {
        alert("주소를 입력해주세요.");
        return false; // 폼 전송 막기
    }

    document.getElementById('address').value = full;
    return true; // 폼 전송
}

// 양식 검사
document.addEventListener('DOMContentLoaded', () => {
    const passwd = document.getElementById('passwd');
    const phone = document.getElementById('phoneNumber');
    const rrn = document.getElementById('registrationNumber');

    const passwdMsg = document.getElementById('passwdMsg');
    const phoneMsg = document.getElementById('phoneMsg');
    const rrnMsg = document.getElementById('rrnMsg');

    const passwdRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;
    const phoneRegex = /^01[016789]-\d{3,4}-\d{4}$/;
    const rrnRegex = /^\d{6}-\d{7}$/;

    passwd.addEventListener('input', () => {
        if (passwd.value === "") {
            passwdMsg.textContent = "";
            passwdMsg.className = "msg";
        } else if (!passwdRegex.test(passwd.value)) {
            passwdMsg.textContent = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자여야 합니다.";
            passwdMsg.className = "msg error";
        } else {
            passwdMsg.textContent = "사용 가능한 비밀번호입니다.";
            passwdMsg.className = "msg success";
        }
    });

    phone.addEventListener('input', () => {
        if (phone.value === "") {
            phoneMsg.textContent = "";
            phoneMsg.className = "msg";
        } else if (!phoneRegex.test(phone.value)) {
            phoneMsg.textContent = "전화번호 형식은 010-1234-5678입니다.";
            phoneMsg.className = "msg error";
        } else {
            phoneMsg.textContent = "올바른 전화번호입니다.";
            phoneMsg.className = "msg success";
        }
    });

    rrn.addEventListener('input', () => {
        if (rrn.value === "") {
            rrnMsg.textContent = "";
            rrnMsg.className = "msg";
        } else if (!rrnRegex.test(rrn.value)) {
            rrnMsg.textContent = "주민등록번호 형식은 123456-1234567입니다.";
            rrnMsg.className = "msg error";
        } else {
            rrnMsg.textContent = "올바른 주민등록번호입니다.";
            rrnMsg.className = "msg success";
        }
    });
});

function validateForm() {
    const passwd = document.getElementById('passwd').value.trim();
    const phone = document.getElementById('phoneNumber').value.trim();
    const rrn = document.getElementById('registrationNumber').value.trim();

    const passwdRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,20}$/;
    const phoneRegex = /^01[016789]-\d{3,4}-\d{4}$/;
    const rrnRegex = /^\d{6}-\d{7}$/;

    if (!passwdRegex.test(passwd)) {
        alert("비밀번호는 영문, 숫자, 특수문자 포함 8~20자여야 합니다.");
        return false;
    }

    if (!phoneRegex.test(phone)) {
        alert("전화번호 형식은 010-1234-5678 입니다.");
        return false;
    }

    if (!rrnRegex.test(rrn)) {
        alert("주민등록번호 형식은 123456-1234567 입니다.");
        return false;
    }

    return true; // 검증 모두 통과
}
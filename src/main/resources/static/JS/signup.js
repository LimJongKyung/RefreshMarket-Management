function checkId() {
  const userId = document.getElementById("id").value;
  if (!userId) {
    alert("아이디를 입력해주세요.");
    return;
  }

  fetch(`/check-id?id=${encodeURIComponent(userId)}`)
    .then(response => response.json())
    .then(exists => {
      if (exists) {
        alert("이미 사용 중인 아이디입니다.");
      } else {
        alert("사용 가능한 아이디입니다.");
      }
    })
    .catch(error => {
      console.error("중복 확인 오류:", error);
    });
}

function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      const baseAddress = data.address;
      document.getElementById("address").value = baseAddress;
      document.getElementById("detailAddress").focus();

      // 상세주소 입력 이벤트 연결
      document.getElementById("detailAddress").addEventListener("input", function () {
        const detail = this.value;
        document.getElementById("address").value = baseAddress + ' ' + detail;
      });
    }
  }).open();
}

function validateInput(id, messageId, successMsg) {
  const input = document.getElementById(id);
  const message = document.getElementById(messageId);
  let pattern;

  // 패턴 가져오기
  const rawPattern = input.getAttribute("pattern");
  if (rawPattern) {
    pattern = new RegExp(rawPattern);
  } else if (input.type === "email") {
    pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  } else {
    pattern = null;
  }

  // 유효성 검사
  if (pattern && pattern.test(input.value)) {
    message.textContent = successMsg;
    message.classList.remove("invalid");
    message.classList.add("valid");
  } else {
    message.textContent = input.title || "올바른 형식을 입력해주세요.";
    message.classList.remove("valid");
    message.classList.add("invalid");
  }
}

// 이벤트 연결
document.getElementById("id").addEventListener("input", () => {
  validateInput("id", "idMessage", "사용 가능한 아이디입니다!");
});
document.getElementById("passwd").addEventListener("input", () => {
  validateInput("passwd", "passwdMessage", "사용 가능한 비밀번호입니다!");
});
document.getElementById("registrationNumber").addEventListener("input", () => {
  validateInput("registrationNumber", "regMessage", "형식이 올바른 주민등록번호입니다!");
});
document.getElementById("phoneNumber").addEventListener("input", () => {
  validateInput("phoneNumber", "phoneMessage", "형식이 올바른 전화번호입니다!");
});
document.getElementById("email").addEventListener("input", () => {
  validateInput("email", "emailMessage", "사용 가능한 이메일입니다!");
});
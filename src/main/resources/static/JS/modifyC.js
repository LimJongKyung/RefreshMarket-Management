// 카카오 주소 검색
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('baseAddress').value = data.address;
        }
    }).open();
}

// 주소 결합
function combineAddress() {
    const base = document.getElementById('baseAddress').value.trim();
    const detail = document.getElementById('detailAddress').value.trim();
    const full = detail !== '' ? `${base} ${detail}` : base;

    if (!base) {
        alert("주소를 입력해주세요.");
        return false;
    }

    document.getElementById('address').value = full;
    return true;
}

// 폼 검증
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

    return true;
}

document.addEventListener('DOMContentLoaded', () => {
    const searchBtn = document.getElementById('searchBenefitBtn');
    const benefitSearch = document.getElementById('benefitSearch');
    const benefitResults = document.getElementById('benefitResults');
    const customerBenefitList = document.getElementById('customerBenefitList');
    const benefitsInput = document.getElementById('benefitsInput');

    // --- hidden input 업데이트 ---
    function updateHiddenInput() {
        const benefits = Array.from(customerBenefitList.querySelectorAll('li'))
            .map(li => `${li.dataset.name}||${li.dataset.description || ''}`)
            .join(',');
        benefitsInput.value = benefits;
    }

    // --- 혜택 삭제 ---
    function removeBenefit(li) {
        li.remove();
        updateHiddenInput();
    }

    // --- 혜택 추가 ---
    function addBenefit(name, description) {
        // 중복 체크
        const exists = Array.from(customerBenefitList.querySelectorAll('li'))
            .some(li => li.dataset.name === name);
        if (exists) return;

        const li = document.createElement('li');
        li.dataset.name = name;
        li.dataset.description = description || '';

        li.innerHTML = `
            <span>${name}</span>
            <input type="text" value="${li.dataset.description}" placeholder="설명 입력" />
            <button type="button">삭제</button>
        `;

        const input = li.querySelector('input');
        const removeBtn = li.querySelector('button');

        input.addEventListener('input', () => {
            li.dataset.description = input.value;
            updateHiddenInput();
        });

        removeBtn.addEventListener('click', () => removeBenefit(li));

        customerBenefitList.appendChild(li);
        updateHiddenInput();
    }

    // --- 기존 혜택 초기화 시 이벤트 붙이기 ---
    customerBenefitList.querySelectorAll('li').forEach(li => {
        const input = li.querySelector('input');
        const removeBtn = li.querySelector('button.removeBenefitBtn');

        li.dataset.description = input.value || '';

        input.addEventListener('input', () => {
            li.dataset.description = input.value;
            updateHiddenInput();
        });

        if (removeBtn) {
            removeBtn.addEventListener('click', () => removeBenefit(li));
        }
    });

    // --- 검색 버튼 클릭 ---
    searchBtn.addEventListener('click', async () => {
        const query = benefitSearch.value.trim();
        if (!query) return;

        try {
            const response = await fetch(`/customer/search?keyword=${encodeURIComponent(query)}`);
            const data = await response.json();

            benefitResults.innerHTML = '';
            data.forEach(b => {
                const li = document.createElement('li');
                li.textContent = `${b.name} (${b.description}) `;

                const addBtn = document.createElement('button');
                addBtn.type = 'button';
                addBtn.textContent = '추가';
                addBtn.addEventListener('click', () => addBenefit(b.name, b.description));

                li.appendChild(addBtn);
                benefitResults.appendChild(li);
            });
        } catch (err) {
            console.error('검색 중 오류 발생:', err);
        }
    });

    // --- 초기 hidden input 세팅 ---
    updateHiddenInput();
});

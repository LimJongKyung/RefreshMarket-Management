// ====== 상품 검색 ======
const productSearchInput = document.getElementById('productSearch');
const productResults = document.getElementById('productResults');
const searchProductBtn = document.getElementById('searchProductBtn');

searchProductBtn.addEventListener('click', () => {
  const keyword = productSearchInput.value.trim();
  if (!keyword) {
    productResults.innerHTML = '';
    return;
  }

  fetch(`/benefits/search?keyword=${encodeURIComponent(keyword)}`)
    .then(res => res.json())
    .then(products => {
      productResults.innerHTML = '';
      products.forEach(p => {
        const li = document.createElement('li');
        li.textContent = p.name; // 상품 이름 필드
        li.style.cursor = 'pointer';
        li.addEventListener('click', () => {
          productSearchInput.value = p.name;
          productResults.innerHTML = '';
        });
        productResults.appendChild(li);
      });
    });
});

// ====== 설명 생성 ======
function buildDescription() {
  const target = document.getElementById('benefitTarget').value;
  const type = document.getElementById('benefitType').value;
  const value = document.getElementById('discountValue').value;
  const product = document.getElementById('productSearch').value;

  let desc = target + ' ' + type;
  if (value) {
    desc += (type === '금액 할인') ? ` ${value}원` : ` ${value}%`;
  }
  if (target === '특정 상품' && product) desc += `: ${product}`;
  return desc;
}

// ====== 특정 상품 선택 시 검색창 표시 ======
document.getElementById('benefitTarget').addEventListener('change', () => {
  const target = document.getElementById('benefitTarget').value;
  document.getElementById('productSearchDiv').style.display =
    (target === '특정 상품') ? 'block' : 'none';
});

// ====== 할인 타입 단위 표시 변경 ======
document.getElementById('benefitType').addEventListener('change', () => {
  const type = document.getElementById('benefitType').value;
  document.getElementById('discountUnit').textContent =
    (type === '금액 할인') ? '원' : '%';
});

// ====== 혜택 목록 로딩 ======
function loadBenefits() {
  fetch('/benefits')
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById('benefitTable');
      tbody.innerHTML = '';
      data.forEach(b => {
        const row = document.createElement('tr');
        row.innerHTML = `
          <td>${b.name}</td>
          <td>${b.description}</td>
          <td><button onclick="deleteBenefit('${b.name}')">삭제</button></td>
        `;
        tbody.appendChild(row);
      });
    });
}

// ====== 생성 ======
document.getElementById('createBtn').addEventListener('click', () => {
  const name = document.getElementById('benefitName').value;
  const description = buildDescription();

  if (!name) {
    alert("혜택 이름을 입력하세요!");
    return;
  }

  fetch('/benefits/new', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, description })
  }).then(() => {
    loadBenefits();
    document.getElementById('benefitName').value = '';
    document.getElementById('discountValue').value = '';
    document.getElementById('productSearch').value = '';
  });
});

// ====== 삭제 ======
function deleteBenefit(name) {
  if (!confirm(`'${name}' 혜택을 삭제하시겠습니까?`)) return;

  fetch('/benefits/delete', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name })
  }).then(() => loadBenefits());
}

// ====== 초기 로딩 ======
loadBenefits();
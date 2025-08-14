document.getElementById('searchForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const keyword = document.getElementById('keyword').value;
    const searchType = document.getElementById('searchType').value;

    fetch(`/customer/search/ajax?keyword=${encodeURIComponent(keyword)}&searchType=${searchType}&page=0&size=20`)
        .then(response => response.json())
        .then(data => {
            const ul = document.getElementById('customerList');
            ul.innerHTML = '';

            if (data.content.length === 0) {
                ul.innerHTML = '<li>검색 결과 없음</li>';
                return;
            }

            data.content.forEach(customer => {
                const li = document.createElement('li');
                li.textContent = `${customer.name} (${customer.id})`;
                ul.appendChild(li);
            });
        })
        .catch(error => {
            console.error('검색 오류:', error);
        });
});
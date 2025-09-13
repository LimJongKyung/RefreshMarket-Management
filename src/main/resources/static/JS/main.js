document.addEventListener("DOMContentLoaded", function () {

    // 1. 주문 통계
    if (Array.isArray(orderStats) && orderStats.length > 0) {
        const labels = orderStats.map(item => item.orderDate);
        const customerCounts = orderStats.map(item => item.customerCount);
        const totalSums = orderStats.map(item => item.totalSum);

        const ctx = document.getElementById('orderChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: '주문 고객 수',
                        data: customerCounts,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    },
                    {
                        label: '총 주문 합계',
                        data: totalSums,
                        backgroundColor: 'rgba(255, 99, 132, 0.6)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1,
                        type: 'line',
                        yAxisID: 'y1'
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '주문 고객 수' } },
                    y1: {
                        beginAtZero: true,
                        position: 'right',
                        title: { display: true, text: '총 주문 합계' },
                        grid: { drawOnChartArea: false }
                    }
                }
            }
        });
    }

    // 2. 가입자 통계
    if (Array.isArray(memberStats) && memberStats.length > 0) {
        const labels = memberStats.map(item => item.day);
        const counts = memberStats.map(item => item.count);

        const ctx2 = document.getElementById('memberChart').getContext('2d');
        new Chart(ctx2, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: '신규 가입자 수',
                    data: counts,
                    fill: false,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    tension: 0.1
                }]
            }
        });
    }

    // 3. 문의사항 통계
    if (Array.isArray(requestStats) && requestStats.length > 0) {
        const labels = requestStats.map(item => item.day);
        const counts = requestStats.map(item => item.count);

        const ctx3 = document.getElementById('requestChart').getContext('2d');
        new Chart(ctx3, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '문의사항 수',
                    data: counts,
                    backgroundColor: 'rgba(153, 102, 255, 0.6)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '요청 수' } }
                }
            }
        });
    }

    // 4. 일별 상품 판매량
    if (Array.isArray(dailyProductSales) && dailyProductSales.length > 0) {
        const labels = dailyProductSales.map(item => item.orderDate + " / " + item.productName);
        const quantities = dailyProductSales.map(item => item.totalQuantity);

        const ctx4 = document.getElementById('dailyProductChart').getContext('2d');
        new Chart(ctx4, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '판매 수량',
                    data: quantities,
                    backgroundColor: 'rgba(255, 206, 86, 0.6)',
                    borderColor: 'rgba(255, 206, 86, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '판매 수량' } }
                }
            }
        });
    }

    // 5. 성별/연령 주문 통계
    if (Array.isArray(genderStats) && genderStats.length > 0) {
        const labels = genderStats.map(item => item.gender + " / " + item.age + "세");
        const counts = genderStats.map(item => item.orderCount);

        const ctx5 = document.getElementById('genderAgeChart').getContext('2d');
        new Chart(ctx5, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '주문 수',
                    data: counts,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true, title: { display: true, text: '주문 수' } }
                }
            }
        });
    }

});


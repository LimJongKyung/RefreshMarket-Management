document.addEventListener('DOMContentLoaded', () => {
    const additionalImagesInput = document.getElementById('additionalImages');
    const typesContainer = document.getElementById('additionalImageTypesContainer');

    additionalImagesInput.addEventListener('change', () => {
        // 기존 hidden input 제거
        typesContainer.innerHTML = '';

        // 선택한 파일 수만큼 hidden input 생성
        for (let i = 0; i < additionalImagesInput.files.length; i++) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'additionalImageTypes';
            input.value = 'ADDITIONAL'; // 필요하면 여기서 타입 조절 가능
            typesContainer.appendChild(input);
        }
    });
});

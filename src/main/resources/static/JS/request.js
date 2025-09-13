function saveAnswer(requestId) {
    const answer = document.getElementById('answerTextarea').value;

    fetch(`/requests/${requestId}/answer`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `answer=${encodeURIComponent(answer)}`
    })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'OK'){
            alert('답변 저장 완료');
            // 기존 버튼 유지, 텍스트만 업데이트
            const existingDiv = document.getElementById('existingAnswer');
            existingDiv.querySelector('p').textContent = answer || '등록된 답변이 없습니다.';
        } else {
            alert('저장 실패');
        }
    })
    .catch(err => {
        console.error(err);
        alert('오류 발생');
    });
}

function deleteAnswer(requestId) {
    if(!confirm('정말 삭제하시겠습니까?')) return;

    fetch(`/requests/${requestId}/answer/delete`, { method: 'POST' })
    .then(res => res.json())
    .then(data => {
        if(data.status === 'OK'){
            alert('답변 삭제 완료');
            const existingDiv = document.getElementById('existingAnswer');
            existingDiv.querySelector('p').textContent = '등록된 답변이 없습니다.';
            document.getElementById('answerTextarea').value = '';
        } else {
            alert('삭제 실패');
        }
    })
    .catch(err => {
        console.error(err);
        alert('오류 발생');
    });
}

function cancelEdit() {
    const textarea = document.getElementById('answerTextarea');
    const existingAnswer = document.querySelector('#existingAnswer p');
    textarea.value = existingAnswer ? existingAnswer.textContent : '';
}

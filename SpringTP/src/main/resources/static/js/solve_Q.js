let codeMirrorInstance;

document.addEventListener("DOMContentLoaded", () => {
  makeDraggable(document.querySelector(".splitter.vertical"), "vertical");
  makeDraggable(document.querySelector(".splitter.horizontal"), "horizontal");

  // CodeMirror 초기화
  codeMirrorInstance = CodeMirror.fromTextArea(document.getElementById("codeEditor"), {
    lineNumbers: true,
    mode: "text/x-csrc",
    theme: "eclipse",
    indentUnit: 4,
    tabSize: 4,
    matchBrackets: true,
    viewportMargin: Infinity,
  });

  codeMirrorInstance.setSize(null, "100%");

  // 언어 선택에 따라 코드 문법 변경
  const langSelector = document.getElementById("languageSelector");
  langSelector.addEventListener("change", (e) => {
    const lang = e.target.value;
    let mode = "text/x-csrc";
    if (lang === "java") mode = "text/x-java";
    else if (lang === "py") mode = "python";
    else if (lang === "cpp") mode = "text/x-c++src";
    codeMirrorInstance.setOption("mode", mode);
  });

  // 문제/제출 내역 toggle
  const toggleBtn = document.getElementById("toggleViewBtn");
  const problemView = document.getElementById("problemView");
  const submissionView = document.getElementById("submissionView");

  toggleBtn.addEventListener("click", () => {
    const isProblemVisible = problemView.style.display !== "none";
    problemView.style.display = isProblemVisible ? "none" : "block";
    submissionView.style.display = isProblemVisible ? "block" : "none";
    toggleBtn.innerText = isProblemVisible ? "문제 보기 ⟷" : "제출 내역 보기 ⟷";
  });

  // 제출 내역 펼치기/닫기
  document.querySelectorAll(".submission-row").forEach((row, index) => {
    row.addEventListener("click", () => {
      const arrow = row.querySelector(".toggle-arrow");
      const codeRow = document.querySelectorAll(".submission-code-row")[index];
      const isOpen = codeRow.style.display === "table-row";

      codeRow.style.display = isOpen ? "none" : "table-row";
      arrow.textContent = isOpen ? "▶" : "▼";
    });
  });

  // 제출 버튼 클릭 시 모달 표시
  document.querySelector(".submit-btn").addEventListener("click", () => {
    // 임시 정답 판별 로직 (실제 채점 결과로 대체 가능)
    const isCorrect = Math.random() > 0.5;
    showResultModal(isCorrect);
  });

  // 모달 제어 함수
  function showResultModal(isCorrect) {
    const modal = document.getElementById("resultModal");
    const message = modal.querySelector(".modal-message");
    const actionBtn = modal.querySelector("#modalActionBtn");

    if (isCorrect) {
      message.textContent = "정답입니다!";
      actionBtn.textContent = "확인";
      actionBtn.onclick = () => modal.style.display = "none";
    } else {
      message.textContent = "틀렸습니다!";
      actionBtn.textContent = "다시 풀기";
      actionBtn.onclick = () => modal.style.display = "none"; // 또는 location.reload();
    }

    modal.style.display = "flex";
  }
});

function makeDraggable(splitter, type) {
  let isDragging = false;

  splitter.addEventListener("mousedown", (e) => {
    e.preventDefault();
    isDragging = true;

    const moveHandler = (e) => {
      if (!isDragging) return;

      if (type === "vertical") {
        const container = document.querySelector(".horizontal-split");
        const left = container.querySelector(".left-pane");
        const right = container.querySelector(".right-pane");

        const containerRect = container.getBoundingClientRect();
        const newLeftWidth = e.clientX - containerRect.left;

        const minLeftWidth = 200;
        const minRightWidth = 300;
        const totalWidth = containerRect.width;

        if (newLeftWidth >= minLeftWidth && (totalWidth - newLeftWidth) >= minRightWidth) {
          left.style.flex = `0 0 ${newLeftWidth}px`;
          right.style.flex = `1 1 ${totalWidth - newLeftWidth - splitter.offsetWidth}px`;
        }
      } else if (type === "horizontal") {
        const container = document.querySelector(".vertical-split");
        const top = container.querySelector(".top-pane");
        const bottom = container.querySelector(".bottom-pane");

        const containerRect = container.getBoundingClientRect();
        const newTopHeight = e.clientY - containerRect.top;

        const minTopHeight = 100;
        const minBottomHeight = 100;
        const totalHeight = containerRect.height;

        if (newTopHeight >= minTopHeight && (totalHeight - newTopHeight) >= minBottomHeight) {
          top.style.flex = `0 0 ${newTopHeight}px`;
          bottom.style.flex = `0 0 ${totalHeight - newTopHeight - splitter.offsetHeight}px`;
        }
      }
    };

    const stopDrag = () => {
      isDragging = false;
      document.removeEventListener("mousemove", moveHandler);
      document.removeEventListener("mouseup", stopDrag);
    };

    document.addEventListener("mousemove", moveHandler);
    document.addEventListener("mouseup", stopDrag);
  });
  
  
  document.querySelector('.submit-btn').addEventListener('click', function() {
      const userCode = codeMirrorInstance.getValue();  // CodeMirror에서 사용자 코드 가져오기
      const language = document.getElementById('languageSelector').value;  // 선택한 언어
      const input = document.getElementById('inputEx').textContent;  // 문제 입력값
      const qid = document.getElementById('problemView').getAttribute('data-qid');  // 문제 ID 가져오기
	  
      // 서버로 코드 제출 요청
      fetch('/submit-code', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({
              code: userCode,  // 사용자 코드
              language: language,  // 프로그래밍 언어
              input: input,  // 문제 입력값
              qid: qid,  // 문제 ID
			  userId: null,
			  userType: null
          })
      })
      .then(response => response.json())
      .then(data => {
          // 실행 결과를 결과 박스에 표시
          document.querySelector('.result-box').textContent = `실행 결과: ${data.result}`;

          // 정답 여부에 따라 피드백
          if (data.isCorrect) {
              document.querySelector('.modal-message').textContent = "정답입니다!";
              document.getElementById('resultModal').style.display = 'block';
          } else {
              document.querySelector('.modal-message').textContent = "오답입니다!";
              document.getElementById('resultModal').style.display = 'block';
          }
      })
      .catch(error => console.error('Error:', error));
  });


}

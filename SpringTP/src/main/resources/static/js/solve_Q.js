let codeMirrorInstance;

document.addEventListener("DOMContentLoaded", () => {
  makeDraggable(document.querySelector(".splitter.vertical"), "vertical");
  makeDraggable(document.querySelector(".splitter.horizontal"), "horizontal");

  // 언어 선택에 따라 코드 문법 변경
  const langSelector = document.getElementById("languageSelector");
  const codeWrite = document.getElementById("codeEditor"); // textarea

  // 언어 선택 시 코드 템플릿을 설정
  function setCodeTemplate(lang) {
      if (lang === "java") {
          codeWrite.value = 
          "//import를 추가하세요.\n" +
          "\n" +
		  "//클래스명은 반드시 Solution으로 하세요\n" +
          "public class Solution {\n" +
          "    public static void main(String[] args) {\n" +
          "        // 여기에 코드를 작성하세요.\n" +
          "    }\n" +
          "}";
      } else if (lang === "py") {
          codeWrite.value = "#여기에 코드를 작성하세요."; // Python 코드 템플릿
      } else if (lang === "cpp") {
          codeWrite.value = 
          "//import를 추가하세요.\n" +
          "\n" +
          "int main() {\n" +
          "    // 여기에 코드를 작성하세요.\n" +
          "}";
      }
  }

  // 처음 언어 선택에 따라 코드 템플릿을 설정
  setCodeTemplate(langSelector.value);
 

  langSelector.addEventListener("change", (e) => {
	const codeWrite = document.getElementById("codeEditor"); // textarea
    const lang = e.target.value;
    let mode = "text/x-csrc";

	// 언어에 맞는 코드 템플릿과 문법 모드 설정
	setCodeTemplate(lang);
	
    if (lang === "java"){ 
		mode = "text/x-java";
	}
    else if (lang === "py"){ 
		mode = "python";
	}
    else if (lang === "cpp"){
		 mode = "text/x-c++src";
	}
    codeMirrorInstance.setOption("mode", mode);
	codeMirrorInstance.setValue(codeWrite.value); // 코드 템플릿을 CodeMirror로 업데이트
  });
  
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
		  
		  arrow.textContent = isOpen ? "▶" : "▼";

          // 숨겨진 tr을 클릭한 tr 바로 아래에 표시
          codeRow.style.display = isOpen ? "none" : "table-row";
          
          // 클릭한 tr 바로 뒤에 숨겨진 tr을 보이게 함
          if (!isOpen) {
              row.insertAdjacentElement('afterend', codeRow);
          }
      });
  });

/*  document.querySelectorAll(".submission-row").forEach((row, index) => {
    row.addEventListener("click", () => {
      const arrow = row.querySelector(".toggle-arrow");
      const codeRow = document.querySelectorAll(".submission-code-row")[index];
      const isOpen = codeRow.style.display === "table-row";

      codeRow.style.display = isOpen ? "none" : "table-row";
      arrow.textContent = isOpen ? "▶" : "▼";
    });
  });*/
  
  //코드 실행버튼
    document.querySelector(".submit-btns").addEventListener("click", () => {
  	const userCode = codeMirrorInstance.getValue();  // CodeMirror에서 사용자 코드 가져오기
  	const language = document.getElementById('languageSelector').value;  // 선택한 언어
  	const input = document.getElementById('inputEx').textContent;  // 문제 입력값
  	const qid = document.getElementById('problemView').getAttribute('data-qid');  // 문제 ID 가져오기
  	console.log(userCode);
  	console.log(language);
  	console.log(input);
  	console.log(qid);
  	
  	// userCode가 비어있으면 fetch 요청을 하지 않고 종료
  	if (userCode.trim() === "") {
  	    alert("코드를 작성해주세요!");  // 비어있을 때 경고 메시지
  	    return;  // fetch 요청을 하지 않고 종료
  	}

  	// 서버로 코드 제출 요청
  	fetch('/run_code', {
  	    method: 'POST',
  	    headers: {
  	        'Content-Type': 'application/json',
  	    },
  	    body: JSON.stringify({
  	        code: userCode,  // 사용자 코드
  	        language: language,  // 프로그래밍 언어
  	        input: input,  // 문제 입력값
  	        qid: qid,  // 문제 ID
  		  userId: 'skrjsdl03',
  		  userType: '일반'
  	    })
  	})
  	.then(response => response.json())
  	.then(data => {
  	    // 실행 결과를 결과 박스에 표시
  		if(data.isCorrect){
  			document.querySelector('.result-box').textContent = `실행 결과: 성공`;
  		}else{
  			document.querySelector('.result-box').textContent = `실행 결과: 실패`;
  		}
  	   /* document.querySelector('.result-box').textContent = `실행 결과: ${data.result}`;*/
  		// 정답 여부에 따라 피드백
  		/*showResultModal(data.isCorrect);*/
  		// 코드 실행 후 코드 필드 비우기
  /*		codeMirrorInstance.setValue('');  // CodeMirror 에디터 내용을 비웁니다.*/
  		

  	})
  	.catch(error => console.error('Error:', error));
    });

  // 제출 버튼 클릭 시 모달 표시
  document.querySelector(".submit-btn").addEventListener("click", () => {
	const userCode = codeMirrorInstance.getValue();  // CodeMirror에서 사용자 코드 가져오기
	const language = document.getElementById('languageSelector').value;  // 선택한 언어
	const input = document.getElementById('inputEx').textContent;  // 문제 입력값
	const qid = document.getElementById('problemView').getAttribute('data-qid');  // 문제 ID 가져오기
	console.log(userCode);
	console.log(language);
	console.log(input);
	console.log(qid);
	
	// userCode가 비어있으면 fetch 요청을 하지 않고 종료
	if (userCode.trim() === "") {
	    alert("코드를 작성해주세요!");  // 비어있을 때 경고 메시지
	    return;  // fetch 요청을 하지 않고 종료
	}

	    // 세션에서 사용자 정보 가져오기
	    fetch('/api/user/current')
	        .then(response => response.json())
	        .then(userData => {
	            const userId = userData.username;  // 사용자 ID
	            const userType = userData.userType;  // 사용자 타입

	            // 서버로 코드 제출 요청
	            fetch('/submit_code', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/json',
	                },
	                body: JSON.stringify({
	                    code: userCode,  // 사용자 코드
	                    language: language,  // 프로그래밍 언어
	                    input: input,  // 문제 입력값
	                    qid: qid,  // 문제 ID
	                    userId: userId,  // 로그인된 사용자 ID
	                    userType: userType,  // 로그인된 사용자 타입
	                })
	            })
	            .then(response => response.json())
	            .then(data => {
	                // 실행 결과를 결과 박스에 표시
	                if (data.isCorrect) {
	                    document.querySelector('.result-box').textContent = `실행 결과: 성공`;
	                } else {
	                    document.querySelector('.result-box').textContent = `실행 결과: 실패`;
	                }
	                // 정답 여부에 따라 피드백
	                showResultModal(data.isCorrect);
	            })
	            .catch(error => console.error('Error:', error));
	        })
	        .catch(error => console.error('Error getting user data:', error));
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
  


}

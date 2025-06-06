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
    viewportMargin: Infinity,  // 높이 자동 확장 제거
  });

  codeMirrorInstance.setSize(null, "100%");
  
  // 언어 선택에 따라 코드 문법 변경
  const langSelector = document.getElementById("languageSelector");
  langSelector.addEventListener("change", (e) => {
    const lang = e.target.value;
    let mode = "text/x-csrc";
    if (lang === "JAVA") mode = "text/x-java";
    else if (lang === "Python") mode = "python";
    else if (lang === "C") mode = "text/x-csrc";
    codeMirrorInstance.setOption("mode", mode);
  });
  
  const toggleBtn = document.getElementById("toggleViewBtn");
  const problemView = document.getElementById("problemView");
  const submissionView = document.getElementById("submissionView");

  toggleBtn.addEventListener("click", () => {
    const isProblemVisible = problemView.style.display !== "none";
    problemView.style.display = isProblemVisible ? "none" : "block";
    submissionView.style.display = isProblemVisible ? "block" : "none";
    toggleBtn.innerText = isProblemVisible ? "문제 보기 ⟷" : "제출 내역 보기 ⟷";
  });
  
  document.querySelectorAll(".submission-row").forEach((row, index) => {
    row.addEventListener("click", () => {
      const arrow = row.querySelector(".toggle-arrow");
      const codeRow = document.querySelectorAll(".submission-code-row")[index];
      const isOpen = codeRow.style.display === "table-row";

      codeRow.style.display = isOpen ? "none" : "table-row";
      arrow.textContent = isOpen ? "▶" : "▼";
    });
  });
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

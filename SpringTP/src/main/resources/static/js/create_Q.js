document.addEventListener("DOMContentLoaded", () => {
  // 예제 추가 기능
  const addBtn = document.getElementById("add-example");

  addBtn.addEventListener("click", () => {
    const form = document.querySelector(".create-form");

    const inputLabel = document.createElement("label");
    inputLabel.textContent = `예제 입력`;
    const inputArea = document.createElement("textarea");
    inputArea.placeholder = "한 줄씩 또는 공백 포함 입력 가능";

    const outputLabel = document.createElement("label");
    outputLabel.textContent = `예제 출력`;
    const outputArea = document.createElement("textarea");
    outputArea.placeholder = "출력 결과를 그대로 기재해주세요.";

    const explainLabel = document.createElement("label");
    explainLabel.textContent = `예제 설명`;
    const explainArea = document.createElement("textarea");
    explainArea.placeholder = "어떻게 도출됐는지 간단한 설명을 적어주세요.";

    form.insertBefore(explainArea, addBtn);
    form.insertBefore(explainLabel, explainArea);
    form.insertBefore(outputArea, explainLabel);
    form.insertBefore(outputLabel, outputArea);
    form.insertBefore(inputArea, outputLabel);
    form.insertBefore(inputLabel, inputArea);
  });

  // 제약조건 선택 시 단위 표시
  const limitType = document.getElementById("limitType");
  const limitValue = document.getElementById("limitValue");

  limitType.addEventListener("change", () => {
    const selected = limitType.value;
    limitValue.style.display = "block";

    if (selected === "time") {
      limitValue.placeholder = "단위: 초";
    } else if (selected === "memory") {
      limitValue.placeholder = "단위: MB";
    } else {
      limitValue.style.display = "none";
    }
  });

  // 이미지 업로드: 최대 5장, 미리보기
  const imageInput = document.getElementById("imageInput");
  const previewContainer = document.getElementById("imagePreviewContainer");
  const addImageBox = document.getElementById("addImageBox");

  let imageFiles = [];

  imageInput.addEventListener("change", (e) => {
    const files = Array.from(e.target.files);
    const remaining = 5 - imageFiles.length;

    if (files.length > remaining) {
      alert(`최대 5장까지 첨부 가능합니다. (${remaining}장 더 첨부 가능)`);
      return;
    }

    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = (event) => {
        const img = document.createElement("img");
        img.src = event.target.result;
        previewContainer.appendChild(img);
      };
      reader.readAsDataURL(file);
      imageFiles.push(file);
    });

    if (imageFiles.length >= 5) {
      addImageBox.style.display = "none";
    }
  });
  
  document.querySelector(".submit-btn").addEventListener("click", (e) => {
    e.preventDefault();  // 실제 제출 방지

    // 예시: 단순 모달 출력
    const modal = document.getElementById("resultModal");
    const message = modal.querySelector(".modal-message");
    const actionBtn = modal.querySelector("#modalActionBtn");

    message.textContent = "출제 문제 검토 요청되었습니다.";
    actionBtn.textContent = "확인";
    actionBtn.onclick = () => modal.style.display = "none";

    modal.style.display = "flex";
  });

});

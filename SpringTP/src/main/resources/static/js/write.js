document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("postForm");
  const imageInput = document.getElementById("imageInput");
  const imagePreviewContainer = document.getElementById("imagePreviewContainer");
  const addImageBox = document.getElementById("addImageBox");

  let imageFiles = [];

  // 파일 선택 시
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
        imagePreviewContainer.appendChild(img);
      };
      reader.readAsDataURL(file);
      imageFiles.push(file);
    });

    if (imageFiles.length >= 5) {
      addImageBox.style.display = "none";
    }
  });

  // 폼 제출
  if (form) {
    form.addEventListener("submit", (e) => {
      e.preventDefault();

      const title = form.title.value.trim();
      const content = form.content.value.trim();

      if (!title || !content) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
      }

      alert("게시글이 작성되었습니다. (연결 예정)");
      window.location.href = "/board/free";
    });
  }
});

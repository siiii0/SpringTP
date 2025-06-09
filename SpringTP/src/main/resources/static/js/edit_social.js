document.addEventListener("DOMContentLoaded", () => {
  const editBtn = document.querySelector(".edit-profile-btn");
  const profileImg = document.querySelector(".edit-profile-img");
  const fileInput = document.getElementById("profileImageInput");

  editBtn?.addEventListener("click", () => {
    fileInput.click();
  });

  fileInput?.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith("image/")) {
      const reader = new FileReader();
      reader.onload = function (event) {
        profileImg.src = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  });

  const form = document.querySelector(".edit-form");
  const nameInput = document.getElementById("name");
  const emailInput = document.getElementById("email");
  const editSuccessModal = document.getElementById("editSuccessModal");
  const editConfirmBtn = editSuccessModal?.querySelector(".modal-confirm-btn");

  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const name = nameInput.value.trim();
    const email = emailInput.value.trim();

    if (name === "") {
      alert("이름을 입력해주세요.");
      nameInput.focus();
      return;
    }

    if (email === "" || !validateEmail(email)) {
      alert("유효한 이메일을 입력해주세요.");
      emailInput.focus();
      return;
    }

    editSuccessModal.style.display = "flex";
  });

  editConfirmBtn?.addEventListener("click", () => {
    editSuccessModal.style.display = "none";
  });

  const withdrawBtn = document.querySelector(".withdraw-btn");
  const withdrawText = document.querySelector(".withdraw-text");
  const modal = document.getElementById("withdrawModal");
  const cancelBtn = document.querySelector(".modal-cancel-btn");
  const confirmBtn = document.querySelector(".modal-confirm-btn");

  // 회원탈퇴 모달 열기 (버튼 or 텍스트)
  withdrawBtn?.addEventListener("click", () => {
    modal.style.display = "flex";
  });

  withdrawText?.addEventListener("click", (e) => {
    e.preventDefault();
    modal.style.display = "flex";
  });

  cancelBtn?.addEventListener("click", () => {
    modal.style.display = "none";
  });

  confirmBtn?.addEventListener("click", () => {
    modal.style.display = "none";
  });

  function validateEmail(email) {
    const regex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    return regex.test(email);
  }
});

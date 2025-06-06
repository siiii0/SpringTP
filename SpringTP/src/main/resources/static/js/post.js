document.addEventListener("DOMContentLoaded", () => {
  // 댓글 등록 버튼 클릭
  document.querySelector(".comment-box button")?.addEventListener("click", () => {
    alert("댓글 등록 기능은 추후 추가");
  });

  // 신고 아이콘 클릭 이벤트
  const reportIcon = document.querySelector(".report-icon");
  const reportModal = document.getElementById("reportModal");
  const confirmBtn = document.getElementById("reportConfirmBtn");
  const cancelBtn = document.getElementById("reportCancelBtn");

  if (reportIcon && reportModal) {
    reportIcon.addEventListener("click", () => {
      reportModal.style.display = "flex";
    });

    cancelBtn?.addEventListener("click", () => {
      reportModal.style.display = "none";
    });

    confirmBtn?.addEventListener("click", () => {
      alert("신고가 접수되었습니다.");
      reportModal.style.display = "none";
    });
  }
});

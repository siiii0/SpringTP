document.addEventListener("DOMContentLoaded", () => {
  const writeBtn = document.querySelector(".write-btn");
  if (writeBtn) {
    writeBtn.addEventListener("click", () => {
      window.location.href = "/board/write";
    });
  }

  // 게시글 클릭 시 해당 ID를 기반으로 상세 페이지로 이동
  document.querySelectorAll(".post-item").forEach(item => {
    item.addEventListener("click", () => {
      const postId = item.getAttribute("data-id");
      if (postId) {
        window.location.href = `/board/post?id=${postId}`;
      }
    });
  });

  // 페이지네이션 기능
  const pagination = document.querySelector(".pagination");
  const pages = pagination.querySelectorAll("a");

  let currentPage = 1;

  function setActivePage(pageNum) {
    pages.forEach((page) => page.classList.remove("active"));

    pages[pageNum].classList.add("active");
    currentPage = pageNum;
  }

  pages.forEach((page, index) => {
    page.addEventListener("click", (e) => {
      e.preventDefault();

      const label = page.textContent.trim();

      if (label === "Prev") {
        if (currentPage > 1) setActivePage(currentPage - 1);
      } else if (label === "Next") {
        if (currentPage < 3) setActivePage(currentPage + 1); // 페이지 수 3 기준
      } else {
        setActivePage(index); // 1페이지: index 1, 2페이지: index 2 ...
      }
    });
  });
});

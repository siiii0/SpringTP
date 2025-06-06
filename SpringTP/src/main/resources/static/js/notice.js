// notice.js
document.addEventListener("DOMContentLoaded", () => {
  const writeBtn = document.querySelector(".write-btn");
  if (writeBtn) {
    writeBtn.addEventListener("click", () => {
      window.location.href = "/board/notice/write";
    });
  }

  document.querySelectorAll(".post-item").forEach((item) => {
    item.addEventListener("click", () => {
      const postId = item.getAttribute("data-id");
      if (postId) {
        window.location.href = `/board/post?id=${postId}`;
      }
    });
  });

  const pagination = document.querySelector(".pagination");
  if (pagination) {
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

        if (label === "Prev" && currentPage > 1) {
          setActivePage(currentPage - 1);
        } else if (label === "Next" && currentPage < pages.length - 2) {
          setActivePage(currentPage + 1);
        } else if (!isNaN(Number(label))) {
          setActivePage(index);
        }
      });
    });
  }
});

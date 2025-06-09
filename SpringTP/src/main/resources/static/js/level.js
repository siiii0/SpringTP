document.addEventListener("DOMContentLoaded", () => {
  const filter = document.querySelector(".filter-bar select");
  const tableBody = document.querySelector(".ranking-table tbody");
  const allRows = Array.from(tableBody.querySelectorAll("tr")); // 원본 백업

  const gradeMap = {
    "브론즈": "Bronze",
    "실버": "Silver",
    "골드": "Gold",
    "플래티넘": "Platinum",
    "다이아": "Diamond",
    "루비": "Ruby"
  };

  if (filter) {
    filter.addEventListener("change", () => {
      const selected = filter.value;
      let filteredRows = [...allRows];

      // 등급 필터링
      if (selected !== "전체") {
        const mapped = gradeMap[selected] || selected;
        filteredRows = filteredRows.filter(row => {
          return row.children[2].textContent.trim() === mapped;
        });
      }

      // 순위 다시 매기기
      filteredRows.forEach((row, index) => {
        row.children[0].textContent = index + 1;
      });

      // 테이블 반영
      tableBody.innerHTML = '';
      filteredRows.forEach(row => tableBody.appendChild(row));
    });
  }

  // 페이지네이션
  const pagination = document.querySelector(".pagination");
  if (pagination) {
    const pages = pagination.querySelectorAll("a");
    let currentPage = 1;

    function setActivePage(pageNum) {
      pages.forEach(p => p.classList.remove("active"));
      pages[pageNum].classList.add("active");
      currentPage = pageNum;
      console.log(`현재 페이지: ${currentPage}`);
    }

    pages.forEach((page, index) => {
      page.addEventListener("click", (e) => {
        e.preventDefault();
        const label = page.textContent.trim();

        if (label === "Prev") {
          if (currentPage > 1) setActivePage(currentPage - 1);
        } else if (label === "Next") {
          if (currentPage < pages.length - 2) setActivePage(currentPage + 1);
        } else {
          setActivePage(index);
        }
      });
    });
  }
});

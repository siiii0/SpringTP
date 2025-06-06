document.addEventListener("DOMContentLoaded", () => {
  // 필터 드롭다운 이벤트 + 정렬 적용
  const filter = document.querySelector(".filter-bar select");
  const tableBody = document.querySelector(".ranking-table tbody");

  // 테이블 원본 데이터 복사
  const originalRows = Array.from(tableBody.querySelectorAll("tr"));

  if (filter) {
	filter.addEventListener("change", () => {
	  const selected = filter.value;

	  // 매번 현재 DOM에서 가져오기
	  const rows = Array.from(tableBody.querySelectorAll("tr"));
	  let sortedRows = [...rows];

	  if (selected === "맞은 문제 많은 순") {
	    sortedRows.sort((a, b) => {
	      const aVal = parseInt(a.children[3].textContent);
	      const bVal = parseInt(b.children[3].textContent);
	      return bVal - aVal;
	    });
	  } else if (selected === "총 점수 높은 순") {
	    sortedRows.sort((a, b) => {
	      const aVal = parseInt(a.children[4].textContent);
	      const bVal = parseInt(b.children[4].textContent);
	      return bVal - aVal;
	    });
	  } else if (selected === "정답 비율 높은 순") {
	    sortedRows.sort((a, b) => {
	      const aVal = parseFloat(a.children[5].textContent);
	      const bVal = parseFloat(b.children[5].textContent);
	      return bVal - aVal;
	    });
	  }

	  // 순위 다시 매기기
	  sortedRows.forEach((row, index) => {
	    row.children[0].textContent = index + 1;
	  });

	  // 테이블 반영
	  tableBody.innerHTML = '';
	  sortedRows.forEach(row => tableBody.appendChild(row));
	});
  }

  // 페이지네이션 기능
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

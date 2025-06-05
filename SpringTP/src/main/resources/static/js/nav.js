document.addEventListener("DOMContentLoaded", () => {
  const navItems = document.querySelectorAll(".nav-item");
  const dropdown = document.getElementById("dropdown");
  const dropdownContents = document.querySelectorAll(".dropdown-content");

  navItems.forEach((item) => {
    item.addEventListener("mouseenter", () => {
      const type = item.dataset.menu;

      // 드롭다운 배경 보이기
      dropdown.classList.add("show");

      // 모든 세부 메뉴 숨기기
      dropdownContents.forEach(el => {
        el.classList.remove("active");
        el.style.left = "0px"; // 초기화
      });

      // 현재 메뉴의 세부 항목 활성화
      const target = dropdown.querySelector(`.dropdown-content.${type}`);
      if (target) {
        target.classList.add("active");

        // 대분류 nav-item과 수직 정렬
        const itemRect = item.getBoundingClientRect();
        const dropdownRect = dropdown.getBoundingClientRect();

        const offset = itemRect.left + item.offsetWidth / 2 - dropdownRect.left - target.offsetWidth / 2;
        target.style.left = `${offset}px`;
      }

      // 활성 밑줄 스타일 처리
      navItems.forEach(n => n.classList.remove("active"));
      item.classList.add("active");
    });

    item.addEventListener("mouseleave", () => {
      item.classList.remove("active");
    });
  });

  dropdown.addEventListener("mouseleave", () => {
    dropdown.classList.remove("show");
    dropdownContents.forEach(el => el.classList.remove("active"));
  });
});

// 필터 상태를 관리하는 객체
const currentFilter = {
  level: null,
  language: null,
  sort: null,
};

// 🔽 여기에 추가
let allRowsOriginal = [];

function removeTag(element) {
  const tag = element.parentElement;
  const type = tag.getAttribute("data-type");
  const value = tag.getAttribute("data-value");

  if (type === "sort") {
    currentFilter.sort = null;
  } else if (type === "level" && currentFilter.level) {
    currentFilter.level = currentFilter.level.filter(v => v !== value);
  } else if (type === "language" && currentFilter.language) {
    currentFilter.language = currentFilter.language.filter(v => v !== value);
  }

  tag.remove();
  applyFilterAndSort();
}

function clearAllTags() {
  const tagContainer = document.getElementById("tagContainer");
  const tags = tagContainer.querySelectorAll(".tag");
  tags.forEach(tag => tag.remove());

  const selects = document.querySelectorAll(".filters select");
  selects.forEach(select => {
    select.selectedIndex = 0;
  });

  const searchInput = document.querySelector('.filters input[type="text"]');
  if (searchInput) searchInput.value = "";

  // 필터 상태 초기화
  currentFilter.level = null;
  currentFilter.language = null;
  currentFilter.sort = null;

  applyFilterAndSort();
}

// ✅ 태그 자동 생성 및 분류 실행
function handleSelectChange(selectElement) {
  const value = selectElement.value;
  const label = selectElement.options[0].text;
  const selectId = selectElement.id;

  if (value === label) return;

  const tagContainer = document.getElementById("tagContainer");

  // 태그 타입 결정
  let type;
  if (selectId === "sortSelect") {
    type = "sort";
    currentFilter.sort = value;

    // 정렬 기준은 1개만 → 기존 태그 제거
    const existing = tagContainer.querySelector(`.tag[data-type="sort"]`);
    if (existing) existing.remove();
  } else if (selectId === "levelSelect") {
    type = "level";
    if (!currentFilter.level) currentFilter.level = [];
    if (currentFilter.level.includes(value)) return; // 중복 방지
    currentFilter.level.push(value);
  } else if (selectId === "languageSelect") {
    type = "language";
    if (!currentFilter.language) currentFilter.language = [];
    if (currentFilter.language.includes(value)) return; // 중복 방지
    currentFilter.language.push(value);
  }

  // 새 태그 생성
  const tag = document.createElement("span");
  tag.className = "tag";
  tag.setAttribute("data-type", type);
  tag.setAttribute("data-value", value);
  tag.innerHTML = `${value} <span class="remove-tag" onclick="removeTag(this)">✕</span>`;
  tagContainer.insertBefore(tag, tagContainer.querySelector(".reset"));

  applyFilterAndSort();
}

function applyFilterAndSort() {
  const table = document.querySelector(".question-table tbody");
  if (!table || !allRowsOriginal.length) return;

  let filteredRows = allRowsOriginal.filter(row => {
    const title = row.children[0].textContent.trim(); // 언어 포함
    const level = row.children[1].textContent.trim();

    // 언어 필터: 일부 포함 허용 (예: JAVA 포함된 제목)
    if (currentFilter.language?.length > 0 &&
        !currentFilter.language.some(lang => title.includes(lang))) {
      return false;
    }

    // 난이도 필터: 정확히 일치하는 값만 허용
    if (currentFilter.level?.length > 0 &&
        !currentFilter.level.includes(level)) {
      return false;
    }

    return true;
  });

  let columnIndex, comparator;

  switch (currentFilter.sort) {
    case "정답률 높은 순":
      columnIndex = 3;
      comparator = (a, b) =>
        parseFloat(b.children[columnIndex].textContent) -
        parseFloat(a.children[columnIndex].textContent);
      break;
    case "정답률 낮은 순":
      columnIndex = 3;
      comparator = (a, b) =>
        parseFloat(a.children[columnIndex].textContent) -
        parseFloat(b.children[columnIndex].textContent);
      break;
    case "완료한 사람 많은 순":
      columnIndex = 2;
      comparator = (a, b) =>
        parseInt(b.children[columnIndex].textContent.replace(/,/g, '')) -
        parseInt(a.children[columnIndex].textContent.replace(/,/g, ''));
      break;
    case "최신 등록 순":
      console.log("최신 등록 정렬 미구현");
      return;
  }

  if (comparator) {
    filteredRows.sort(comparator);
  }

  table.innerHTML = "";
  filteredRows.forEach(row => table.appendChild(row));
}

document.addEventListener("DOMContentLoaded", () => {
  const table = document.querySelector(".question-table tbody");
  if (table) {
    allRowsOriginal = Array.from(table.querySelectorAll("tr"));
  }
});

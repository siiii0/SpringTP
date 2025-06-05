const userFilter = {
  level: null,
  language: null,
  sort: null,
};

let userAllRows = [];

function removeUserTag(element) {
  const tag = element.parentElement;
  const type = tag.getAttribute("data-type");
  const value = tag.getAttribute("data-value");

  if (type === "sort") {
    userFilter.sort = null;
  } else if (type === "level" && userFilter.level) {
    userFilter.level = userFilter.level.filter(v => v !== value);
  } else if (type === "language" && userFilter.language) {
    userFilter.language = userFilter.language.filter(v => v !== value);
  }

  tag.remove();
  applyUserFilterAndSort();
}

function clearAllUserTags() {
  const tagContainer = document.getElementById("tagContainer");
  const tags = tagContainer.querySelectorAll(".user-tag");
  tags.forEach(tag => tag.remove());

  document.querySelectorAll(".user-filters select").forEach(select => {
    select.selectedIndex = 0;
  });

  const searchInput = document.querySelector('.user-filters input[type="text"]');
  if (searchInput) searchInput.value = "";

  userFilter.level = null;
  userFilter.language = null;
  userFilter.sort = null;

  applyUserFilterAndSort();
}

function handleUserSelectChange(selectElement) {
  const value = selectElement.value;
  const label = selectElement.options[0].text;
  const selectId = selectElement.id;

  if (value === label) return;

  const tagContainer = document.getElementById("tagContainer");

  let type;
  if (selectId === "sortSelect") {
    type = "sort";
    userFilter.sort = value;
    const existing = tagContainer.querySelector(`.user-tag[data-type="sort"]`);
    if (existing) existing.remove();
  } else if (selectId === "levelSelect") {
    type = "level";
    if (!userFilter.level) userFilter.level = [];
    if (userFilter.level.includes(value)) return;
    userFilter.level.push(value);
  } else if (selectId === "languageSelect") {
    type = "language";
    if (!userFilter.language) userFilter.language = [];
    if (userFilter.language.includes(value)) return;
    userFilter.language.push(value);
  }

  const tag = document.createElement("span");
  tag.className = "user-tag";
  tag.setAttribute("data-type", type);
  tag.setAttribute("data-value", value);
  tag.innerHTML = `${value} <span class="remove-tag" onclick="removeUserTag(this)">✕</span>`;
  tagContainer.insertBefore(tag, tagContainer.querySelector(".reset"));

  applyUserFilterAndSort();
}

function applyUserFilterAndSort() {
  const table = document.querySelector(".user-question-table tbody");
  if (!table || !userAllRows.length) return;

  let filtered = userAllRows.filter(row => {
    const title = row.children[0].textContent.trim();
    const level = row.children[1].textContent.trim();

    if (userFilter.language?.length > 0 &&
        !userFilter.language.some(lang => title.includes(lang))) {
      return false;
    }

    if (userFilter.level?.length > 0 &&
        !userFilter.level.includes(level)) {
      return false;
    }

    return true;
  });

  let columnIndex, comparator;
  switch (userFilter.sort) {
    case "정답률 높은 순":
      columnIndex = 3;
      comparator = (a, b) => parseFloat(b.children[columnIndex].textContent) -
                             parseFloat(a.children[columnIndex].textContent);
      break;
    case "정답률 낮은 순":
      columnIndex = 3;
      comparator = (a, b) => parseFloat(a.children[columnIndex].textContent) -
                             parseFloat(b.children[columnIndex].textContent);
      break;
    case "완료한 사람 많은 순":
      columnIndex = 2;
      comparator = (a, b) => parseInt(b.children[columnIndex].textContent.replace(/,/g, '')) -
                             parseInt(a.children[columnIndex].textContent.replace(/,/g, ''));
      break;
    case "최신 등록 순":
      console.log("정렬 미구현");
      return;
  }

  if (comparator) filtered.sort(comparator);

  table.innerHTML = "";
  filtered.forEach(row => table.appendChild(row));
}

document.addEventListener("DOMContentLoaded", () => {
  const table = document.querySelector(".user-question-table tbody");
  if (table) {
    userAllRows = Array.from(table.querySelectorAll("tr"));

    userAllRows.forEach((row) => {
      row.classList.add("clickable-row");
      row.addEventListener("click", () => {
        const title = row.querySelector("strong")?.innerText.trim();
        if (!title) return;

        const slug = encodeURIComponent(title.replace(/\s+/g, "_"));
        window.location.href = `/codingtest/solve_Q?title=${slug}`;
      });
    });
  }
});

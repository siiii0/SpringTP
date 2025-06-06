// error_write.js
const form = document.getElementById("postForm");

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const language = form.language.value;
  const title = form.title.value.trim();
  const content = form.content.value.trim();

  if (!language || !title || !content) {
    alert("언어, 제목, 내용을 모두 입력해주세요.");
    return;
  }

  alert("오류 제보가 등록되었습니다. (연결 예정)");
  window.location.href = "/board/error";
});

const questionInput = document.getElementById("question");
const findQuestionBtn = document.getElementById("findQuestionBtn");
const questionModal = document.getElementById("questionModal");
const questionListEl = document.getElementById("questionList");
const searchQuestionInput = document.getElementById("searchQuestionInput");

const questionData = [
  { id: 1, title: "나이 출력하기" },
  { id: 2, title: "두 수 비교하기" },
  { id: 3, title: "나머지 구하기" }
];

findQuestionBtn.addEventListener("click", () => {
  questionModal.style.display = "flex";
  renderQuestionList(questionData);
});

function closeQuestionModal() {
  questionModal.style.display = "none";
}

function renderQuestionList(data) {
  questionListEl.innerHTML = "";
  data.forEach(q => {
    const li = document.createElement("li");
    li.textContent = q.title;
    li.style.cursor = "pointer";
    li.addEventListener("click", () => {
      questionInput.value = q.title;
      closeQuestionModal();
    });
    questionListEl.appendChild(li);
  });
}

searchQuestionInput.addEventListener("input", () => {
  const keyword = searchQuestionInput.value.toLowerCase();
  const filtered = questionData.filter(q =>
    q.title.toLowerCase().includes(keyword)
  );
  renderQuestionList(filtered);
});

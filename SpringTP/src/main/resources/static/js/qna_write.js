const form = document.getElementById("postForm");

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const category = form.category.value;
  const title = form.title.value.trim();
  const content = form.content.value.trim();

  if (!category || !title || !content) {
    alert("Q&A 유형, 제목, 내용을 모두 입력해주세요.");
    return;
  }

  alert("Q&A 질문이 등록되었습니다. (연결 예정)");
  window.location.href = "/board/qna";
});

const questionInput = document.getElementById("question");
const findQuestionBtn = document.getElementById("findQuestionBtn");
const questionModal = document.getElementById("questionModal");
const questionListEl = document.getElementById("questionList");
const searchQuestionInput = document.getElementById("searchQuestionInput");

const questionData = [
  { id: 1, title: "나머지 구하기" },
  { id: 2, title: "나머지가 10이 되는 수 찾기" },
  { id: 3, title: "나이 출력하기" }
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

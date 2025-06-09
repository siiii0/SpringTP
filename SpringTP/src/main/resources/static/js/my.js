document.addEventListener("DOMContentLoaded", () => {
  const ctx = document.getElementById('activityChart').getContext('2d');

  const chart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
               'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      datasets: [{
        label: '제출 수',
        data: [3, 5, 8, 2, 6, 9, 4, 7, 5, 8, 6, 10],
        backgroundColor: '#2ac1bc',
        borderRadius: 4,
        barThickness: 20,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: { display: false }
      },
      scales: {
        x: {
          grid: { display: false }
        },
        y: {
          grid: { display: false },
          ticks: {
            stepSize: 2,
            beginAtZero: true
          }
        }
      }
    }
  });

  const yearButton = document.querySelector(".year-button");
  const yearOptions = document.querySelector(".year-options");
  const yearLabel = document.querySelector(".year-label");

  if (yearButton && yearOptions) {
    // 드롭다운 열기/닫기
    yearButton.addEventListener("click", () => {
      yearOptions.style.display = yearOptions.style.display === "block" ? "none" : "block";
    });

    // 옵션 선택 시 연도 변경
    yearOptions.querySelectorAll("li").forEach(li => {
      li.addEventListener("click", () => {
        yearButton.childNodes[0].nodeValue = li.textContent + " ";
        if (yearLabel) yearLabel.textContent = li.textContent;
        yearOptions.style.display = "none";
        // TODO: 선택된 연도에 맞는 데이터 갱신 처리 필요 시 여기서 fetch 등 가능
      });
    });

    // 외부 클릭 시 닫기
    document.addEventListener("click", (e) => {
      if (!yearButton.contains(e.target) && !yearOptions.contains(e.target)) {
        yearOptions.style.display = "none";
      }
    });
  }
});

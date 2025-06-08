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
});

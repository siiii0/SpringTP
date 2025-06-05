document.addEventListener("DOMContentLoaded", () => {
  // 🔹 슬라이더 기능
  const slides = document.querySelectorAll('.slide');
  const dots = document.querySelectorAll('.dot');
  let current = 0;

  function showSlide(index) {
    slides.forEach((slide, i) => {
      slide.style.transform = `translateX(-${index * 100}%)`;
      dots[i].classList.toggle('active', i === index);
    });
    current = index;
  }

  function nextSlide() {
    const next = (current + 1) % slides.length;
    showSlide(next);
  }

  if (slides.length && dots.length) {
    dots.forEach((dot, i) => {
      dot.addEventListener('click', () => showSlide(i));
    });

    setInterval(nextSlide, 2000); // 2초마다 자동 슬라이드
  }
});

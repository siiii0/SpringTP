/**
 * 로그인 관련 JavaScript 함수
 */

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('로그인 스크립트 로드됨');
    
    // 로그인 폼이 있는지 확인
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            // 폼 제출 전 유효성 검사
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            
            if (!username || !password) {
                e.preventDefault();
                alert('아이디와 비밀번호를 모두 입력해주세요.');
                return false;
            }
            
            // 로그인 시도 로깅
            console.log('로그인 시도:', username);
            
            // 폼 제출 후 서버에서 처리됨 (SecurityConfig에서 설정된 대로 메인 페이지로 리다이렉트)
        });
    }
    
    // 로그인 상태 확인 (모든 페이지에서 실행)
    checkLoginStatus();
});

// 로그인 상태 확인 및 UI 업데이트
function checkLoginStatus() {
    fetch('/api/user/current', {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Cache-Control': 'no-cache, no-store, must-revalidate',
            'Pragma': 'no-cache',
            'Expires': '0'
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log('로그인 상태 확인:', data);
        
        // 로그인 상태에 따라 UI 업데이트
        updateLoginUI(data.authenticated, data.username);
        
        // 현재 페이지가 메인 페이지인지 확인
        if (window.location.pathname === '/main' || window.location.pathname === '/') {
            console.log('메인 페이지에서 로그인 상태 확인됨');
        }
    })
    .catch(error => {
        console.error('로그인 상태 확인 오류:', error);
    });
}

// 로그인 상태에 따라 UI 업데이트
function updateLoginUI(isLoggedIn, username) {
    const loggedInSection = document.getElementById('loggedIn');
    const loggedOutSection = document.getElementById('loggedOut');
    const usernameDisplay = document.getElementById('usernameDisplay');
    
    if (isLoggedIn && loggedInSection && loggedOutSection) {
        // 로그인 상태일 때
        if (usernameDisplay) {
            usernameDisplay.textContent = username || '사용자';
        }
        
        loggedInSection.style.display = 'flex';
        loggedOutSection.style.display = 'none';
        
        console.log('로그인 UI 업데이트: 로그인 상태');
    } else if (loggedInSection && loggedOutSection) {
        // 로그아웃 상태일 때
        loggedInSection.style.display = 'none';
        loggedOutSection.style.display = 'flex';
        
        console.log('로그인 UI 업데이트: 로그아웃 상태');
    }
    
    // 로그인 상태를 세션 스토리지에 저장 (페이지 간 상태 유지)
    if (isLoggedIn) {
        sessionStorage.setItem('isLoggedIn', 'true');
        sessionStorage.setItem('username', username || '사용자');
    } else {
        sessionStorage.removeItem('isLoggedIn');
        sessionStorage.removeItem('username');
    }
} 